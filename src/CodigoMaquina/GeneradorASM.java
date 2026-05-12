package CodigoMaquina;

import java.util.*;
import java.util.regex.*;

/**
 * Generates x86-64 NASM assembly for Windows (win64 / Microsoft x64 ABI).
 *
 * Calling convention (first 4 integer/pointer args):
 *   arg1 → RCX,  arg2 → RDX,  arg3 → R8,  arg4 → R9
 *   Caller allocates 32-byte "shadow space" below the args on the stack.
 *   Return value in RAX.
 *   Stack must be 16-byte aligned at every CALL instruction.
 *
 * Assemble: nasm -f win64 salida.asm -o salida.obj
 * Link:     gcc salida.obj -o salida.exe
 */
public class GeneradorASM {

    // ── IR model ─────────────────────────────────────────────────────────────

    private enum Kind {
        ASSIGN, BINOP, UNOP, PRINT, READ_INTO,
        RETURN, LABEL, GOTO, IF_FALSE_GOTO,
        CALL_ASSIGN, CALL_ARG
    }

    private static class Instr {
        Kind   kind;
        String dst, src, left, right, op, label, funcName, value;
        int    argCount;
    }

    private static class FuncBlock {
        String       name;
        List<String> params = new ArrayList<>();
        List<Instr>  instrs = new ArrayList<>();
    }

    // ── Variable table ────────────────────────────────────────────────────────
    // All variables (params AND locals) live at [rbp - slot*8].
    // Params are saved from their registers in the function prologue.

    private static class VarTable {
        final List<String>        params;
        final Map<String,Integer> slots  = new LinkedHashMap<>(); // name → 1-based slot index
        int slotCount = 0;

        VarTable(List<String> params) {
            this.params = new ArrayList<>(params);
            for (String p : params) alloc(p); // reserve slots for params first
        }

        void alloc(String name) {
            if (!slots.containsKey(name)) slots.put(name, ++slotCount);
        }

        /** Returns "[rbp-N]" or null. */
        String ref(String name) {
            Integer s = slots.get(name);
            return s == null ? null : "[rbp-" + (s * 8) + "]";
        }

        /**
         * Frame size to subtract from RSP.
         * N = round_up_to_16(localBytes + 32_shadow).
         * After push rbp + sub rsp N, RSP is 16-byte aligned at every CALL.
         */
        int frameSize() {
            int raw = slotCount * 8 + 32;   // 32 bytes shadow space
            return ((raw + 15) / 16) * 16;   // round up to multiple of 16
        }
    }

    // ── State ─────────────────────────────────────────────────────────────────

    private final Map<String,String> strLiterals = new LinkedHashMap<>(); // quotedContent → label
    private int     strCount   = 0;
    private boolean needScanFmt = false;

    // ── Public entry point ────────────────────────────────────────────────────

    public String generar(String tacCode) {
        strLiterals.clear();
        strCount    = 0;
        needScanFmt = false;

        List<FuncBlock> blocks = parseTAC(tacCode);

        List<String> text = new ArrayList<>();
        text.add("section .text");
        text.add("    extern printf");
        text.add("    extern scanf");
        text.add("    extern fflush");
        text.add("    global main");
        text.add("");

        for (int i = 1; i < blocks.size(); i++) {
            text.addAll(emitBlock(blocks.get(i), false));
        }
        text.addAll(emitBlock(blocks.get(0), true));   // global → main

        // data section (built after code gen so string literals are collected)
        List<String> data = new ArrayList<>();
        data.add("section .data");
        data.add("    _fmt_int   db \"%d\", 10, 0");
        data.add("    _fmt_str   db \"%s\", 10, 0");
        data.add("    _fmt_true  db \"verdadero\", 10, 0");
        data.add("    _fmt_false db \"falso\", 10, 0");
        if (needScanFmt) data.add("    _fmt_scan  db \"%d\", 0");
        for (Map.Entry<String,String> e : strLiterals.entrySet()) {
            data.add("    " + e.getValue() + " db " + nasmString(e.getKey()));
        }
        data.add("");

        StringBuilder sb = new StringBuilder();
        sb.append("; Generado por Compilador - UMG\n");
        sb.append("; Ensamblar: nasm -f win64 salida.asm -o salida.obj\n");
        sb.append("; Enlazar:   gcc salida.obj -o salida.exe\n");
        sb.append("bits 64\n");
        sb.append("default rel\n\n");
        data.forEach(l -> sb.append(l).append('\n'));
        text.forEach(l -> sb.append(l).append('\n'));
        return sb.toString();
    }

    // ── TAC parser ────────────────────────────────────────────────────────────

    private static final Pattern P_IFFALSE  = Pattern.compile("ifFalso (\\S+) goto (\\S+)");
    private static final Pattern P_CALL_RHS = Pattern.compile("call (\\w+),\\s*(\\d+)");
    private static final Pattern P_UNARY    = Pattern.compile("([!\\-])\\s+(\\S+)");
    private static final Pattern P_BINARY   =
        Pattern.compile("(\\S+)\\s*([+\\-*/%]|==|!=|<=|>=|<|>|&&|\\|\\||<<|>>)\\s*(\\S+)");

    private List<FuncBlock> parseTAC(String tac) {
        List<FuncBlock> blocks  = new ArrayList<>();
        FuncBlock       global  = new FuncBlock();
        global.name = "main";
        blocks.add(global);

        FuncBlock current         = global;
        boolean   insideFunc      = false;
        boolean   collectingParams = false;

        for (String raw : tac.split("\n")) {
            String line = raw.trim();
            if (line.isEmpty()) continue;

            if (line.startsWith("func ")) {
                FuncBlock fb = new FuncBlock();
                fb.name = line.substring(5).trim();
                blocks.add(fb);
                current         = fb;
                insideFunc      = true;
                collectingParams = true;
                continue;
            }
            if (line.startsWith("end func ")) {
                insideFunc      = false;
                collectingParams = false;
                current         = global;
                continue;
            }
            // param inside func header = parameter declaration
            if (line.startsWith("param ") && collectingParams && insideFunc) {
                current.params.add(line.substring(6).trim());
                continue;
            }
            collectingParams = false;

            if (line.endsWith(":") && !line.contains(" ")) {
                Instr ins = new Instr(); ins.kind = Kind.LABEL;
                ins.label = line.substring(0, line.length() - 1);
                current.instrs.add(ins); continue;
            }
            if (line.startsWith("goto ")) {
                Instr ins = new Instr(); ins.kind = Kind.GOTO;
                ins.label = line.substring(5).trim();
                current.instrs.add(ins); continue;
            }
            Matcher mif = P_IFFALSE.matcher(line);
            if (mif.matches()) {
                Instr ins = new Instr(); ins.kind = Kind.IF_FALSE_GOTO;
                ins.src = mif.group(1); ins.label = mif.group(2);
                current.instrs.add(ins); continue;
            }
            if (line.startsWith("imprimir ")) {
                Instr ins = new Instr(); ins.kind = Kind.PRINT;
                ins.value = line.substring(9).trim();
                current.instrs.add(ins); continue;
            }
            if (line.equals("retornar")) {
                Instr ins = new Instr(); ins.kind = Kind.RETURN;
                current.instrs.add(ins); continue;
            }
            if (line.startsWith("retornar ")) {
                Instr ins = new Instr(); ins.kind = Kind.RETURN;
                ins.value = line.substring(9).trim();
                current.instrs.add(ins); continue;
            }
            if (line.startsWith("param ")) {
                Instr ins = new Instr(); ins.kind = Kind.CALL_ARG;
                ins.value = line.substring(6).trim();
                current.instrs.add(ins); continue;
            }

            int eq = line.indexOf('=');
            if (eq > 0) {
                String dst = line.substring(0, eq).trim();
                String rhs = line.substring(eq + 1).trim();

                Matcher mc = P_CALL_RHS.matcher(rhs);
                if (mc.matches()) {
                    Instr ins = new Instr(); ins.kind = Kind.CALL_ASSIGN;
                    ins.dst = dst; ins.funcName = mc.group(1);
                    ins.argCount = Integer.parseInt(mc.group(2));
                    current.instrs.add(ins); continue;
                }
                if (rhs.equals("leer()")) {
                    Instr ins = new Instr(); ins.kind = Kind.READ_INTO;
                    ins.dst = dst; current.instrs.add(ins);
                    needScanFmt = true; continue;
                }
                Matcher mu = P_UNARY.matcher(rhs);
                if (mu.matches()) {
                    Instr ins = new Instr(); ins.kind = Kind.UNOP;
                    ins.dst = dst; ins.op = mu.group(1); ins.src = mu.group(2);
                    current.instrs.add(ins); continue;
                }
                Matcher mb = P_BINARY.matcher(rhs);
                if (mb.matches()) {
                    Instr ins = new Instr(); ins.kind = Kind.BINOP;
                    ins.dst = dst; ins.left = mb.group(1);
                    ins.op = mb.group(2); ins.right = mb.group(3);
                    current.instrs.add(ins); continue;
                }
                Instr ins = new Instr(); ins.kind = Kind.ASSIGN;
                ins.dst = dst; ins.src = rhs;
                current.instrs.add(ins);
            }
        }
        return blocks;
    }

    // ── Code emitter ──────────────────────────────────────────────────────────

    private List<String> emitBlock(FuncBlock block, boolean isMain) {
        VarTable vt = new VarTable(block.params);

        // Pre-allocate all referenced variables so frame size is known before emit
        for (Instr ins : block.instrs) {
            tryAlloc(ins.dst,   vt);
            tryAlloc(ins.src,   vt);
            tryAlloc(ins.left,  vt);
            tryAlloc(ins.right, vt);
            tryAlloc(ins.value, vt);
        }

        List<String> body   = new ArrayList<>();
        List<Instr>  argBuf = new ArrayList<>();
        for (Instr ins : block.instrs) emitInstr(ins, vt, body, argBuf);

        // Prologue
        List<String> out = new ArrayList<>();
        out.add(block.name + ":");
        out.add("    push rbp");
        out.add("    mov rbp, rsp");
        int fs = vt.frameSize();
        if (fs > 0) out.add("    sub rsp, " + fs);
        out.add("");

        // Save incoming params from registers to their stack slots
        String[] pRegs = {"rcx", "rdx", "r8", "r9"};
        for (int i = 0; i < block.params.size() && i < 4; i++) {
            out.add("    mov " + vt.ref(block.params.get(i)) + ", " + pRegs[i]);
        }
        if (!block.params.isEmpty()) out.add("");

        out.addAll(body);

        if (isMain) {
            out.add("    xor rcx, rcx");
            out.add("    call fflush");
        }
        // Fallthrough epilogue — ensures every function ends with ret even
        // if the TAC had no explicit retornar.
        out.add("    xor eax, eax");
        out.add("    mov rsp, rbp");
        out.add("    pop rbp");
        out.add("    ret");
        out.add("");
        return out;
    }

    private void emitInstr(Instr ins, VarTable vt,
                           List<String> code, List<Instr> argBuf) {
        switch (ins.kind) {

            case CALL_ARG -> argBuf.add(ins);

            case ASSIGN -> {
                String ref = ensureRef(ins.dst, vt);
                loadRax(ins.src, vt, code);
                code.add("    mov " + ref + ", rax");
            }

            case READ_INTO -> {
                String ref = ensureRef(ins.dst, vt);
                code.add("    lea rcx, [_fmt_scan]");
                code.add("    lea rdx, " + ref);
                code.add("    call scanf");
                // Zero-extend the 32-bit value scanf wrote into the 8-byte slot
                code.add("    mov eax, dword " + ref);
                code.add("    mov " + ref + ", rax");
            }

            case BINOP -> emitBinop(ins, vt, code);

            case UNOP -> {
                String ref = ensureRef(ins.dst, vt);
                loadRax(ins.src, vt, code);
                switch (ins.op) {
                    case "-" -> code.add("    neg rax");
                    case "!" -> {
                        code.add("    cmp rax, 0");
                        code.add("    sete al");
                        code.add("    movzx rax, al");
                    }
                    default -> code.add("    ; unknown unop: " + ins.op);
                }
                code.add("    mov " + ref + ", rax");
            }

            case PRINT -> emitPrint(ins.value, vt, code);

            case RETURN -> {
                if (ins.value != null) loadRax(ins.value, vt, code);
                else                   code.add("    xor eax, eax");
                code.add("    mov rsp, rbp");
                code.add("    pop rbp");
                code.add("    ret");
            }

            case LABEL        -> code.add("  ." + ins.label + ":");
            case GOTO         -> code.add("    jmp ." + ins.label);
            case IF_FALSE_GOTO -> {
                loadRax(ins.src, vt, code);
                code.add("    cmp rax, 0");
                code.add("    je ." + ins.label);
            }

            case CALL_ASSIGN -> {
                String ref = ensureRef(ins.dst, vt);
                // Load first 4 args into registers (Windows x64 ABI)
                String[] regs = {"rcx", "rdx", "r8", "r9"};
                for (int i = 0; i < argBuf.size() && i < 4; i++) {
                    loadIntoReg(argBuf.get(i).value, regs[i], vt, code);
                }
                argBuf.clear();
                code.add("    call " + ins.funcName);
                code.add("    mov " + ref + ", rax");
            }
        }
    }

    // ── Binary operation ──────────────────────────────────────────────────────

    private void emitBinop(Instr ins, VarTable vt, List<String> code) {
        String ref = ensureRef(ins.dst, vt);
        loadRax(ins.left,  vt, code);   // rax = left
        loadRbx(ins.right, vt, code);   // rbx = right

        switch (ins.op) {
            case "+"  -> code.add("    add rax, rbx");
            case "-"  -> code.add("    sub rax, rbx");
            case "*"  -> code.add("    imul rax, rbx");
            case "/"  -> { code.add("    cqo");         code.add("    idiv rbx"); }
            case "%"  -> { code.add("    cqo");         code.add("    idiv rbx");
                           code.add("    mov rax, rdx"); }
            case "<<" -> { code.add("    mov rcx, rbx"); code.add("    sal rax, cl"); }
            case ">>" -> { code.add("    mov rcx, rbx"); code.add("    sar rax, cl"); }
            case "==" -> emitSetcc("sete",  code);
            case "!=" -> emitSetcc("setne", code);
            case "<"  -> emitSetcc("setl",  code);
            case ">"  -> emitSetcc("setg",  code);
            case "<=" -> emitSetcc("setle", code);
            case ">=" -> emitSetcc("setge", code);
            case "&&" -> {
                code.add("    cmp rax, 0"); code.add("    setne cl");
                code.add("    cmp rbx, 0"); code.add("    setne al");
                code.add("    and al, cl");  code.add("    movzx rax, al");
            }
            case "||" -> {
                code.add("    cmp rax, 0"); code.add("    setne cl");
                code.add("    cmp rbx, 0"); code.add("    setne al");
                code.add("    or al, cl");   code.add("    movzx rax, al");
            }
            default -> code.add("    ; unsupported op: " + ins.op);
        }
        code.add("    mov " + ref + ", rax");
    }

    private void emitSetcc(String cc, List<String> code) {
        code.add("    cmp rax, rbx");
        code.add("    " + cc + " al");
        code.add("    movzx rax, al");
    }

    // ── Print ─────────────────────────────────────────────────────────────────

    private void emitPrint(String val, VarTable vt, List<String> code) {
        if (isStringLit(val)) {
            code.add("    lea rcx, [_fmt_str]");
            code.add("    lea rdx, [" + internStr(val) + "]");
        } else if ("verdadero".equals(val)) {
            code.add("    lea rcx, [_fmt_true]");
        } else if ("falso".equals(val)) {
            code.add("    lea rcx, [_fmt_false]");
        } else {
            loadRax(val, vt, code);
            code.add("    lea rcx, [_fmt_int]");
            code.add("    mov rdx, rax");
        }
        code.add("    call printf");
    }

    // ── Value loaders ─────────────────────────────────────────────────────────

    /** Load val into RAX. */
    private void loadRax(String val, VarTable vt, List<String> code) {
        if (val == null)               { code.add("    xor rax, rax");             return; }
        if ("verdadero".equals(val))   { code.add("    mov rax, 1");               return; }
        if ("falso".equals(val))       { code.add("    mov rax, 0");               return; }
        if ("leer()".equals(val))      { code.add("    xor rax, rax");             return; }
        if (isStringLit(val)) { code.add("    lea rax, [" + internStr(val) + "]"); return; }
        if (isNumLit(val))    { code.add("    mov rax, " + toLong(val));           return; }
        String ref = vt.ref(val);
        if (ref == null) { vt.alloc(val); ref = vt.ref(val); }
        code.add("    mov rax, " + ref);
    }

    /** Load val into RBX (for right side of binary operations). */
    private void loadRbx(String val, VarTable vt, List<String> code) {
        if ("verdadero".equals(val)) { code.add("    mov rbx, 1"); return; }
        if ("falso".equals(val))     { code.add("    mov rbx, 0"); return; }
        if (isNumLit(val))           { code.add("    mov rbx, " + toLong(val)); return; }
        String ref = vt.ref(val);
        if (ref == null) { vt.alloc(val); ref = vt.ref(val); }
        code.add("    mov rbx, " + ref);
    }

    /** Load val into the named register (for function call arguments). */
    private void loadIntoReg(String val, String reg, VarTable vt, List<String> code) {
        if ("verdadero".equals(val)) { code.add("    mov " + reg + ", 1"); return; }
        if ("falso".equals(val))     { code.add("    mov " + reg + ", 0"); return; }
        if (isNumLit(val))           { code.add("    mov " + reg + ", " + toLong(val)); return; }
        if (isStringLit(val)) {
            code.add("    lea " + reg + ", [" + internStr(val) + "]"); return;
        }
        String ref = vt.ref(val);
        if (ref == null) { vt.alloc(val); ref = vt.ref(val); }
        code.add("    mov " + reg + ", " + ref);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private String ensureRef(String name, VarTable vt) {
        vt.alloc(name);
        return vt.ref(name);
    }

    private void tryAlloc(String s, VarTable vt) {
        if (s == null || isNumLit(s) || isStringLit(s)) return;
        if ("verdadero".equals(s) || "falso".equals(s) || "leer()".equals(s)) return;
        vt.alloc(s);
    }

    private boolean isNumLit(String s) {
        if (s == null) return false;
        try { Double.parseDouble(s); return true; }
        catch (NumberFormatException e) { return false; }
    }

    private boolean isStringLit(String s) {
        return s != null && s.startsWith("\"") && s.endsWith("\"") && s.length() >= 2;
    }

    private long toLong(String s) { return (long) Double.parseDouble(s); }

    private String internStr(String quoted) {
        return strLiterals.computeIfAbsent(quoted, k -> "_str" + strCount++);
    }

    /** Convert a Java quoted string literal to NASM db format. */
    private String nasmString(String quoted) {
        String inner = quoted.substring(1, quoted.length() - 1);
        StringBuilder sb  = new StringBuilder();
        boolean       inQ = false;
        for (int i = 0; i < inner.length(); i++) {
            if (inner.charAt(i) == '\\' && i + 1 < inner.length()) {
                int b = switch (inner.charAt(i + 1)) {
                    case 'n' -> 10; case 't' -> 9;
                    case 'r' -> 13; case '0' -> 0;
                    default  -> -1;
                };
                if (b >= 0) {
                    if (inQ) { sb.append("\", "); inQ = false; }
                    else if (sb.length() > 0) sb.append(", ");
                    sb.append(b);
                    i++;
                    continue;
                }
            }
            if (!inQ) {
                if (sb.length() > 0) sb.append(", ");
                sb.append('"');
                inQ = true;
            }
            sb.append(inner.charAt(i));
        }
        if (inQ) sb.append('"');
        sb.append(sb.length() > 0 ? ", 0" : "0");
        return sb.toString();
    }
}

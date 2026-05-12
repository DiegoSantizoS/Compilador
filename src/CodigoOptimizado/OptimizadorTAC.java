package CodigoOptimizado;

import java.util.*;
import java.util.regex.*;

public class OptimizadorTAC {

    // ── Patterns ─────────────────────────────────────────────────────────────
    private static final Pattern P_CONST_BINOP = Pattern.compile(
        "(\\w+)\\s*=\\s*(-?[0-9]+(?:\\.[0-9]+)?)\\s*([+\\-*/%])\\s*(-?[0-9]+(?:\\.[0-9]+)?)"
    );
    private static final Pattern P_MUL_POW2 = Pattern.compile(
        "(\\w+)\\s*=\\s*(\\w+)\\s*\\*\\s*([0-9]+)"
    );
    private static final Pattern P_MUL_POW2_REV = Pattern.compile(
        "(\\w+)\\s*=\\s*([0-9]+)\\s*\\*\\s*(\\w+)"
    );
    private static final Pattern P_DIV_POW2 = Pattern.compile(
        "(\\w+)\\s*=\\s*(\\w+)\\s*/\\s*([0-9]+)"
    );
    private static final Pattern P_SIMPLE_ASSIGN = Pattern.compile(
        "(\\w+)\\s*=\\s*(-?[0-9]+(?:\\.[0-9]+)?|\"[^\"]*\"|verdadero|falso)$"
    );
    private static final Pattern P_IDENTIFIER = Pattern.compile("\\b([a-zA-Z_]\\w*)\\b");
    private static final Pattern P_TEMP_ASSIGN = Pattern.compile("(t\\d+)\\s*=.*");
    private static final Set<String> KEYWORDS = Set.of(
        "call", "goto", "ifFalso", "verdadero", "falso",
        "imprimir", "retornar", "param", "func", "end", "leer"
    );

    // ── Log ───────────────────────────────────────────────────────────────────
    private final List<String> log = new ArrayList<>();

    public List<String> getLog() {
        return Collections.unmodifiableList(log);
    }

    // ── Public entry point ────────────────────────────────────────────────────
    public String optimizar(String tac) {
        log.clear();
        log.add("=== Fase de Optimización ===");

        List<String> lines = new ArrayList<>(Arrays.asList(tac.split("\n")));

        log.add("");
        log.add("Optimización Local - Constant Folding (Plegado de Constantes):");
        lines = constantFolding(lines);

        log.add("");
        log.add("Optimización Local - Propagación de Constantes:");
        lines = constantPropagation(lines);

        log.add("");
        log.add("Reducción de Fuerza:");
        lines = strengthReduction(lines);

        log.add("");
        log.add("Eliminación de Código Muerto:");
        lines = deadCodeElimination(lines);

        StringBuilder sb = new StringBuilder();
        for (String l : lines) {
            if (!l.isEmpty()) sb.append(l).append("\n");
        }
        return sb.toString();
    }

    // ── 1. Constant Folding ───────────────────────────────────────────────────
    private List<String> constantFolding(List<String> lines) {
        List<String> result = new ArrayList<>(lines.size());
        int count = 0;
        for (String line : lines) {
            String trimmed = line.trim();
            String folded  = fold(trimmed);
            if (!folded.equals(trimmed)) {
                log.add("  " + trimmed + "  →  " + folded);
                count++;
            }
            result.add(folded);
        }
        if (count == 0) log.add("  (ninguna aplicada)");
        return result;
    }

    private String fold(String line) {
        Matcher m = P_CONST_BINOP.matcher(line);
        if (!m.matches()) return line;

        String var   = m.group(1);
        double left  = Double.parseDouble(m.group(2));
        String op    = m.group(3);
        double right = Double.parseDouble(m.group(4));
        double result;

        switch (op) {
            case "+" -> result = left + right;
            case "-" -> result = left - right;
            case "*" -> result = left * right;
            case "/" -> { if (right == 0) return line; result = left / right; }
            case "%" -> { if (right == 0) return line; result = left % right; }
            default  -> { return line; }
        }

        if (result == Math.floor(result) && !Double.isInfinite(result))
            return var + " = " + (long) result;
        return var + " = " + result;
    }

    // ── 2. Constant Propagation ───────────────────────────────────────────────
    private List<String> constantPropagation(List<String> lines) {
        // Pre-scan: variables assigned more than once (e.g. loop counters) are
        // never safe to propagate — they change at runtime.
        Set<String> multiAssigned = new HashSet<>();
        Set<String> seenOnce     = new HashSet<>();
        for (String line : lines) {
            String t = line.trim();
            if (t.startsWith("func ") || t.startsWith("end func")
                    || t.endsWith(":") || t.startsWith("goto ")
                    || t.startsWith("ifFalso ")) continue;
            int eq = t.indexOf('=');
            if (eq > 0) {
                String lhs = t.substring(0, eq).trim();
                if (lhs.matches("[a-zA-Z_]\\w*")) {
                    if (!seenOnce.add(lhs)) multiAssigned.add(lhs);
                }
            }
        }

        Map<String, String> constants = new LinkedHashMap<>();
        List<String> result = new ArrayList<>(lines.size());
        int count = 0;

        for (String line : lines) {
            String trimmed = line.trim();

            Matcher simple = P_SIMPLE_ASSIGN.matcher(trimmed);
            if (simple.matches()) {
                String varName = simple.group(1);
                if (!multiAssigned.contains(varName))
                    constants.put(varName, simple.group(2));
                result.add(trimmed);
                continue;
            }

            // Invalidate var on non-constant reassignment
            int eq = trimmed.indexOf('=');
            if (eq > 0) constants.remove(trimmed.substring(0, eq).trim());

            String substituted = substituteConstants(trimmed, constants);
            substituted = fold(substituted);
            if (!substituted.equals(trimmed)) {
                log.add("  " + trimmed + "  →  " + substituted);
                count++;
            }
            result.add(substituted);
        }
        if (count == 0) log.add("  (ninguna aplicada)");
        return result;
    }

    private String substituteConstants(String line, Map<String, String> constants) {
        if (constants.isEmpty()) return line;
        if (line.endsWith(":") || line.startsWith("func ")
                || line.startsWith("end func") || line.startsWith("goto ")) return line;

        int eq = line.indexOf('=');
        if (eq < 0) return replaceIds(line, constants);

        return line.substring(0, eq + 1) + replaceIds(line.substring(eq + 1), constants);
    }

    private String replaceIds(String text, Map<String, String> constants) {
        StringBuffer sb = new StringBuffer();
        Matcher m = P_IDENTIFIER.matcher(text);
        while (m.find()) {
            String id  = m.group(1);
            String val = constants.get(id);
            m.appendReplacement(sb, Matcher.quoteReplacement(
                (val != null && !KEYWORDS.contains(id)) ? val : id
            ));
        }
        m.appendTail(sb);
        return sb.toString();
    }

    // ── 3. Strength Reduction ─────────────────────────────────────────────────
    private List<String> strengthReduction(List<String> lines) {
        List<String> result = new ArrayList<>(lines.size());
        int count = 0;
        for (String line : lines) {
            String trimmed = line.trim();
            String reduced = reduceStrength(trimmed);
            if (!reduced.equals(trimmed)) {
                log.add("  " + trimmed + "  →  " + reduced);
                count++;
            }
            result.add(reduced);
        }
        if (count == 0) log.add("  (ninguna aplicada)");
        return result;
    }

    private String reduceStrength(String line) {
        Matcher m = P_MUL_POW2.matcher(line);
        if (m.matches()) {
            int n = parseIntSafe(m.group(3));
            if (n > 1 && isPow2(n))
                return m.group(1) + " = " + m.group(2) + " << " + log2(n);
        }
        m = P_MUL_POW2_REV.matcher(line);
        if (m.matches()) {
            int n = parseIntSafe(m.group(2));
            if (n > 1 && isPow2(n))
                return m.group(1) + " = " + m.group(3) + " << " + log2(n);
        }
        m = P_DIV_POW2.matcher(line);
        if (m.matches()) {
            int n = parseIntSafe(m.group(3));
            if (n > 1 && isPow2(n))
                return m.group(1) + " = " + m.group(2) + " >> " + log2(n);
        }
        return line;
    }

    // ── 4. Dead Code Elimination ──────────────────────────────────────────────
    private List<String> deadCodeElimination(List<String> lines) {
        // Phase 1: unreachable code after unconditional goto
        List<String> reachable = new ArrayList<>();
        boolean dead = false;
        for (String line : lines) {
            String t = line.trim();
            if (t.endsWith(":") && !t.contains(" ")) dead = false;
            if (dead) {
                log.add("  [Código inalcanzable eliminado] " + t);
            } else {
                reachable.add(line);
            }
            if (t.startsWith("goto ") && !t.startsWith("ifFalso")) dead = true;
        }

        // Phase 2: unused temporaries
        Set<String> used = new HashSet<>();
        for (String line : reachable) collectUsed(line.trim(), used);

        List<String> result = new ArrayList<>();
        int removedTemps = 0;
        for (String line : reachable) {
            Matcher m = P_TEMP_ASSIGN.matcher(line.trim());
            if (m.matches() && !used.contains(m.group(1))) {
                log.add("  [Temporal sin uso eliminado] " + line.trim());
                removedTemps++;
                continue;
            }
            result.add(line);
        }

        if (result.size() == reachable.size() && removedTemps == 0
                && reachable.size() == lines.size()) {
            log.add("  (ninguna aplicada)");
        }
        return result;
    }

    private void collectUsed(String line, Set<String> used) {
        if (line.endsWith(":") || line.equals("retornar")) return;
        String rhs;
        int eq = line.indexOf('=');
        if (line.startsWith("imprimir ") || line.startsWith("retornar ")
                || line.startsWith("param ") || line.startsWith("ifFalso ")
                || line.startsWith("goto ")) {
            rhs = line;
        } else if (eq >= 0) {
            rhs = line.substring(eq + 1);
        } else {
            rhs = line;
        }
        Matcher m = P_IDENTIFIER.matcher(rhs);
        while (m.find()) {
            String id = m.group(1);
            if (!KEYWORDS.contains(id)) used.add(id);
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private boolean isPow2(int n) { return n > 0 && (n & (n - 1)) == 0; }
    private int log2(int n)       { return Integer.numberOfTrailingZeros(n); }
    private int parseIntSafe(String s) {
        try { return Integer.parseInt(s); } catch (NumberFormatException e) { return -1; }
    }
}

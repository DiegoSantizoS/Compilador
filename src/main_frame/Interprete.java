package main_frame;

import generated.LenguajeBaseVisitor;
import generated.LenguajeParser;
import java.awt.Color;
import java.util.*;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class Interprete extends LenguajeBaseVisitor<Object> {

    // ── internal data types ───────────────────────────────────────────────

    private static class Simbolo {
        String tipo;
        Object valor;
        Simbolo(String tipo, Object valor) { this.tipo = tipo; this.valor = valor; }
    }

    private static class InfoFuncion {
        String tipoRetorno;
        final List<String> nombres = new ArrayList<>();
        final List<String> tipos   = new ArrayList<>();
        LenguajeParser.FuncionContext ctx;
    }

    // thrown by visitRetornar and caught by visitLlamadaFuncion
    static class RetornoPropagado extends RuntimeException {
        final Object valor;
        RetornoPropagado(Object valor) { super(null, null, true, false); this.valor = valor; }
    }

    // ── state ─────────────────────────────────────────────────────────────

    private final JTextPane terminal;
    private final Stack<Map<String, Simbolo>> pilaAmbitos = new Stack<>();
    private final Map<String, InfoFuncion> tablaFunciones = new HashMap<>();

    public Interprete(JTextPane terminal) {
        this.terminal = terminal;
        pilaAmbitos.push(new LinkedHashMap<>());
    }

    // ── scope management ──────────────────────────────────────────────────

    private void entrarAmbito() { pilaAmbitos.push(new LinkedHashMap<>()); }

    private void salirAmbito() { if (!pilaAmbitos.isEmpty()) pilaAmbitos.pop(); }

    private void declararVar(String id, String tipo, Object valor) {
        pilaAmbitos.peek().put(id, new Simbolo(tipo, coerce(valor, tipo)));
    }

    private Simbolo buscarSimbolo(String id) {
        for (int i = pilaAmbitos.size() - 1; i >= 0; i--) {
            Simbolo s = pilaAmbitos.get(i).get(id);
            if (s != null) return s;
        }
        return null;
    }

    private void asignarValor(String id, Object valor) {
        for (int i = pilaAmbitos.size() - 1; i >= 0; i--) {
            if (pilaAmbitos.get(i).containsKey(id)) {
                Simbolo s = pilaAmbitos.get(i).get(id);
                s.valor = coerce(valor, s.tipo);
                return;
            }
        }
    }

    // ── output ────────────────────────────────────────────────────────────

    private void escribir(String msg, Color color) {
        if (terminal == null) { System.out.println(msg); return; }
        StyledDocument doc = terminal.getStyledDocument();
        Style style = terminal.addStyle("s" + System.nanoTime(), null);
        StyleConstants.setForeground(style, color);
        try {
            doc.insertString(doc.getLength(), msg + "\n", style);
        } catch (Exception ex) {
            System.out.println(msg);
        }
    }

    // ── type coercion helpers ─────────────────────────────────────────────

    private double aDouble(Object v) {
        if (v instanceof Double)  return (Double) v;
        if (v instanceof Integer) return ((Integer) v).doubleValue();
        if (v instanceof Boolean) return ((Boolean) v) ? 1.0 : 0.0;
        return 0.0;
    }

    private int aEntero(Object v) {
        if (v instanceof Integer) return (Integer) v;
        if (v instanceof Double)  return ((Double) v).intValue();
        if (v instanceof Boolean) return ((Boolean) v) ? 1 : 0;
        return 0;
    }

    private boolean aBooleano(Object v) {
        if (v instanceof Boolean) return (Boolean) v;
        if (v instanceof Integer) return (Integer) v != 0;
        if (v instanceof Double)  return (Double) v != 0.0;
        return false;
    }

    private String aTexto(Object v) {
        if (v == null) return "null";
        if (v instanceof Boolean) return (Boolean) v ? "verdadero" : "falso";
        if (v instanceof Double) {
            double d = (Double) v;
            if (!Double.isInfinite(d) && !Double.isNaN(d) && d == Math.floor(d) && Math.abs(d) < 1e15)
                return String.valueOf((long) d);
            return String.valueOf(d);
        }
        return v.toString();
    }

    private Object coerce(Object v, String tipo) {
        if (v == null) return defecto(tipo);
        switch (tipo) {
            case "entero":   return aEntero(v);
            case "real":     return aDouble(v);
            case "booleano": return aBooleano(v);
            case "cadena":   return aTexto(v);
            default:         return v;
        }
    }

    private Object defecto(String tipo) {
        switch (tipo) {
            case "entero":   return 0;
            case "real":     return 0.0;
            case "booleano": return false;
            case "cadena":   return "";
            default:         return null;
        }
    }

    private String tipoDeCtx(LenguajeParser.TipoContext ctx) {
        if (ctx.ENTERO()   != null) return "entero";
        if (ctx.REAL()     != null) return "real";
        if (ctx.CADENA()   != null) return "cadena";
        if (ctx.BOOLEANO() != null) return "booleano";
        return "vacio";
    }

    // ── program / top-level ───────────────────────────────────────────────

    @Override
    public Object visitPrograma(LenguajeParser.ProgramaContext ctx) {
        // register all functions before executing statements
        for (LenguajeParser.ElementoContext e : ctx.elemento()) {
            if (e.funcion() != null) registrarFuncion(e.funcion());
        }
        for (LenguajeParser.ElementoContext e : ctx.elemento()) {
            if (e.sentencia() != null) visit(e.sentencia());
        }
        return null;
    }

    private void registrarFuncion(LenguajeParser.FuncionContext ctx) {
        InfoFuncion f  = new InfoFuncion();
        f.tipoRetorno  = tipoDeCtx(ctx.tipo());
        f.ctx          = ctx;
        if (ctx.parametros() != null) {
            for (LenguajeParser.ParametroContext p : ctx.parametros().parametro()) {
                f.tipos.add(tipoDeCtx(p.tipo()));
                f.nombres.add(p.ID().getText());
            }
        }
        tablaFunciones.put(ctx.ID().getText(), f);
    }

    @Override public Object visitElemento(LenguajeParser.ElementoContext ctx)                { return visitChildren(ctx); }
    @Override public Object visitFuncion(LenguajeParser.FuncionContext ctx)                   { return null; }
    @Override public Object visitSentencia(LenguajeParser.SentenciaContext ctx)               { return visitChildren(ctx); }
    @Override public Object visitSentenciaBloque(LenguajeParser.SentenciaBloqueContext ctx)   { return visitChildren(ctx); }

    @Override
    public Object visitBloque(LenguajeParser.BloqueContext ctx) {
        entrarAmbito();
        try {
            for (LenguajeParser.SentenciaBloqueContext s : ctx.sentenciaBloque()) {
                visit(s);
            }
        } finally {
            salirAmbito();
        }
        return null;
    }

    // ── declarations / assignment ─────────────────────────────────────────

    @Override
    public Object visitDeclaracion(LenguajeParser.DeclaracionContext ctx) {
        String id   = ctx.ID().getText();
        String tipo = tipoDeCtx(ctx.tipo());
        Object val  = ctx.e != null ? visit(ctx.e) : null;
        declararVar(id, tipo, val);
        return null;
    }

    @Override
    public Object visitAsignacion(LenguajeParser.AsignacionContext ctx) {
        Object val = visit(ctx.expresion());
        asignarValor(ctx.ID().getText(), val);
        return null;
    }

    // ── I/O ───────────────────────────────────────────────────────────────

    @Override
    public Object visitImprimir(LenguajeParser.ImprimirContext ctx) {
        Object val = visit(ctx.e);
        escribir(aTexto(val), Color.WHITE);
        return null;
    }

    @Override
    public Object visitLeer(LenguajeParser.LeerContext ctx) {
        String tipo   = tipoDeCtx(ctx.tipo());
        String prompt = "Ingrese un valor de tipo " + tipo + ":";
        String input  = JOptionPane.showInputDialog(null, prompt, "Entrada del programa", JOptionPane.PLAIN_MESSAGE);
        if (input == null) input = "";
        return parsearEntrada(input.trim(), tipo);
    }

    private Object parsearEntrada(String input, String tipo) {
        try {
            switch (tipo) {
                case "entero":   return Integer.parseInt(input);
                case "real":     return Double.parseDouble(input);
                case "booleano": return "verdadero".equalsIgnoreCase(input)
                                     || "true".equalsIgnoreCase(input)
                                     || "1".equals(input);
                default:         return input;
            }
        } catch (NumberFormatException e) {
            escribir("Advertencia: '" + input + "' no es " + tipo + " valido, se usa valor por defecto.", Color.YELLOW);
            return defecto(tipo);
        }
    }

    // ── control flow ──────────────────────────────────────────────────────

    @Override
    public Object visitRetornar(LenguajeParser.RetornarContext ctx) {
        Object val = ctx.e != null ? visit(ctx.e) : null;
        throw new RetornoPropagado(val);
    }

    @Override
    public Object visitSi(LenguajeParser.SiContext ctx) {
        if (aBooleano(visit(ctx.c))) {
            visit(ctx.b1);
        } else if (ctx.b2 != null) {
            visit(ctx.b2);
        }
        return null;
    }

    @Override
    public Object visitMientras(LenguajeParser.MientrasContext ctx) {
        int iteraciones = 0;
        final int MAX = 100_000;
        while (aBooleano(visit(ctx.c))) {
            if (++iteraciones > MAX) {
                escribir("Error: limite de iteraciones alcanzado (posible bucle infinito).", Color.RED);
                break;
            }
            visit(ctx.b);
        }
        return null;
    }

    // ── function calls ────────────────────────────────────────────────────

    @Override
    public Object visitLlamadaFuncion(LenguajeParser.LlamadaFuncionContext ctx) {
        String nombre = ctx.ID().getText();
        InfoFuncion f = tablaFunciones.get(nombre);
        if (f == null) {
            escribir("Error de ejecucion: funcion no definida -> " + nombre, Color.RED);
            return null;
        }
        List<Object> args = new ArrayList<>();
        if (ctx.argumentos() != null) {
            for (LenguajeParser.ExpresionContext e : ctx.argumentos().expresion()) {
                args.add(visit(e));
            }
        }
        entrarAmbito();
        for (int i = 0; i < f.nombres.size(); i++) {
            Object val = i < args.size() ? args.get(i) : null;
            declararVar(f.nombres.get(i), f.tipos.get(i), val);
        }
        Object retVal = null;
        try {
            for (LenguajeParser.SentenciaBloqueContext s : f.ctx.sentenciaBloque()) {
                visit(s);
            }
        } catch (RetornoPropagado ex) {
            retVal = ex.valor;
        } finally {
            salirAmbito();
        }
        return retVal;
    }

    // ── expressions ───────────────────────────────────────────────────────

    @Override
    public Object visitExpresion(LenguajeParser.ExpresionContext ctx) { return visit(ctx.o); }

    @Override
    public Object visitOrExpr(LenguajeParser.OrExprContext ctx) {
        boolean result = aBooleano(visit(ctx.andExpr(0)));
        for (int i = 1; i < ctx.andExpr().size(); i++) {
            if (result) return true;
            result = aBooleano(visit(ctx.andExpr(i)));
        }
        return result;
    }

    @Override
    public Object visitAndExpr(LenguajeParser.AndExprContext ctx) {
        boolean result = aBooleano(visit(ctx.igualdad(0)));
        for (int i = 1; i < ctx.igualdad().size(); i++) {
            if (!result) return false;
            result = aBooleano(visit(ctx.igualdad(i)));
        }
        return result;
    }

    @Override
    public Object visitIgualdad(LenguajeParser.IgualdadContext ctx) {
        Object result = visit(ctx.comparacion(0));
        for (int i = 1; i < ctx.comparacion().size(); i++) {
            Object right = visit(ctx.comparacion(i));
            boolean eq   = iguales(result, right);
            result = ctx.IGUAL_IGUAL(i - 1) != null ? eq : !eq;
        }
        return result;
    }

    private boolean iguales(Object a, Object b) {
        if (a instanceof String && b instanceof String) return a.equals(b);
        if (a instanceof Boolean || b instanceof Boolean) return aBooleano(a) == aBooleano(b);
        return aDouble(a) == aDouble(b);
    }

    @Override
    public Object visitComparacion(LenguajeParser.ComparacionContext ctx) {
        Object result = visit(ctx.suma(0));
        for (int i = 1; i < ctx.suma().size(); i++) {
            double left  = aDouble(result);
            double right = aDouble(visit(ctx.suma(i)));
            if      (ctx.MENOR(i - 1)       != null) result = left < right;
            else if (ctx.MENOR_IGUAL(i - 1) != null) result = left <= right;
            else if (ctx.MAYOR(i - 1)       != null) result = left > right;
            else if (ctx.MAYOR_IGUAL(i - 1) != null) result = left >= right;
        }
        return result;
    }

    @Override
    public Object visitSuma(LenguajeParser.SumaContext ctx) {
        Object result = visit(ctx.mult(0));
        for (int i = 1; i < ctx.mult().size(); i++) {
            Object right = visit(ctx.mult(i));
            boolean suma = ctx.MAS(i - 1) != null;
            if (suma && (result instanceof String || right instanceof String)) {
                result = aTexto(result) + aTexto(right);
            } else if (result instanceof Double || right instanceof Double) {
                result = suma ? aDouble(result) + aDouble(right) : aDouble(result) - aDouble(right);
            } else {
                result = suma ? aEntero(result) + aEntero(right) : aEntero(result) - aEntero(right);
            }
        }
        return result;
    }

    @Override
    public Object visitMult(LenguajeParser.MultContext ctx) {
        Object result = visit(ctx.unario(0));
        for (int i = 1; i < ctx.unario().size(); i++) {
            Object right = visit(ctx.unario(i));
            if (ctx.POR(i - 1) != null) {
                result = (result instanceof Double || right instanceof Double)
                       ? aDouble(result) * aDouble(right)
                       : aEntero(result) * aEntero(right);
            } else {
                double divisor = aDouble(right);
                if (divisor == 0.0) {
                    escribir("Error de ejecucion: division entre cero.", Color.RED);
                    result = 0.0;
                } else {
                    result = aDouble(result) / divisor;
                }
            }
        }
        return result;
    }

    @Override
    public Object visitUnario(LenguajeParser.UnarioContext ctx) {
        if (ctx.primario() != null) return visit(ctx.primario());
        Object val = visit(ctx.u);
        if (ctx.MENOS() != null) return (val instanceof Double) ? -aDouble(val) : -aEntero(val);
        if (ctx.NO()    != null) return !aBooleano(val);
        return val;
    }

    @Override
    public Object visitPrimario(LenguajeParser.PrimarioContext ctx) {
        if (ctx.NUMERO() != null) {
            String t = ctx.NUMERO().getText();
            return t.contains(".") ? Double.parseDouble(t) : Integer.parseInt(t);
        }
        if (ctx.VERDADERO()    != null) return true;
        if (ctx.FALSO()        != null) return false;
        if (ctx.CADENA_LIT()   != null) {
            String s = ctx.CADENA_LIT().getText();
            return procesarEscapes(s.substring(1, s.length() - 1));
        }
        if (ctx.ID()           != null) {
            Simbolo s = buscarSimbolo(ctx.ID().getText());
            return s != null ? s.valor : null;
        }
        if (ctx.e              != null) return visit(ctx.e);
        if (ctx.llamadaFuncion() != null) return visit(ctx.llamadaFuncion());
        if (ctx.leer()         != null) return visit(ctx.leer());
        return null;
    }

    private String procesarEscapes(String s) {
        return s.replace("\\n", "\n")
                .replace("\\t", "\t")
                .replace("\\\\", "\\")
                .replace("\\\"", "\"");
    }
}

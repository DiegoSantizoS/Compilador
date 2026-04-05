package AnalizadorSemantico;

import generated.LenguajeBaseVisitor;
import generated.LenguajeParser;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JTextPane;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class Analizadorsem extends LenguajeBaseVisitor<Double> {

    private final Map<String, Double> tabla = new HashMap<>();
    private final Map<String, String> tipos = new HashMap<>();
    private final List<String> errores = new ArrayList<>();
    private final JTextPane terminal;

    public Analizadorsem(JTextPane terminal) {
        this.terminal = terminal;
    }

    public Map<String, Double> getTabla() {
        return tabla;
    }

    public Map<String, String> getTipos() {
        return tipos;
    }

    public List<String> getErrores() {
        return errores;
    }

    private void log(String mensaje) {
        errores.add(mensaje);

        if (terminal != null) {
            StyledDocument doc = terminal.getStyledDocument();
            Style errorStyle = terminal.addStyle("ErrorStyle", null);
            StyleConstants.setForeground(errorStyle, Color.RED);

            try {
                doc.insertString(doc.getLength(), mensaje + "\n", errorStyle);
            } catch (Exception e) {
                System.out.println(mensaje);
            }
        } else {
            System.out.println(mensaje);
        }
    }

    public void logNormal(String mensaje) {
        if (terminal != null) {
            StyledDocument doc = terminal.getStyledDocument();
            Style normalStyle = terminal.addStyle("NormalStyle", null);
            StyleConstants.setForeground(normalStyle, Color.BLACK);

            try {
                doc.insertString(doc.getLength(), mensaje + "\n", normalStyle);
            } catch (Exception e) {
                System.out.println(mensaje);
            }
        } else {
            System.out.println(mensaje);
        }
    }

    private String obtenerTipoDeclarado(LenguajeParser.TipoContext ctx) {
        if (ctx.ENTERO() != null) return "entero";
        if (ctx.REAL() != null) return "real";
        if (ctx.CADENA() != null) return "cadena";
        if (ctx.BOOLEANO() != null) return "booleano";
        return "desconocido";
    }

    public String inferirTipo(LenguajeParser.ExpresionContext ctx) {
        return inferirTipoOr(ctx.o);
    }

    private String inferirTipoOr(LenguajeParser.OrExprContext ctx) {
        if (ctx.andExpr().size() > 1) return "booleano";
        return inferirTipoAnd(ctx.andExpr(0));
    }

    private String inferirTipoAnd(LenguajeParser.AndExprContext ctx) {
        if (ctx.igualdad().size() > 1) return "booleano";
        return inferirTipoIgualdad(ctx.igualdad(0));
    }

    private String inferirTipoIgualdad(LenguajeParser.IgualdadContext ctx) {
        if (ctx.comparacion().size() > 1) return "booleano";
        return inferirTipoComparacion(ctx.comparacion(0));
    }

    private String inferirTipoComparacion(LenguajeParser.ComparacionContext ctx) {
        if (ctx.suma().size() > 1) return "booleano";
        return inferirTipoSuma(ctx.suma(0));
    }

    private String inferirTipoSuma(LenguajeParser.SumaContext ctx) {
        String tipo = inferirTipoMult(ctx.mult(0));

        for (int i = 1; i < ctx.mult().size(); i++) {
            String der = inferirTipoMult(ctx.mult(i));

            if ("cadena".equals(tipo) || "cadena".equals(der)
                    || "booleano".equals(tipo) || "booleano".equals(der)) {
                return "incompatible";
            }

            if ("real".equals(tipo) || "real".equals(der)) {
                tipo = "real";
            } else {
                tipo = "entero";
            }
        }

        return tipo;
    }

    private String inferirTipoMult(LenguajeParser.MultContext ctx) {
        String tipo = inferirTipoUnario(ctx.unario(0));

        for (int i = 1; i < ctx.unario().size(); i++) {
            String der = inferirTipoUnario(ctx.unario(i));

            if ("cadena".equals(tipo) || "cadena".equals(der)
                    || "booleano".equals(tipo) || "booleano".equals(der)) {
                return "incompatible";
            }

            if ("real".equals(tipo) || "real".equals(der)) {
                tipo = "real";
            } else {
                tipo = "entero";
            }
        }

        return tipo;
    }

    private String inferirTipoUnario(LenguajeParser.UnarioContext ctx) {
        if (ctx.primario() != null) {
            return inferirTipoPrimario(ctx.primario());
        }

        if (ctx.NO() != null) {
            return "booleano";
        }

        return inferirTipoUnario(ctx.u);
    }

    private String inferirTipoPrimario(LenguajeParser.PrimarioContext ctx) {
        if (ctx.NUMERO() != null) {
            return ctx.NUMERO().getText().contains(".") ? "real" : "entero";
        }

        if (ctx.VERDADERO() != null || ctx.FALSO() != null) {
            return "booleano";
        }

        if (ctx.CADENA_LIT() != null) {
            return "cadena";
        }

        if (ctx.ID() != null) {
            String id = ctx.ID().getText();
            return tipos.getOrDefault(id, "desconocido");
        }

        if (ctx.e != null) {
            return inferirTipo(ctx.e);
        }

        return "desconocido";
    }

    private boolean tiposCompatibles(String esperado, String recibido) {
        if (esperado.equals(recibido)) return true;
        if ("real".equals(esperado) && "entero".equals(recibido)) return true;
        return false;
    }

    @Override
    public Double visitPrograma(LenguajeParser.ProgramaContext ctx) {
        for (LenguajeParser.SentenciaContext s : ctx.sentencia()) {
            visit(s);
        }
        return 0.0;
    }

    @Override
    public Double visitSentencia(LenguajeParser.SentenciaContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Double visitBloque(LenguajeParser.BloqueContext ctx) {
        for (LenguajeParser.SentenciaBloqueContext s : ctx.sentenciaBloque()) {
            visit(s);
        }
        return 0.0;
    }

    @Override
    public Double visitSentenciaBloque(LenguajeParser.SentenciaBloqueContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Double visitDeclaracion(LenguajeParser.DeclaracionContext ctx) {
        String id = ctx.ID().getText();
        String tipoDeclarado = obtenerTipoDeclarado(ctx.tipo());

        if (tabla.containsKey(id)) {
            log("Error semántico: variable ya declarada -> " + id);
            return 0.0;
        }

        double valor = 0.0;

        if (ctx.e != null) {
            String tipoExpresion = inferirTipo(ctx.e);

            if (!tiposCompatibles(tipoDeclarado, tipoExpresion)) {
                log("Error semántico: Asignación de tipos incompatibles -> "
                        + id + " (" + tipoDeclarado + " = " + tipoExpresion + ")");
                return 0.0;
            }

            valor = visit(ctx.e);
        }

        tabla.put(id, valor);
        tipos.put(id, tipoDeclarado);
        return valor;
    }

    @Override
    public Double visitAsignacion(LenguajeParser.AsignacionContext ctx) {
        String id = ctx.ID().getText();

        if (!tabla.containsKey(id)) {
            log("Error semántico: variable no declarada -> " + id);
            return 0.0;
        }

        String tipoVariable = tipos.get(id);
        String tipoExpresion = inferirTipo(ctx.expresion());

        if (!tiposCompatibles(tipoVariable, tipoExpresion)) {
            log("Error semántico: Asignación de tipos incompatibles -> "
                    + id + " (" + tipoVariable + " = " + tipoExpresion + ")");
            return 0.0;
        }

        double valor = visit(ctx.expresion());
        tabla.put(id, valor);
        return valor;
    }

    @Override
    public Double visitImprimir(LenguajeParser.ImprimirContext ctx) {
        return visit(ctx.e);
    }

    @Override
    public Double visitSi(LenguajeParser.SiContext ctx) {
        double condicion = visit(ctx.expresion());
        visit(ctx.bloque(0));

        if (ctx.bloque().size() > 1) {
            visit(ctx.bloque(1));
        }

        return condicion;
    }

    @Override
    public Double visitMientras(LenguajeParser.MientrasContext ctx) {
        double condicion = visit(ctx.expresion());
        visit(ctx.bloque());
        return condicion;
    }

    @Override
    public Double visitExpresion(LenguajeParser.ExpresionContext ctx) {
        return visit(ctx.o);
    }

    @Override
    public Double visitOrExpr(LenguajeParser.OrExprContext ctx) {
        double result = visit(ctx.andExpr(0));

        for (int i = 1; i < ctx.andExpr().size(); i++) {
            double right = visit(ctx.andExpr(i));
            result = (result != 0 || right != 0) ? 1.0 : 0.0;
        }

        return result;
    }

    @Override
    public Double visitAndExpr(LenguajeParser.AndExprContext ctx) {
        double result = visit(ctx.igualdad(0));

        for (int i = 1; i < ctx.igualdad().size(); i++) {
            double right = visit(ctx.igualdad(i));
            result = (result != 0 && right != 0) ? 1.0 : 0.0;
        }

        return result;
    }

    @Override
    public Double visitIgualdad(LenguajeParser.IgualdadContext ctx) {
        double result = visit(ctx.comparacion(0));

        for (int i = 1; i < ctx.comparacion().size(); i++) {
            double right = visit(ctx.comparacion(i));

            if (ctx.IGUAL_IGUAL(i - 1) != null) {
                result = (result == right) ? 1.0 : 0.0;
            } else {
                result = (result != right) ? 1.0 : 0.0;
            }
        }

        return result;
    }

    @Override
    public Double visitComparacion(LenguajeParser.ComparacionContext ctx) {
        double result = visit(ctx.suma(0));

        for (int i = 1; i < ctx.suma().size(); i++) {
            double right = visit(ctx.suma(i));

            if (ctx.MENOR(i - 1) != null) {
                result = (result < right) ? 1.0 : 0.0;
            } else if (ctx.MENOR_IGUAL(i - 1) != null) {
                result = (result <= right) ? 1.0 : 0.0;
            } else if (ctx.MAYOR(i - 1) != null) {
                result = (result > right) ? 1.0 : 0.0;
            } else if (ctx.MAYOR_IGUAL(i - 1) != null) {
                result = (result >= right) ? 1.0 : 0.0;
            }
        }

        return result;
    }

    @Override
    public Double visitSuma(LenguajeParser.SumaContext ctx) {
        double result = visit(ctx.mult(0));

        for (int i = 1; i < ctx.mult().size(); i++) {
            if (ctx.MAS(i - 1) != null) {
                result += visit(ctx.mult(i));
            } else if (ctx.MENOS(i - 1) != null) {
                result -= visit(ctx.mult(i));
            }
        }

        return result;
    }

    @Override
    public Double visitMult(LenguajeParser.MultContext ctx) {
        double result = visit(ctx.unario(0));

        for (int i = 1; i < ctx.unario().size(); i++) {
            double right = visit(ctx.unario(i));

            if (ctx.POR(i - 1) != null) {
                result *= right;
            } else if (ctx.DIV(i - 1) != null) {
                if (right == 0) {
                    log("Error semántico: división entre cero");
                    return 0.0;
                }
                result /= right;
            }
        }

        return result;
    }

    @Override
    public Double visitUnario(LenguajeParser.UnarioContext ctx) {
        if (ctx.primario() != null) {
            return visit(ctx.primario());
        }

        double valor = visit(ctx.u);

        if (ctx.MENOS() != null) {
            return -valor;
        }

        if (ctx.NO() != null) {
            return (valor == 0) ? 1.0 : 0.0;
        }

        return valor;
    }

    @Override
    public Double visitPrimario(LenguajeParser.PrimarioContext ctx) {
        if (ctx.NUMERO() != null) {
            return Double.parseDouble(ctx.NUMERO().getText());
        }

        if (ctx.VERDADERO() != null) {
            return 1.0;
        }

        if (ctx.FALSO() != null) {
            return 0.0;
        }

        if (ctx.CADENA_LIT() != null) {
            return 0.0;
        }

        if (ctx.ID() != null) {
            String id = ctx.ID().getText();

            if (!tabla.containsKey(id)) {
                log("Error semántico: variable no definida -> " + id);
                return 0.0;
            }

            return tabla.get(id);
        }

        if (ctx.e != null) {
            return visit(ctx.e);
        }

        return 0.0;
    }
}
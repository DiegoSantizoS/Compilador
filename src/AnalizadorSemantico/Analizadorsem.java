package AnalizadorSemantico;

import generated.LenguajeBaseVisitor;
import generated.LenguajeParser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JTextArea;

public class Analizadorsem extends LenguajeBaseVisitor<Double> {

    private final Map<String, Double> tabla = new HashMap<>();
    private final List<String> errores = new ArrayList<>();
    private final JTextArea terminal;

    public Analizadorsem(JTextArea terminal) {
        this.terminal = terminal;
    }

    public Map<String, Double> getTabla() {
        return tabla;
    }

    public List<String> getErrores() {
        return errores;
    }

    private void log(String mensaje) {
        errores.add(mensaje);

        if (terminal != null) {
            terminal.append(mensaje + "\n");
        } else {
            System.out.println(mensaje);
        }
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

        if (tabla.containsKey(id)) {
            log("Error semántico: variable ya declarada -> " + id);
            return 0.0;
        }

        double valor = 0.0;

        if (ctx.e != null) {
            valor = visit(ctx.e);
        }

        tabla.put(id, valor);
        return valor;
    }

    @Override
    public Double visitAsignacion(LenguajeParser.AsignacionContext ctx) {
        String id = ctx.ID().getText();

        if (!tabla.containsKey(id)) {
            log("Error semántico: variable no declarada -> " + id);
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
package AnalizadorSemantico;

import generated.LenguajeBaseVisitor;
import generated.LenguajeParser;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JTextArea;

/**
 *
 * @author carlo
 */
public class Analizadorsem extends LenguajeBaseVisitor<Double> {

    Map<String, Double> tabla = new HashMap<>();

    private JTextArea terminal;

    public Analizadorsem(JTextArea terminal) {
        this.terminal = terminal;
    }

    @Override
    public Double visitDeclaracion(LenguajeParser.DeclaracionContext ctx) {
        String id = ctx.ID().getText();

        if (tabla.containsKey(id)) {
            System.out.println("Error: variable ya declarada " + id);
            return 0.0;
        }

        double valor = 0.0;

        if (ctx.e != null) {
            valor = visit(ctx.e);
        }

        tabla.put(id, valor);
        return 0.0;
    }

    @Override
    public Double visitAsignacion(LenguajeParser.AsignacionContext ctx) {
        String id = ctx.ID().getText();

        if (!tabla.containsKey(id)) {
            System.out.println("Error: variable no declarada " + id);
            return 0.0;
        }

        double valor = visit(ctx.expresion());
        tabla.put(id, valor);

        return valor;
    }

    @Override
    public Double visitSuma(LenguajeParser.SumaContext ctx) {
        double result = visit(ctx.mult(0));

        for (int i = 1; i < ctx.mult().size(); i++) {
            if (ctx.MAS(i - 1) != null) {
                result += visit(ctx.mult(i));
            } else {
                result -= visit(ctx.mult(i));
            }
        }

        return result;
    }

    @Override
    public Double visitPrimario(LenguajeParser.PrimarioContext ctx) {
        if (ctx.NUMERO() != null) {
            return Double.parseDouble(ctx.NUMERO().getText());
        }

        if (ctx.ID() != null) {
            String id = ctx.ID().getText();

            if (!tabla.containsKey(id)) {
                System.out.println("Error: variable no definida " + id);
                return 0.0;
            }

            return tabla.get(id);
        }

        return visit(ctx.expresion());
    }

}

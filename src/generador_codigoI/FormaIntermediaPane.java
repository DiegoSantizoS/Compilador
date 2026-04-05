package generador_codigoI;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import org.antlr.v4.runtime.tree.ParseTree;

public class FormaIntermediaPane extends JPanel {

    private final JLabel titulo;
    private final JTextArea areaCodigo;

    private final StringBuilder ir = new StringBuilder();
    private int labelCount = 0;

    public FormaIntermediaPane() {
        setLayout(new BorderLayout());

        titulo = new JLabel("Codigo Intermedio", SwingConstants.CENTER);
        add(titulo, BorderLayout.NORTH);

        areaCodigo = new JTextArea();
        areaCodigo.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(areaCodigo);
        add(scrollPane, BorderLayout.CENTER);
    }

    private String nuevaEtiqueta() {
        return "L" + (labelCount++);
    }

    public void generarYMostrarIR(ParseTree tree) {
        ir.setLength(0);
        labelCount = 0;
        recorrer(tree);
        areaCodigo.setText(ir.toString());
        areaCodigo.setCaretPosition(0);

        revalidate();
        repaint();
    }

    private String recorrer(ParseTree tree) {
        System.out.println("Nodo: " + tree.getClass().getSimpleName());

        // PROGRAMA
        if (tree instanceof generated.LenguajeParser.ProgramaContext ctx) {
            for (var s : ctx.sentencia()) {
                recorrer(s);
            }
        }

        // SENTENCIA
        else if (tree instanceof generated.LenguajeParser.SentenciaContext ctx) {
            recorrer(ctx.getChild(0));
        }

        // SENTENCIA EN BLOQUE
        else if (tree instanceof generated.LenguajeParser.SentenciaBloqueContext ctx) {
            recorrer(ctx.getChild(0));
        }

        // DECLARACION
        else if (tree instanceof generated.LenguajeParser.DeclaracionContext ctx) {
            String id = ctx.ID().getText();
            if (ctx.expresion() != null) {
                String expr = recorrer(ctx.expresion());
                ir.append("DECL_ASSIGN(").append(id).append(", ").append(expr).append(")\n");
            } else {
                ir.append("DECL(").append(id).append(")\n");
            }
        }

        // ASIGNACION
        else if (tree instanceof generated.LenguajeParser.AsignacionContext ctx) {
            String id = ctx.ID().getText();
            String expr = recorrer(ctx.expresion());
            ir.append("ASSIGN(").append(id).append(", ").append(expr).append(")\n");
        }

        // PRINT
        else if (tree instanceof generated.LenguajeParser.ImprimirContext ctx) {
            String expr = recorrer(ctx.expresion());
            ir.append("PRINT(").append(expr).append(")\n");
        }

        // IF
        else if (tree instanceof generated.LenguajeParser.SiContext ctx) {
            String cond = recorrer(ctx.expresion());
            String L1 = nuevaEtiqueta();
            String L2 = nuevaEtiqueta();

            ir.append("IF NOT ").append(cond).append(" GOTO ").append(L1).append("\n");
            recorrer(ctx.bloque(0));

            if (ctx.bloque().size() > 1) {
                ir.append("GOTO ").append(L2).append("\n");
                ir.append(L1).append(":\n");
                recorrer(ctx.bloque(1));
                ir.append(L2).append(":\n");
            } else {
                ir.append(L1).append(":\n");
            }
        }

        // WHILE
        else if (tree instanceof generated.LenguajeParser.MientrasContext ctx) {
            String L1 = nuevaEtiqueta();
            String L2 = nuevaEtiqueta();

            ir.append(L1).append(":\n");
            String cond = recorrer(ctx.expresion());
            ir.append("IF NOT ").append(cond).append(" GOTO ").append(L2).append("\n");
            recorrer(ctx.bloque());
            ir.append("GOTO ").append(L1).append("\n");
            ir.append(L2).append(":\n");
        }

        // BLOQUE
        else if (tree instanceof generated.LenguajeParser.BloqueContext ctx) {
            for (var s : ctx.sentenciaBloque()) {
                recorrer(s);
            }
        }

        else if (tree instanceof generated.LenguajeParser.ExpresionContext ctx) {
            return recorrer(ctx.getChild(0));
        }

        // OR
        else if (tree instanceof generated.LenguajeParser.OrExprContext ctx) {
            String result = recorrer(ctx.andExpr(0));
            for (int i = 1; i < ctx.andExpr().size(); i++) {
                String right = recorrer(ctx.andExpr(i));
                result = "OR(" + result + ", " + right + ")";
            }
            return result;
        }

        // AND
        else if (tree instanceof generated.LenguajeParser.AndExprContext ctx) {
            String result = recorrer(ctx.igualdad(0));
            for (int i = 1; i < ctx.igualdad().size(); i++) {
                String right = recorrer(ctx.igualdad(i));
                result = "AND(" + result + ", " + right + ")";
            }
            return result;
        }

        // == !=
        else if (tree instanceof generated.LenguajeParser.IgualdadContext ctx) {
            String result = recorrer(ctx.comparacion(0));
            for (int i = 1; i < ctx.comparacion().size(); i++) {
                String right = recorrer(ctx.comparacion(i));
                String op = ctx.getChild(2 * i - 1).getText();
                result = op.equals("==")
                        ? "EQ(" + result + ", " + right + ")"
                        : "NE(" + result + ", " + right + ")";
            }
            return result;
        }

        // > < >= <=
        else if (tree instanceof generated.LenguajeParser.ComparacionContext ctx) {
            String result = recorrer(ctx.suma(0));
            for (int i = 1; i < ctx.suma().size(); i++) {
                String right = recorrer(ctx.suma(i));
                String op = ctx.getChild(2 * i - 1).getText();
                result = switch (op) {
                    case ">"  -> "GT(" + result + ", " + right + ")";
                    case "<"  -> "LT(" + result + ", " + right + ")";
                    case ">=" -> "GE(" + result + ", " + right + ")";
                    case "<=" -> "LE(" + result + ", " + right + ")";
                    default   -> result;
                };
            }
            return result;
        }

        // + -
        else if (tree instanceof generated.LenguajeParser.SumaContext ctx) {
            String result = recorrer(ctx.mult(0));
            for (int i = 1; i < ctx.mult().size(); i++) {
                String right = recorrer(ctx.mult(i));
                String op = ctx.getChild(2 * i - 1).getText();
                result = op.equals("+")
                        ? "ADD(" + result + ", " + right + ")"
                        : "SUB(" + result + ", " + right + ")";
            }
            return result;
        }

        // * /
        else if (tree instanceof generated.LenguajeParser.MultContext ctx) {
            String result = recorrer(ctx.unario(0));
            for (int i = 1; i < ctx.unario().size(); i++) {
                String right = recorrer(ctx.unario(i));
                String op = ctx.getChild(2 * i - 1).getText();
                result = op.equals("*")
                        ? "MUL(" + result + ", " + right + ")"
                        : "DIV(" + result + ", " + right + ")";
            }
            return result;
        }

        // UNARIO
        else if (tree instanceof generated.LenguajeParser.UnarioContext ctx) {
            if (ctx.unario() != null) {
                String val = recorrer(ctx.unario());
                String op = ctx.getChild(0).getText();
                return op.equals("!") ? "NOT(" + val + ")" : "NEG(" + val + ")";
            }
            return recorrer(ctx.primario());
        }

        // PRIMARIO
        else if (tree instanceof generated.LenguajeParser.PrimarioContext ctx) {
            if (ctx.expresion() != null) return recorrer(ctx.expresion());
            if (ctx.NUMERO() != null) return "CONST(" + ctx.NUMERO().getText() + ")";
            if (ctx.ID() != null) return "VAR(" + ctx.ID().getText() + ")";
            if (ctx.VERDADERO() != null) return "TRUE";
            if (ctx.FALSO() != null) return "FALSE";
            if (ctx.CADENA_LIT() != null) return "STR(" + ctx.CADENA_LIT().getText() + ")";
            if (ctx.leer() != null) return "READ()";
        }

        return "";
    }
}
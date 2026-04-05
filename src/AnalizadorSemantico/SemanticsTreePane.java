package AnalizadorSemantico;

import generated.LenguajeLexer;
import generated.LenguajeParser;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.Tree;

public class SemanticsTreePane extends JPanel {

    private final JLabel titulo;

    public SemanticsTreePane() {
        setLayout(new BorderLayout());

        titulo = new JLabel("Árbol Semántico", SwingConstants.CENTER);
        add(titulo, BorderLayout.NORTH);
    }

    public void showTreeGui(String codigo) {
        CharStream input = CharStreams.fromString(codigo);
        LenguajeLexer lexer = new LenguajeLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LenguajeParser parser = new LenguajeParser(tokens);

        ParseTree parseTree = parser.programa();

        Analizadorsem analizador = new Analizadorsem(null);
        analizador.visit(parseTree);

        SemanticNode semanticRoot = buildSemanticTree(parseTree, parser, analizador);

        removeAll();
        setLayout(new BorderLayout());
        add(titulo, BorderLayout.NORTH);

        TreeViewer viewer = new TreeViewer(Arrays.asList(parser.getRuleNames()), semanticRoot);
        JScrollPane scrollPane = new JScrollPane(viewer);

        add(scrollPane, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private SemanticNode buildSemanticTree(
            ParseTree node,
            LenguajeParser parser,
            Analizadorsem analizador
    ) {
        String label = getSemanticLabel(node, parser, analizador);
        SemanticNode semanticNode = new SemanticNode(label);

        for (int i = 0; i < node.getChildCount(); i++) {
            semanticNode.addChild(buildSemanticTree(node.getChild(i), parser, analizador));
        }

        return semanticNode;
    }

    private String getSemanticLabel(
            ParseTree node,
            LenguajeParser parser,
            Analizadorsem analizador
    ) {
        if (node instanceof TerminalNode terminal) {
            return terminal.getText();
        }

        if (!(node instanceof ParserRuleContext ruleNode)) {
            return node.getText();
        }

        String base = parser.getRuleNames()[ruleNode.getRuleIndex()];

        try {
            if (node instanceof LenguajeParser.ProgramaContext) {
                return "programa";
            }

            if (node instanceof LenguajeParser.SentenciaContext) {
                return "sentencia";
            }

            if (node instanceof LenguajeParser.SentenciaBloqueContext) {
                return "sentenciaBloque";
            }

            if (node instanceof LenguajeParser.BloqueContext) {
                return "bloque";
            }

            if (node instanceof LenguajeParser.DeclaracionContext ctx) {
                String id = ctx.ID().getText();
                String tipo = ctx.tipo().getText();

                if (ctx.e != null) {
                    String tipoExp = analizador.inferirTipo(ctx.e);
                    String valorVisual = obtenerValorVisual(ctx.e, analizador);
                    return "declaracion -> " + id + " : " + tipo + " = " + valorVisual + " [" + tipoExp + "]";
                }

                return "declaracion -> " + id + " : " + tipo;
            }

            if (node instanceof LenguajeParser.AsignacionContext ctx) {
                String id = ctx.ID().getText();
                String tipo = analizador.inferirTipo(ctx.expresion());
                String valorVisual = obtenerValorVisual(ctx.expresion(), analizador);
                return "asignacion -> " + id + " = " + valorVisual + " [" + tipo + "]";
            }

            if (node instanceof LenguajeParser.ImprimirContext ctx) {
                String tipo = analizador.inferirTipo(ctx.e);
                String valorVisual = obtenerValorVisual(ctx.e, analizador);
                return "imprimir -> valor = " + valorVisual + " [" + tipo + "]";
            }

            if (node instanceof LenguajeParser.SiContext ctx) {
                String valorVisual = obtenerValorVisual(ctx.expresion(), analizador);
                return "si -> condicion = " + valorVisual;
            }

            if (node instanceof LenguajeParser.MientrasContext ctx) {
                String valorVisual = obtenerValorVisual(ctx.expresion(), analizador);
                return "mientras -> condicion = " + valorVisual;
            }

            if (node instanceof LenguajeParser.ExpresionContext ctx) {
                String tipo = analizador.inferirTipo(ctx);
                String valorVisual = obtenerValorVisual(ctx, analizador);
                return "expresion -> valor = " + valorVisual + " [" + tipo + "]";
            }

            if (node instanceof LenguajeParser.OrExprContext ctx) {
                String valorVisual = obtenerValorVisual(ctx, analizador);
                return "orExpr -> valor = " + valorVisual;
            }

            if (node instanceof LenguajeParser.AndExprContext ctx) {
                String valorVisual = obtenerValorVisual(ctx, analizador);
                return "andExpr -> valor = " + valorVisual;
            }

            if (node instanceof LenguajeParser.IgualdadContext ctx) {
                String valorVisual = obtenerValorVisual(ctx, analizador);
                return "igualdad -> valor = " + valorVisual;
            }

            if (node instanceof LenguajeParser.ComparacionContext ctx) {
                String valorVisual = obtenerValorVisual(ctx, analizador);
                return "comparacion -> valor = " + valorVisual;
            }

            if (node instanceof LenguajeParser.SumaContext ctx) {
                String valorVisual = obtenerValorVisual(ctx, analizador);
                return "suma -> valor = " + valorVisual;
            }

            if (node instanceof LenguajeParser.MultContext ctx) {
                String valorVisual = obtenerValorVisual(ctx, analizador);
                return "mult -> valor = " + valorVisual;
            }

            if (node instanceof LenguajeParser.UnarioContext ctx) {
                String valorVisual = obtenerValorVisual(ctx, analizador);
                return "unario -> valor = " + valorVisual;
            }

            if (node instanceof LenguajeParser.PrimarioContext ctx) {
                if (ctx.NUMERO() != null) {
                    String numero = ctx.NUMERO().getText();
                    String tipo = numero.contains(".") ? "real" : "entero";
                    return "primario -> numero = " + numero + " [" + tipo + "]";
                }

                if (ctx.VERDADERO() != null) {
                    return "primario -> booleano = verdadero";
                }

                if (ctx.FALSO() != null) {
                    return "primario -> booleano = falso";
                }

                if (ctx.CADENA_LIT() != null) {
                    return "primario -> cadena = " + ctx.CADENA_LIT().getText();
                }

                if (ctx.ID() != null) {
                    String id = ctx.ID().getText();
                    Map<String, Double> tabla = analizador.getTabla();
                    Map<String, String> tipos = analizador.getTipos();

                    if (tabla.containsKey(id)) {
                        String tipo = tipos.getOrDefault(id, "desconocido");

                        if ("booleano".equals(tipo)) {
                            return "primario -> id = " + id + ", valor = "
                                    + (tabla.get(id) != 0 ? "verdadero" : "falso")
                                    + " [" + tipo + "]";
                        }

                        if ("cadena".equals(tipo)) {
                            return "primario -> id = " + id + " [" + tipo + "]";
                        }

                        return "primario -> id = " + id
                                + ", valor = " + tabla.get(id)
                                + " [" + tipo + "]";
                    }

                    return "primario -> id = " + id + " [ERROR: no definida]";
                }

                if (ctx.e != null) {
                    String tipo = analizador.inferirTipo(ctx.e);
                    String valorVisual = obtenerValorVisual(ctx.e, analizador);
                    return "primario -> (expresion) = " + valorVisual + " [" + tipo + "]";
                }
            }

        } catch (Exception e) {
            return base + " [ERROR]";
        }

        return base;
    }

    private String obtenerValorVisual(ParseTree node, Analizadorsem analizador) {
        if (node instanceof LenguajeParser.ExpresionContext ctx) {
            String tipo = analizador.inferirTipo(ctx);

            if ("cadena".equals(tipo)) {
                return extraerTextoExpresion(ctx);
            }

            if ("booleano".equals(tipo)) {
                Double valor = analizador.visit(ctx);
                return (valor != null && valor != 0) ? "verdadero" : "falso";
            }

            Double valor = analizador.visit(ctx);
            return valor != null ? String.valueOf(valor) : "null";
        }

        if (node instanceof LenguajeParser.OrExprContext ctx) {
            Double valor = analizador.visit(ctx);
            return valor != null ? String.valueOf(valor) : "null";
        }

        if (node instanceof LenguajeParser.AndExprContext ctx) {
            Double valor = analizador.visit(ctx);
            return valor != null ? String.valueOf(valor) : "null";
        }

        if (node instanceof LenguajeParser.IgualdadContext ctx) {
            Double valor = analizador.visit(ctx);
            return valor != null ? String.valueOf(valor) : "null";
        }

        if (node instanceof LenguajeParser.ComparacionContext ctx) {
            Double valor = analizador.visit(ctx);
            return valor != null ? String.valueOf(valor) : "null";
        }

        if (node instanceof LenguajeParser.SumaContext ctx) {
            Double valor = analizador.visit(ctx);
            return valor != null ? String.valueOf(valor) : "null";
        }

        if (node instanceof LenguajeParser.MultContext ctx) {
            Double valor = analizador.visit(ctx);
            return valor != null ? String.valueOf(valor) : "null";
        }

        if (node instanceof LenguajeParser.UnarioContext ctx) {
            Double valor = analizador.visit(ctx);
            return valor != null ? String.valueOf(valor) : "null";
        }

        if (node instanceof LenguajeParser.PrimarioContext ctx) {
            if (ctx.CADENA_LIT() != null) {
                return ctx.CADENA_LIT().getText();
            }

            if (ctx.VERDADERO() != null) {
                return "verdadero";
            }

            if (ctx.FALSO() != null) {
                return "falso";
            }

            if (ctx.NUMERO() != null) {
                return ctx.NUMERO().getText();
            }

            if (ctx.ID() != null) {
                String id = ctx.ID().getText();
                String tipo = analizador.getTipos().getOrDefault(id, "desconocido");

                if ("booleano".equals(tipo) && analizador.getTabla().containsKey(id)) {
                    return analizador.getTabla().get(id) != 0 ? "verdadero" : "falso";
                }

                if ("cadena".equals(tipo)) {
                    return id;
                }

                if (analizador.getTabla().containsKey(id)) {
                    return String.valueOf(analizador.getTabla().get(id));
                }

                return id;
            }

            if (ctx.e != null) {
                return obtenerValorVisual(ctx.e, analizador);
            }
        }

        return node.getText();
    }

    private String extraerTextoExpresion(ParseTree node) {
        if (node instanceof LenguajeParser.PrimarioContext ctx) {
            if (ctx.CADENA_LIT() != null) {
                return ctx.CADENA_LIT().getText();
            }

            if (ctx.ID() != null) {
                return ctx.ID().getText();
            }

            if (ctx.e != null) {
                return extraerTextoExpresion(ctx.e);
            }
        }

        for (int i = 0; i < node.getChildCount(); i++) {
            String texto = extraerTextoExpresion(node.getChild(i));
            if (texto != null) {
                return texto;
            }
        }

        return node.getText();
    }

    private static class SemanticNode implements Tree {

        private final String text;
        private final List<Tree> children = new ArrayList<>();
        private Tree parent;

        public SemanticNode(String text) {
            this.text = text;
        }

        public void addChild(SemanticNode child) {
            child.parent = this;
            children.add(child);
        }

        @Override
        public Tree getParent() {
            return parent;
        }

        @Override
        public Tree getChild(int i) {
            return children.get(i);
        }

        @Override
        public int getChildCount() {
            return children.size();
        }

        @Override
        public Object getPayload() {
            return text;
        }

        @Override
        public String toStringTree() {
            if (children.isEmpty()) {
                return text;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("(").append(text);

            for (Tree child : children) {
                sb.append(" ").append(child.toString());
            }

            sb.append(")");
            return sb.toString();
        }

        @Override
        public String toString() {
            return text;
        }
    }
}
package AnalizadorSemantico;

import generated.LenguajeLexer;
import generated.LenguajeParser;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

public class SemanticsTreePane extends JPanel {

    private final JLabel titulo;

    public SemanticsTreePane() {
        setLayout(new BorderLayout());

        titulo = new JLabel("Árbol Semántico", SwingConstants.CENTER);
        add(titulo, BorderLayout.NORTH);
        
        
    }
    
    DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer() {
        @Override
        public Component getTreeCellRendererComponent(
                JTree tree,
                Object value,
                boolean sel,
                boolean expanded,
                boolean leaf,
                int row,
                boolean hasFocus
        ) {
            Component c = super.getTreeCellRendererComponent(
                    tree, value, sel, expanded, leaf, row, hasFocus
            );

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            String texto = node.getUserObject().toString();

            setLeafIcon(null);
            setClosedIcon(null);
            setOpenIcon(null);

            if (texto.equals("sentencia")) {
                setForeground(new Color(180, 60, 60));
            } else {
                setForeground(Color.BLACK);
            }

            return c;
        }
    };

    public void showTreeGui(String codigo) {
        CharStream input = CharStreams.fromString(codigo);
        LenguajeLexer lexer = new LenguajeLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LenguajeParser parser = new LenguajeParser(tokens);

        ParseTree tree = parser.programa();

        Analizadorsem analizador = new Analizadorsem(null);
        analizador.visit(tree);

        DefaultMutableTreeNode root = buildSemanticNode(tree, parser, analizador);
        JTree semanticTree = new JTree(root);
        expandAll(semanticTree);

        removeAll();
        setLayout(new BorderLayout());
        add(titulo, BorderLayout.NORTH);
        add(new JScrollPane(semanticTree), BorderLayout.CENTER);
        
        semanticTree.setCellRenderer(renderer);
        revalidate();
        repaint();
    }

    private DefaultMutableTreeNode buildSemanticNode(
            ParseTree node,
            LenguajeParser parser,
            Analizadorsem analizador
    ) {
        String label = getSemanticLabel(node, parser, analizador);
        DefaultMutableTreeNode visualNode = new DefaultMutableTreeNode(label);

        for (int i = 0; i < node.getChildCount(); i++) {
            visualNode.add(buildSemanticNode(node.getChild(i), parser, analizador));
        }

        return visualNode;
    }

    private String getSemanticLabel(
            ParseTree node,
            LenguajeParser parser,
            Analizadorsem analizador
    ) {
        if (node instanceof TerminalNode terminal) {
            return terminal.getText();
        }

        if (!(node instanceof org.antlr.v4.runtime.ParserRuleContext ruleNode)) {
            return node.getText();
        }

        String base = parser.getRuleNames()[ruleNode.getRuleIndex()];

        try {
            if (node instanceof LenguajeParser.ProgramaContext) {
                return "programa";
            }

            if (node instanceof LenguajeParser.DeclaracionContext ctx) {
                String id = ctx.ID().getText();
                String tipo = ctx.tipo().getText();
                if (ctx.e != null) {
                    Double valor = analizador.visit(ctx.e);
                    return "declaracion -> " + id + " : " + tipo + " = " + valor;
                }
                return "declaracion -> " + id + " : " + tipo;
            }

            if (node instanceof LenguajeParser.AsignacionContext ctx) {
                String id = ctx.ID().getText();
                Double valor = analizador.getTabla().get(id);
                return "asignacion -> " + id + " = " + valor;
            }

            if (node instanceof LenguajeParser.ImprimirContext ctx) {
                Double valor = analizador.visit(ctx.e);
                return "imprimir -> valor = " + valor;
            }

            if (node instanceof LenguajeParser.SiContext ctx) {
                Double condicion = analizador.visit(ctx.expresion());
                return "si -> condicion = " + condicion;
            }

            if (node instanceof LenguajeParser.MientrasContext ctx) {
                Double condicion = analizador.visit(ctx.expresion());
                return "mientras -> condicion = " + condicion;
            }

            if (node instanceof LenguajeParser.ExpresionContext ctx) {
                Double valor = analizador.visit(ctx);
                return "expresion -> valor = " + valor;
            }

            if (node instanceof LenguajeParser.OrExprContext ctx) {
                Double valor = analizador.visit(ctx);
                return "orExpr -> valor = " + valor;
            }

            if (node instanceof LenguajeParser.AndExprContext ctx) {
                Double valor = analizador.visit(ctx);
                return "andExpr -> valor = " + valor;
            }

            if (node instanceof LenguajeParser.IgualdadContext ctx) {
                Double valor = analizador.visit(ctx);
                return "igualdad -> valor = " + valor;
            }

            if (node instanceof LenguajeParser.ComparacionContext ctx) {
                Double valor = analizador.visit(ctx);
                return "comparacion -> valor = " + valor;
            }

            if (node instanceof LenguajeParser.SumaContext ctx) {
                Double valor = analizador.visit(ctx);
                return "suma -> valor = " + valor;
            }

            if (node instanceof LenguajeParser.MultContext ctx) {
                Double valor = analizador.visit(ctx);
                return "mult -> valor = " + valor;
            }

            if (node instanceof LenguajeParser.UnarioContext ctx) {
                Double valor = analizador.visit(ctx);
                return "unario -> valor = " + valor;
            }

            if (node instanceof LenguajeParser.PrimarioContext ctx) {
                if (ctx.NUMERO() != null) {
                    return "primario -> numero = " + ctx.NUMERO().getText();
                }

                if (ctx.ID() != null) {
                    String id = ctx.ID().getText();
                    Map<String, Double> tabla = analizador.getTabla();

                    if (tabla.containsKey(id)) {
                        return "primario -> id = " + id + ", valor = " + tabla.get(id);
                    }

                    return "primario -> id = " + id + " [ERROR: no definida]";
                }

                if (ctx.e != null) {
                    Double valor = analizador.visit(ctx.e);
                    return "primario -> (expresion) = " + valor;
                }
            }
        } catch (Exception e) {
            return base + " [ERROR]";
        }

        return base;
    }

    private void expandAll(JTree tree) {
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
    }
}
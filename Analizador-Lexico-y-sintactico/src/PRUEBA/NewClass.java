package PRUEBA;

import generated.LenguajeLexer;
import generated.LenguajeParser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

// GUI tree viewer
import org.antlr.v4.gui.TreeViewer;

import javax.swing.*;
import java.util.Arrays;

// DOT export
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class NewClass {

    // Listener de errores más claro
    static class ThrowingErrorListener extends BaseErrorListener {
        @Override
        public void syntaxError(Recognizer<?, ?> recognizer,
                                Object offendingSymbol,
                                int line, int charPositionInLine,
                                String msg,
                                RecognitionException e) {
            throw new RuntimeException("Error de sintaxis en línea " + line + ":" + charPositionInLine + " -> " + msg);
        }
    }

    public static void main(String[] args) {

        String input = """
            entero x = 5;
            imprimir x;

            mientras (x > 0) {
                x = x - 1;
                imprimir x;
            }

            si (x == 0) {
                imprimir "termino";
            } sino {
                imprimir "no termino";
            }

            booleano ok = verdadero;
            imprimir ok;

            cadena nombre = leer();
            imprimir nombre;
            """;

        // 1) CharStream
        CharStream cs = CharStreams.fromString(input);

        // 2) Lexer
        LenguajeLexer lexer = new LenguajeLexer(cs);

        // Token stream
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        tokenStream.fill();

        System.out.println("TOKENS:");
        for (Token t : tokenStream.getTokens()) {
            if (t.getType() == Token.EOF) break;
            String type = lexer.getVocabulary().getSymbolicName(t.getType());
            System.out.println("'" + t.getText() + "' -> " + type);
        }

        // 3) Parser
        LenguajeParser parser = new LenguajeParser(tokenStream);

        parser.removeErrorListeners();
        parser.addErrorListener(new ThrowingErrorListener());

        // Regla inicial
        ParseTree tree = parser.programa();

        System.out.println("\nPARSE TREE (text):");
        System.out.println(tree.toStringTree(parser));

        // ✅ A) Visual GUI parse tree (TreeViewer)
        showTreeGui(parser, tree);

        // ✅ B) Export DOT file for Graphviz rendering
        try {
            exportParseTreeToDot(parser, tree, Path.of("parse_tree.dot"));
            System.out.println("\nDOT generado: parse_tree.dot");
            System.out.println("Convierte a PNG con:");
            System.out.println("  dot -Tpng parse_tree.dot -o parse_tree.png");
            System.out.println("O a SVG con:");
            System.out.println("  dot -Tsvg parse_tree.dot -o parse_tree.svg");
        } catch (IOException e) {
            System.err.println("No se pudo escribir el archivo DOT: " + e.getMessage());
        }
    }

    // -------------------------
    // A) GUI Viewer
    // -------------------------
    private static void showTreeGui(LenguajeParser parser, ParseTree tree) {
        JFrame frame = new JFrame("ANTLR Parse Tree");
        JPanel panel = new JPanel();

        TreeViewer viewer = new TreeViewer(Arrays.asList(parser.getRuleNames()), tree);
        viewer.setScale(1.2); // zoom

        panel.add(viewer);

        JScrollPane scroll = new JScrollPane(panel);
        frame.add(scroll);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(900, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // -------------------------
    // B) DOT Export (Graphviz)
    // -------------------------
    private static void exportParseTreeToDot(LenguajeParser parser, ParseTree tree, Path outFile) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph ParseTree {\n");
        sb.append("  node [shape=box, fontname=\"Arial\"];\n");

        int[] id = {0};
        buildDot(tree, parser, sb, -1, id);

        sb.append("}\n");

        Files.writeString(outFile, sb.toString(), StandardCharsets.UTF_8);
    }

    private static void buildDot(ParseTree node,
                                 LenguajeParser parser,
                                 StringBuilder sb,
                                 int parentId,
                                 int[] idCounter) {

        int myId = idCounter[0]++;

        String label = escapeDotLabel(getNodeLabel(node, parser));
        sb.append("  n").append(myId).append(" [label=\"").append(label).append("\"];\n");

        if (parentId >= 0) {
            sb.append("  n").append(parentId).append(" -> n").append(myId).append(";\n");
        }

        for (int i = 0; i < node.getChildCount(); i++) {
            buildDot((ParseTree) node.getChild(i), parser, sb, myId, idCounter);
        }
    }

    private static String getNodeLabel(ParseTree node, LenguajeParser parser) {
        // Terminal nodes have text, rule nodes show rule name
        if (node instanceof org.antlr.v4.runtime.tree.TerminalNode) {
            String text = node.getText();
            // shorten long literals a bit
            if (text.length() > 30) text = text.substring(0, 30) + "...";
            return "T: " + text;
        }
        if (node instanceof org.antlr.v4.runtime.tree.RuleNode) {
            int ruleIndex = ((org.antlr.v4.runtime.tree.RuleNode) node).getRuleContext().getRuleIndex();
            return parser.getRuleNames()[ruleIndex];
        }
        return node.getText();
    }

    private static String escapeDotLabel(String s) {
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "");
    }
}

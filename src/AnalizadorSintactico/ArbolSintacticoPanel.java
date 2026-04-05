package AnalizadorSintactico;

import generated.LenguajeLexer;
import generated.LenguajeParser;
import java.awt.BorderLayout;
import java.util.Arrays;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class ArbolSintacticoPanel extends JPanel {

    private final JLabel titulo;

    public ArbolSintacticoPanel() {
        setLayout(new BorderLayout());

        titulo = new JLabel("Árbol Sintáctico", SwingConstants.CENTER);
        add(titulo, BorderLayout.NORTH);
    }

    public void showTreeGui(String codigo) {
        CharStream input = CharStreams.fromString(codigo);
        LenguajeLexer lexer = new LenguajeLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LenguajeParser parser = new LenguajeParser(tokens);

        ParseTree tree = parser.programa();

        removeAll();
        setLayout(new BorderLayout());
        add(titulo, BorderLayout.NORTH);

        TreeViewer viewer = new TreeViewer(Arrays.asList(parser.getRuleNames()), tree);
        JScrollPane scrollPane = new JScrollPane(viewer);

        add(scrollPane, BorderLayout.CENTER);

        revalidate();
        repaint();
    }
}
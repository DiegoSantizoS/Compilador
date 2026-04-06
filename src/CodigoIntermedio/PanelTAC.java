package CodigoIntermedio;

import generated.LenguajeLexer;
import generated.LenguajeParser;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class PanelTAC extends JPanel {

    private final JLabel titulo;
    private final JTextArea areaCodigo;

    public PanelTAC() {
        setLayout(new BorderLayout());

        titulo = new JLabel("Código de Tres Direcciones (TAC)", SwingConstants.CENTER);
        titulo.setPreferredSize(new java.awt.Dimension(0, 35));
        add(titulo, BorderLayout.NORTH);
        
        areaCodigo = new JTextArea();
        areaCodigo.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(areaCodigo);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void showTAC(String codigoFuente) {
        CharStream input = CharStreams.fromString(codigoFuente);
        LenguajeLexer lexer = new LenguajeLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LenguajeParser parser = new LenguajeParser(tokens);

        ParseTree tree = parser.programa();

        GeneradorTAC generador = new GeneradorTAC();
        generador.visit(tree);

        areaCodigo.setText(generador.getTAC());
        areaCodigo.setCaretPosition(0);

        revalidate();
        repaint();
    }
}
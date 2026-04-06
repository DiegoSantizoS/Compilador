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

        java.awt.Color PANEL_BG = new java.awt.Color(30, 37, 40);    // same as PiccoloTreePanel
        java.awt.Color TITLE_COLOR = new java.awt.Color(221, 230, 234);
        java.awt.Color TEXT_COLOR = new java.awt.Color(231, 238, 241);

        java.awt.Font titleFont = new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14);
        java.awt.Font codeFont = new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13);

        setBackground(PANEL_BG);

        titulo = new JLabel("Código de Tres Direcciones (TAC)", SwingConstants.CENTER);
        titulo.setPreferredSize(new java.awt.Dimension(0, 35));
        titulo.setOpaque(true);
        titulo.setBackground(PANEL_BG);   // same as panel
        titulo.setForeground(TITLE_COLOR);
        titulo.setFont(titleFont);
        add(titulo, BorderLayout.NORTH);

        areaCodigo = new JTextArea();
        areaCodigo.setEditable(false);
        areaCodigo.setBackground(PANEL_BG);
        areaCodigo.setForeground(TEXT_COLOR);
        areaCodigo.setCaretColor(TEXT_COLOR);
        areaCodigo.setFont(codeFont);

        JScrollPane scrollPane = new JScrollPane(areaCodigo);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(PANEL_BG);
        scrollPane.setBackground(PANEL_BG);

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
        areaCodigo.setFocusable(false);

        revalidate();
        repaint();
    }
}
package CodigoOptimizado;

import CodigoIntermedio.GeneradorTAC;
import generated.LenguajeLexer;
import generated.LenguajeParser;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Collections;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class PanelCodigoOptimizado extends JPanel {

    private static final Color PANEL_BG    = new Color(30, 37, 40);
    private static final Color TITLE_COLOR = new Color(221, 230, 234);
    private static final Color TEXT_COLOR  = new Color(231, 238, 241);

    private final JTextArea areaCodigo;
    private String codigoOptimizado = "";
    private List<String> ultimoLog  = Collections.emptyList();

    public PanelCodigoOptimizado() {
        setLayout(new BorderLayout());
        setBackground(PANEL_BG);

        JLabel titulo = new JLabel("Código TAC Optimizado", SwingConstants.CENTER);
        titulo.setPreferredSize(new Dimension(0, 35));
        titulo.setOpaque(true);
        titulo.setBackground(PANEL_BG);
        titulo.setForeground(TITLE_COLOR);
        titulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(titulo, BorderLayout.NORTH);

        areaCodigo = new JTextArea();
        areaCodigo.setEditable(false);
        areaCodigo.setBackground(PANEL_BG);
        areaCodigo.setForeground(TEXT_COLOR);
        areaCodigo.setCaretColor(TEXT_COLOR);
        areaCodigo.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JScrollPane scroll = new JScrollPane(areaCodigo);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(PANEL_BG);
        scroll.setBackground(PANEL_BG);
        add(scroll, BorderLayout.CENTER);
    }

    public void showOptimizado(String codigoFuente) {
        CharStream input = CharStreams.fromString(codigoFuente);
        LenguajeLexer lexer = new LenguajeLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LenguajeParser parser = new LenguajeParser(tokens);
        parser.removeErrorListeners();
        lexer.removeErrorListeners();

        ParseTree tree = parser.programa();
        GeneradorTAC generador = new GeneradorTAC();
        generador.visit(tree);

        OptimizadorTAC optimizador = new OptimizadorTAC();
        codigoOptimizado = optimizador.optimizar(generador.getTAC());
        ultimoLog        = optimizador.getLog();

        areaCodigo.setText(codigoOptimizado);
        areaCodigo.setCaretPosition(0);
        areaCodigo.setFocusable(false);
        revalidate();
        repaint();
    }

    public String getCodigoOptimizado() { return codigoOptimizado; }
    public List<String> getLog()        { return ultimoLog; }
}

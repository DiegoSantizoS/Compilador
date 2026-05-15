package CodigoOptimizado;

import CodigoIntermedio.GeneradorTAC;
import generated.LenguajeLexer;
import generated.LenguajeParser;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class PanelCodigoOptimizado extends JPanel {

    private static final Color PANEL_BG     = new Color(30, 37, 40);
    private static final Color PANEL_BG2    = new Color(25, 32, 35);
    private static final Color TITLE_COLOR  = new Color(221, 230, 234);
    private static final Color TEXT_COLOR   = new Color(231, 238, 241);

    private final JTextArea areaCodigo;
    private final JTextArea areaResumen;
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

        JScrollPane scrollCodigo = new JScrollPane(areaCodigo);
        scrollCodigo.setBorder(null);
        scrollCodigo.getViewport().setBackground(PANEL_BG);
        scrollCodigo.setBackground(PANEL_BG);

        areaResumen = new JTextArea();
        areaResumen.setEditable(false);
        areaResumen.setBackground(PANEL_BG2);
        areaResumen.setForeground(TEXT_COLOR);
        areaResumen.setCaretColor(TEXT_COLOR);
        areaResumen.setFont(new Font("Consolas", Font.PLAIN, 12));

        JScrollPane scrollResumen = new JScrollPane(areaResumen);
        scrollResumen.setBorder(null);
        scrollResumen.getViewport().setBackground(PANEL_BG2);
        scrollResumen.setBackground(PANEL_BG2);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollCodigo, scrollResumen);
        split.setResizeWeight(0.6);
        split.setDividerSize(4);
        split.setBorder(null);
        split.setBackground(PANEL_BG);
        add(split, BorderLayout.CENTER);
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

        areaResumen.setText(buildResumen(ultimoLog));
        areaResumen.setCaretPosition(0);
        areaResumen.setFocusable(false);

        revalidate();
        repaint();
    }

    public String getCodigoOptimizado() { return codigoOptimizado; }
    public List<String> getLog()        { return ultimoLog; }

    private String buildResumen(List<String> log) {
        List<String> locales = new ArrayList<>();
        List<String> fuerza  = new ArrayList<>();
        List<String> muerto  = new ArrayList<>();

        List<String> current = null;
        for (String line : log) {
            String trim = line.trim();
            if (trim.startsWith("Optimización Local")) {
                current = locales;
            } else if (trim.startsWith("Reducción de Fuerza")) {
                current = fuerza;
            } else if (trim.startsWith("Eliminación de Código Muerto")) {
                current = muerto;
            } else if (current != null && !trim.isEmpty() && !trim.startsWith("===")) {
                current.add(line);
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== Resumen de Optimizaciones Aplicadas ===\n\n");
        sb.append(formatCategory("[ Optimizaciones Locales (Constant Folding + Propagación de Constantes) ]", locales));
        sb.append("\n");
        sb.append(formatCategory("[ Reducción de Fuerza ]", fuerza));
        sb.append("\n");
        sb.append(formatCategory("[ Eliminación de Código Muerto ]", muerto));
        return sb.toString();
    }

    private String formatCategory(String header, List<String> entries) {
        List<String> actual = new ArrayList<>();
        for (String e : entries) {
            if (!e.trim().equals("(ninguna aplicada)")) actual.add(e);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(header).append("\n");
        if (actual.isEmpty()) {
            sb.append("  (ninguna aplicada)\n");
        } else {
            for (String e : actual) sb.append(e).append("\n");
        }
        return sb.toString();
    }
}

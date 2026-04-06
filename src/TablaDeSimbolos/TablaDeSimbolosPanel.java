package TablaDeSimbolos;

import generated.LenguajeLexer;
import generated.LenguajeParser;
import java.awt.BorderLayout;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class TablaDeSimbolosPanel extends JPanel {

    private final JTable tabla;
    private final DefaultTableModel modelo;
    private final GeneradorTabla listener;

    public TablaDeSimbolosPanel() {
        setLayout(new BorderLayout());

        java.awt.Color PANEL_BG = new java.awt.Color(30, 37, 40);
        java.awt.Color TITLE_COLOR = new java.awt.Color(221, 230, 234);
        java.awt.Color TEXT_COLOR = new java.awt.Color(231, 238, 241);
        java.awt.Color HEADER_BG = new java.awt.Color(41, 49, 52);
        java.awt.Color GRID_COLOR = new java.awt.Color(74, 90, 96);

        java.awt.Font titleFont = new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14);
        java.awt.Font tableFont = new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13);
        java.awt.Font headerFont = new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13);

        setBackground(PANEL_BG);

        JLabel titulo = new JLabel("Tabla de Símbolos", SwingConstants.CENTER);
        titulo.setPreferredSize(new java.awt.Dimension(0, 35));
        titulo.setOpaque(true);
        titulo.setBackground(PANEL_BG);
        titulo.setForeground(TITLE_COLOR);
        titulo.setFont(titleFont);

        modelo = new DefaultTableModel(
            new Object[]{"No", "Nombre", "Tipo", "Línea"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        tabla.setFont(tableFont);
        tabla.setRowHeight(24);
        tabla.setBackground(PANEL_BG);
        tabla.setForeground(TEXT_COLOR);
        tabla.setSelectionBackground(HEADER_BG);
        tabla.setSelectionForeground(TEXT_COLOR);
        tabla.setGridColor(GRID_COLOR);
        tabla.setShowGrid(true);
        tabla.setFillsViewportHeight(true);

        tabla.getTableHeader().setFont(headerFont);
        tabla.getTableHeader().setBackground(HEADER_BG);
        tabla.getTableHeader().setForeground(TITLE_COLOR);
        tabla.getTableHeader().setReorderingAllowed(false);

        listener = new GeneradorTabla();

        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(null);
        scrollPane.setBackground(PANEL_BG);
        scrollPane.getViewport().setBackground(PANEL_BG);

        add(titulo, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void actualizarDesdeCodigo(String codigo) {
        modelo.setRowCount(0);
        listener.clear();

        CharStream input = CharStreams.fromString(codigo);
        LenguajeLexer lexer = new LenguajeLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LenguajeParser parser = new LenguajeParser(tokens);

        LenguajeParser.ProgramaContext tree = parser.programa();
        ParseTreeWalker.DEFAULT.walk(listener, tree);

        List<Simbolo> simbolos = listener.getTablaDeSimbolos();

        int i = 1;
        for (Simbolo s : simbolos) {
            modelo.addRow(new Object[]{
                i++,
                s.getNombre(),
                s.getTipo(),
                s.getLinea()
            });
        }
    }

    public JTable getTabla() {
        return tabla;
    }

    public void exportar() {
        JFileChooser fc = new JFileChooser();

        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String ruta = fc.getSelectedFile().getAbsolutePath();
            listener.generarArchivo(ruta);
            JOptionPane.showMessageDialog(this, "Archivo guardado:\n" + ruta);
        }
    }
}
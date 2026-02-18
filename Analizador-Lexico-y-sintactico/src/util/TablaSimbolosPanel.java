 package util;


import generated.LenguajeLexer;
import generated.LenguajeParser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class TablaSimbolosPanel extends JPanel {

    private final DefaultTableModel modelo;
    private final JTable tabla;
    private final JLabel titulo;

    private final GeneradorTabla listener = new GeneradorTabla();

    public TablaSimbolosPanel() {
        setLayout(new BorderLayout(0, 0));
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        titulo = new JLabel("Tabla de Símbolos");
        titulo.setFont(new java.awt.Font("Segoe UI", 0, 36));
        setBackground(new Color(41, 49, 52));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        modelo = new DefaultTableModel(
                new Object[]{"No", "Nombre", "Tipo", "Línea"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);

        add(titulo, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    public void actualizarDesdeCodigo(String codigo) {
        DefaultTableModel m = (DefaultTableModel) tabla.getModel();
        m.setRowCount(0);

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
            m.addRow(new Object[]{i++, s.getNombre(), s.getTipo(), s.getLinea()});
        }

        m.fireTableDataChanged();
        
        
        
    }

    
    public JTable getTabla() {
        return tabla;
    }

    private void exportar() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Guardar tabla de símbolos");

        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String ruta = fc.getSelectedFile().getAbsolutePath();
            listener.generarArchivo(ruta);
            JOptionPane.showMessageDialog(this, "Archivo guardado:\n" + ruta);
        }
    }
}

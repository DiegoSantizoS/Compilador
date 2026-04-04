package symbols;

import generated.LenguajeLexer;
import generated.LenguajeParser;
import java.awt.BorderLayout;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class TablaSimbolosPanel extends JPanel {

    private final JTable tabla;
    private final GeneradorTabla listener = new GeneradorTabla();

    public TablaSimbolosPanel() {
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Tabla de Símbolos", SwingConstants.CENTER);

        DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"No", "Nombre", "Tipo", "Línea"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modelo);

        add(titulo, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }

    public void actualizarDesdeCodigo(String codigo) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
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
            modelo.addRow(new Object[]{i++, s.getNombre(), s.getTipo(), s.getLinea()});
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
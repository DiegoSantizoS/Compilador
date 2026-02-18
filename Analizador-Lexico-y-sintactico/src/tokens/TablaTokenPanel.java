package tokens;

import generated.LenguajeLexer;
import org.antlr.v4.runtime.*;
import javax.swing.table.DefaultTableModel;
import java.util.HashSet;
import java.util.Set;

public class TablaTokenPanel extends javax.swing.JPanel {

    private DefaultTableModel modelo;

    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;

    public TablaTokenPanel() {
        initComponents();
        crearTabla();
    }

    public void actualizarTabla(String codigo) {
        modelo.setRowCount(0); 
        analizarConANTLR(codigo);
    }

    private void crearTabla() {
        modelo = new DefaultTableModel();
        modelo.addColumn("No");
        modelo.addColumn("Lexema");
        modelo.addColumn("Tipo");

        jTable1.setModel(modelo);
    }

    private void analizarConANTLR(String codigo) {
        CharStream input = CharStreams.fromString(codigo);
        LenguajeLexer lexer = new LenguajeLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        tokens.fill();

        int contador = 1;
        Set<String> tokensUnicos = new HashSet<>();

        for (Token token : tokens.getTokens()) {
            if (token.getType() != Token.EOF) {
                String lexema = token.getText();
                String tipo = LenguajeLexer.VOCABULARY.getSymbolicName(token.getType());

                String clave = lexema + "-" + tipo;

                if (!tokensUnicos.contains(clave)) {
                    tokensUnicos.add(clave);

                    modelo.addRow(new Object[]{
                        contador,
                        lexema,
                        tipo
                    });

                    contador++;
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][]{},
            new String[]{}
        ));
        jScrollPane1.setViewportView(jTable1);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 36));
        jLabel1.setText("Tabla de Tokens");
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        setLayout(new java.awt.BorderLayout(0, 0));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        add(jLabel1, java.awt.BorderLayout.NORTH);
        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }
}

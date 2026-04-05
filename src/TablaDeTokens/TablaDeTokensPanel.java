package TablaDeTokens;

import generated.LenguajeLexer;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.util.HashSet;
import java.util.Set;
import org.antlr.v4.runtime.*;

public class TablaDeTokensPanel extends JPanel {

    public boolean mostrarSoloNoRepetidos = false;

    private final DefaultTableModel modelo;
    private final JTable tabla;

    public TablaDeTokensPanel() {
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Tabla de Tokens", SwingConstants.CENTER);

        modelo = new DefaultTableModel(
            new Object[]{"No", "Lexema", "Tipo"}, 0
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

    public void actualizarTabla(String codigo) {
        modelo.setRowCount(0);
        analizarConANTLR(codigo);
    }

    private void analizarConANTLR(String codigo) {
        CharStream input = CharStreams.fromString(codigo);
        LenguajeLexer lexer = new LenguajeLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        tokens.fill();

        int contador = 1;
        Set<String> tokensUnicos = new HashSet<>();

        for (Token token : tokens.getTokens()) {
            if (token.getType() == Token.EOF) {
                continue;
            }

            String lexema = token.getText();
            String tipo = LenguajeLexer.VOCABULARY.getSymbolicName(token.getType());

            if (tipo == null) {
                tipo = String.valueOf(token.getType());
            }

            if (mostrarSoloNoRepetidos) {
                String clave = lexema + "|" + tipo;

                if (!tokensUnicos.add(clave)) {
                    continue;
                }
            }

            modelo.addRow(new Object[]{
                contador++,
                lexema,
                tipo
            });
        }
    }

    public JTable getTabla() {
        return tabla;
    }
}
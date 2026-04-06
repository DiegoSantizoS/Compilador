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

        java.awt.Color PANEL_BG = new java.awt.Color(30, 37, 40);
        java.awt.Color TITLE_COLOR = new java.awt.Color(221, 230, 234);
        java.awt.Color TEXT_COLOR = new java.awt.Color(231, 238, 241);
        java.awt.Color HEADER_BG = new java.awt.Color(41, 49, 52);
        java.awt.Color GRID_COLOR = new java.awt.Color(74, 90, 96);

        java.awt.Font titleFont = new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14);
        java.awt.Font tableFont = new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13);
        java.awt.Font headerFont = new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13);

        setBackground(PANEL_BG);

        JLabel titulo = new JLabel("Tabla de Tokens", SwingConstants.CENTER);
        titulo.setPreferredSize(new java.awt.Dimension(0, 35));
        titulo.setOpaque(true);
        titulo.setBackground(PANEL_BG);
        titulo.setForeground(TITLE_COLOR);
        titulo.setFont(titleFont);

        modelo = new DefaultTableModel(
            new Object[]{"No", "Lexema", "Tipo"}, 0
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
        tabla.getTableHeader().setOpaque(true);
        tabla.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(null);
        scrollPane.setBackground(PANEL_BG);
        scrollPane.getViewport().setBackground(PANEL_BG);

        add(titulo, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
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
package main_frame;
//@author DFSS

import AnalizadorSemantico.*;
import generated.LenguajeLexer;
import generated.LenguajeParser;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import main_components.ClosableTabComponent;
import main_components.RunButton;
import main_components.TerminalErrorListener;
import main_components.WindowControlButton;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.fife.ui.rtextarea.RTextScrollPane;

public class main extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(main.class.getName());
    private javax.swing.JPopupMenu popupFiles;
    private javax.swing.JMenuItem itemNuevo, itemAbrir, itemGuardar, itemGuardarComo;
    private int xMouse, yMouse;
    private static final int BORDER = 5;
    private static final int MIN_W = 500;
    private static final int MIN_H = 350;
    private static final int RESIZE_NONE = 0;
    private static final int RESIZE_N = 1;
    private static final int RESIZE_S = 2;
    private static final int RESIZE_W = 4;
    private static final int RESIZE_E = 8;
    private java.awt.Point resizeStartScreen;
    private java.awt.Rectangle frameStartBounds;
    private int resizeDirection = RESIZE_NONE;
    
    //private boolean popupOpen = false;
    
    private final Map<java.awt.Component, File> tabFiles = new HashMap<>();
    private int nuevoContador = 1;
    private JTextPane terminalUnica;
    
    public main() {
        getContentPane().setBackground(new Color(41, 49, 52));
        setBackground(new Color(41, 49, 52));
        javax.swing.UIManager.put("Panel.background", new Color(41, 49, 52));
        javax.swing.UIManager.put("control", new Color(41, 49, 52));
        
        setUndecorated(true);
        try {
            javax.swing.UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        javax.swing.UIManager.put("Button.arc", 16);
        javax.swing.UIManager.put("Component.arc", 16);
        javax.swing.UIManager.put("TextComponent.arc", 12);
        javax.swing.UIManager.put("ScrollBar.thumbArc", 999);
        javax.swing.UIManager.put("ScrollBar.thumbInsets", new java.awt.Insets(2, 2, 2, 2));
 
        initComponents();
        
        AbstractTokenMakerFactory atmf =
            (AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance();
        atmf.putMapping("text/lenguaje", "AnalizadorSintactico.LenguajeTokenMaker");
        
        configurarTabsCerrables();
        crearPopupArchivo();
        configurarBotonesVentana();
        jSplitPanelVertical.setDividerLocation(1.0);
        jComboBoxGraphicsSelected.removeAllItems();
        jComboBoxGraphicsSelected.addItem("-");
        actualizarGrafico();
        jComboBoxGraphicsSelected.setEditable(false);
        jComboBoxGraphicsSelected.setEnabled(false);
        jSplitPanelHorizontal.setEnabled(true);
        actualizarGrafico();
        if (jTabbedEditorPanel.getTabCount() > 0) {
            jTabbedEditorPanel.removeTabAt(0);
        }
        nuevoArchivo();
        
        jTabbedPaneTerminal.setMinimumSize(new Dimension(0,20));
        jSplitPanelVertical.setDividerSize(4);
        jSplitPanelVertical.setDividerLocation(0.7);
        inicializarTerminal();
        resetApp();
    }
    private void configurarBotonesVentana() {
        estilizarBotonVentana(jButtonMin);
        estilizarBotonVentana(jButtonMax);
        estilizarBotonVentana(jButtonClose);
    }
    
    private void estilizarBotonVentana(javax.swing.JButton btn) {
        btn.setPreferredSize(new java.awt.Dimension(45, 30));
        btn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btn.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        btn.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        btn.setForeground(java.awt.Color.WHITE);
    }
    
    private void resetApp() {
        if (terminalUnica == null){
            return;
        }
        jTabbedPaneTerminal.setMinimumSize(new Dimension(0,20));
        if (jTabbedPaneTerminal.getTabCount() > 0) {
            jTabbedPaneTerminal.removeAll();
        }
        jSplitPanelVertical.setDividerLocation(1.0);
        //jSplitPanelVertical.setEnabled(false);  
        jSplitPanelVertical.setDividerSize(0);
        jComboBoxGraphicsSelected.removeAllItems();
        jComboBoxGraphicsSelected.addItem("-");
        jComboBoxGraphicsSelected.setSelectedIndex(0);
        jComboBoxGraphicsSelected.setEnabled(false);
        java.awt.CardLayout cl = (java.awt.CardLayout) jPanelGraphics.getLayout();
        cl.show(jPanelGraphics, "logo_panel");
        terminalUnica.setText("");
    }
    
    private void actualizarGrafico() {
        java.awt.CardLayout cl = (java.awt.CardLayout) jPanelGraphics.getLayout();
        String selected = String.valueOf(jComboBoxGraphicsSelected.getSelectedItem());

        switch (selected) {
            case "-":
                cl.show(jPanelGraphics, "logo_panel");
                break;
            case "Tabla de Tokens":
                cl.show(jPanelGraphics, "tokens_table");
                break;
            case "Arbol Sintactico":
                cl.show(jPanelGraphics, "syntax_tree");
                break;
            case "Tabla de Simbolos":
                cl.show(jPanelGraphics, "symbol_table");
                break;
            case "Arbol Semantico":
                cl.show(jPanelGraphics, "semantics_tree");
                break;
            case "Codigo Intermedio":
                cl.show(jPanelGraphics, "intermediate_code");
                break;
            case "Codigo TAC":
                cl.show(jPanelGraphics, "tac_code");
                break;
            default:
                cl.show(jPanelGraphics, "logo_panel");
                break;
        }
    }

    private void inicializarTerminal() {
        terminalUnica = new JTextPane();
        terminalUnica.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(terminalUnica);

        jTabbedPaneTerminal.removeAll();
        jTabbedPaneTerminal.addTab("Terminal", scrollPane);
        jTabbedPaneTerminal.setSelectedComponent(scrollPane);
    }

    private JTextPane nuevaTerminal() {
        int editorIndex = jTabbedEditorPanel.getSelectedIndex();
        String nombreEditor = (editorIndex != -1)
                ? jTabbedEditorPanel.getTitleAt(editorIndex)
                : "Sin archivo";

        if (terminalUnica == null) {
            terminalUnica = new JTextPane();
            terminalUnica.setEditable(false);
        }

        JScrollPane scrollPane;

        if (jTabbedPaneTerminal.getTabCount() == 0) {
            scrollPane = new JScrollPane(terminalUnica);
            jTabbedPaneTerminal.addTab("Output - " + nombreEditor, scrollPane);
        } else {
            java.awt.Component comp = jTabbedPaneTerminal.getComponentAt(0);

            if (comp instanceof JScrollPane) {
                scrollPane = (JScrollPane) comp;
                scrollPane.setViewportView(terminalUnica);
            } else {
                scrollPane = new JScrollPane(terminalUnica);
                jTabbedPaneTerminal.setComponentAt(0, scrollPane);
            }

            jTabbedPaneTerminal.setTitleAt(0, "Output - " + nombreEditor);
        }

        jTabbedPaneTerminal.setSelectedIndex(0);
        //terminalUnica.setText(""); 
        return terminalUnica;
    }
    
    private void appendTerminal(JTextPane terminal, String mensaje, Color color) {
        if (terminal == null) {
            System.out.println(mensaje);
            return;
        }

        StyledDocument doc = terminal.getStyledDocument();
        Style style = terminal.addStyle("color_" + color.getRGB(), null);
        StyleConstants.setForeground(style, color);

        try {
            doc.insertString(doc.getLength(), mensaje + "\n", style);
        } catch (Exception e) {
            System.out.println(mensaje);
        }
    }

    private void appendNormal(JTextPane terminal, String mensaje) {
        appendTerminal(terminal, mensaje, Color.WHITE);
    }
    
    private void appendNormalSuccess(JTextPane terminal, String mensaje) {
        appendTerminal(terminal, mensaje, Color.GREEN);
    }

    private void appendError(JTextPane terminal, String mensaje) {
        appendTerminal(terminal, mensaje, Color.RED);
    }

    private void configurarTabsCerrables() {
        for (int i = 0; i < jTabbedEditorPanel.getTabCount(); i++) {
            jTabbedEditorPanel.setTabComponentAt(i, new ClosableTabComponent(jTabbedEditorPanel));
        }

        //jTabbedPaneTerminal
    }

    private void crearPopupArchivo() {
        popupFiles = new javax.swing.JPopupMenu();
        popupFiles.setOpaque(true);
        popupFiles.setBackground(new java.awt.Color(60, 63, 65));
        popupFiles.setBorderPainted(true);
        popupFiles.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(80, 80, 80), 1));
        popupFiles.setLightWeightPopupEnabled(false);

        itemNuevo = new javax.swing.JMenuItem("Nuevo");
        itemAbrir = new javax.swing.JMenuItem("Abrir");
        itemGuardar = new javax.swing.JMenuItem("Guardar");
        itemGuardarComo = new javax.swing.JMenuItem("Guardar Como");

        estilizarMenuItem(itemNuevo);
        estilizarMenuItem(itemAbrir);
        estilizarMenuItem(itemGuardar);
        estilizarMenuItem(itemGuardarComo);

        itemNuevo.addActionListener(e -> nuevoArchivo());

        itemAbrir.addActionListener(e -> abrirArchivo());

        itemGuardar.addActionListener(e -> guardarArchivo());

        itemGuardarComo.addActionListener(e -> guardarArchivoComo());

        popupFiles.add(itemNuevo);
        popupFiles.add(itemAbrir);
        popupFiles.add(itemGuardar);
        popupFiles.add(itemGuardarComo);
    }

    private void nuevoArchivo() {
        RSyntaxTextArea textArea = new RSyntaxTextArea();
        textArea.setSyntaxEditingStyle("text/lenguaje");
        textArea.setCodeFoldingEnabled(true);
        textArea.setAntiAliasingEnabled(true);
        textArea.setBracketMatchingEnabled(true);
        textArea.setHighlightCurrentLine(true);
        textArea.setTabsEmulated(true);
        textArea.setTabSize(4);

        try {
            Theme theme = Theme.load(getClass().getResourceAsStream("/main_components/customDarkTheme.xml"));
            theme.apply(textArea);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        RTextScrollPane scrollPane = new RTextScrollPane(textArea);
        scrollPane.setLineNumbersEnabled(true);
        scrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder());

        String titulo = "Nuevo " + nuevoContador++;
        jTabbedEditorPanel.addTab(titulo, scrollPane);

        int index = jTabbedEditorPanel.getTabCount() - 1;
        jTabbedEditorPanel.setTabComponentAt(index, new ClosableTabComponent(jTabbedEditorPanel));
        jTabbedEditorPanel.setSelectedIndex(index);
    }

    private void abrirArchivo() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);

        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = chooser.getSelectedFile();

        try {
            String contenido = Files.readString(file.toPath());

            RSyntaxTextArea textArea = new RSyntaxTextArea();
            textArea.setSyntaxEditingStyle("text/lenguaje");
            textArea.setCodeFoldingEnabled(true);
            textArea.setAntiAliasingEnabled(true);
            textArea.setBracketMatchingEnabled(true);
            textArea.setHighlightCurrentLine(true);
            textArea.setTabsEmulated(true);
            textArea.setTabSize(4);
            textArea.setText(contenido);
            textArea.setCaretPosition(0);

            try {
                Theme theme = Theme.load(getClass().getResourceAsStream("/main_components/customDarkTheme.xml"));
                theme.apply(textArea);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            RTextScrollPane scrollPane = new RTextScrollPane(textArea);
            scrollPane.setLineNumbersEnabled(true);
            scrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder());

            jTabbedEditorPanel.addTab(file.getName(), scrollPane);

            int index = jTabbedEditorPanel.getTabCount() - 1;
            jTabbedEditorPanel.setTabComponentAt(index, new ClosableTabComponent(jTabbedEditorPanel));
            jTabbedEditorPanel.setSelectedIndex(index);

            tabFiles.put(scrollPane, file);
            

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo abrir el archivo:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void guardarArchivo() {
        java.awt.Component tabActual = jTabbedEditorPanel.getSelectedComponent();

        if (tabActual == null) {
            return;
        }

        File file = tabFiles.get(tabActual);

        if (file == null) {
            guardarArchivoComo();
            return;
        }

        RSyntaxTextArea textArea = getTextAreaFromTab(tabActual);

        if (textArea == null) {
            return;
        }

        try {
            Files.writeString(file.toPath(), textArea.getText());
            javax.swing.JOptionPane.showMessageDialog(this, "Archivo guardado.");
        } catch (IOException ex) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "No se pudo guardar el archivo:\n" + ex.getMessage(),
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void guardarArchivoComo() {
        java.awt.Component tabActual = jTabbedEditorPanel.getSelectedComponent();

        if (tabActual == null) {
            return;
        }

        RSyntaxTextArea textArea = getTextAreaFromTab(tabActual);

        if (textArea == null) {
            return;
        }

        JFileChooser chooser = new JFileChooser();
        int result = chooser.showSaveDialog(this);

        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = chooser.getSelectedFile();

        try {
            Files.writeString(file.toPath(), textArea.getText());

            tabFiles.put(tabActual, file);
            jTabbedEditorPanel.setTitleAt(
                    jTabbedEditorPanel.getSelectedIndex(),
                    file.getName()
            );

            javax.swing.JOptionPane.showMessageDialog(this, "Archivo guardado.");
        } catch (IOException ex) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "No se pudo guardar el archivo:\n" + ex.getMessage(),
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private RSyntaxTextArea getTextAreaFromTab(java.awt.Component tabComponent) {
        if (tabComponent instanceof RTextScrollPane rsp) {
            return (RSyntaxTextArea)rsp.getTextArea();
        }
        if (tabComponent instanceof JScrollPane scrollPane) {
            java.awt.Component view = scrollPane.getViewport().getView();
            if (view instanceof RSyntaxTextArea textArea) {
                return textArea;
            }
        }
        return null;
    }

    private void estilizarMenuItem(javax.swing.JMenuItem item) {
        java.awt.Dimension menuSize = new java.awt.Dimension(150, 30);

        item.setPreferredSize(menuSize);
        item.setIcon(null);
        item.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        item.setIconTextGap(0);
        item.setOpaque(true);
        item.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        item.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
    }

    private int getResizeDirection(int x, int y) {
        int width = getWidth();
        int height = getHeight();

        boolean north = y <= BORDER;
        boolean south = y >= height - BORDER;
        boolean west = x <= BORDER;
        boolean east = x >= width - BORDER;

        int dir = RESIZE_NONE;

        if (north) {
            dir |= RESIZE_N;
        }
        if (south) {
            dir |= RESIZE_S;
        }
        if (west) {
            dir |= RESIZE_W;
        }
        if (east) {
            dir |= RESIZE_E;
        }

        return dir;
    }

    private void setResizeCursor(int dir) {
        java.awt.Cursor cursor;

        switch (dir) {
            case RESIZE_N:
                cursor = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.N_RESIZE_CURSOR);
                break;
            case RESIZE_S:
                cursor = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.S_RESIZE_CURSOR);
                break;
            case RESIZE_W:
                cursor = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.W_RESIZE_CURSOR);
                break;
            case RESIZE_E:
                cursor = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.E_RESIZE_CURSOR);
                break;
            case RESIZE_N | RESIZE_W:
                cursor = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.NW_RESIZE_CURSOR);
                break;
            case RESIZE_N | RESIZE_E:
                cursor = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.NE_RESIZE_CURSOR);
                break;
            case RESIZE_S | RESIZE_W:
                cursor = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.SW_RESIZE_CURSOR);
                break;
            case RESIZE_S | RESIZE_E:
                cursor = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.SE_RESIZE_CURSOR);
                break;
            default:
                cursor = java.awt.Cursor.getDefaultCursor();
                break;
        }

        if (getCursor() != cursor) {
            setCursor(cursor);
        }
    }

    private RSyntaxTextArea getCurrentEditor() {
        java.awt.Component selected = jTabbedEditorPanel.getSelectedComponent();

        if (selected instanceof RTextScrollPane scrollPane) {
            return (RSyntaxTextArea) scrollPane.getTextArea();
        }
        if (selected instanceof JScrollPane scrollPane) {
            java.awt.Component view = scrollPane.getViewport().getView();
            if (view instanceof RSyntaxTextArea textArea) {
                return textArea;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jSplitPanelVertical = new javax.swing.JSplitPane();
        jSplitPanelHorizontal = new javax.swing.JSplitPane();
        jPanelEditor = new javax.swing.JPanel();
        jPanelEditorTools = new javax.swing.JPanel();
        jPanelEditorButtons = new javax.swing.JPanel();
        jButtonRun = new RunButton();
        jPanelEditorToolsFiller = new javax.swing.JPanel();
        jComboBoxGraphicsSelected = new javax.swing.JComboBox<>();
        jTabbedEditorPanel = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanelGraphics = new javax.swing.JPanel();
        formaIntermediaPane1 = new CodigoIntermedio.FormaIntermediaPanel();
        tacPane1 = new CodigoIntermedio.PanelTAC();
        arbolSemanticoPanel2 = new AnalizadorSemantico.ArbolSemanticoPanel();
        tablaSimbolosPanel1 = new TablaDeSimbolos.TablaDeSimbolosPanel();
        tablaDeTokensPanel1 = new TablaDeTokens.TablaDeTokensPanel();
        arbolSintacticoPanel1 = new AnalizadorSintactico.ArbolSintacticoPanel();
        jPanelLogo = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTabbedPaneTerminal = new javax.swing.JTabbedPane();
        jPanelTitleBar = new javax.swing.JPanel();
        jPanelMainTabs = new javax.swing.JPanel();
        jButtonLogo = new javax.swing.JButton();
        jButtonFiles = new javax.swing.JButton();
        jPanelDraggingPane = new javax.swing.JPanel();
        jPanelFrameMods = new javax.swing.JPanel();
        jButtonMin = new WindowControlButton(WindowControlButton.ButtonType.MINIMIZE);
        jButtonMax = new WindowControlButton(WindowControlButton.ButtonType.RESTORE);
        jButtonClose = new WindowControlButton(WindowControlButton.ButtonType.CLOSE);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(610, 500));
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                formMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
        });
        addWindowStateListener(this::formWindowStateChanged);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jSplitPanelVertical.setDividerLocation(355);
        jSplitPanelVertical.setDividerSize(0);
        jSplitPanelVertical.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPanelVertical.setResizeWeight(1.0);

        jSplitPanelHorizontal.setDividerLocation(300);
        jSplitPanelHorizontal.setDividerSize(3);
        jSplitPanelHorizontal.setMinimumSize(new java.awt.Dimension(604, 200));

        jPanelEditor.setBackground(new java.awt.Color(52, 61, 64));
        jPanelEditor.setMinimumSize(new java.awt.Dimension(300, 35));
        jPanelEditor.setLayout(new java.awt.GridBagLayout());

        jPanelEditorTools.setMaximumSize(new java.awt.Dimension(360, 35));
        jPanelEditorTools.setMinimumSize(new java.awt.Dimension(360, 35));
        jPanelEditorTools.setPreferredSize(new java.awt.Dimension(360, 35));
        jPanelEditorTools.setLayout(new java.awt.GridBagLayout());

        jPanelEditorButtons.setBackground(new java.awt.Color(52, 61, 64));
        jPanelEditorButtons.setLayout(new java.awt.GridBagLayout());

        jButtonRun.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonRun.setMaximumSize(new java.awt.Dimension(40, 35));
        jButtonRun.setMinimumSize(new java.awt.Dimension(40, 35));
        jButtonRun.setPreferredSize(new java.awt.Dimension(40, 35));
        jButtonRun.addActionListener(this::jButtonRunActionPerformed);
        jButtonRun.setContentAreaFilled(false);
        jButtonRun.setBorderPainted(false);
        jButtonRun.setFocusPainted(false);
        jButtonRun.setOpaque(false);
        jPanelEditorButtons.add(jButtonRun, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weighty = 1.0;
        jPanelEditorTools.add(jPanelEditorButtons, gridBagConstraints);

        jPanelEditorToolsFiller.setBackground(new java.awt.Color(52, 61, 64));

        javax.swing.GroupLayout jPanelEditorToolsFillerLayout = new javax.swing.GroupLayout(jPanelEditorToolsFiller);
        jPanelEditorToolsFiller.setLayout(jPanelEditorToolsFillerLayout);
        jPanelEditorToolsFillerLayout.setHorizontalGroup(
            jPanelEditorToolsFillerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelEditorToolsFillerLayout.setVerticalGroup(
            jPanelEditorToolsFillerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 35, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelEditorTools.add(jPanelEditorToolsFiller, gridBagConstraints);

        jComboBoxGraphicsSelected.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-", "Tabla de Tokens", "Arbol Sintactico", "Tabla de Simbolos", "Arbol Semantico", "Codigo Intermedio", "Codigo TAC" }));
        jComboBoxGraphicsSelected.setBorder(null);
        jComboBoxGraphicsSelected.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jComboBoxGraphicsSelected.setMaximumSize(new java.awt.Dimension(150, 22));
        jComboBoxGraphicsSelected.setMinimumSize(new java.awt.Dimension(150, 22));
        jComboBoxGraphicsSelected.setPreferredSize(new java.awt.Dimension(150, 22));
        jComboBoxGraphicsSelected.addActionListener(this::jComboBoxGraphicsSelectedActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        jPanelEditorTools.add(jComboBoxGraphicsSelected, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        jPanelEditor.add(jPanelEditorTools, gridBagConstraints);

        jTabbedEditorPanel.addTab("tab1", jScrollPane1);

        jTabbedEditorPanel.setOpaque(false);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.95;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        jPanelEditor.add(jTabbedEditorPanel, gridBagConstraints);

        jSplitPanelHorizontal.setLeftComponent(jPanelEditor);

        jPanelGraphics.setBackground(new java.awt.Color(51, 255, 51));
        jPanelGraphics.setMinimumSize(new java.awt.Dimension(450, 550));
        jPanelGraphics.setOpaque(false);
        jPanelGraphics.setPreferredSize(new java.awt.Dimension(450, 550));
        jPanelGraphics.setLayout(new java.awt.CardLayout());
        jPanelGraphics.add(formaIntermediaPane1, "intermediate_code");
        jPanelGraphics.add(tacPane1, "tac_code");
        jPanelGraphics.add(arbolSemanticoPanel2, "semantics_tree");
        jPanelGraphics.add(tablaSimbolosPanel1, "symbol_table");
        jPanelGraphics.add(tablaDeTokensPanel1, "tokens_table");
        jPanelGraphics.add(arbolSintacticoPanel1, "syntax_tree");

        jPanelLogo.setBackground(new java.awt.Color(58, 69, 73));
        jPanelLogo.setMaximumSize(new java.awt.Dimension(250, 357));
        jPanelLogo.setMinimumSize(new java.awt.Dimension(250, 357));
        jPanelLogo.setPreferredSize(new java.awt.Dimension(250, 357));
        jPanelLogo.setLayout(new java.awt.GridBagLayout());

        jPanel1.setBackground(new java.awt.Color(58, 69, 73));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Lucida Sans", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("DIEGO FERNANDO SANTIZO SAMAYOA 0901-22-15950");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jLabel1, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("Lucida Sans", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("CARLOS ANDRES ARRIAZA LARA 0901-23-13862");
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jLabel2, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("Lucida Sans", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("MIGUEL DAVID CONTRERAS JACINTO 0901-21-3878");
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jLabel3, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("Lucida Sans", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("PEDRO JOSÉ GÓMEZ VILLALOBOS 0901-23-4868");
        jLabel4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(jLabel4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanelLogo.add(jPanel1, gridBagConstraints);

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/main_components/Escudo_de_la_universidad_Mariano_Gálvez_Guatemala.svg.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        jPanelLogo.add(jLabel5, gridBagConstraints);

        jPanelGraphics.add(jPanelLogo, "logo_panel");

        jSplitPanelHorizontal.setRightComponent(jPanelGraphics);

        jSplitPanelVertical.setTopComponent(jSplitPanelHorizontal);

        jTabbedPaneTerminal.setForeground(new java.awt.Color(255, 255, 255));
        jTabbedPaneTerminal.setMaximumSize(new java.awt.Dimension(0, 110));
        jTabbedPaneTerminal.setMinimumSize(new java.awt.Dimension(0, 0));
        jTabbedPaneTerminal.setPreferredSize(new java.awt.Dimension(0, 0));
        jSplitPanelVertical.setBottomComponent(jTabbedPaneTerminal);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 3, 3);
        getContentPane().add(jSplitPanelVertical, gridBagConstraints);

        jPanelTitleBar.setBackground(new java.awt.Color(30, 36, 38));
        jPanelTitleBar.setMaximumSize(new java.awt.Dimension(0, 30));
        jPanelTitleBar.setMinimumSize(new java.awt.Dimension(0, 30));
        jPanelTitleBar.setPreferredSize(new java.awt.Dimension(0, 30));
        jPanelTitleBar.setLayout(new java.awt.GridBagLayout());

        jPanelMainTabs.setMinimumSize(new java.awt.Dimension(120, 0));
        jPanelMainTabs.setPreferredSize(new java.awt.Dimension(120, 0));
        jPanelMainTabs.setLayout(new java.awt.GridBagLayout());

        jButtonLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/main_components/java.png"))); // NOI18N
        jButtonLogo.setBorder(null);
        jButtonLogo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonLogo.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonLogo.setMaximumSize(new java.awt.Dimension(35, 35));
        jButtonLogo.setMinimumSize(new java.awt.Dimension(35, 35));
        jButtonLogo.setPreferredSize(new java.awt.Dimension(35, 35));
        jButtonLogo.addActionListener(this::jButtonLogoActionPerformed);
        jButtonLogo.setContentAreaFilled(false);
        jButtonLogo.setBorderPainted(false);
        jButtonLogo.setFocusPainted(false);
        jButtonLogo.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 15);
        jPanelMainTabs.add(jButtonLogo, gridBagConstraints);

        jButtonFiles.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButtonFiles.setText("Archivos");
        jButtonFiles.setBorder(null);
        jButtonFiles.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonFiles.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonFiles.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonFiles.setMaximumSize(new java.awt.Dimension(70, 0));
        jButtonFiles.setMinimumSize(new java.awt.Dimension(70, 0));
        jButtonFiles.setPreferredSize(new java.awt.Dimension(70, 0));
        jButtonFiles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButtonFilesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButtonFilesMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButtonFilesMousePressed(evt);
            }
        });
        jButtonFiles.addActionListener(this::jButtonFilesActionPerformed);
        jButtonFiles.setContentAreaFilled(false);
        jButtonFiles.setBorderPainted(false);
        jButtonFiles.setFocusPainted(false);
        jButtonFiles.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        jPanelMainTabs.add(jButtonFiles, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        jPanelTitleBar.add(jPanelMainTabs, gridBagConstraints);

        jPanelDraggingPane.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jPanelDraggingPaneMouseDragged(evt);
            }
        });
        jPanelDraggingPane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanelDraggingPaneMousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanelDraggingPaneLayout = new javax.swing.GroupLayout(jPanelDraggingPane);
        jPanelDraggingPane.setLayout(jPanelDraggingPaneLayout);
        jPanelDraggingPaneLayout.setHorizontalGroup(
            jPanelDraggingPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelDraggingPaneLayout.setVerticalGroup(
            jPanelDraggingPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelTitleBar.add(jPanelDraggingPane, gridBagConstraints);

        jPanelFrameMods.setMaximumSize(new java.awt.Dimension(150, 0));
        jPanelFrameMods.setMinimumSize(new java.awt.Dimension(150, 0));
        jPanelFrameMods.setPreferredSize(new java.awt.Dimension(150, 0));
        jPanelFrameMods.setLayout(new java.awt.GridBagLayout());

        jButtonMin.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonMin.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonMin.setMaximumSize(new java.awt.Dimension(20, 20));
        jButtonMin.setMinimumSize(new java.awt.Dimension(20, 20));
        jButtonMin.setOpaque(true);
        jButtonMin.setPreferredSize(new java.awt.Dimension(20, 20));
        jButtonMin.setContentAreaFilled(true);
        jButtonMin.setBorderPainted(true);
        jButtonMin.setFocusPainted(true);
        jButtonMin.setOpaque(true);
        jButtonMin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButtonMinMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButtonMinMouseExited(evt);
            }
        });
        jButtonMin.addActionListener(this::jButtonMinActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelFrameMods.add(jButtonMin, gridBagConstraints);

        jButtonMax.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonMax.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonMax.setMaximumSize(new java.awt.Dimension(20, 20));
        jButtonMax.setMinimumSize(new java.awt.Dimension(20, 20));
        jButtonMax.setOpaque(true);
        jButtonMax.setPreferredSize(new java.awt.Dimension(20, 20));
        jButtonMax.setContentAreaFilled(false);
        jButtonMax.setBorderPainted(false);
        jButtonMax.setFocusPainted(false);
        jButtonMax.setOpaque(false);
        jButtonMax.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButtonMaxMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButtonMaxMouseExited(evt);
            }
        });
        jButtonMax.addActionListener(this::jButtonMaxActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelFrameMods.add(jButtonMax, gridBagConstraints);

        jButtonClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonClose.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonClose.setMaximumSize(new java.awt.Dimension(20, 20));
        jButtonClose.setMinimumSize(new java.awt.Dimension(20, 20));
        jButtonClose.setOpaque(true);
        jButtonClose.setPreferredSize(new java.awt.Dimension(20, 20));
        jButtonClose.setContentAreaFilled(false);
        jButtonClose.setBorderPainted(false);
        jButtonClose.setFocusPainted(false);
        jButtonClose.setOpaque(false);
        jButtonClose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButtonCloseMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButtonCloseMouseExited(evt);
            }
        });
        jButtonClose.addActionListener(this::jButtonCloseActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelFrameMods.add(jButtonClose, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        jPanelTitleBar.add(jPanelFrameMods, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jPanelTitleBar, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonFilesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFilesActionPerformed
        popupFiles.show(jButtonFiles, 0, jButtonFiles.getHeight());
    }//GEN-LAST:event_jButtonFilesActionPerformed

    private void jPanelDraggingPaneMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelDraggingPaneMousePressed
        xMouse = evt.getX();
        yMouse = evt.getY();
    }//GEN-LAST:event_jPanelDraggingPaneMousePressed

    private void jPanelDraggingPaneMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelDraggingPaneMouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xMouse - jPanelMainTabs.getWidth() - 5, y - yMouse - 5);
        setExtendedState(java.awt.Frame.NORMAL);
        jButtonMax.setText("⬜");
    }//GEN-LAST:event_jPanelDraggingPaneMouseDragged

    private void jButtonMinMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonMinMouseEntered
        jButtonMin.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_jButtonMinMouseEntered

    private void jButtonMinMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonMinMouseExited
        jButtonMin.setBackground(new Color(0,0,0));
    }//GEN-LAST:event_jButtonMinMouseExited

    private void jButtonMinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMinActionPerformed
        setExtendedState(this.ICONIFIED);
    }//GEN-LAST:event_jButtonMinActionPerformed

    private void jButtonMaxMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonMaxMouseEntered
        jButtonMax.setBackground(Color.BLACK);
    }//GEN-LAST:event_jButtonMaxMouseEntered

    private void jButtonMaxMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonMaxMouseExited
        jButtonMax.setBackground(Color.WHITE);
    }//GEN-LAST:event_jButtonMaxMouseExited

    private void jButtonMaxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMaxActionPerformed
        if ((this.getExtendedState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH) {
            this.setExtendedState(Frame.NORMAL);
        } else {
            this.setExtendedState(Frame.MAXIMIZED_BOTH);
        }
    }//GEN-LAST:event_jButtonMaxActionPerformed

    private void jButtonCloseMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonCloseMouseEntered
        jButtonClose.setBackground(Color.RED);
    }//GEN-LAST:event_jButtonCloseMouseEntered

    private void jButtonCloseMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonCloseMouseExited
        jButtonClose.setBackground(Color.WHITE);
    }//GEN-LAST:event_jButtonCloseMouseExited

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButtonCloseActionPerformed

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        resizeDirection = getResizeDirection(evt.getX(), evt.getY());

        if (resizeDirection != RESIZE_NONE) {
            resizeStartScreen = evt.getLocationOnScreen();
            frameStartBounds = getBounds();
        }
    }//GEN-LAST:event_formMousePressed

    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
        resizeDirection = RESIZE_NONE;
        resizeStartScreen = null;
        frameStartBounds = null;

        setCursor(java.awt.Cursor.getDefaultCursor());
    }//GEN-LAST:event_formMouseReleased

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        if ((getExtendedState() & java.awt.Frame.MAXIMIZED_BOTH) == java.awt.Frame.MAXIMIZED_BOTH) {
            return;
        }
        
        if (resizeDirection == RESIZE_NONE || resizeStartScreen == null || frameStartBounds == null) {
            return;
        }

        java.awt.Point current = evt.getLocationOnScreen();
        int dx = current.x - resizeStartScreen.x;
        int dy = current.y - resizeStartScreen.y;

        int x = frameStartBounds.x;
        int y = frameStartBounds.y;
        int w = frameStartBounds.width;
        int h = frameStartBounds.height;

        if ((resizeDirection & RESIZE_E) != 0) {
            w = Math.max(MIN_W, frameStartBounds.width + dx);
        }

        if ((resizeDirection & RESIZE_S) != 0) {
            h = Math.max(MIN_H, frameStartBounds.height + dy);
        }

        if ((resizeDirection & RESIZE_W) != 0) {
            int newW = frameStartBounds.width - dx;
            if (newW >= MIN_W) {
                x = frameStartBounds.x + dx;
                w = newW;
            } else {
                x = frameStartBounds.x + (frameStartBounds.width - MIN_W);
                w = MIN_W;
            }
        }

        if ((resizeDirection & RESIZE_N) != 0) {
            int newH = frameStartBounds.height - dy;
            if (newH >= MIN_H) {
                y = frameStartBounds.y + dy;
                h = newH;
            } else {
                y = frameStartBounds.y + (frameStartBounds.height - MIN_H);
                h = MIN_H;
            }
        }
        
        setBounds(x, y, w, h);
        setResizeCursor(resizeDirection);
        
        setExtendedState(java.awt.Frame.NORMAL);
        jButtonMax.setText("⬜");
        
    }//GEN-LAST:event_formMouseDragged

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        if ((getExtendedState() & java.awt.Frame.MAXIMIZED_BOTH) == java.awt.Frame.MAXIMIZED_BOTH) {
            return;
        }
        int dir = getResizeDirection(evt.getX(), evt.getY());
        setResizeCursor(dir);
    }//GEN-LAST:event_formMouseMoved

    private void formMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseExited
        if (resizeDirection == RESIZE_NONE) {
            setCursor(java.awt.Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_formMouseExited

    private void jComboBoxGraphicsSelectedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxGraphicsSelectedActionPerformed
        actualizarGrafico();
    }//GEN-LAST:event_jComboBoxGraphicsSelectedActionPerformed

    private void jButtonRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRunActionPerformed

        jTabbedPaneTerminal.setMinimumSize(new Dimension(0,20));
        if (terminalUnica == null) {
            inicializarTerminal();
        }
        terminalUnica.setText("");
        jSplitPanelVertical.setEnabled(true);
        jSplitPanelVertical.setDividerSize(4);
        if (jComboBoxGraphicsSelected.getItemCount() == 1) {
            jComboBoxGraphicsSelected.addItem("Tabla de Tokens");
            jComboBoxGraphicsSelected.addItem("Arbol Sintactico");
            jComboBoxGraphicsSelected.addItem("Tabla de Simbolos");
            jComboBoxGraphicsSelected.addItem("Arbol Semantico");
            jComboBoxGraphicsSelected.addItem("Codigo Intermedio");
            jComboBoxGraphicsSelected.addItem("Codigo TAC");
            jComboBoxGraphicsSelected.removeItem("-");
        }
        jComboBoxGraphicsSelected.setEnabled(true);
        
        RSyntaxTextArea editor = getCurrentEditor();
        if (editor == null) {
            javax.swing.JOptionPane.showMessageDialog(this, "No hay un editor activo.");
            return;
        }

        String code = editor.getText();

        JTextPane terminal = nuevaTerminal();
        if (terminal == null) {
            return;
        }

        int editorIndex = jTabbedEditorPanel.getSelectedIndex();
        String nombreEditor = "Sin archivo";

        if (editorIndex != -1) {
            nombreEditor = jTabbedEditorPanel.getTitleAt(editorIndex);
        }

        jSplitPanelVertical.setDividerLocation(0.7);
        appendNormal(terminal, "Compilando " + nombreEditor + "...");

        try {
            CharStream input = CharStreams.fromString(code);

            LenguajeLexer lexer = new LenguajeLexer(input);
            lexer.removeErrorListeners();

            CommonTokenStream tokens = new CommonTokenStream(lexer);

            LenguajeParser parser = new LenguajeParser(tokens);
            parser.removeErrorListeners();

            TerminalErrorListener errorListener = new TerminalErrorListener(terminal);

            lexer.addErrorListener(errorListener);
            parser.addErrorListener(errorListener);

            ParseTree tree = parser.programa();

            if (!errorListener.hasErrors()) {
                AnalisisSemantica visitor = new AnalisisSemantica(terminal);
                visitor.visit(tree);

                if (visitor.getErrores().isEmpty()) {
                    appendNormalSuccess(terminal, "Ejecución finalizada sin errores.");
                }
            }

            tablaSimbolosPanel1.actualizarDesdeCodigo(code);
            tablaDeTokensPanel1.actualizarTabla(code);
            arbolSintacticoPanel1.showTreeGui(code);
            arbolSemanticoPanel2.showSemanticTree(code);
            formaIntermediaPane1.generarYMostrarIR(tree);
            tacPane1.showTAC(code);

        } catch (Exception ex) {
            appendError(terminal, "Error interno: " + ex.getMessage());
        }
    }//GEN-LAST:event_jButtonRunActionPerformed

    private void jButtonLogoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLogoActionPerformed
        resetApp();
    }//GEN-LAST:event_jButtonLogoActionPerformed

    private void formWindowStateChanged(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowStateChanged
        boolean maximized = (getExtendedState() & java.awt.Frame.MAXIMIZED_BOTH) == java.awt.Frame.MAXIMIZED_BOTH;
        if (maximized) {
            jButtonMax.setText("❐");
        } else {
            jButtonMax.setText("⬜");
        }
    }//GEN-LAST:event_formWindowStateChanged

    private void jButtonFilesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonFilesMousePressed
        
    }//GEN-LAST:event_jButtonFilesMousePressed

    private void jButtonFilesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonFilesMouseEntered
        jButtonFiles.setForeground(new Color(190,190,190));
    }//GEN-LAST:event_jButtonFilesMouseEntered

    private void jButtonFilesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonFilesMouseExited
        jButtonFiles.setForeground(new Color(255,255,255));
    }//GEN-LAST:event_jButtonFilesMouseExited

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new main().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private AnalizadorSemantico.ArbolSemanticoPanel arbolSemanticoPanel2;
    private AnalizadorSintactico.ArbolSintacticoPanel arbolSintacticoPanel1;
    private CodigoIntermedio.FormaIntermediaPanel formaIntermediaPane1;
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButtonFiles;
    private javax.swing.JButton jButtonLogo;
    private javax.swing.JButton jButtonMax;
    private javax.swing.JButton jButtonMin;
    private javax.swing.JButton jButtonRun;
    private javax.swing.JComboBox<String> jComboBoxGraphicsSelected;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelDraggingPane;
    private javax.swing.JPanel jPanelEditor;
    private javax.swing.JPanel jPanelEditorButtons;
    private javax.swing.JPanel jPanelEditorTools;
    private javax.swing.JPanel jPanelEditorToolsFiller;
    private javax.swing.JPanel jPanelFrameMods;
    private javax.swing.JPanel jPanelGraphics;
    private javax.swing.JPanel jPanelLogo;
    private javax.swing.JPanel jPanelMainTabs;
    private javax.swing.JPanel jPanelTitleBar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPanelHorizontal;
    private javax.swing.JSplitPane jSplitPanelVertical;
    private javax.swing.JTabbedPane jTabbedEditorPanel;
    private javax.swing.JTabbedPane jTabbedPaneTerminal;
    private TablaDeTokens.TablaDeTokensPanel tablaDeTokensPanel1;
    private TablaDeSimbolos.TablaDeSimbolosPanel tablaSimbolosPanel1;
    private CodigoIntermedio.PanelTAC tacPane1;
    // End of variables declaration//GEN-END:variables
}

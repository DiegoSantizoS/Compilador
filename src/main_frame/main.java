package main_frame;
//@author DFSS

import AnalizadorSemantico.Analizadorsem;
import generated.LenguajeLexer;
import generated.LenguajeParser;
import java.awt.Color;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import main_components.ClosableTabComponent;
import main_components.TerminalErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

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

    private final Map<java.awt.Component, File> tabFiles = new HashMap<>();
    private int nuevoContador = 1;
    private JTextArea terminalUnica;

    public main() {
        setUndecorated(true);
        initComponents();
        configurarTabsCerrables();
        crearPopupArchivo();
        jSplitPanelVertical.setDividerLocation(1.0);

        inicializarTerminal();
    }

    private void inicializarTerminal() {
        terminalUnica = new JTextArea();
        terminalUnica.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(terminalUnica);

        jTabbedPaneTerminal.removeAll();
        jTabbedPaneTerminal.addTab("Terminal", scrollPane);
        jTabbedPaneTerminal.setSelectedComponent(scrollPane);
    }

    private JTextArea nuevaTerminal() {
        if (terminalUnica == null) {
            inicializarTerminal();
        }

        int editorIndex = jTabbedEditorPanel.getSelectedIndex();
        String nombreEditor = "Sin archivo";

        if (editorIndex != -1) {
            nombreEditor = jTabbedEditorPanel.getTitleAt(editorIndex);
        }

        terminalUnica.setText("");
        jTabbedPaneTerminal.setTitleAt(0, "Output - " + nombreEditor);
        jTabbedPaneTerminal.setSelectedIndex(0);

        return terminalUnica;
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
        JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);

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

            JTextArea textArea = new JTextArea();
            textArea.setText(contenido);

            JScrollPane scrollPane = new JScrollPane(textArea);

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

        JTextArea textArea = getTextAreaFromTab(tabActual);

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

        JTextArea textArea = getTextAreaFromTab(tabActual);

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

    private JTextArea getTextAreaFromTab(java.awt.Component tabComponent) {
        if (!(tabComponent instanceof JScrollPane scrollPane)) {
            return null;
        }

        java.awt.Component view = scrollPane.getViewport().getView();

        if (view instanceof JTextArea textArea) {
            return textArea;
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

    private JTextArea getCurrentEditor() {
        java.awt.Component selected = jTabbedEditorPanel.getSelectedComponent();

        if (selected instanceof JScrollPane scrollPane) {
            java.awt.Component view = scrollPane.getViewport().getView();

            if (view instanceof JTextArea textArea) {
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
        jButtonRun = new javax.swing.JButton();
        jButtonSearch = new javax.swing.JButton();
        jPanelEditorToolsFiller = new javax.swing.JPanel();
        jComboBoxGraphicsSelected = new javax.swing.JComboBox<>();
        jTabbedEditorPanel = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanelGraphics = new javax.swing.JPanel();
        tablaTokenPanel1 = new tokens.TablaTokenPanel();
        syntaxTreePane1 = new syntax_tree.SyntaxTreePane();
        tablaSimbolosPanel1 = new symbols.TablaSimbolosPanel();
        jTabbedPaneTerminal = new javax.swing.JTabbedPane();
        jScrollPanelTerminal0 = new javax.swing.JScrollPane();
        jTextAreaTerminal0 = new javax.swing.JTextArea();
        jPanelTitleBar = new javax.swing.JPanel();
        jPanelMainTabs = new javax.swing.JPanel();
        jButtonLogo = new javax.swing.JButton();
        jButtonFiles = new javax.swing.JButton();
        jPanelDraggingPane = new javax.swing.JPanel();
        jPanelFrameMods = new javax.swing.JPanel();
        jButtonMin = new javax.swing.JButton();
        jButtonMax = new javax.swing.JButton();
        jButtonClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
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
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jSplitPanelVertical.setDividerLocation(350);
        jSplitPanelVertical.setDividerSize(3);
        jSplitPanelVertical.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPanelVertical.setResizeWeight(1.0);

        jSplitPanelHorizontal.setDividerLocation(300);
        jSplitPanelHorizontal.setDividerSize(3);
        jSplitPanelHorizontal.setMinimumSize(new java.awt.Dimension(604, 200));

        jPanelEditor.setBackground(new java.awt.Color(255, 255, 102));
        jPanelEditor.setMinimumSize(new java.awt.Dimension(300, 35));
        jPanelEditor.setLayout(new java.awt.GridBagLayout());

        jPanelEditorTools.setMaximumSize(new java.awt.Dimension(360, 35));
        jPanelEditorTools.setMinimumSize(new java.awt.Dimension(360, 35));
        jPanelEditorTools.setPreferredSize(new java.awt.Dimension(360, 35));
        jPanelEditorTools.setLayout(new java.awt.GridBagLayout());

        jPanelEditorButtons.setLayout(new java.awt.GridBagLayout());

        jButtonRun.setText("RUN");
        jButtonRun.setMaximumSize(new java.awt.Dimension(75, 35));
        jButtonRun.setMinimumSize(new java.awt.Dimension(75, 35));
        jButtonRun.setPreferredSize(new java.awt.Dimension(75, 35));
        jButtonRun.addActionListener(this::jButtonRunActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelEditorButtons.add(jButtonRun, gridBagConstraints);

        jButtonSearch.setText("SEARCH");
        jButtonSearch.setMaximumSize(new java.awt.Dimension(75, 35));
        jButtonSearch.setMinimumSize(new java.awt.Dimension(75, 35));
        jButtonSearch.setPreferredSize(new java.awt.Dimension(75, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelEditorButtons.add(jButtonSearch, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weighty = 1.0;
        jPanelEditorTools.add(jPanelEditorButtons, gridBagConstraints);

        javax.swing.GroupLayout jPanelEditorToolsFillerLayout = new javax.swing.GroupLayout(jPanelEditorToolsFiller);
        jPanelEditorToolsFiller.setLayout(jPanelEditorToolsFillerLayout);
        jPanelEditorToolsFillerLayout.setHorizontalGroup(
            jPanelEditorToolsFillerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelEditorToolsFillerLayout.setVerticalGroup(
            jPanelEditorToolsFillerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelEditorTools.add(jPanelEditorToolsFiller, gridBagConstraints);

        jComboBoxGraphicsSelected.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxGraphicsSelected.setMaximumSize(new java.awt.Dimension(120, 22));
        jComboBoxGraphicsSelected.setMinimumSize(new java.awt.Dimension(120, 22));
        jComboBoxGraphicsSelected.setPreferredSize(new java.awt.Dimension(120, 22));
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

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jTabbedEditorPanel.addTab("tab1", jScrollPane1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.95;
        jPanelEditor.add(jTabbedEditorPanel, gridBagConstraints);

        jSplitPanelHorizontal.setLeftComponent(jPanelEditor);

        jPanelGraphics.setBackground(new java.awt.Color(51, 255, 51));
        jPanelGraphics.setLayout(new java.awt.CardLayout());
        jPanelGraphics.add(tablaTokenPanel1, "token_table");
        jPanelGraphics.add(syntaxTreePane1, "syntax_tree");
        jPanelGraphics.add(tablaSimbolosPanel1, "symbol_table");

        jSplitPanelHorizontal.setRightComponent(jPanelGraphics);

        jSplitPanelVertical.setTopComponent(jSplitPanelHorizontal);

        jTabbedPaneTerminal.setMaximumSize(new java.awt.Dimension(0, 110));
        jTabbedPaneTerminal.setMinimumSize(new java.awt.Dimension(0, 55));
        jTabbedPaneTerminal.setPreferredSize(new java.awt.Dimension(0, 110));

        jTextAreaTerminal0.setEditable(false);
        jTextAreaTerminal0.setColumns(20);
        jTextAreaTerminal0.setRows(5);
        jScrollPanelTerminal0.setViewportView(jTextAreaTerminal0);

        jTabbedPaneTerminal.addTab("tab1", jScrollPanelTerminal0);

        jSplitPanelVertical.setBottomComponent(jTabbedPaneTerminal);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        getContentPane().add(jSplitPanelVertical, gridBagConstraints);

        jPanelTitleBar.setBackground(new java.awt.Color(255, 153, 153));
        jPanelTitleBar.setMaximumSize(new java.awt.Dimension(0, 35));
        jPanelTitleBar.setMinimumSize(new java.awt.Dimension(0, 35));
        jPanelTitleBar.setPreferredSize(new java.awt.Dimension(0, 35));
        jPanelTitleBar.setLayout(new java.awt.GridBagLayout());

        jPanelMainTabs.setBackground(new java.awt.Color(204, 255, 204));
        jPanelMainTabs.setMinimumSize(new java.awt.Dimension(150, 0));
        jPanelMainTabs.setPreferredSize(new java.awt.Dimension(150, 0));
        jPanelMainTabs.setLayout(new java.awt.GridBagLayout());

        jButtonLogo.setText("*LOGO*");
        jButtonLogo.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonLogo.setMaximumSize(new java.awt.Dimension(50, 35));
        jButtonLogo.setMinimumSize(new java.awt.Dimension(50, 35));
        jButtonLogo.setPreferredSize(new java.awt.Dimension(50, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        jPanelMainTabs.add(jButtonLogo, gridBagConstraints);

        jButtonFiles.setText("Archivos");
        jButtonFiles.setMaximumSize(new java.awt.Dimension(100, 35));
        jButtonFiles.setMinimumSize(new java.awt.Dimension(100, 35));
        jButtonFiles.setPreferredSize(new java.awt.Dimension(100, 35));
        jButtonFiles.addActionListener(this::jButtonFilesActionPerformed);
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

        jPanelDraggingPane.setBackground(new java.awt.Color(102, 255, 102));
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
            .addGap(0, 35, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelTitleBar.add(jPanelDraggingPane, gridBagConstraints);

        jPanelFrameMods.setBackground(new java.awt.Color(51, 153, 0));
        jPanelFrameMods.setMaximumSize(new java.awt.Dimension(150, 0));
        jPanelFrameMods.setMinimumSize(new java.awt.Dimension(150, 0));
        jPanelFrameMods.setPreferredSize(new java.awt.Dimension(150, 0));
        jPanelFrameMods.setLayout(new java.awt.GridBagLayout());

        jButtonMin.setText("-");
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

        jButtonMax.setText("+");
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

        jButtonClose.setText("x");
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
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
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
    }//GEN-LAST:event_jPanelDraggingPaneMouseDragged

    private void jButtonMinMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonMinMouseEntered
        jButtonMin.setBackground(Color.BLACK);
    }//GEN-LAST:event_jButtonMinMouseEntered

    private void jButtonMinMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonMinMouseExited
        jButtonMin.setBackground(Color.WHITE);
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
    }//GEN-LAST:event_formMouseDragged

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        int dir = getResizeDirection(evt.getX(), evt.getY());
        setResizeCursor(dir);
    }//GEN-LAST:event_formMouseMoved

    private void formMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseExited
        if (resizeDirection == RESIZE_NONE) {
            setCursor(java.awt.Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_formMouseExited

    private void jComboBoxGraphicsSelectedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxGraphicsSelectedActionPerformed
        java.awt.CardLayout cl = (java.awt.CardLayout) jPanelGraphics.getLayout();

        if (jComboBoxGraphicsSelected.getSelectedItem().equals("Item 1")) {
            cl.show(jPanelGraphics, "token_table");
        } else if (jComboBoxGraphicsSelected.getSelectedItem().equals("Item 2")) {
            cl.show(jPanelGraphics, "syntax_tree");
        } else if (jComboBoxGraphicsSelected.getSelectedItem().equals("Item 3")) {
            cl.show(jPanelGraphics, "symbol_table");
        }
    }//GEN-LAST:event_jComboBoxGraphicsSelectedActionPerformed

    private void jButtonRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRunActionPerformed
        JTextArea editor = getCurrentEditor();
        if (editor == null) {
            javax.swing.JOptionPane.showMessageDialog(this, "No hay un editor activo.");
            return;
        }

        String code = editor.getText();

        JTextArea terminal = nuevaTerminal();
        if (terminal == null) {
            return;
        }

        //terminal.setText("");
        int editorIndex = jTabbedEditorPanel.getSelectedIndex();
        String nombreEditor = "Sin archivo";

        if (editorIndex != -1) {
            nombreEditor = jTabbedEditorPanel.getTitleAt(editorIndex);
        }

        jSplitPanelVertical.setDividerLocation(0.7);
        terminal.append("Compilando " + nombreEditor + "...\n");

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
                Analizadorsem visitor = new Analizadorsem(terminal);
                visitor.visit(tree);

                terminal.append("Ejecución finalizada sin errores.\n");
            }

            tablaSimbolosPanel1.actualizarDesdeCodigo(code);
            tablaTokenPanel1.actualizarTabla(code);
            syntaxTreePane1.showTreeGui(code);

        } catch (Exception ex) {
            terminal.append("Error interno: " + ex.getMessage() + "\n");
        }
    }//GEN-LAST:event_jButtonRunActionPerformed

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
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButtonFiles;
    private javax.swing.JButton jButtonLogo;
    private javax.swing.JButton jButtonMax;
    private javax.swing.JButton jButtonMin;
    private javax.swing.JButton jButtonRun;
    private javax.swing.JButton jButtonSearch;
    private javax.swing.JComboBox<String> jComboBoxGraphicsSelected;
    private javax.swing.JPanel jPanelDraggingPane;
    private javax.swing.JPanel jPanelEditor;
    private javax.swing.JPanel jPanelEditorButtons;
    private javax.swing.JPanel jPanelEditorTools;
    private javax.swing.JPanel jPanelEditorToolsFiller;
    private javax.swing.JPanel jPanelFrameMods;
    private javax.swing.JPanel jPanelGraphics;
    private javax.swing.JPanel jPanelMainTabs;
    private javax.swing.JPanel jPanelTitleBar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPanelTerminal0;
    private javax.swing.JSplitPane jSplitPanelHorizontal;
    private javax.swing.JSplitPane jSplitPanelVertical;
    private javax.swing.JTabbedPane jTabbedEditorPanel;
    private javax.swing.JTabbedPane jTabbedPaneTerminal;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextAreaTerminal0;
    private syntax_tree.SyntaxTreePane syntaxTreePane1;
    private symbols.TablaSimbolosPanel tablaSimbolosPanel1;
    private tokens.TablaTokenPanel tablaTokenPanel1;
    // End of variables declaration//GEN-END:variables
}

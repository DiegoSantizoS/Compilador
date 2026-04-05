package mainFrame;
import java.io.IOException;
import org.fife.ui.rsyntaxtextarea.*;
import org.fife.ui.rtextarea.*;
import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Insets;
import java.awt.Toolkit;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;
import org.fife.ui.rtextarea.SearchResult;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Frame;

public class mainOldOld extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(mainOldOld.class.getName());
    private org.fife.ui.rsyntaxtextarea.RSyntaxTextArea textArea = new RSyntaxTextArea();
    private RunButton btnRun; 
    private SearchButton btnSearch1; 
    private javax.swing.JPopupMenu popupArchivo;
    private javax.swing.JMenuItem itemNuevo;
    private javax.swing.JMenuItem itemAbrir;
    private javax.swing.JMenuItem itemGuardar;
    private javax.swing.JMenuItem itemGuardarComo;
    //private tabla_token panelTokens;
    
    public mainOldOld() {
        getContentPane().setBackground(new Color(41, 49, 52)); 
        setBackground(new Color(41, 49, 52));
        UIManager.put("Panel.background", new Color(41, 49, 52));
        UIManager.put("control", new Color(41, 49, 52));
        
        setUndecorated(true);
        //setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf()); 
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        UIManager.put("Button.arc", 16);          
        UIManager.put ("Component.arc", 16);
        UIManager.put("TextComponent.arc", 12);
        UIManager.put("ScrollBar.thumbArc", 999);
        UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));
        initComponents();
        
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        textArea.setCodeFoldingEnabled(true);
        textArea.setAntiAliasingEnabled(true);
        textArea.setBracketMatchingEnabled(true);
        textArea.setHighlightCurrentLine(true);
        textArea.setTabsEmulated(true);
        textArea.setTabSize(4);

        RTextScrollPane sp = new RTextScrollPane(textArea);
        sp.setBorder(BorderFactory.createEmptyBorder());   
        sp.setViewportBorder(null);  
        sp.setLineNumbersEnabled(true);
        jScrollPane1.setViewportView(sp);
        
        try {
           Theme theme = Theme.load(getClass().getResourceAsStream(
                 "/mainFrame/customDarkTheme.xml"));
           theme.apply(textArea);
        } catch (IOException ioe) { 
           ioe.printStackTrace();
        }
        
       //jScrollPane1.setViewportView(textArea);
        jScrollPane1.putClientProperty("editor", textArea);
        syntaxTreePane1.showTreeGui("");
        
        crearPopupArchivo();
        configurarBotonesVentana();
        
        btnMax.setContentAreaFilled(false);
        btnMax.setBorderPainted(false);
        btnMin.setContentAreaFilled(false);
        btnMin.setBorderPainted(false);
        btnClose.setContentAreaFilled(false);
        btnClose.setBorderPainted(false);
      }
    
    
    private void crearPopupArchivo() {
        popupArchivo = new javax.swing.JPopupMenu();
        popupArchivo.setOpaque(true);
        popupArchivo.setBackground(new java.awt.Color(60, 63, 65));
        popupArchivo.setBorderPainted(true);
        popupArchivo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(80, 80, 80), 1));
        //popupArchivo.setLightWeightPopupEnabled(false);

        itemNuevo = new javax.swing.JMenuItem("Nuevo");
        itemAbrir = new javax.swing.JMenuItem("Abrir");
        itemGuardar = new javax.swing.JMenuItem("Guardar");
        itemGuardarComo = new javax.swing.JMenuItem("Guardar Como");
        
        estilizarMenuItem(itemNuevo);
        estilizarMenuItem(itemAbrir);
        estilizarMenuItem(itemGuardar);
        estilizarMenuItem(itemGuardarComo);
        
        itemNuevo.addActionListener(e -> {
            javax.swing.JOptionPane.showMessageDialog(this, "Click en Nuevo");
        });

        itemAbrir.addActionListener(e -> {
            javax.swing.JOptionPane.showMessageDialog(this, "Click en Abrir");
        });

        itemGuardar.addActionListener(e -> {
            javax.swing.JOptionPane.showMessageDialog(this, "Click en Guardar");
        });
        
        itemGuardar.addActionListener(e -> {
            javax.swing.JOptionPane.showMessageDialog(this, "Click en Guardar Como");
        });

        popupArchivo.add(itemNuevo);
        popupArchivo.add(itemAbrir);
        popupArchivo.add(itemGuardar);
        popupArchivo.add(itemGuardarComo);
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
    
    private void configurarBotonesVentana() {
        estilizarBotonVentana(btnMin);
        estilizarBotonVentana(btnMax);
        estilizarBotonVentana(btnClose);
    }

    private void estilizarBotonVentana(javax.swing.JButton btn) {
        btn.setPreferredSize(new java.awt.Dimension(45, 30));
        btn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btn.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        btn.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(false);
        btn.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));

        //btn.setBackground(new java.awt.Color(41, 49, 52));
        btn.setForeground(java.awt.Color.WHITE);
        //btn.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 16));
    }

    
    private int untitledCount = 2;

    private void addNewEditorTab(String title) {
        RSyntaxTextArea ta = new RSyntaxTextArea();
        ta.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        ta.setCodeFoldingEnabled(true);
        ta.setAntiAliasingEnabled(true);
        ta.setBracketMatchingEnabled(true);
        ta.setHighlightCurrentLine(true);
        ta.setTabsEmulated(true);
        ta.setTabSize(4);

        try {
            Theme theme = Theme.load(getClass().getResourceAsStream("/mainFrame/customDarkTheme.xml"));
            theme.apply(ta);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        RTextScrollPane sp = new RTextScrollPane(ta);
        sp.setLineNumbersEnabled(true);
        sp.putClientProperty("editor", ta);

        jTabbedPane1.addTab(title, sp);

        int idx = jTabbedPane1.indexOfComponent(sp);
        jTabbedPane1.setTabComponentAt(idx, new ClosableTabComponent(jTabbedPane1));

        jTabbedPane1.setSelectedIndex(idx);
        ta.requestFocusInWindow();
    }
    
    private Component getCurrentTabComponent() {
        return jTabbedPane1.getSelectedComponent(); // usually your (RTextScrollPane/JScrollPane)
    }

    private Path getCurrentTabPath() {
        Component c = getCurrentTabComponent();
        if (c instanceof JComponent jc) {
            Object p = jc.getClientProperty("filePath");
            return (p instanceof Path path) ? path : null;
        }
        return null;
    }

    private void setCurrentTabPath(Path path) {
        Component c = getCurrentTabComponent();
        if (c instanceof JComponent jc) {
            jc.putClientProperty("filePath", path);
        }
    }

    private void setTabTitleForComponent(Component tabComponent, String title) {
        int idx = jTabbedPane1.indexOfComponent(tabComponent);
        if (idx >= 0) {
            jTabbedPane1.setTitleAt(idx, title);

            // If you use a custom tab header (ClosableTabComponent), update its label too
            Component header = jTabbedPane1.getTabComponentAt(idx);
            if (header instanceof ClosableTabComponent ctc) {
                ctc.setTitle(title);  // you’ll add this method (below)
            } else if (header instanceof Container cont) {
                // fallback: try to find a JLabel in the header and update it
                for (Component child : cont.getComponents()) {
                    if (child instanceof JLabel lab) { lab.setText(title); break; }
                }
            }
        }
    }
    
    private void saveCurrentTab(boolean forceSaveAs) {
        RSyntaxTextArea ta = getCurrentEditor();
        if (ta == null) return;

        Component tabComp = getCurrentTabComponent();
        if (!(tabComp instanceof JComponent)) return;

        Path path = getCurrentTabPath();

        if (forceSaveAs || path == null) {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle(forceSaveAs ? "Save As" : "Save");
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            int res = chooser.showSaveDialog(this); // or your JFrame
            if (res != JFileChooser.APPROVE_OPTION) return;

            File f = chooser.getSelectedFile();

            String name = f.getName();
            if (!name.toLowerCase().endsWith(".txt")) {
                f = new File(f.getParentFile(), name + ".txt");
            }

            path = f.toPath();
            setCurrentTabPath(path);
        }

        try {
            Files.writeString(path, ta.getText(), StandardCharsets.UTF_8);

            // Update tab name to the file name
            setTabTitleForComponent(tabComp, path.getFileName().toString());

            // Optional: clear "modified" state if you track it
            // ta.discardAllEdits();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not save file:\n" + ex.getMessage(),
                    "Save error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private Integer findTabByPath(Path path) {
        for (int i = 0; i < jTabbedPane1.getTabCount(); i++) {
            Component comp = jTabbedPane1.getComponentAt(i); // your scrollpane
            if (comp instanceof JComponent jc) {
                Object fp = jc.getClientProperty("filePath");
                if (fp instanceof Path p && Objects.equals(p.toAbsolutePath().normalize(), path.toAbsolutePath().normalize())) {
                    return i;
                }
            }
        }
        return null;
    }
    
    private void openFileIntoTab() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Open");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setAcceptAllFileFilterUsed(true);
        chooser.setFileFilter(new FileNameExtensionFilter("Text files (*.txt)", "txt"));

        int res = chooser.showOpenDialog(this); // or your frame
        if (res != JFileChooser.APPROVE_OPTION) return;

        File f = chooser.getSelectedFile();
        Path path = f.toPath();

        // If already open, just focus it
        Integer existing = findTabByPath(path);
        if (existing != null) {
            jTabbedPane1.setSelectedIndex(existing);
            RSyntaxTextArea ta = getCurrentEditor();
            if (ta != null) ta.requestFocusInWindow();
            return;
        }

        try {
            String content = Files.readString(path, StandardCharsets.UTF_8);

            addNewEditorTab(f.getName());
            RSyntaxTextArea ta = getCurrentEditor(); 
            if (ta != null) {
                ta.setText(content);
                ta.setCaretPosition(0);
                ta.discardAllEdits();
            }
            // Store the path on the selected tab component
            Component tabComp = jTabbedPane1.getSelectedComponent();
            if (tabComp instanceof JComponent jc) {
                jc.putClientProperty("filePath", path);
            }

            // (Optional) if your header needs explicit title update:
            // setTabTitleForComponent(tabComp, f.getName());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not open file:\n" + ex.getMessage(),
                    "Open error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
 

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panelEditor = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        javax.swing.JTextArea jTextArea1 = new RSyntaxTextArea();
        jToolBar1 = new javax.swing.JToolBar();
        javax.swing.JButton btnRun = new RunButton();
        javax.swing.JButton btnSearch1 = new SearchButton();
        tablaTokenPanel1 = new TablaDeTokens.TablaDeTokensPanel();
        syntaxTreePane1 = new AnalizadorSintactico.SyntaxTreePane1();
        titlePanel = new javax.swing.JPanel();
        panelPestanas = new javax.swing.JPanel();
        btnArchivo = new javax.swing.JButton();
        panelMods = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        btnMin = new javax.swing.JButton();
        btnMax = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        dragPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setIconImages(null);
        setMinimumSize(new java.awt.Dimension(600, 250));
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panelEditor.setMinimumSize(new java.awt.Dimension(0, 0));
        panelEditor.setPreferredSize(new java.awt.Dimension(450, 150));
        panelEditor.setLayout(new java.awt.GridBagLayout());

        jTabbedPane1.setMinimumSize(new java.awt.Dimension(0, 0));
        jTabbedPane1.setOpaque(true);

        jScrollPane1.setBorder(null);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane1.setMinimumSize(new java.awt.Dimension(0, 0));

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jTextArea1.setRows(5);
        jTextArea1.setFocusable(false);
        jTextArea1.setMinimumSize(new java.awt.Dimension(0, 0));
        jTextArea1.setFocusTraversalKeysEnabled(false);
        jTextArea1.setFocusable(false);
        jTextArea1.setBorder(BorderFactory.createEmptyBorder());
        jScrollPane1.setViewportView(jTextArea1);

        jScrollPane1.setFocusTraversalKeysEnabled(false);
        jScrollPane1.setFocusable(false);
        jScrollPane1.setBorder(BorderFactory.createEmptyBorder());

        jTabbedPane1.addTab("untitled1", jScrollPane1);

        jTabbedPane1.setTabComponentAt(
            jTabbedPane1.indexOfComponent(jScrollPane1),
            new ClosableTabComponent(jTabbedPane1)
        );

        jScrollPane1.setFocusTraversalKeysEnabled(false);
        jScrollPane1.setFocusable(false);
        jScrollPane1.setBorder(BorderFactory.createEmptyBorder());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panelEditor.add(jTabbedPane1, gridBagConstraints);

        jToolBar1.setRollover(true);

        btnRun.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRun.setFocusPainted(false);
        btnRun.setFocusable(false);
        btnRun.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRun.setMaximumSize(new java.awt.Dimension(40, 40));
        btnRun.setMinimumSize(new java.awt.Dimension(40, 40));
        btnRun.setPreferredSize(new java.awt.Dimension(40, 40));
        btnRun.setSelected(true);
        btnRun.setVerifyInputWhenFocusTarget(false);
        btnRun.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRun.addActionListener(this::btnRunActionPerformed);
        btnRun.setSelected(false);
        btnRun.setFocusable(false);
        btnRun.setFocusPainted(false);
        btnRun.setContentAreaFilled(false);
        //btnRun.setIcon(loadScaledIcon("/mainFrame/play.png", 40, 40));
        jToolBar1.add(btnRun);

        btnSearch1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSearch1.setFocusable(false);
        btnSearch1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSearch1.setMaximumSize(new java.awt.Dimension(40, 40));
        btnSearch1.setMinimumSize(new java.awt.Dimension(40, 40));
        btnSearch1.setPreferredSize(new java.awt.Dimension(40, 40));
        btnSearch1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSearch1.addActionListener(this::btnSearch1ActionPerformed);
        jToolBar1.add(btnSearch1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.05;
        panelEditor.add(jToolBar1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.95;
        getContentPane().add(panelEditor, gridBagConstraints);

        tablaTokenPanel1.setMaximumSize(new java.awt.Dimension(2000, 2500));
        tablaTokenPanel1.setMinimumSize(new java.awt.Dimension(0, 0));
        tablaTokenPanel1.setPreferredSize(new java.awt.Dimension(380, 350));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(tablaTokenPanel1, gridBagConstraints);

        syntaxTreePane1.setToolTipText("");
        syntaxTreePane1.setMinimumSize(new java.awt.Dimension(0, 0));
        syntaxTreePane1.setOpaque(false);
        syntaxTreePane1.setPreferredSize(new java.awt.Dimension(0, 500));

        javax.swing.GroupLayout syntaxTreePane1Layout = new javax.swing.GroupLayout(syntaxTreePane1);
        syntaxTreePane1.setLayout(syntaxTreePane1Layout);
        syntaxTreePane1Layout.setHorizontalGroup(
            syntaxTreePane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 797, Short.MAX_VALUE)
        );
        syntaxTreePane1Layout.setVerticalGroup(
            syntaxTreePane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 509, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 20, 10);
        getContentPane().add(syntaxTreePane1, gridBagConstraints);

        titlePanel.setMaximumSize(new java.awt.Dimension(1250, 35));
        titlePanel.setMinimumSize(new java.awt.Dimension(1250, 35));
        titlePanel.setPreferredSize(new java.awt.Dimension(1250, 35));
        titlePanel.setLayout(new java.awt.GridBagLayout());

        panelPestanas.setMaximumSize(new java.awt.Dimension(101, 35));
        panelPestanas.setMinimumSize(new java.awt.Dimension(101, 35));
        panelPestanas.setName(""); // NOI18N
        panelPestanas.setPreferredSize(new java.awt.Dimension(101, 35));
        panelPestanas.setLayout(new java.awt.GridBagLayout());

        btnArchivo.setText("ARCHIVO");
        btnArchivo.setBorder(null);
        btnArchivo.setBorderPainted(false);
        btnArchivo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnArchivo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnArchivo.setIconTextGap(0);
        btnArchivo.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnArchivo.setMaximumSize(new java.awt.Dimension(101, 35));
        btnArchivo.setMinimumSize(new java.awt.Dimension(101, 35));
        btnArchivo.setPreferredSize(new java.awt.Dimension(101, 35));
        btnArchivo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnArchivoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnArchivoMouseExited(evt);
            }
        });
        btnArchivo.addActionListener(this::btnArchivoActionPerformed);
        btnArchivo.setOpaque(false);
        btnArchivo.setContentAreaFilled(true);
        btnArchivo.setBorderPainted(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panelPestanas.add(btnArchivo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        titlePanel.add(panelPestanas, gridBagConstraints);

        panelMods.setMaximumSize(new java.awt.Dimension(0, 35));
        panelMods.setMinimumSize(new java.awt.Dimension(0, 35));
        panelMods.setPreferredSize(new java.awt.Dimension(0, 35));
        panelMods.setLayout(new java.awt.GridBagLayout());

        jPanel3.setMaximumSize(new java.awt.Dimension(120, 35));
        jPanel3.setMinimumSize(new java.awt.Dimension(120, 35));
        jPanel3.setPreferredSize(new java.awt.Dimension(120, 35));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        btnMin.setText("—");
        btnMin.setAlignmentY(0.0F);
        btnMin.setBorder(null);
        btnMin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMin.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMin.setIconTextGap(0);
        btnMin.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnMin.setMaximumSize(new java.awt.Dimension(100, 35));
        btnMin.setMinimumSize(new java.awt.Dimension(100, 35));
        btnMin.setPreferredSize(new java.awt.Dimension(100, 35));
        btnMin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMinMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMinMouseExited(evt);
            }
        });
        btnMin.addActionListener(this::btnMinActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel3.add(btnMin, gridBagConstraints);

        btnMax.setFont(new java.awt.Font("Segoe UI Variable", 0, 24)); // NOI18N
        btnMax.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mainFrame/maximize.png"))); // NOI18N
        btnMax.setAlignmentY(0.0F);
        btnMax.setBorder(null);
        btnMax.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMax.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMax.setIconTextGap(0);
        btnMax.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnMax.setMaximumSize(new java.awt.Dimension(100, 35));
        btnMax.setMinimumSize(new java.awt.Dimension(100, 35));
        btnMax.setPreferredSize(new java.awt.Dimension(100, 35));
        btnMax.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMaxMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMaxMouseExited(evt);
            }
        });
        btnMax.addActionListener(this::btnMaxActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel3.add(btnMax, gridBagConstraints);

        btnClose.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnClose.setText("X");
        btnClose.setAlignmentY(0.0F);
        btnClose.setBorder(null);
        btnClose.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClose.setIconTextGap(0);
        btnClose.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnClose.setMaximumSize(new java.awt.Dimension(100, 35));
        btnClose.setMinimumSize(new java.awt.Dimension(100, 35));
        btnClose.setPreferredSize(new java.awt.Dimension(100, 35));
        btnClose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCloseMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCloseMouseExited(evt);
            }
        });
        btnClose.addActionListener(this::btnCloseActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel3.add(btnClose, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panelMods.add(jPanel3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 1.0;
        titlePanel.add(panelMods, gridBagConstraints);

        dragPanel.setMaximumSize(new java.awt.Dimension(0, 35));
        dragPanel.setMinimumSize(new java.awt.Dimension(0, 35));
        dragPanel.setPreferredSize(new java.awt.Dimension(0, 35));
        dragPanel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                dragPanelMouseDragged(evt);
            }
        });
        dragPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                dragPanelMousePressed(evt);
            }
        });

        javax.swing.GroupLayout dragPanelLayout = new javax.swing.GroupLayout(dragPanel);
        dragPanel.setLayout(dragPanelLayout);
        dragPanelLayout.setHorizontalGroup(
            dragPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        dragPanelLayout.setVerticalGroup(
            dragPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.weighty = 1.0;
        titlePanel.add(dragPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.05;
        getContentPane().add(titlePanel, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private RSyntaxTextArea getCurrentEditor() {
        Component c = jTabbedPane1.getSelectedComponent();

        if (c instanceof JComponent jc) {
            Object editor = jc.getClientProperty("editor");
            if (editor instanceof RSyntaxTextArea ta) return ta;

            if (c instanceof JScrollPane sp) {
                Component view = sp.getViewport().getView();
                if (view instanceof RSyntaxTextArea ta2) return ta2;
            }
        }
        return null;
    }


    private String lastQuery = "";
    private boolean lastMatchCase = false;
    private boolean lastRegex = false;
    private void btnSearch1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearch1ActionPerformed
        RSyntaxTextArea ta = getCurrentEditor();
        ta.setBorder(BorderFactory.createEmptyBorder()); 
        if (ta == null) return;

        String q = JOptionPane.showInputDialog(this, "Find:", lastQuery);
        if (q == null) return;
        q = q.trim();
        if (q.isEmpty()) return;

        lastQuery = q;

        SearchContext ctx = new SearchContext(q);
        ctx.setMatchCase(lastMatchCase);
        ctx.setRegularExpression(lastRegex);
        ctx.setWholeWord(false);

        // 1) Mark all (highlight all occurrences) - independent of caret position
        ctx.setMarkAll(true);
        SearchEngine.markAll(ta, ctx);

        // 2) Find "next" from caret
        ctx.setMarkAll(false);        // don't rely on markAll for "found" logic
        ctx.setSearchForward(true);

        SearchResult r = SearchEngine.find(ta, ctx);

        // 3) Wrap-around: if not found, jump to start and try again
        if (!r.wasFound()) {
            ta.setCaretPosition(0);
            r = SearchEngine.find(ta, ctx);
        }

        // 4) Only now say not found
        if (!r.wasFound()) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this, "Not found: " + q);
        }
    }//GEN-LAST:event_btnSearch1ActionPerformed

    private void btnRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRunActionPerformed
        RSyntaxTextArea ta = getCurrentEditor();
            if (ta != null) {
                    String code = ta.getText();
                    //tablaSimbolosPanel1.actualizarDesdeCodigo(code);
                    tablaTokenPanel1.actualizarTabla(code);
                    syntaxTreePane1.showTreeGui(code);
                    System.out.println(code);
                }
    }//GEN-LAST:event_btnRunActionPerformed

    private void btnArchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnArchivoActionPerformed
        popupArchivo.show(btnArchivo, 0, btnArchivo.getHeight());
    }//GEN-LAST:event_btnArchivoActionPerformed

    private void btnMinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMinActionPerformed
        setExtendedState(this.ICONIFIED);
    }//GEN-LAST:event_btnMinActionPerformed

    private void btnMinMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMinMouseEntered
        btnMin.setOpaque(true);
        btnMin.setContentAreaFilled(true);
        btnMin.setBorderPainted(true);
        btnMin.setBackground(Color.GRAY);
    }//GEN-LAST:event_btnMinMouseEntered

    private void btnMinMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMinMouseExited
        btnMin.setOpaque(false);
        btnMin.setContentAreaFilled(false);
        btnMin.setBorderPainted(false);
    }//GEN-LAST:event_btnMinMouseExited

    private void btnMaxMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMaxMouseEntered
        btnMax.setOpaque(true);
        btnMax.setContentAreaFilled(true);
        btnMax.setBorderPainted(true);
        btnMax.setBackground(Color.GRAY);
    }//GEN-LAST:event_btnMaxMouseEntered

    private void btnMaxMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMaxMouseExited
        btnMax.setOpaque(false);
        btnMax.setContentAreaFilled(false);
        btnMax.setBorderPainted(false);
    }//GEN-LAST:event_btnMaxMouseExited

    private void btnCloseMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseEntered
        btnClose.setOpaque(true);
        btnClose.setContentAreaFilled(true);
        btnClose.setBorderPainted(true);
        btnClose.setBackground(Color.RED);
    }//GEN-LAST:event_btnCloseMouseEntered

    private void btnCloseMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseExited
        btnClose.setOpaque(false);
        btnClose.setContentAreaFilled(false);
        btnClose.setBorderPainted(false);
    }//GEN-LAST:event_btnCloseMouseExited

    private void btnArchivoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnArchivoMouseEntered
        btnArchivo.setOpaque(false);
        btnArchivo.setContentAreaFilled(true);
        btnArchivo.setBorderPainted(true);
    }//GEN-LAST:event_btnArchivoMouseEntered

    private void btnArchivoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnArchivoMouseExited
        btnArchivo.setOpaque(true);
        btnArchivo.setContentAreaFilled(false);
        btnArchivo.setBorderPainted(false);
    }//GEN-LAST:event_btnArchivoMouseExited

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnMaxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMaxActionPerformed
        if ((this.getExtendedState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH) {
                    this.setExtendedState(Frame.NORMAL);
                } else {
                    this.setExtendedState(Frame.MAXIMIZED_BOTH);
                }
    }//GEN-LAST:event_btnMaxActionPerformed
private int xMouse, yMouse;
    private void dragPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dragPanelMousePressed
        xMouse = evt.getX();
        yMouse = evt.getY();
    }//GEN-LAST:event_dragPanelMousePressed

    private void dragPanelMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dragPanelMouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xMouse - 101, y - yMouse);
    }//GEN-LAST:event_dragPanelMouseDragged

    private static final int BORDER = 8;
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
    
    
    private int getResizeDirection(int x, int y) {
        int width = getWidth();
        int height = getHeight();

        boolean north = y <= BORDER;
        boolean south = y >= height - BORDER;
        boolean west = x <= BORDER;
        boolean east = x >= width - BORDER;

        int dir = RESIZE_NONE;

        if (north) dir |= RESIZE_N;
        if (south) dir |= RESIZE_S;
        if (west) dir |= RESIZE_W;
        if (east) dir |= RESIZE_E;

        return dir;
    }
    
    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        resizeDirection = getResizeDirection(evt.getX(), evt.getY());

        if (resizeDirection != RESIZE_NONE) {
            resizeStartScreen = evt.getLocationOnScreen();
            frameStartBounds = getBounds();
        }
    }//GEN-LAST:event_formMousePressed

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
    }//GEN-LAST:event_formMouseDragged

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        int dir = getResizeDirection(evt.getX(), evt.getY());

        switch (dir) {
            case RESIZE_N:
                setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.N_RESIZE_CURSOR));
                break;
            case RESIZE_S:
                setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.S_RESIZE_CURSOR));
                break;
            case RESIZE_W:
                setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.W_RESIZE_CURSOR));
                break;
            case RESIZE_E:
                setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.E_RESIZE_CURSOR));
                break;
            case RESIZE_N | RESIZE_W:
                setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.NW_RESIZE_CURSOR));
                break;
            case RESIZE_N | RESIZE_E:
                setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.NE_RESIZE_CURSOR));
                break;
            case RESIZE_S | RESIZE_W:
                setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.SW_RESIZE_CURSOR));
                break;
            case RESIZE_S | RESIZE_E:
                setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.SE_RESIZE_CURSOR));
                break;
            default:
                setCursor(java.awt.Cursor.getDefaultCursor());
                break;
        }
    }//GEN-LAST:event_formMouseMoved

    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
        resizeDirection = RESIZE_NONE;
        resizeStartScreen = null;
        frameStartBounds = null;
        setCursor(java.awt.Cursor.getDefaultCursor());
    }//GEN-LAST:event_formMouseReleased

    /**
     * @param args the command line arguments
     */
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
        java.awt.EventQueue.invokeLater(() -> new mainOldOld().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnArchivo;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnMax;
    private javax.swing.JButton btnMin;
    private javax.swing.JPanel dragPanel;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JPanel panelEditor;
    private javax.swing.JPanel panelMods;
    private javax.swing.JPanel panelPestanas;
    private AnalizadorSintactico.SyntaxTreePane1 syntaxTreePane1;
    private TablaDeTokens.TablaDeTokensPanel tablaTokenPanel1;
    private javax.swing.JPanel titlePanel;
    // End of variables declaration//GEN-END:variables
}

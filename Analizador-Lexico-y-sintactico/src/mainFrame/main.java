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

/**
 *
 * @author DFSS
 */
public class main extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(main.class.getName());
    private org.fife.ui.rsyntaxtextarea.RSyntaxTextArea textArea = new RSyntaxTextArea();
    private RunButton btnRun; 
    private SearchButton btnSearch1; 
    //private tabla_token panelTokens;
    
    /**
     * Creates new form main
     */
    public main() {
        
        getContentPane().setBackground(new Color(41, 49, 52)); 
        setBackground(new Color(41, 49, 52));
        UIManager.put("Panel.background", new Color(41, 49, 52));
        UIManager.put("control", new Color(41, 49, 52));
        
        setUndecorated(true);
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
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
        tablaTokenPanel1 = new tokens.TablaTokenPanel();
        tablaSimbolosPanel1 = new util.TablaSimbolosPanel();
        syntaxTreePane1 = new SyntaxTree.SyntaxTreePane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItemNew = new javax.swing.JMenuItem();
        jMenuItemOpen = new javax.swing.JMenuItem();
        jMenuItemSave = new javax.swing.JMenuItem();
        jMenuItemSaveAs = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setIconImages(null);
        setMaximumSize(new java.awt.Dimension(1920, 1680));
        setMinimumSize(new java.awt.Dimension(960, 500));
        setPreferredSize(new java.awt.Dimension(960, 500));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panelEditor.setMinimumSize(new java.awt.Dimension(250, 150));
        panelEditor.setPreferredSize(new java.awt.Dimension(250, 150));
        panelEditor.setLayout(new java.awt.GridBagLayout());

        jTabbedPane1.setMinimumSize(new java.awt.Dimension(232, 124));
        jTabbedPane1.setOpaque(true);

        jScrollPane1.setBorder(null);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jTextArea1.setRows(5);
        jTextArea1.setFocusable(false);
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
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(panelEditor, gridBagConstraints);

        tablaTokenPanel1.setMaximumSize(new java.awt.Dimension(2000, 2500));
        tablaTokenPanel1.setMinimumSize(new java.awt.Dimension(380, 350));
        tablaTokenPanel1.setPreferredSize(new java.awt.Dimension(380, 350));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(tablaTokenPanel1, gridBagConstraints);

        tablaSimbolosPanel1.setMaximumSize(new java.awt.Dimension(2000, 2500));
        tablaSimbolosPanel1.setMinimumSize(new java.awt.Dimension(380, 350));
        tablaSimbolosPanel1.setPreferredSize(new java.awt.Dimension(380, 350));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(tablaSimbolosPanel1, gridBagConstraints);

        syntaxTreePane1.setToolTipText("");
        syntaxTreePane1.setMinimumSize(new java.awt.Dimension(0, 500));
        syntaxTreePane1.setOpaque(false);
        syntaxTreePane1.setPreferredSize(new java.awt.Dimension(0, 500));

        javax.swing.GroupLayout syntaxTreePane1Layout = new javax.swing.GroupLayout(syntaxTreePane1);
        syntaxTreePane1.setLayout(syntaxTreePane1Layout);
        syntaxTreePane1Layout.setHorizontalGroup(
            syntaxTreePane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        syntaxTreePane1Layout.setVerticalGroup(
            syntaxTreePane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 20, 10);
        getContentPane().add(syntaxTreePane1, gridBagConstraints);

        jMenuBar1.setOpaque(true);

        jMenu1.setText("File");
        jMenu1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jMenu1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jMenu1.setIconTextGap(0);

        jMenuItemNew.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jMenuItemNew.setText("New");
        jMenuItemNew.addActionListener(this::jMenuItemNewActionPerformed);
        jMenu1.add(jMenuItemNew);

        jMenuItemOpen.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jMenuItemOpen.setText("Open");
        jMenuItemOpen.addActionListener(this::jMenuItemOpenActionPerformed);
        jMenu1.add(jMenuItemOpen);

        jMenuItemSave.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jMenuItemSave.setText("Save");
        jMenuItemSave.addActionListener(this::jMenuItemSaveActionPerformed);
        jMenu1.add(jMenuItemSave);

        jMenuItemSaveAs.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jMenuItemSaveAs.setText("Save as");
        jMenuItemSaveAs.addActionListener(this::jMenuItemSaveAsActionPerformed);
        jMenu1.add(jMenuItemSaveAs);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

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
                    tablaSimbolosPanel1.actualizarDesdeCodigo(code);
                    tablaTokenPanel1.actualizarTabla(code);
                    syntaxTreePane1.showTreeGui(code);
                    System.out.println(code);
                }
    }//GEN-LAST:event_btnRunActionPerformed

    private void jMenuItemNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemNewActionPerformed
        addNewEditorTab("untitled" + (untitledCount++));
    }//GEN-LAST:event_jMenuItemNewActionPerformed

    private void jMenuItemOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOpenActionPerformed
        openFileIntoTab();
    }//GEN-LAST:event_jMenuItemOpenActionPerformed

    private void jMenuItemSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSaveActionPerformed
        saveCurrentTab(false);
    }//GEN-LAST:event_jMenuItemSaveActionPerformed

    private void jMenuItemSaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSaveAsActionPerformed
        saveCurrentTab(false);
    }//GEN-LAST:event_jMenuItemSaveAsActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new main().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItemNew;
    private javax.swing.JMenuItem jMenuItemOpen;
    private javax.swing.JMenuItem jMenuItemSave;
    private javax.swing.JMenuItem jMenuItemSaveAs;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JPanel panelEditor;
    private SyntaxTree.SyntaxTreePane syntaxTreePane1;
    private util.TablaSimbolosPanel tablaSimbolosPanel1;
    private tokens.TablaTokenPanel tablaTokenPanel1;
    // End of variables declaration//GEN-END:variables
}

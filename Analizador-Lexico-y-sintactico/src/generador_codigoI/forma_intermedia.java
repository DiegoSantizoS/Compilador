
package generador_codigoI;

/**
 *
 * @author migue
 */
import generated.LenguajeLexer;
import generated.LenguajeParser;
import java.awt.BorderLayout; 
import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class forma_intermedia extends javax.swing.JFrame  {

    /**
     * Creates new form forma_intermedia
     */
    public forma_intermedia() {
        
        initComponents();
    
    
}
    
     
    private StringBuilder ir = new StringBuilder();
    private int labelCount = 0;
    private String nuevaEtiqueta() {
    return "L" + (labelCount++);
}
    
     public void generarYMostrarIR(org.antlr.v4.runtime.tree.ParseTree tree) {
        ir.setLength(0);
        recorrer(tree);
        jTextArea1.setText(ir.toString());
        
    }
   private String recorrer(org.antlr.v4.runtime.tree.ParseTree tree) {
    System.out.println("Nodo: " + tree.getClass().getSimpleName());

    // PROGRAMA
    if (tree instanceof generated.LenguajeParser.ProgramaContext ctx) {
        for (var s : ctx.sentencia()) recorrer(s);
    }

    // SENTENCIA (wrapper — NUEVO)
    else if (tree instanceof generated.LenguajeParser.SentenciaContext ctx) {
        recorrer(ctx.getChild(0));
    }

    // SENTENCIA EN BLOQUE (wrapper — NUEVO)
    else if (tree instanceof generated.LenguajeParser.SentenciaBloqueContext ctx) {
        recorrer(ctx.getChild(0));
    }

    // DECLARACION
    else if (tree instanceof generated.LenguajeParser.DeclaracionContext ctx) {
        String id = ctx.ID().getText();
        if (ctx.expresion() != null) {
            String expr = recorrer(ctx.expresion());
            ir.append("DECL_ASSIGN(").append(id).append(", ").append(expr).append(")\n");
        } else {
            ir.append("DECL(").append(id).append(")\n");
        }
    }

    // ASIGNACION
    else if (tree instanceof generated.LenguajeParser.AsignacionContext ctx) {
        String id = ctx.ID().getText();
        String expr = recorrer(ctx.expresion());
        ir.append("ASSIGN(").append(id).append(", ").append(expr).append(")\n");
    }

    // PRINT
    else if (tree instanceof generated.LenguajeParser.ImprimirContext ctx) {
        String expr = recorrer(ctx.expresion());
        ir.append("PRINT(").append(expr).append(")\n");
    }

    // IF
    else if (tree instanceof generated.LenguajeParser.SiContext ctx) {
        String cond = recorrer(ctx.expresion());
        String L1 = nuevaEtiqueta();
        String L2 = nuevaEtiqueta();

        ir.append("IF NOT ").append(cond).append(" GOTO ").append(L1).append("\n");
        recorrer(ctx.bloque(0));

        if (ctx.bloque().size() > 1) {
            ir.append("GOTO ").append(L2).append("\n");
            ir.append(L1).append(":\n");
            recorrer(ctx.bloque(1));
            ir.append(L2).append(":\n");
        } else {
            ir.append(L1).append(":\n");
        }
    }

    // WHILE
    else if (tree instanceof generated.LenguajeParser.MientrasContext ctx) {
        String L1 = nuevaEtiqueta();
        String L2 = nuevaEtiqueta();

        ir.append(L1).append(":\n");
        String cond = recorrer(ctx.expresion());
        ir.append("IF NOT ").append(cond).append(" GOTO ").append(L2).append("\n");
        recorrer(ctx.bloque());
        ir.append("GOTO ").append(L1).append("\n");
        ir.append(L2).append(":\n");
    }

    // BLOQUE
    else if (tree instanceof generated.LenguajeParser.BloqueContext ctx) {
        for (var s : ctx.sentenciaBloque()) recorrer(s);
    }

    else if (tree instanceof generated.LenguajeParser.ExpresionContext ctx) {
    return recorrer(ctx.getChild(0));
}
    // OR
    else if (tree instanceof generated.LenguajeParser.OrExprContext ctx) {
        String result = recorrer(ctx.andExpr(0));
        for (int i = 1; i < ctx.andExpr().size(); i++) {
            String right = recorrer(ctx.andExpr(i));
            result = "OR(" + result + ", " + right + ")";
        }
        return result;
    }

    // AND
    else if (tree instanceof generated.LenguajeParser.AndExprContext ctx) {
        String result = recorrer(ctx.igualdad(0));
        for (int i = 1; i < ctx.igualdad().size(); i++) {
            String right = recorrer(ctx.igualdad(i));
            result = "AND(" + result + ", " + right + ")";
        }
        return result;
    }

    // == !=
    else if (tree instanceof generated.LenguajeParser.IgualdadContext ctx) {
        String result = recorrer(ctx.comparacion(0));
        for (int i = 1; i < ctx.comparacion().size(); i++) {
            String right = recorrer(ctx.comparacion(i));
            String op = ctx.getChild(2 * i - 1).getText();
            result = op.equals("==") ? "EQ(" + result + ", " + right + ")"
                                     : "NE(" + result + ", " + right + ")";
        }
        return result;
    }

    // > < >= <=
    else if (tree instanceof generated.LenguajeParser.ComparacionContext ctx) {
        String result = recorrer(ctx.suma(0));
        for (int i = 1; i < ctx.suma().size(); i++) {
            String right = recorrer(ctx.suma(i));
            String op = ctx.getChild(2 * i - 1).getText();
            result = switch (op) {
                case ">"  -> "GT(" + result + ", " + right + ")";
                case "<"  -> "LT(" + result + ", " + right + ")";
                case ">=" -> "GE(" + result + ", " + right + ")";
                case "<=" -> "LE(" + result + ", " + right + ")";
                default   -> result;
            };
        }
        return result;
    }

    // + -
    else if (tree instanceof generated.LenguajeParser.SumaContext ctx) {
        String result = recorrer(ctx.mult(0));
        for (int i = 1; i < ctx.mult().size(); i++) {
            String right = recorrer(ctx.mult(i));
            String op = ctx.getChild(2 * i - 1).getText();
            result = op.equals("+") ? "ADD(" + result + ", " + right + ")"
                                    : "SUB(" + result + ", " + right + ")";
        }
        return result;
    }

    // * /
    else if (tree instanceof generated.LenguajeParser.MultContext ctx) {
        String result = recorrer(ctx.unario(0));
        for (int i = 1; i < ctx.unario().size(); i++) {
            String right = recorrer(ctx.unario(i));
            String op = ctx.getChild(2 * i - 1).getText();
            result = op.equals("*") ? "MUL(" + result + ", " + right + ")"
                                    : "DIV(" + result + ", " + right + ")";
        }
        return result;
    }

    // UNARIO
    else if (tree instanceof generated.LenguajeParser.UnarioContext ctx) {
        if (ctx.unario() != null) {
            String val = recorrer(ctx.unario());
            String op = ctx.getChild(0).getText();
            return op.equals("!") ? "NOT(" + val + ")" : "NEG(" + val + ")";
        }
        return recorrer(ctx.primario());
    }

    // PRIMARIO
    else if (tree instanceof generated.LenguajeParser.PrimarioContext ctx) {
        if (ctx.expresion()  != null) return recorrer(ctx.expresion());
        if (ctx.NUMERO()     != null) return "CONST(" + ctx.NUMERO().getText() + ")";
        if (ctx.ID()         != null) return "VAR(" + ctx.ID().getText() + ")";
        if (ctx.VERDADERO()  != null) return "TRUE";
        if (ctx.FALSO()      != null) return "FALSE";
        if (ctx.CADENA_LIT() != null) return "STR(" + ctx.CADENA_LIT().getText() + ")";
        if (ctx.leer()       != null) return "READ()";
    }

    return "";
}
    
     
     
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel1.setText("Tabla de Tokens");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel2.setText("Codigo Intermedio");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 490, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(108, 108, 108)
                        .addComponent(jLabel2)))
                .addContainerGap(36, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(forma_intermedia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(forma_intermedia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(forma_intermedia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(forma_intermedia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new forma_intermedia().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}

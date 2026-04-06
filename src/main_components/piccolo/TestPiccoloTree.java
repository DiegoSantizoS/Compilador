/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main_components.piccolo;

import javax.swing.JFrame;

public class TestPiccoloTree {
    public static void main(String[] args) {
        TreeNodeModel root = new TreeNodeModel("programa");

        TreeNodeModel decl = new TreeNodeModel("declaracion");
        decl.addChild(new TreeNodeModel("tipo"));
        decl.addChild(new TreeNodeModel("ID"));
        decl.addChild(new TreeNodeModel("="));
        decl.addChild(new TreeNodeModel("expresion"));

        TreeNodeModel imprimir = new TreeNodeModel("imprimir");
        imprimir.addChild(new TreeNodeModel("IMPRIMIR"));
        imprimir.addChild(new TreeNodeModel("cadena"));

        root.addChild(decl);
        root.addChild(imprimir);

        PiccoloTreePanel panel = new PiccoloTreePanel("Árbol de prueba");
        panel.setTree(root);

        JFrame frame = new JFrame("Test Piccolo Tree");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);
        frame.add(panel);
        frame.setVisible(true);
    }
}
package main_components.piccolo;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

public class ANTLRTreeBuilder {

    public static TreeNodeModel fromParseTree(ParseTree tree) {
        if (tree == null) {
            return null;
        }
        return build(tree);
    }

    private static TreeNodeModel build(ParseTree tree) {
        String label;

        if (tree instanceof TerminalNode terminal) {
            label = terminal.getText();
        } else {
            String raw = tree.getClass().getSimpleName();
            label = raw.replace("Context", "");
        }

        TreeNodeModel node = new TreeNodeModel(label);

        for (int i = 0; i < tree.getChildCount(); i++) {
            node.addChild(build(tree.getChild(i)));
        }

        return node;
    }
}
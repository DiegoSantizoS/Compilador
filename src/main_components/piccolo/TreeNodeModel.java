package main_components.piccolo;
import java.util.ArrayList;
import java.util.List;

public class TreeNodeModel {

    private final String label;
    private final List<TreeNodeModel> children = new ArrayList<>();

    public TreeNodeModel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public List<TreeNodeModel> getChildren() {
        return children;
    }

    public void addChild(TreeNodeModel child) {
        children.add(child);
    }
}
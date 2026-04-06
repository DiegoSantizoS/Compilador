package AnalizadorSintactico;

import generated.LenguajeLexer;
import generated.LenguajeParser;
import main_components.piccolo.ANTLRTreeBuilder;
import main_components.piccolo.PiccoloTreePanel;
import main_components.piccolo.TreeNodeModel;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class ArbolSintacticoPanel extends PiccoloTreePanel {

    public ArbolSintacticoPanel() {
        super("Árbol Sintáctico");
    }

    public void showTreeGui(String codigo) {
        CharStream input = CharStreams.fromString(codigo);
        LenguajeLexer lexer = new LenguajeLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LenguajeParser parser = new LenguajeParser(tokens);

        ParseTree tree = parser.programa();
        TreeNodeModel root = ANTLRTreeBuilder.fromParseTree(tree);

        setTree(root);
    }
}
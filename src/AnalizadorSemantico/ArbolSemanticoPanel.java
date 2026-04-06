package AnalizadorSemantico;

import generated.LenguajeLexer;
import generated.LenguajeParser;
import javax.swing.JTextPane;
import main_components.piccolo.PiccoloTreePanel;
import main_components.piccolo.TreeNodeModel;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class ArbolSemanticoPanel extends PiccoloTreePanel {

    private final JTextPane terminal;

    public ArbolSemanticoPanel(JTextPane terminal) {
        super("Árbol Semántico");
        this.terminal = terminal;
    }
    
    public ArbolSemanticoPanel() {
        super("Árbol Semántico");
        this.terminal = null;
    }

    public void showSemanticTree(String codigo) {
        CharStream input = CharStreams.fromString(codigo);
        LenguajeLexer lexer = new LenguajeLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LenguajeParser parser = new LenguajeParser(tokens);

        LenguajeParser.ProgramaContext tree = parser.programa();

        AnalisisSemantica analizador = new AnalisisSemantica(terminal);
        analizador.visit(tree);

        TreeNodeModel root = analizador.getArbolSemantico();
        setTree(root);
    }
}
package main_components;

import javax.swing.JTextArea;
import org.antlr.v4.runtime.*;

public class TerminalErrorListener extends BaseErrorListener {
    private final JTextArea terminal;
    private boolean hasErrors = false;

    public TerminalErrorListener(JTextArea terminal) {
        this.terminal = terminal;
    }

    public boolean hasErrors() {
        return hasErrors;
    }

    @Override
    public void syntaxError(
            Recognizer<?, ?> recognizer,
            Object offendingSymbol,
            int line,
            int charPositionInLine,
            String msg,
            RecognitionException e) {

        hasErrors = true;
        terminal.append("Error [line " + line + ":" + charPositionInLine + "] " + msg + "\n");
    }
}
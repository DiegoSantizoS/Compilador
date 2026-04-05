package main_components;

import java.awt.Color;
import javax.swing.JTextPane;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class TerminalErrorListener extends BaseErrorListener {
    private final JTextPane terminal;
    private boolean hasErrors = false;

    public TerminalErrorListener(JTextPane terminal) {
        this.terminal = terminal;
    }

    public boolean hasErrors() {
        return hasErrors;
    }

    private void logError(String mensaje) {
        if (terminal != null) {
            StyledDocument doc = terminal.getStyledDocument();
            Style errorStyle = terminal.addStyle("SyntaxErrorStyle", null);
            StyleConstants.setForeground(errorStyle, Color.RED);

            try {
                doc.insertString(doc.getLength(), mensaje + "\n", errorStyle);
            } catch (Exception e) {
                System.out.println(mensaje);
            }
        } else {
            System.out.println(mensaje);
        }
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
        logError("Error [line " + line + ":" + charPositionInLine + "] " + msg);
    }
}
package syntax;

import javax.swing.text.Segment;
import org.fife.ui.rsyntaxtextarea.AbstractTokenMaker;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMap;

public class LenguajeTokenMaker extends AbstractTokenMaker {

    @Override
    public TokenMap getWordsToHighlight() {
        TokenMap map = new TokenMap(true);

        map.put("si", Token.RESERVED_WORD);
        map.put("sino", Token.RESERVED_WORD);
        map.put("mientras", Token.RESERVED_WORD);
        map.put("imprimir", Token.RESERVED_WORD);
        map.put("leer", Token.RESERVED_WORD);
        map.put("retornar", Token.RESERVED_WORD);
        map.put("vacio", Token.RESERVED_WORD);

        map.put("entero", Token.DATA_TYPE);
        map.put("real", Token.DATA_TYPE);
        map.put("cadena", Token.DATA_TYPE);
        map.put("booleano", Token.DATA_TYPE);

        map.put("verdadero", Token.LITERAL_BOOLEAN);
        map.put("falso", Token.LITERAL_BOOLEAN);

        return map;
    }

    @Override
    public void addToken(Segment segment, int start, int end, int tokenType, int startOffset) {
        if (tokenType == Token.IDENTIFIER) {
            int value = wordsToHighlight.get(segment, start, end);
            if (value != -1) {
                tokenType = value;
            }
        }
        super.addToken(segment, start, end, tokenType, startOffset);
    }

    @Override
    public String[] getLineCommentStartAndEnd(int languageIndex) {
        return new String[] { "//", null };
    }

    @Override
    public boolean getMarkOccurrencesOfTokenType(int type) {
        return type == Token.IDENTIFIER;
    }

    @Override
    public Token getTokenList(Segment text, int initialTokenType, int startOffset) {
        resetTokenList();

        char[] array = text.array;
        int offset = text.offset;
        int count = text.count;
        int end = offset + count;

        int currentTokenStart = offset;
        int currentTokenType = Token.NULL;

        for (int i = offset; i < end; i++) {
            char c = array[i];

            switch (currentTokenType) {

                case Token.NULL:
                    currentTokenStart = i;

                    if (Character.isWhitespace(c)) {
                        currentTokenType = Token.WHITESPACE;
                    } else if (Character.isLetter(c) || c == '_' || esLetraEspanol(c)) {
                        currentTokenType = Token.IDENTIFIER;
                    } else if (Character.isDigit(c)) {
                        currentTokenType = Token.LITERAL_NUMBER_DECIMAL_INT;
                    } else if (c == '"') {
                        currentTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
                    } else if (c == '/' && i + 1 < end && array[i + 1] == '/') {
                        addToken(text, i, end - 1, Token.COMMENT_EOL, startOffset + i - offset);
                        addNullToken();
                        return firstToken;
                    } else if (c == '/' && i + 1 < end && array[i + 1] == '*') {
                        currentTokenType = Token.COMMENT_MULTILINE;
                    } else if (esSeparador(c)) {
                        addToken(text, i, i, Token.SEPARATOR, startOffset + i - offset);
                        currentTokenType = Token.NULL;
                    } else if (esOperador(c)) {
                        if (i + 1 < end) {
                            char next = array[i + 1];
                            if ((c == '=' && next == '=') ||
                                (c == '!' && next == '=') ||
                                (c == '<' && next == '=') ||
                                (c == '>' && next == '=') ||
                                (c == '&' && next == '&') ||
                                (c == '|' && next == '|')) {
                                addToken(text, i, i + 1, Token.OPERATOR, startOffset + i - offset);
                                i++;
                            } else {
                                addToken(text, i, i, Token.OPERATOR, startOffset + i - offset);
                            }
                        } else {
                            addToken(text, i, i, Token.OPERATOR, startOffset + i - offset);
                        }
                        currentTokenType = Token.NULL;
                    } else {
                        addToken(text, i, i, Token.IDENTIFIER, startOffset + i - offset);
                        currentTokenType = Token.NULL;
                    }
                    break;

                case Token.WHITESPACE:
                    if (!Character.isWhitespace(c)) {
                        addToken(text, currentTokenStart, i - 1, Token.WHITESPACE,
                                startOffset + currentTokenStart - offset);
                        currentTokenStart = i;
                        i--;
                        currentTokenType = Token.NULL;
                    }
                    break;

                case Token.IDENTIFIER:
                    if (!(Character.isLetterOrDigit(c) || c == '_' || esLetraEspanol(c))) {
                        addToken(text, currentTokenStart, i - 1, Token.IDENTIFIER,
                                startOffset + currentTokenStart - offset);
                        currentTokenStart = i;
                        i--;
                        currentTokenType = Token.NULL;
                    }
                    break;

                case Token.LITERAL_NUMBER_DECIMAL_INT:
                    if (Character.isDigit(c)) {
                        // sigue entero
                    } else if (c == '.') {
                        currentTokenType = Token.LITERAL_NUMBER_FLOAT;
                    } else {
                        addToken(text, currentTokenStart, i - 1, Token.LITERAL_NUMBER_DECIMAL_INT,
                                startOffset + currentTokenStart - offset);
                        currentTokenStart = i;
                        i--;
                        currentTokenType = Token.NULL;
                    }
                    break;

                case Token.LITERAL_NUMBER_FLOAT:
                    if (!Character.isDigit(c)) {
                        addToken(text, currentTokenStart, i - 1, Token.LITERAL_NUMBER_FLOAT,
                                startOffset + currentTokenStart - offset);
                        currentTokenStart = i;
                        i--;
                        currentTokenType = Token.NULL;
                    }
                    break;

                case Token.LITERAL_STRING_DOUBLE_QUOTE:
                    if (c == '\\') {
                        if (i + 1 < end) {
                            i++;
                        }
                    } else if (c == '"') {
                        addToken(text, currentTokenStart, i, Token.LITERAL_STRING_DOUBLE_QUOTE,
                                startOffset + currentTokenStart - offset);
                        currentTokenType = Token.NULL;
                    }
                    break;

                case Token.COMMENT_MULTILINE:
                    if (c == '*' && i + 1 < end && array[i + 1] == '/') {
                        addToken(text, currentTokenStart, i + 1, Token.COMMENT_MULTILINE,
                                startOffset + currentTokenStart - offset);
                        i++;
                        currentTokenType = Token.NULL;
                    }
                    break;

                default:
                    addToken(text, currentTokenStart, i, Token.IDENTIFIER,
                            startOffset + currentTokenStart - offset);
                    currentTokenType = Token.NULL;
                    break;
            }
        }

        switch (currentTokenType) {
            case Token.WHITESPACE:
                addToken(text, currentTokenStart, end - 1, Token.WHITESPACE,
                        startOffset + currentTokenStart - offset);
                break;
            case Token.IDENTIFIER:
                addToken(text, currentTokenStart, end - 1, Token.IDENTIFIER,
                        startOffset + currentTokenStart - offset);
                break;
            case Token.LITERAL_NUMBER_DECIMAL_INT:
                addToken(text, currentTokenStart, end - 1, Token.LITERAL_NUMBER_DECIMAL_INT,
                        startOffset + currentTokenStart - offset);
                break;
            case Token.LITERAL_NUMBER_FLOAT:
                addToken(text, currentTokenStart, end - 1, Token.LITERAL_NUMBER_FLOAT,
                        startOffset + currentTokenStart - offset);
                break;
            case Token.LITERAL_STRING_DOUBLE_QUOTE:
                addToken(text, currentTokenStart, end - 1, Token.LITERAL_STRING_DOUBLE_QUOTE,
                        startOffset + currentTokenStart - offset);
                break;
            case Token.COMMENT_MULTILINE:
                addToken(text, currentTokenStart, end - 1, Token.COMMENT_MULTILINE,
                        startOffset + currentTokenStart - offset);
                break;
            case Token.NULL:
            default:
                break;
        }

        addNullToken();
        return firstToken;
    }

    private boolean esSeparador(char c) {
        return c == '(' || c == ')' ||
               c == '{' || c == '}' ||
               c == ';' || c == ',';
    }

    private boolean esOperador(char c) {
        return c == '=' || c == '!' ||
               c == '<' || c == '>' ||
               c == '+' || c == '-' ||
               c == '*' || c == '/' ||
               c == '&' || c == '|';
    }

    private boolean esLetraEspanol(char c) {
        return "áéíóúÁÉÍÓÚñÑ".indexOf(c) >= 0;
    }
}
package lexical;

import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.HashMap;
import java.util.Map;

import error.InternalException;
import interpreter.literal.AtomLiteral;
import interpreter.value.AtomValue;
import interpreter.value.IntValue;
import interpreter.value.StringValue;

public class LexicalAnalysis implements AutoCloseable {

    private int line;
    private PushbackInputStream input;
    private static Map<String, Token.Type> keywords;

    static {
        keywords = new HashMap<String, Token.Type>();

        // SYMBOLS
        keywords.put(",", Token.Type.COMMA);
        keywords.put(":", Token.Type.COLON);
        keywords.put("|", Token.Type.LIST_SEPARATOR);
        keywords.put("<-", Token.Type.LEFT_ARROW);
        keywords.put("->", Token.Type.RIGHT_ARROW);
        keywords.put("(", Token.Type.OPEN_PAR);
        keywords.put(")", Token.Type.CLOSE_PAR);
        keywords.put("[", Token.Type.OPEN_BRA);
        keywords.put("]", Token.Type.CLOSE_BRA);
        keywords.put("{", Token.Type.OPEN_CUR);
        keywords.put("}", Token.Type.CLOSE_CUR);

        // OPERATORS
        keywords.put("=", Token.Type.ASSIGN);
        keywords.put("&&", Token.Type.AND);
        keywords.put("||", Token.Type.OR);
        keywords.put("<", Token.Type.LOWER_THAN);
        keywords.put("<=", Token.Type.LOWER_EQUAL);
        keywords.put(">", Token.Type.GREATER_THAN);
        keywords.put(">=", Token.Type.GREATER_EQUAL);
        keywords.put("==", Token.Type.EQUAL);
        keywords.put("!=", Token.Type.NOT_EQUAL);
        keywords.put("+", Token.Type.ADD);
        keywords.put("-", Token.Type.SUB);
        keywords.put("*", Token.Type.MUL);
        keywords.put("/", Token.Type.DIV);
        keywords.put("!", Token.Type.NOT);
        keywords.put("++", Token.Type.LIST_CONCAT);
        keywords.put("--", Token.Type.LIST_SUBTRACT);
        keywords.put("<>", Token.Type.STR_CONCAT);

        // KEYWORDS
        keywords.put("if", Token.Type.IF);
        keywords.put("do", Token.Type.DO);
        keywords.put("else", Token.Type.ELSE);
        keywords.put("end", Token.Type.END);
        keywords.put("unless", Token.Type.UNLESS);
        keywords.put("for", Token.Type.FOR);
        keywords.put("cond", Token.Type.COND);
        keywords.put("fn", Token.Type.FN);
        keywords.put("hd", Token.Type.HD);
        keywords.put("tl", Token.Type.TL);
        keywords.put("puts", Token.Type.PUTS);
        keywords.put("read", Token.Type.READ);
        keywords.put("int", Token.Type.INT);
        keywords.put("str", Token.Type.STR);
        keywords.put("length", Token.Type.LENGTH);
        keywords.put("at", Token.Type.AT);
        keywords.put("rem", Token.Type.REM);
    }

    public LexicalAnalysis(InputStream is) {
        input = new PushbackInputStream(is);
        line = 1;
    }

    public void close() {
        try {
            input.close();
        } catch (Exception e) {
            throw new InternalException("Unable to close file");
        }
    }

    public int getLine() {
        return this.line;
    }

    public Token nextToken() {
        Token token = new Token("", Token.Type.END_OF_FILE, null);

        int state = 1;
        while (state != 14 && state != 15) {
            int c = getc();
            // System.out.printf("  [%02d, %03d ('%c')]\n",
            //     state, c, (char) c);

            switch (state) {
                case 1:
                    if (c == ' ' || c == '\t' || c == '\r') {
                        state = 1;
                    } else if (c == '\n') {
                        this.line++;
                        state = 1;
                    } else if (c == '#') {
                        state = 2;
                    } else if (c == '<') {
                        token.lexeme += (char) c;
                        state = 3;
                    } else if (c == '=' || c == '!' || c == '>') {
                        token.lexeme += (char) c;
                        state = 4;
                    } else if (c == '&') {
                        token.lexeme += (char) c;
                        state = 5;
                    } else if (c == '|') {
                        token.lexeme += (char) c;
                        state = 6;
                    } else if (c == '+') {
                        token.lexeme += (char) c;
                        state = 7;
                    } else if (c == '-') {
                        token.lexeme += (char) c;
                        state = 8;
                    } else if (c == ',' || c == '*' || c == '/' ||
                                c == '(' || c == ')' || c == '[' || c == ']' ||
                                c == '{' || c == '}') {
                        token.lexeme += (char) c;
                        state = 14;
                    } else if (c == '_' || Character.isLetter(c)) {
                        token.lexeme += (char) c;
                        state = 9;
                    } else if (c == ':') {
                        token.lexeme += (char) c;
                        state = 10;
                    } else if (Character.isDigit(c)) {
                        token.lexeme += (char) c;
                        state = 12;
                    } else if (c == '"') {
                        state = 13;
                    } else if (c == -1) {
                        token.type = Token.Type.END_OF_FILE;
                        state = 15;
                    } else {
                        token.lexeme += (char) c;
                        token.type = Token.Type.INVALID_TOKEN;
                        state = 15;
                    }

                    break;
                case 2:
                    if (c == '\n') {
                        this.line++;
                        state = 1;
                    } else if (c == -1) {
                        token.type = Token.Type.END_OF_FILE;
                        state = 15;
                    }

                    break;
                case 3:
                    if (c == '>' || c == '=' || c == '-') {
                        token.lexeme += (char) c;
                        state = 14;
                    } else {
                        ungetc(c);
                        state = 14;
                    }

                    break;
                case 4:
                    if (c == '=') {
                        token.lexeme += (char) c;
                        state = 14;
                    } else {
                        ungetc(c);
                        state = 14;
                    }

                    break;
                case 5:
                    if (c == '&') {
                        token.lexeme += (char) c;
                        state = 14;
                    } else {
                        token.type = Token.Type.INVALID_TOKEN;
                        state = 15;
                    }

                    break;
                case 6:
                    if (c == '|') {
                        token.lexeme += (char) c;
                        state = 14;
                    } else {
                        ungetc(c);
                        state = 14;
                    }

                    break;
                case 7:
                    if (c == '+') {
                        token.lexeme += (char) c;
                        state = 14;
                    } else {
                        ungetc(c);
                        state = 14;
                    }

                    break;
                case 8:
                    if (c == '-' || c == '>') {
                        token.lexeme += (char) c;
                        state = 14;
                    } else {
                        ungetc(c);
                        state = 14;
                    }

                    break;
                case 9:
                    if (c == '_' || Character.isLetter(c) || Character.isDigit(c)) {
                        token.lexeme += (char) c;
                        state = 9;
                    } else {
                        ungetc(c);
                        state = 14;
                    }

                    break;
                case 10:
                    if (c == '_' || Character.isLetter(c)) {
                        token.lexeme += (char) c;
                        state = 11;
                    } else {
                        ungetc(c);
                        state = 14;
                    }

                    break;
                case 11:
                    if (c == '_' || Character.isLetter(c) || Character.isDigit(c)) {
                        token.lexeme += (char) c;
                        state = 11;
                    } else {
                        ungetc(c);
                        token.literal = new AtomValue(AtomLiteral.instance(token.lexeme));
                        token.type = Token.Type.ATOM_LITERAL;
                        state = 15;
                    }

                    break;
                case 12:
                    if (Character.isDigit(c)) {
                        token.lexeme += (char) c;
                        state = 12;
                    } else {
                        ungetc(c);
                        token.literal = new IntValue(this.toInt(token.lexeme));
                        token.type = Token.Type.INTEGER_LITERAL;
                        state = 15;
                    }

                    break;
                case 13:
                    if (c == '"') {
                        token.literal = new StringValue(token.lexeme);
                        token.type = Token.Type.STRING_LITERAL;
                        state = 15;
                    } else {
                        if (c == -1) {
                            token.type = Token.Type.UNEXPECTED_EOF;
                            state = 15;
                        } else {
                            if (c == '\n')
                              this.line++;

                            token.lexeme += (char) c;
                            state = 13;
                        }
                    }

                    break;
                default:
                    throw new InternalException("Unreachable");
            }
        } //Lexema formado

        if (state == 14)
            token.type = keywords.containsKey(token.lexeme) ?
                keywords.get(token.lexeme) : Token.Type.NAME;

        token.line = this.line;

        return token;
    }

    private int getc() {
        try {
            return input.read();
        } catch (Exception e) {
            throw new InternalException("Unable to read file");
        }
    }

    private void ungetc(int c) {
        if (c != -1) {
            try {
                input.unread(c);
            } catch (Exception e) {
                throw new InternalException("Unable to ungetc");
            }
        }
    }

    private int toInt(String lexeme) {
        try {
            return Integer.parseInt(lexeme);
        } catch (Exception e) {
            return 0;
        }
    }

}

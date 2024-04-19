package lexical;

import interpreter.value.Value;

public class Token {

    public static enum Type {
        // Specials.
        INVALID_TOKEN,
        UNEXPECTED_EOF,
        END_OF_FILE,

        // Symbols.
        COMMA,           // ,
        COLON,           // :
        LIST_SEPARATOR,  // |
        LEFT_ARROW,      // <-
        RIGHT_ARROW,     // ->
        OPEN_PAR,        // (
        CLOSE_PAR,       // )
        OPEN_BRA,        // [
        CLOSE_BRA,       // ]
        OPEN_CUR,        // {
        CLOSE_CUR,       // }

        // Operators.
        ASSIGN,          // =
        AND,             // &&
        OR,              // ||
        LOWER_THAN,      // <
        LOWER_EQUAL,     // <=
        GREATER_THAN,    // >
        GREATER_EQUAL,   // >=
        EQUAL,           // ==
        NOT_EQUAL,       // !=
        ADD,             // +
        SUB,             // -
        MUL,             // *
        DIV,             // /
        NOT,             // !
        LIST_CONCAT,     // ++
        LIST_SUBTRACT,   // --
        STR_CONCAT,      // <>

        // Keywords.
        IF,              // if
        DO,              // do
        ELSE,            // else
        END,             // end
        UNLESS,          // unless
        FOR,             // for
        COND,            // cond
        FN,              // fn
        PUTS,            // puts
        READ,            // read
        INT,             // int
        STR,             // str
        LENGTH,          // length
        HD,              // hd
        TL,              // tl
        AT,              // at
        REM,             // rem

        // Others.
        NAME,            // identifier
        INTEGER_LITERAL, // integer literal
        FLOAT_LITERAL,   // float literal
        STRING_LITERAL,  // string literal
        ATOM_LITERAL     // char literal

    };

    public String lexeme;
    public Type type;
    public int line;
    public Value<?> literal;

    public Token(String lexeme, Type type, Value<?> literal) {
        this.lexeme = lexeme;
        this.type = type;
        this.line = 0;
        this.literal = literal;
    }

    public String toString() {
        return new StringBuffer()
            .append("(\"")
            .append(this.lexeme)
            .append("\", ")
            .append(this.type)
            .append(", ")
            .append(this.line)
            .append(", ")
            .append(this.literal)
            .append(")")
            .toString();
    }

}

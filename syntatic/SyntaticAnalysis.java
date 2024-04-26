package syntatic;

import static error.LanguageException.Error.InvalidLexeme;
import static error.LanguageException.Error.UnexpectedEOF;
import static error.LanguageException.Error.UnexpectedLexeme;
import static lexical.Token.Type.ADD;
import static lexical.Token.Type.AND;
import static lexical.Token.Type.ASSIGN;
import static lexical.Token.Type.LOWER_THAN;
import static lexical.Token.Type.LOWER_EQUAL;
import static lexical.Token.Type.GREATER_THAN;
import static lexical.Token.Type.GREATER_EQUAL;
import static lexical.Token.Type.EQUAL;
import static lexical.Token.Type.NOT_EQUAL;
import static lexical.Token.Type.AT;
import static lexical.Token.Type.ATOM_LITERAL;
import static lexical.Token.Type.CLOSE_BRA;
import static lexical.Token.Type.CLOSE_PAR;
import static lexical.Token.Type.COMMA;
import static lexical.Token.Type.COND;
import static lexical.Token.Type.DIV;
import static lexical.Token.Type.DO;
import static lexical.Token.Type.ELSE;
import static lexical.Token.Type.END;
import static lexical.Token.Type.END_OF_FILE;
import static lexical.Token.Type.FN;
import static lexical.Token.Type.FOR;
import static lexical.Token.Type.HD;
import static lexical.Token.Type.IF;
import static lexical.Token.Type.INT;
import static lexical.Token.Type.INTEGER_LITERAL;
import static lexical.Token.Type.LEFT_ARROW;
import static lexical.Token.Type.LENGTH;
import static lexical.Token.Type.LIST_CONCAT;
import static lexical.Token.Type.LIST_SUBTRACT;
import static lexical.Token.Type.MUL;
import static lexical.Token.Type.NAME;
import static lexical.Token.Type.NOT;
import static lexical.Token.Type.OPEN_BRA;
import static lexical.Token.Type.OPEN_CUR;
import static lexical.Token.Type.OPEN_PAR;
import static lexical.Token.Type.OR;
import static lexical.Token.Type.PUTS;
import static lexical.Token.Type.READ;
import static lexical.Token.Type.REM;
import static lexical.Token.Type.RIGHT_ARROW;
import static lexical.Token.Type.STR;
import static lexical.Token.Type.STRING_LITERAL;
import static lexical.Token.Type.STR_CONCAT;
import static lexical.Token.Type.SUB;
import static lexical.Token.Type.TL;
import static lexical.Token.Type.UNLESS;

import error.LanguageException;
import interpreter.expr.AssignExpr;
import interpreter.expr.BinaryExpr;
import interpreter.expr.ConstExpr;
import interpreter.expr.Expr;
import interpreter.expr.ExprBlock;
import interpreter.expr.UnaryExpr;
import interpreter.expr.UnlessExpr;
import interpreter.expr.Variable;
import interpreter.expr.BinaryExpr.Op;
import interpreter.value.Value;
import lexical.LexicalAnalysis;
import lexical.Token;

public class SyntaticAnalysis {

    private LexicalAnalysis lex;
    private Token current;
    private Token previous;

    public SyntaticAnalysis(LexicalAnalysis lex) {
        this.lex = lex;
        this.current = lex.nextToken();
        this.previous = null;
    }

    public Expr process() {
        Expr expr = procCode();
        eat(END_OF_FILE);
        return expr;
    }

    private void advance() {
        System.out.println("Found " + current);
        previous = current;
        current = lex.nextToken();
    }

    private void eat(Token.Type type) {
        if (type == current.type) {
            advance();
        } else {
            System.out.println("Expected (..., " + type + ", ..., ...), found " + current);
            reportError();
        }
    }

    private boolean check(Token.Type ...types) {
        for (Token.Type type : types) {
            if (current.type == type)
                return true;
        }

        return false;
    }

    private boolean match(Token.Type ...types) {
        if (check(types)) {
            advance();
            return true;
        } else {
            return false;
        }
    }

    private void reportError() {
        int line = current.line;
        switch (current.type) {
            case INVALID_TOKEN:
                throw LanguageException.instance(line, InvalidLexeme, current.lexeme);
            case UNEXPECTED_EOF:
            case END_OF_FILE:
                throw LanguageException.instance(line, UnexpectedEOF);
            default:
                throw LanguageException.instance(line, UnexpectedLexeme, current.lexeme);
        }
    }

    // <code> ::= { <expr> }
    private ExprBlock procCode() {
        ExprBlock block = new ExprBlock(current.line);
        while (check(NOT, SUB, OPEN_PAR, INTEGER_LITERAL, STRING_LITERAL,
                ATOM_LITERAL, OPEN_BRA, OPEN_CUR, IF, UNLESS, COND, FOR,
                FN, PUTS, READ, INT, Token.Type.STR, LENGTH, HD, TL, AT, REM, NAME)) {
            Expr expr = procExpr();
            block.addExpr(expr);
        }

        return block;
    }

    // <expr> ::= <logic> [ '=' <expr> ]
    private Expr procExpr() {
        Expr expr = procLogic();
        if (match(ASSIGN)) {
            int line = previous.line;

            Expr rhs = procExpr();
            expr = new AssignExpr(line, expr, rhs);
        }

        return expr;
    }

    // <logic> ::= <rel> [ ( '&&' | '||' ) <expr> ]
    private Expr procLogic() {
        Expr expr = procRel();
        if (match(AND, OR)) {
            procExpr();
        }

        return expr;
    }

    // <rel> ::= <subtract> [ ( '<' | '>' | '<=' | '>=' | '==' | '!=' ) <expr> ]
    private Expr procRel() {
        Expr expr = procSubtract();
        if(match(LOWER_THAN, GREATER_THAN, LOWER_EQUAL, GREATER_EQUAL, EQUAL, NOT_EQUAL)){
            BinaryExpr.Op op = null;
            int line = previous.line;
            switch (previous.type) {
                case LOWER_THAN:
                    op = BinaryExpr.Op.LowerThan;
                    break;
                case GREATER_THAN:
                    op = BinaryExpr.Op.GreaterThan;
                    break;
                case LOWER_EQUAL:
                    op = BinaryExpr.Op.LowerEqual;
                    break;
                case GREATER_EQUAL:
                    op = BinaryExpr.Op.GreaterEqual;
                    break;
                case EQUAL:
                    op = BinaryExpr.Op.Equal;
                    break;
                case NOT_EQUAL:
                    op = BinaryExpr.Op.NotEqual;
                    break;
                default:
                    reportError();
                    break;
            }

            Expr right = procExpr();

            expr = new BinaryExpr(line, expr, op, right);          
        }
        return expr;
    }

    // <subtract> ::= <concat> [ '--' <expr> ]
    private Expr procSubtract() {
        Expr expr = procConcat();
        if (match(LIST_SUBTRACT)) {
            BinaryExpr.Op op = BinaryExpr.Op.ListSubtract;
            int line = previous.line;
            
            Expr right = procExpr();
            
            expr = new BinaryExpr(line, expr, op, right);
        }

        return expr;
    }

    // <concat> ::= <arith> [ ( '++' | '<>' ) <expr> ]
    private Expr procConcat() {
        Expr left = procArith();
        if (match(LIST_CONCAT, STR_CONCAT)) {
            BinaryExpr.Op op = null;
            int line = previous.line;
            switch(previous.type){
                case LIST_CONCAT:
                    op = BinaryExpr.Op.ListConcat;
                    break;
                case STR_CONCAT:
                    op = BinaryExpr.Op.StringConcat;
                    break;
                default:
                    reportError();
            }

            Expr right = procExpr();

            left = new BinaryExpr(line, left, op, right);
        }

        return left;
    }

    // <arith> ::= <term> [ ( '+' | '-' ) <expr> ]
    private Expr procArith() {
        Expr left = procTerm();
        if (match(ADD, SUB)) {
            BinaryExpr.Op op = null;
            int line = previous.line;
            switch (previous.type) {
                case ADD:
                    op = BinaryExpr.Op.Add;
                    break;
                case SUB:
                    op = BinaryExpr.Op.Sub;
                    break;
                default:
                    reportError();
            }

            Expr right = procExpr();

            left = new BinaryExpr(line, left, op, right);
        }

        return left;
    }

    // <term> ::= <prefix> [ ( '*' | '/' ) <expr> ]
    private Expr procTerm() {
        Expr left = procPrefix();
        if (match(MUL, DIV)) {
            BinaryExpr.Op op = null;
            int line = previous.line;
            switch (previous.type) {
                case MUL:
                    op = BinaryExpr.Op.Mul;
                    break;
                case DIV:
                    op = BinaryExpr.Op.Div;
                    break;
                default:
                    reportError();
            }

            Expr right = procExpr();

            left = new BinaryExpr(line, left, op, right);
        }

        return left;
    }

    // <prefix> ::= [ '!' | '-' ] <factor>
    private Expr procPrefix() {
        UnaryExpr.Op op = null;
        int line = -1;
        if (match(NOT, SUB)) {
            line = previous.line;
            switch (previous.type) {
                case NOT:
                    op = UnaryExpr.Op.Not;
                    break;
                case SUB:
                    op = UnaryExpr.Op.Neg;
                    break;
                default:
                    reportError();
            }
        }

        Expr expr = procFactor();

        if (op != null)
            expr = new UnaryExpr(line, op, expr);

        return expr;
    }

    // <factor> ::= ( '(' <expr> ')' | <rvalue> ) <invoke>
    private Expr procFactor() {
        Expr expr = null;
        if (match(OPEN_PAR)) {
            expr = procExpr();
            eat(CLOSE_PAR);
        } else {
            expr = procRValue();
        }

        procInvoke();

        return expr;
    }

    // <rvalue> ::= <const> | <list> | <tuple> | <if> | <unless> | <cond> | <for> | <fn> | <native> | <name>
    private Expr procRValue() {
        Expr expr = null;
        if (check(INTEGER_LITERAL, STRING_LITERAL, ATOM_LITERAL)) {
            expr = procConst();
        } else if (check(OPEN_BRA)) {
            procList();
        } else if (check(OPEN_CUR)) {
            procTuple();
        } else if (check(IF)) {
            procIf();
        } else if (check(UNLESS)) {
            expr = procUnless();
        } else if (check(COND)) {
            procCond();
        } else if (check(FOR)) {
            procFor();
        } else if (check(FN)) {
            procFn();
        } else if (check(PUTS, READ, INT, Token.Type.STR, LENGTH, HD, TL, AT, REM)) {
            procNative();
        } else if (check(NAME)) {
            expr = procName();
        } else {
            reportError();
        }

        return expr;
    }

    // <const> ::= <int> | <string> | <atom>
    private ConstExpr procConst() {
        Value<?> value = null;
        if (check(INTEGER_LITERAL)) {
            value = procInt();
        } else if (check(STRING_LITERAL)) {
            value = procString();
        } else if (check(ATOM_LITERAL)) {
            value = procAtom();
        } else {
            reportError();
        }

        ConstExpr cexpr = new ConstExpr(previous.line, value);
        return cexpr;
    }

    // <list> ::= '[' [ <expr> { ',' <expr> } ] ']'
    private void procList() {
        eat(OPEN_BRA);
        if (!check(CLOSE_BRA)) {
            procExpr();
            while (match(COMMA)) {
                procExpr();
            }
        }
    }

    // <tuple> ::= '{' [ <expr> ':' <expr> { ',' <expr> ':' <expr> } ] '}'
    private void procTuple() {
        // TODO: implement me!
    }

    // <if> ::= if <expr> do <code> [ else <code> ] end
    private void procIf() {
        eat(IF);
        procExpr();
        eat(DO);
        procCode();
        if (match(ELSE)) {
            procCode();
        }
        eat(END);
    }

    // <unless> ::= unless <expr> do <code> end
    private UnlessExpr procUnless() {
        eat(UNLESS);
        int line = previous.line;

        Expr cond = procExpr();
        eat(DO);
        Expr expr = procCode();
        eat(END);

        UnlessExpr uexpr = new UnlessExpr(line, cond, expr);
        return uexpr;
    }

    // <cond> ::= cond do { <expr> '->' <expr> } end
    private void procCond() {
        // TODO: implement me!
    }

    // <for> ::= for <name> '<-' <expr> { ',' <expr> } do <code> end
    private void procFor() {
        eat(FOR);
        procName();
        eat(LEFT_ARROW);
        procExpr();
        while (match(COMMA)) {
            procExpr();
        }
        eat(DO);
        procCode();
        eat(END);
    }

    // <fn> ::= fn [ <name> { ',' <name> } ] '->' <code> end
    private void procFn() {
        eat(FN);
        if (check(NAME)) {
            procName();
            while (match(COMMA)) {
                procName();
            }
        }
        eat(RIGHT_ARROW);
        procCode();
        eat(END);
    }

    // <native> ::= puts | read | int | str | length | hd | tl | at | rem
    private void procNative() {
        if (match(PUTS, READ, INT, Token.Type.STR, LENGTH, HD, TL, AT, REM)) {
            // Do nothing.
        } else {
            reportError();
        }
    }

    // <invoke> ::= [ '(' [ <expr> { ',' <expr> } ] ')' ]
    private Expr procInvoke() {
        Expr expr = null;
        if(match(OPEN_PAR)){
            if(check(NOT, SUB, OPEN_PAR, INTEGER_LITERAL, STRING_LITERAL,
            ATOM_LITERAL, OPEN_BRA, OPEN_CUR, IF, UNLESS, COND, FOR,
            FN, PUTS, READ, INT, Token.Type.STR, LENGTH, HD, TL, AT, REM, NAME)) {
                //TODO: Implement me!
            }
        }
        return expr;
        
    }

    private Variable procName() {
        eat(NAME);
        Variable var = new Variable(previous);
        return var;
    }

    private Value<?> procInt() {
        eat(INTEGER_LITERAL);
        return previous.literal;
    }

    private Value<?> procString() {
        eat(STRING_LITERAL);
        return previous.literal;
    }

    private Value<?> procAtom() {
        eat(ATOM_LITERAL);
        return previous.literal;
    }
}

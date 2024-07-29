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
import static lexical.Token.Type.CLOSE_CUR;
import static lexical.Token.Type.CLOSE_PAR;
import static lexical.Token.Type.COLON;
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

import java.util.ArrayList;
import java.util.List;

import error.LanguageException;
import interpreter.expr.AssignExpr;
import interpreter.expr.BinaryExpr;
import interpreter.expr.CondExpr;
import interpreter.expr.ConstExpr;
import interpreter.expr.Expr;
import interpreter.expr.ExprBlock;
import interpreter.expr.FnExpr;
import interpreter.expr.ForExpr;
import interpreter.expr.FunctionInvocationExpr;
import interpreter.expr.IfExpr;
import interpreter.expr.ListExpr;
import interpreter.expr.TupleExpr;
import interpreter.expr.UnaryExpr;
import interpreter.expr.UnlessExpr;
import interpreter.expr.Variable;
import interpreter.literal.NativeFunctionLiteral;
import interpreter.value.FunctionValue;
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
        //System.out.println("Found " + current);
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
    
    // Verifica a ocorrência de uma abstração Expr
    private boolean checkExpr(){
        return check(NOT, SUB, OPEN_PAR, INTEGER_LITERAL, STRING_LITERAL,
                    ATOM_LITERAL, OPEN_BRA, OPEN_CUR, IF, UNLESS, COND, FOR,
                    FN, PUTS, READ, INT, STR, LENGTH, HD, TL, AT, REM, NAME);
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
        while (checkExpr()) {
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

            Expr right = procExpr();
            expr = new AssignExpr(line, expr, right);
        }

        return expr;
    }

    // <logic> ::= <rel> [ ( '&&' | '||' ) <expr> ]
    private Expr procLogic() {
        Expr left = procRel();
        if (match(AND, OR)) {
            BinaryExpr.Op op = null;
            int line = previous.line;
            switch (previous.type) {
                case AND:   
                    op = BinaryExpr.Op.And;
                    break;
                case OR:
                    op = BinaryExpr.Op.Or;
                    break;
                default:
                    reportError();
            }
            
            Expr right = procExpr();

            left = new BinaryExpr(line, left, op, right);
        }

        return left;
    }

    // <rel> ::= <subtract> [ ( '<' | '>' | '<=' | '>=' | '==' | '!=' ) <expr> ]
    private Expr procRel() {
        Expr left = procSubtract();
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
            }

            Expr right = procExpr();

            left = new BinaryExpr(line, left, op, right);          
        }

        return left;
    }

    // <subtract> ::= <concat> [ '--' <expr> ]
    private Expr procSubtract() {
        Expr left = procConcat();
        if (match(LIST_SUBTRACT)) {
            BinaryExpr.Op op = BinaryExpr.Op.ListSubtract;
            int line = previous.line;
            
            Expr right = procExpr();
            
            left = new BinaryExpr(line, left, op, right);
        }

        return left;
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
        
        ListExpr invoke = procInvoke();
        if(invoke != null){
            expr = new FunctionInvocationExpr(previous.line, expr);
            for(Expr arg : invoke.getList()){
                ((FunctionInvocationExpr)expr).addArg(arg);
            }
        }
        
        return expr;
    }

    // <rvalue> ::= <const> | <list> | <tuple> | <if> | <unless> | <cond> | <for> | <fn> | <native> | <name>
    private Expr procRValue() {
        Expr expr = null;
        if (check(INTEGER_LITERAL, STRING_LITERAL, ATOM_LITERAL)) {
            expr = procConst();
        } else if (check(OPEN_BRA)) {
            expr = procList();
        } else if (check(OPEN_CUR)) {
            expr = procTuple();
        } else if (check(IF)) {
            expr = procIf();
        } else if (check(UNLESS)) {
            expr = procUnless();
        } else if (check(COND)) {
            expr = procCond();
        } else if (check(FOR)) {
            expr = procFor();
        } else if (check(FN)) {
            expr = procFn();
        } else if (check(PUTS, READ, INT, STR, LENGTH, HD, TL, AT, REM)) {
            expr = procNative();
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
    private ListExpr procList() {
        eat(OPEN_BRA);
        ListExpr listExpr = new ListExpr(previous.line);

        if (!check(CLOSE_BRA)){
            do{
                listExpr.add(procExpr());
            } while(match(COMMA));
        }
        eat(CLOSE_BRA);

        return listExpr;
    }

    // <tuple> ::= '{' [ <expr> ':' <expr> { ',' <expr> ':' <expr> } ] '}'
    private TupleExpr procTuple() {
        eat(OPEN_CUR);
        TupleExpr tupleExpr = new TupleExpr(previous.line);

        if(!check(CLOSE_CUR)){
            do{
                Expr key = procExpr();
                eat(COLON);
                Expr value = procExpr();
                
                tupleExpr.add(key, value);
            } while(match(COMMA));
        }
        eat(CLOSE_CUR);

        return tupleExpr;
    }

    // <if> ::= if <expr> do <code> [ else <code> ] end
    private IfExpr procIf() {
        eat(IF);
        int line = previous.line;

        Expr cond = procExpr();
        eat(DO);
        Expr thenExpr = procCode();

        Expr elseExpr = null;
        if (match(ELSE)) {
            elseExpr = procCode();
        }
        eat(END);

        return new IfExpr(line, cond, thenExpr, elseExpr);
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
    private CondExpr procCond() {
        eat(COND);
        eat(DO);
        CondExpr cExpr = new CondExpr(previous.line);

        while(checkExpr()){
            Expr cond = procExpr();
            eat(RIGHT_ARROW);
            Expr body = procExpr();

            cExpr.add(cond, body);
        }
        eat(END);

        return cExpr;
    }

    // <for> ::= for <name> '<-' <expr> { ',' <expr> } do <code> end
    private ForExpr procFor() {
        eat(FOR);
        int line = previous.line;
        Variable var = procName();

        eat(LEFT_ARROW);
        Expr expr = procExpr();

        List<Expr> filters = new ArrayList<Expr>();
        while (match(COMMA)) {
            filters.add(procExpr());
        }
        eat(DO);
        Expr body = procCode();
        eat(END);

        return new ForExpr(line, var, expr, filters, body);
    }

    // <fn> ::= fn [ <name> { ',' <name> } ] '->' <code> end
    private FnExpr procFn() {
        eat(FN);
        FnExpr fn = new FnExpr(previous.line);

        if(check(NAME)){
            do{
                fn.addParam(procName());
            }
            while (match(COMMA));
        }
        eat(RIGHT_ARROW);
        fn.setCode(procCode());
        eat(END);

        return fn;
    }

    // <native> ::= puts | read | int | str | length | hd | tl | at | rem
    private Expr procNative() {
        Expr expr = null;
        if (match(PUTS, READ, INT, STR, LENGTH, HD, TL, AT, REM)) {
            int line = previous.line;

            NativeFunctionLiteral.Op op = null;
            switch (previous.type) {
                case PUTS:
                    op = NativeFunctionLiteral.Op.Puts;
                    break;
                case READ:
                    op = NativeFunctionLiteral.Op.Read;
                    break;
                case INT:
                    op = NativeFunctionLiteral.Op.Int;
                    break;
                case STR:
                    op = NativeFunctionLiteral.Op.Str;
                    break;
                case LENGTH:
                    op = NativeFunctionLiteral.Op.Length;
                    break;
                case HD:
                    op = NativeFunctionLiteral.Op.Hd;
                    break;
                case TL:
                    op = NativeFunctionLiteral.Op.Tl;
                    break;
                case AT:
                    op = NativeFunctionLiteral.Op.At;
                    break;
                case REM:
                    op = NativeFunctionLiteral.Op.Rem;
                    break;
                default:
                    throw new RuntimeException("Unreachable");
            }  

            NativeFunctionLiteral nfliteral = NativeFunctionLiteral.instance(op);
            expr = new ConstExpr(line, new FunctionValue(nfliteral));             
        } else{
            reportError();
        }

        return expr;
    }

    // <invoke> ::= [ '(' [ <expr> { ',' <expr> } ] ')' ]
    private ListExpr procInvoke() {
        if(match(OPEN_PAR)){
            ListExpr args = new ListExpr(current.line);
            if(checkExpr()) {
                do{
                    args.add(procExpr());
                } 
                while (match(COMMA));
            }
            eat(CLOSE_PAR);

            return args;
        } else{
            return null;
        }

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

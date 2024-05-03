package interpreter.expr;

import error.LanguageException;
import interpreter.Environment;
import interpreter.literal.AtomLiteral;
import interpreter.value.AtomValue;
import interpreter.value.IntValue;
import interpreter.value.StringValue;
import interpreter.value.Value;

public class BinaryExpr extends Expr {

    public static enum Op {
        And,            // &&
        Or,             // ||
        Equal,          // ==
        NotEqual,       // !=
        LowerThan,      // <
        LowerEqual,     // <=
        GreaterThan,    // >
        GreaterEqual,   // >=
        ListSubtract,   // --
        ListConcat,     // ++
        StringConcat,   // <>
        Add,            // +
        Sub,            // -
        Mul,            // *
        Div             // /
    }

    private Expr left;
    private Op op;
    private Expr right;

    public BinaryExpr(int line, Expr left, Op op, Expr right) {
        super(line);

        this.left = left;
        this.op = op;
        this.right = right;
    }

    @Override
    public Value<?> expr(Environment env) {
        switch (op) {
            case And:
                return andOp(env);
            case Or:
                return orOp(env);
            case Equal:
                return equalOp(env);
            case NotEqual:
                return notEqualOp(env);
            case LowerThan:
                return lowerThanOp(env);
            case GreaterThan:
                return greaterThanOp(env);
            case LowerEqual:
                return lowerEqualOp(env);
            case GreaterEqual:
                return greaterEqualOp(env);
            case ListSubtract:
                return listSubtractOp(env);
            case ListConcat:
                return listConcatOp(env);
            case StringConcat:
                return stringConcatOp(env);
            case Add:
                return addOp(env);
            case Sub:
                return subOp(env);
            case Mul:
                return mulOp(env);
            case Div:
                return divOp(env);
            default:
                throw new RuntimeException("Unreachable");
        }
    }

    private Value<?> andOp(Environment env) {
        Value<?> v1 = right.expr(env);
        Value<?> v2 = left.expr(env);

        // Retorna o valor do último termo avaliado na expressão
        // Se v1 for falso, retorna v1
        if(!v1.eval())
            return v1;
        else    // Se v1 for verdadeiro, retorna v2
            return v2;
    }

    private Value<?> orOp(Environment env) {
        Value<?> v1 = right.expr(env);
        Value<?> v2 = left.expr(env);

        // Retorna o valor do último termo avaliado na expressão
        // Se v1 for verdadeiro, retorna v1
        if(v1.eval())
            return v1;
        else    // Se v1 for falso, retorna v2
            return v2;
    }

    private Value<?> equalOp(Environment env) {
        Value<?> v1 = left.expr(env);
        Value<?> v2 = right.expr(env);

        // Se o tipo forem diferentes, retorna :false
        if(v1.getClass() != v2.getClass())
            return AtomValue.FALSE;
        else if(v1.equals(v2))
            return AtomValue.TRUE;
        else
            return AtomValue.FALSE;
    }

    private Value<?> notEqualOp(Environment env) {
        Value<?> v1 = right.expr(env);
        Value<?> v2 = left.expr(env);
        
        // Se os tipos forem diferentes, retorna :false
        if(v1.getClass() != v2.getClass())
            return AtomValue.FALSE;
        else if (!v1.equals(v2))
            return AtomValue.TRUE;
        else
            return AtomValue.FALSE;
    }

    private Value<?> lowerThanOp(Environment env) {
        throw new RuntimeException("Implement me!2");
    }

    private Value<?> greaterThanOp(Environment env) {
        throw new RuntimeException("Implement me!3");
    }

    private Value<?> lowerEqualOp(Environment env) {
        throw new RuntimeException("Implement me!4");
    }

    private Value<?> greaterEqualOp(Environment env) {
        throw new RuntimeException("Implement me!5");
    }

    private Value<?> listSubtractOp(Environment env) {
        throw new RuntimeException("Implement me!6");
    }

    private Value<?> listConcatOp(Environment env) {
        throw new RuntimeException("Implement me!7");
    }

    private Value<?> stringConcatOp(Environment env) {
        Value<?> v1 = left.expr(env);
        Value<?> v2 = right.expr(env);

        if (v1 instanceof StringValue && v2 instanceof StringValue){
            StringValue sv1 = (StringValue) v1;
            StringValue sv2 = (StringValue) v2;

            String concat =  sv1.value() + sv2.value();

            return new StringValue(concat);
        } else
            throw LanguageException.instance(super.getLine(), LanguageException.Error.InvalidOperation);
    }

    private Value<?> addOp(Environment env) {
        Value<?> v1 = left.expr(env);
        Value<?> v2 = right.expr(env);

        if(v1 instanceof IntValue && v2 instanceof IntValue){
            IntValue iv1 = (IntValue) v1;
            IntValue iv2 = (IntValue) v2;

            int n = iv1.value() + iv2.value();

            return new IntValue(n);
        } else
            throw LanguageException.instance(super.getLine(), LanguageException.Error.InvalidOperation);
    }

    private Value<?> subOp(Environment env) {
        Value<?> v1 = left.expr(env);
        Value<?> v2 = right.expr(env);

        if (v1 instanceof IntValue && v2 instanceof IntValue){
            IntValue iv1 = (IntValue) v1;
            IntValue iv2 = (IntValue) v2;

            int n = iv1.value() - iv2.value();

            return new IntValue(n);
        } else
            throw LanguageException.instance(super.getLine(), LanguageException.Error.InvalidOperation);
    }

    private Value<?> mulOp(Environment env) {
        Value<?> v1 = this.left.expr(env);
        Value<?> v2 = this.right.expr(env);

        if (v1 instanceof IntValue && v2 instanceof IntValue) {
            IntValue iv1 = (IntValue) v1;
            IntValue iv2 = (IntValue) v2;

            int n = iv1.value() * iv2.value();
            return new IntValue(n);
        } else
            throw LanguageException.instance(super.getLine(), LanguageException.Error.InvalidOperation);
    }

    private Value<?> divOp(Environment env) {
        Value<?> v1 = this.left.expr(env);
        Value<?> v2 = this.right.expr(env);

        if (v1 instanceof IntValue && v2 instanceof IntValue) {
            IntValue iv1 = (IntValue) v1;
            IntValue iv2 = (IntValue) v2;

            int n = iv1.value() / iv2.value();
            return new IntValue(n);
        } else
            throw LanguageException.instance(super.getLine(), LanguageException.Error.InvalidOperation);
    }

}

package interpreter.expr;

import error.LanguageException;
import interpreter.Environment;
import interpreter.value.IntValue;
import interpreter.value.Value;

public class BinaryExpr extends Expr {

    public static enum Op {
        And,
        Or,
        Equal,
        NotEqual,
        LowerThan,
        LowerEqual,
        GreaterThan,
        GreaterEqual,
        ListSubtract,
        ListConcat,
        StringConcat,
        Add,
        Sub,
        Mul,
        Div
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
        throw new RuntimeException("Implement me!");
    }

    private Value<?> orOp(Environment env) {
        throw new RuntimeException("Implement me!");
    }

    private Value<?> equalOp(Environment env) {
        throw new RuntimeException("Implement me!");
    }

    private Value<?> notEqualOp(Environment env) {
        throw new RuntimeException("Implement me!");
    }

    private Value<?> lowerThanOp(Environment env) {
        throw new RuntimeException("Implement me!");
    }

    private Value<?> greaterThanOp(Environment env) {
        throw new RuntimeException("Implement me!");
    }

    private Value<?> lowerEqualOp(Environment env) {
        throw new RuntimeException("Implement me!");
    }

    private Value<?> greaterEqualOp(Environment env) {
        throw new RuntimeException("Implement me!");
    }

    private Value<?> listSubtractOp(Environment env) {
        throw new RuntimeException("Implement me!");
    }

    private Value<?> listConcatOp(Environment env) {
        throw new RuntimeException("Implement me!");
    }

    private Value<?> stringConcatOp(Environment env) {
        throw new RuntimeException("Implement me!");
    }

    private Value<?> addOp(Environment env) {
        throw new RuntimeException("Implement me!");
    }

    private Value<?> subOp(Environment env) {
        throw new RuntimeException("Implement me!");
    }

    private Value<?> mulOp(Environment env) {
        Value<?> v1 = this.left.expr(env);
        Value<?> v2 = this.right.expr(env);

        if (v1 instanceof IntValue && v2 instanceof IntValue) {
            IntValue iv1 = (IntValue) v1;
            IntValue iv2 = (IntValue) v2;

            int n = iv1.value() * iv2.value();
            return new IntValue(n);
        } else {
            throw LanguageException.instance(super.getLine(), LanguageException.Error.InvalidOperation);
        }
    }

    private Value<?> divOp(Environment env) {
        Value<?> v1 = this.left.expr(env);
        Value<?> v2 = this.right.expr(env);

        if (v1 instanceof IntValue && v2 instanceof IntValue) {
            IntValue iv1 = (IntValue) v1;
            IntValue iv2 = (IntValue) v2;

            int n = iv1.value() / iv2.value();
            return new IntValue(n);
        } else {
            throw LanguageException.instance(super.getLine(), LanguageException.Error.InvalidOperation);
        }
    }

}

package interpreter.expr;

import error.LanguageException;
import interpreter.Environment;
import interpreter.value.AtomValue;
import interpreter.value.IntValue;
import interpreter.value.Value;

public class UnaryExpr extends Expr {

    public static enum Op {
        Not,
        Neg
    }

    private Op op;
    private Expr expr;

    public UnaryExpr(int line, Op op, Expr expr) {
        super(line);
        this.op = op;
        this.expr = expr;
    }

    public Value<?> expr(Environment env) {
        Value<?> value = this.expr.expr(env);

        Value<?> ret = null;
        switch (this.op) {
            case Not:
                ret = notOp(value);
                break;
            case Neg:
                ret = negOp(value);
                break;
            default:
                throw new RuntimeException("Unreachable");
        }

        return ret;
    }
    
    private Value<?> notOp(Value<?> value) {
        boolean b = !value.eval();
        return (b ? AtomValue.TRUE : AtomValue.FALSE);
    }

    private Value<?> negOp(Value<?> value) {
        if (value instanceof IntValue) {
            IntValue iv = (IntValue) value;
            int n = -iv.value();
            return new IntValue(n);
        } else {
            throw LanguageException.instance(super.getLine(), LanguageException.Error.InvalidOperation);
        }
    }

}

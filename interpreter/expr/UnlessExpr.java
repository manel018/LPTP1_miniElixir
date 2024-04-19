package interpreter.expr;

import interpreter.Environment;
import interpreter.value.AtomValue;
import interpreter.value.Value;

public class UnlessExpr extends Expr {

    private Expr cond;
    private Expr expr;

    public UnlessExpr(int line, Expr cond, Expr expr) {
        super(line);
        this.cond = cond;
        this.expr = expr;
    }

    public Value<?> expr(Environment env) {
        Value<?> cvalue = cond.expr(env);
        if (!cvalue.eval()) {
            Environment newEnv = new Environment(env);
            Value<?> bvalue = expr.expr(newEnv);
            return bvalue;
        } else {
            return AtomValue.NIL;
        }
    }

}

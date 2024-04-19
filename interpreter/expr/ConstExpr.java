package interpreter.expr;

import interpreter.Environment;
import interpreter.value.Value;

public class ConstExpr extends Expr {

    private Value<?> value;

    public ConstExpr(int line, Value<?> value) {
        super(line);
        this.value = value;
    }

    public Value<?> expr(Environment env) {
        return value;
    }

}

package interpreter;

import interpreter.expr.Expr;
import interpreter.value.Value;

public class Interpreter {

    public final static Environment globals =
        new Environment();

    private Interpreter() {
    }

    public static Value<?> interpret(Expr expr) {
        return expr.expr(globals);
    }

}

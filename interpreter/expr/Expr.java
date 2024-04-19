package interpreter.expr;

import interpreter.Environment;
import interpreter.value.Value;

public abstract class Expr {

    private int line;

    protected Expr(int line) {
        this.line = line;
    }

    public int getLine() {
        return line;
    }

    public abstract Value<?> expr(Environment env);

}

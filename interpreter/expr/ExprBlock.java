package interpreter.expr;

import java.util.ArrayList;
import java.util.List;

import interpreter.Environment;
import interpreter.value.AtomValue;
import interpreter.value.Value;

public class ExprBlock extends Expr {

    private List<Expr> block;

    public ExprBlock(int line) {
        super(line);

        block = new ArrayList<Expr>();
    }

    public void addExpr(Expr expr) {
        block.add(expr);
    }

    public Value<?> expr(Environment env) {
        Value<?> last = null;
        for (Expr expr : block) {
            last = expr.expr(env);
        }

        return last != null ? last : AtomValue.NIL;
    }

}

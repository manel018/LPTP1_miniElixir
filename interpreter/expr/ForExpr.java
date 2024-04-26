package interpreter.expr;

import java.util.List;

import interpreter.Environment;
import interpreter.value.Value;

public class ForExpr extends Expr{
    private Variable var;
    private Expr expr;
    private List<Expr> filters;
    private Expr body;

    public ForExpr(int line, Variable var, Expr expr, List<Expr> filters, Expr body){
        super(line);
        this.var = var;
        this.expr = expr;
        this.filters = filters;
        this.body = body;
    }

    public Value<?> expr(Environment env) {
        // TODO: Implement me!
        throw new UnsupportedOperationException("Unimplemented method 'expr'");
    }


}

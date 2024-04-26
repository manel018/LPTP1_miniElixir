package interpreter.expr;

public class CondItem {
    public Expr cond;
    public Expr body;

    public CondItem(Expr cond, Expr body){
        this.cond = cond;
        this.body = body;
    }
}

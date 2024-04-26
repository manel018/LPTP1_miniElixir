package interpreter.expr;

import interpreter.Environment;
import interpreter.value.Value;

public class IfExpr extends Expr{
    private Expr cond;
    private Expr thenExpr;
    private Expr elseExpr;

    public IfExpr(int line, Expr cond, Expr thenExpr, Expr elsExpr){
        super(line);
        this.cond = cond;
        this.thenExpr =  thenExpr;
        this.elseExpr = elsExpr;
    }

    public Value<?> expr(Environment env){
        //TODO: Implement me!
        throw new UnsupportedOperationException("Unimplemented method 'expr'");
    }
}

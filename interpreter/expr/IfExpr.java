package interpreter.expr;

import interpreter.Environment;
import interpreter.value.AtomValue;
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
        Value<?> cvalue = cond.expr(env);

        if(cvalue.eval() || cvalue.equals(AtomValue.ERROR)){
            Environment newEnv = new Environment(env);
            return thenExpr.expr(newEnv);
        } else if(elseExpr != null){
            Environment newEnv = new Environment(env);
            return elseExpr.expr(newEnv);
        } else 
            return AtomValue.NIL;
    }
}

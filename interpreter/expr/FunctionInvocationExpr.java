package interpreter.expr;

import java.util.ArrayList;
import java.util.List;

import interpreter.Environment;
import interpreter.value.Value;

public class FunctionInvocationExpr extends Expr{
    private Expr expr;
    private List<Expr> args;

    public FunctionInvocationExpr(int line, Expr expr){
        super(line);
        this.expr = expr;
        args = new ArrayList<Expr>();
    }

    public void addArg(Expr arg){
        args.add(arg);
    }
    
    public Value<?> expr(Environment env){
        // TODO: Implement me!
        return null;
    }
}

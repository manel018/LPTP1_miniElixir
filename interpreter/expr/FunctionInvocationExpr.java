package interpreter.expr;

import java.util.ArrayList;
import java.util.List;

import interpreter.Environment;
import interpreter.literal.StandardFunctionLiteral;
import interpreter.value.FunctionValue;
import interpreter.value.Value;

public class FunctionInvocationExpr extends Expr{
    private Expr expr;
    private List<Variable> args;

    public FunctionInvocationExpr(int line, Expr expr){
        super(line);
        this.expr = expr;
        args = new ArrayList<Variable>();
    }

    public void addArg(Variable arg){
        args.add(arg);
    }
    
    public Value<?> expr(Environment env){
        StandardFunctionLiteral fn = new StandardFunctionLiteral();
        fn.setBody(expr);
        for(Variable arg : args){
            fn.addParam(arg);
        }

        return new FunctionValue(fn);
    }
}

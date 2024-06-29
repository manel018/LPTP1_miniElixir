package interpreter.expr;

import java.util.ArrayList;
import java.util.List;

import error.LanguageException;
import interpreter.Environment;
import interpreter.literal.FunctionLiteral;
import interpreter.value.FunctionValue;
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
        Value<?> refValue = expr.expr(env);

        if(refValue instanceof FunctionValue){
            FunctionLiteral function = ((FunctionValue) refValue).value();
            List<Variable> params = function.getParams();
            
            // Amarrando os argumentos aos parâmetros da função
            if(params.size() == args.size()){
                Environment newEnv = new Environment(env);
                int index = 0;

                for(Expr argExpr : args){
                    Value<?> argValue = argExpr.expr(env);
                    Variable var = params.get(index);
                    
                    var.setValue(newEnv, argValue);
                    index++;
                }
                try{
                    return function.invoke(newEnv);
                } catch(LanguageException e){
                    throw LanguageException.instance(super.getLine(), LanguageException.Error.InvalidOperation);
                }
            }
        } 
        // else
        throw LanguageException.instance(super.getLine(), LanguageException.Error.InvalidOperation);
    }
}

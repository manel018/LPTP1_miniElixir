package interpreter.expr;

import java.util.ArrayList;
import java.util.List;

import interpreter.Environment;
import interpreter.literal.StandardFunctionLiteral;
import interpreter.value.FunctionValue;
import interpreter.value.Value;

public class FnExpr extends Expr{
    private Expr code;
    private List<Variable> params;

    public FnExpr(int line){
        super(line);
        params = new ArrayList<Variable>();
    }

    public void setCode(Expr code){
        this.code = code;
    }

    public void addParam(Variable param){
        this.params.add(param);
    }

    public Value<?> expr(Environment env){
        StandardFunctionLiteral fn = new StandardFunctionLiteral();
        fn.setBody(code);
        for(Variable param : params){
            fn.addParam(param);
        }

        return new FunctionValue(fn);
    }


}
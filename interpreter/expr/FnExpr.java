package interpreter.expr;

import java.util.ArrayList;
import java.util.List;

import interpreter.Environment;
import interpreter.value.Value;

public class FnExpr extends Expr{
    private Expr code;
    private List<Variable> params;

    public FnExpr(int line, Expr code){
        super(line);
        this.code = code;
        params = new ArrayList<Variable>();
    }

    public void addParam(Variable param){
        this.params.add(param);
    }

    public Value<?> expr(Environment env){
        // TODO: Implement me!
        return null;
    }


}
package interpreter.literal;

import interpreter.Environment;
import interpreter.expr.Expr;
import interpreter.value.Value;

public class StandardFunctionLiteral extends FunctionLiteral{
    private Expr expr;

    public StandardFunctionLiteral(){
        super();
    }

    public void setBody(Expr expr){
        this.expr = expr;
    }

    public Value<?> invoke(Environment env){
        return expr.expr(env);
    }

    @Override
    public int hashCode(){
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + this.expr.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj){
            return true;
        } else if (obj instanceof StandardFunctionLiteral){
            StandardFunctionLiteral standFunc = (StandardFunctionLiteral) obj;

            return (getParams().equals(standFunc.getParams())
                && this.expr.equals(standFunc.expr));
        } else
            return false;
    }

}

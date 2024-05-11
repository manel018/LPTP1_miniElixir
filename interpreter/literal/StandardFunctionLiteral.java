package interpreter.literal;

import javax.management.StandardEmitterMBean;

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
        //TODO: Implement me
        return null;
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
            StandardFunctionLiteral standOp = (StandardFunctionLiteral) obj;

            return (getParams().equals(standOp.getParams())
                && this.expr.equals(standOp.expr));
        } else
            return false;
    }

}

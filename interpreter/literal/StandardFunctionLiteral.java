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

    public String toString() {
        return "fn<std>";
    }

    @Override
    public int hashCode(){
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + this.expr.hashCode();
        return result;
    }
}

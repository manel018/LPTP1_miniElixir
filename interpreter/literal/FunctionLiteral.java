package interpreter.literal;

import java.util.ArrayList;
import java.util.List;

import interpreter.Environment;
import interpreter.expr.Variable;
import interpreter.value.Value;

public abstract class FunctionLiteral{
    private List<Variable> params;

    protected FunctionLiteral(){
        this.params = new ArrayList<Variable>();
    }

    public boolean addParam(Variable var){
        return params.add(var);
    }

    public List<Variable> getParams(){
        return params;
    }

    @Override
    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append("fn<std>");

        return sb.toString();
    }

    @Override
    public boolean equals(Object obj){
        return this == obj;
    }


    public abstract Value<?> invoke(Environment env);
}

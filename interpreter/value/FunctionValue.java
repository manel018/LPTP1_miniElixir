package interpreter.value;

import interpreter.literal.FunctionLiteral;

public class FunctionValue extends Value<FunctionLiteral>{
    private FunctionLiteral value;

    public FunctionValue(FunctionLiteral value){
        this.value = value;
    }

    @Override
    public FunctionLiteral value(){
        return this.value;
    }

    @Override
    public boolean eval(){
        return true;
    }

    @Override   
    public int hashCode(){
        return this.value.hashCode();
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj){
            return true;
        } else if(obj instanceof FunctionValue){
            return this.value.equals(((FunctionValue)obj).value());
        } else
            return false;
    }

    @Override
    public String toString(){
        return this.value.toString();
    }
 
}

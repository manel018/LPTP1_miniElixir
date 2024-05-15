package interpreter.value;

import interpreter.literal.FunctionLiteral;
import interpreter.literal.NativeFunctionLiteral;
import interpreter.literal.NativeFunctionLiteral.NativeOp;

public class FunctionValue extends Value<FunctionLiteral>{
    private FunctionLiteral value;

    public static final FunctionValue PUTS_FN;
    public static final FunctionValue READ_FN;
    public static final FunctionValue INT_FN;
    public static final FunctionValue STR_FN;
    public static final FunctionValue LENGTH_FN;
    public static final FunctionValue HD_FN;
    public static final FunctionValue TL_FN;
    public static final FunctionValue AT_FN;
    public static final FunctionValue REM_FN;

    // Este bloco estático inicializa as 9 funções nativas do Mini Exixir
    static {
        PUTS_FN =  new FunctionValue(NativeFunctionLiteral.instance(NativeOp.PutsOp));
        READ_FN = new FunctionValue(NativeFunctionLiteral.instance(NativeOp.ReadOp));
        INT_FN = new FunctionValue(NativeFunctionLiteral.instance(NativeOp.IntOp));
        STR_FN = new FunctionValue(NativeFunctionLiteral.instance(NativeOp.StrOp));
        LENGTH_FN = new FunctionValue(NativeFunctionLiteral.instance(NativeOp.LengthOp));
        HD_FN = new FunctionValue(NativeFunctionLiteral.instance(NativeOp.HdOp));
        TL_FN = new FunctionValue(NativeFunctionLiteral.instance(NativeOp.TlOp));
        AT_FN = new FunctionValue(NativeFunctionLiteral.instance(NativeOp.AtOp));
        REM_FN = new FunctionValue(NativeFunctionLiteral.instance(NativeOp.RemOp));
    }

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
            return value.equals(((FunctionValue) obj).value);
        } else{
            return false;
        }
    }

    @Override
    public String toString(){
        return this.value.toString();
    }
 
}

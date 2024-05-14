package interpreter.literal;

import java.util.HashMap;
import java.util.Map;

import interpreter.Environment;
import interpreter.value.Value;

public class NativeFunctionLiteral extends FunctionLiteral{
    public static enum NativeOp{
        PutsOp,
        ReadOp,
        IntOp,
        StrOp,
        LengthOp,
        HdOp,
        TlOp,
        AtOp,
        RemOp
    }

    private NativeOp op;

    private static Map<NativeOp,NativeFunctionLiteral> instances =
        new HashMap<NativeOp,NativeFunctionLiteral>();

    public NativeFunctionLiteral(NativeOp op){
        this.op = op;
    }

    public static NativeFunctionLiteral instance(NativeOp op){
        // Se a função nativa não existir, cria uma nova
        NativeFunctionLiteral natFn = instances.get(op);
        if(natFn == null){
            natFn = new NativeFunctionLiteral(op);
            instances.put(op, natFn);
        }
        // Retorna a função nativa asssociada ao operador
        return natFn;
    }

    public Value<?> invoke(Environment env){
        // TODO: Implement me!
        return null;
    }

    public int hashCode(){
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + this.op.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj){
            return true;
        } else if (obj instanceof NativeFunctionLiteral){
            NativeFunctionLiteral natOp = (NativeFunctionLiteral) obj;
            
            return (getParams().equals(natOp.getParams())
                && this.op.equals(natOp.op));
        } else{
            return false;
        }
    }

}

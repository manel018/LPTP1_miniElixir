package interpreter.literal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import error.LanguageException;
import interpreter.Environment;
import interpreter.expr.Variable;
import interpreter.value.AtomValue;
import interpreter.value.IntValue;
import interpreter.value.ListValue;
import interpreter.value.StringValue;
import interpreter.value.TupleValue;
import interpreter.value.Value;
import lexical.Token;

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
    
    static{
        Variable param1 = new Variable(new Token("param1", null, null));
        Variable param2 = new Variable(new Token("param2", null, null));

        NativeFunctionLiteral putsFn = NativeFunctionLiteral.instance(NativeOp.PutsOp);
        putsFn.addParam(param1);
        NativeFunctionLiteral readFn = NativeFunctionLiteral.instance(NativeOp.ReadOp);
        readFn.addParam(param1);
        NativeFunctionLiteral intFn = NativeFunctionLiteral.instance(NativeOp.IntOp);
        intFn.addParam(param1);
        NativeFunctionLiteral strFn = NativeFunctionLiteral.instance(NativeOp.StrOp);
        strFn.addParam(param1);
        NativeFunctionLiteral lengthFn = NativeFunctionLiteral.instance(NativeOp.LengthOp);
        lengthFn.addParam(param1);
        NativeFunctionLiteral hdFn = NativeFunctionLiteral.instance(NativeOp.HdOp);
        hdFn.addParam(param1);
        NativeFunctionLiteral tlFn = NativeFunctionLiteral.instance(NativeOp.TlOp);
        tlFn.addParam(param1);
        NativeFunctionLiteral atFn = NativeFunctionLiteral.instance(NativeOp.AtOp);
        atFn.addParam(param1);
        atFn.addParam(param2);
        NativeFunctionLiteral remFn = NativeFunctionLiteral.instance(NativeOp.RemOp);
        remFn.addParam(param1);
        remFn.addParam(param2);
    }

    public NativeFunctionLiteral(NativeOp op){
        super();
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
        switch (this.op) {
            case NativeOp.PutsOp:
                return putsFunction(env); 
            case NativeOp.ReadOp: 
                try{
                    return readFunction(env); 
                } catch (IOException e){
                    return AtomValue.ERROR;
                }
            case NativeOp.IntOp:
                return intFunction(env);
            case NativeOp.StrOp:
                return strFunction(env);
            case NativeOp.LengthOp:
                return lengthFunction(env);
            case NativeOp.HdOp:
                return hdFunction(env);
            case NativeOp.TlOp:
                return tlFunction(env);
            case NativeOp.AtOp:
                return atFunction(env);
            case NativeOp.RemOp:
                return remFunction(env);
            default:
                break;
        }
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

    private Value<?> putsFunction(Environment env){
        Variable param = instances.get(op).getParams().get(0);
        System.out.println(param.expr(env));

        return AtomValue.OK;  
    }

    private Value<?> readFunction(Environment env) throws IOException{
        Variable param = instances.get(op).getParams().get(0);
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        System.out.print(param.expr(env));
        
        return new StringValue(reader.readLine());
    }

    private Value<?> intFunction(Environment env){
        Variable param = instances.get(op).getParams().get(0);
        Value<?> val = param.expr(env);

        if(val instanceof StringValue)
            return new IntValue(Integer.parseInt(((StringValue)val).value()));
        else
            return new IntValue(0);
    }

    private Value<?> strFunction(Environment env){
        Variable param = instances.get(op).getParams().get(0);
        Value<?> val = param.expr(env);

        return new StringValue(val.toString());
    }

    private Value<?> lengthFunction(Environment env){
        Variable param = instances.get(op).getParams().get(0);
        Value<?> val = param.expr(env);

        if(val instanceof ListValue){
            ListLiteral list = ((ListValue)val).value();
            return new IntValue(list.size());
        } else if (val instanceof TupleValue){
            TupleLiteral tuple = ((TupleValue)val).value();
            return new IntValue(tuple.size());
        } else
            throw LanguageException.instance(0, LanguageException.Error.InvalidOperation);
    }

    private Value<?> hdFunction(Environment env){
        Variable param = instances.get(op).getParams().get(0);
        Value<?> val = param.expr(env);

        if(val instanceof ListValue){
            Value<?> head = ((ListValue) val).value().head();
            if(head != null)
                return head;
        } 
        else if (val instanceof TupleValue){
            TupleItem item = ((TupleValue)val).value().head();
            if(item != null){
                ListLiteral head = new ListLiteral();
                head.add(item.key);
                head.add(item.value);
    
                return new ListValue(head);
            }
        }
        throw LanguageException.instance(0, LanguageException.Error.InvalidOperation);
    }

    private Value<?> tlFunction(Environment env){
        Variable param = instances.get(op).getParams().get(0);
        Value<?> val = param.expr(env);

        if(val instanceof ListValue){
            List<Value<?>> list = ((ListValue) val).value().tail();
            if(list != null){
                ListLiteral tail = new ListLiteral();
                for(Value<?> item : list){
                    tail.add(item);
                }
                return new ListValue(tail);
            }               
        } 
        else if(val instanceof TupleValue){
            List<TupleItem> list = ((TupleValue) val).value().tail();
            if(list != null){
                TupleLiteral tail = new TupleLiteral();
                for(TupleItem item : list){
                    tail.add(item);
                }
                return new TupleValue(tail);
            }
        }
        throw LanguageException.instance(0, LanguageException.Error.InvalidOperation);
    }

    private Value<?> atFunction(Environment env){
        Variable param1 = instances.get(op).getParams().get(0);
        Variable param2 = instances.get(op).getParams().get(1);
        Value<?> val1 = param1.expr(env);
        Value<?> val2 = param2.expr(env);

        if((val1 instanceof ListValue) && (val2 instanceof IntValue)){
            ListLiteral list = ((ListValue) val1).value();
            Value<?> item = list.get(((IntValue) val2).value());
            if(item != null){
                return item;
            }
        }
        else if(val1 instanceof TupleValue){
            TupleLiteral tuple = ((TupleValue) val1).value();
            for(TupleItem item : tuple){
                if(item.key.equals(val2)){
                    return item.value;
                }
            }
        }
        throw LanguageException.instance(0, LanguageException.Error.InvalidOperation);
    }

    private Value<?> remFunction(Environment env){
        Variable param1 = instances.get(op).getParams().get(0);
        Variable param2 = instances.get(op).getParams().get(1);
        Value<?> val1 = param1.expr(env);
        Value<?> val2 = param2.expr(env);

        if((val1 instanceof IntValue) && (val2 instanceof IntValue)){
            Integer dend = ((IntValue) val1).value().intValue();
            Integer dsor = ((IntValue) val2).value().intValue();
            if(dsor != 0){
                return new IntValue(dend % dsor);
            } 
        } 
        throw LanguageException.instance(0, LanguageException.Error.InvalidOperation);
    }

}
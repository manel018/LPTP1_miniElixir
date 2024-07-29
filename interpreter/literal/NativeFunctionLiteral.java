package interpreter.literal;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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

    public static enum Op {
        Puts("puts"),
        Read("read"),
        Int("int"),
        Str("str"),
        Length("length"),
        Hd("hd"),
        Tl("tl"),
        At("at"),
        Rem("rem");

        private String name;

        Op(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
    
    private NativeFunctionLiteral.Op op;
    private static Scanner input = new Scanner(new InputStreamReader(System.in));
    private static Map<Op,NativeFunctionLiteral> instances;

    static{
        // Inicialização do mapa de funções nativas
        instances = new HashMap<Op,NativeFunctionLiteral>();
        
        // Amarração dos parâmetros formais às funções nativas
        Variable param1 = new Variable(new Token("param1", null, null));
        Variable param2 = new Variable(new Token("param2", null, null));

        NativeFunctionLiteral putsFn = NativeFunctionLiteral.instance(Op.Puts);
        putsFn.addParam(param1);
        NativeFunctionLiteral readFn = NativeFunctionLiteral.instance(Op.Read);
        readFn.addParam(param1);
        NativeFunctionLiteral intFn = NativeFunctionLiteral.instance(Op.Int);
        intFn.addParam(param1);
        NativeFunctionLiteral strFn = NativeFunctionLiteral.instance(Op.Str);
        strFn.addParam(param1);
        NativeFunctionLiteral lengthFn = NativeFunctionLiteral.instance(Op.Length);
        lengthFn.addParam(param1);
        NativeFunctionLiteral hdFn = NativeFunctionLiteral.instance(Op.Hd);
        hdFn.addParam(param1);
        NativeFunctionLiteral tlFn = NativeFunctionLiteral.instance(Op.Tl);
        tlFn.addParam(param1);
        NativeFunctionLiteral atFn = NativeFunctionLiteral.instance(Op.At);
        atFn.addParam(param1);
        atFn.addParam(param2);
        NativeFunctionLiteral remFn = NativeFunctionLiteral.instance(Op.Rem);
        remFn.addParam(param1);
        remFn.addParam(param2);
    }

    public NativeFunctionLiteral(Op op){
        super();
        this.op = op;
    }

    public static NativeFunctionLiteral instance(Op op){
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
            case Puts:
                return putsFunction(env); 
            case Read:
                return readFunction(env); 
            case Int:
                return intFunction(env);
            case Str:
                return strFunction(env);
            case Length:
                return lengthFunction(env);
            case Hd:
                return hdFunction(env);
            case Tl:
                return tlFunction(env);
            case At:
                return atFunction(env);
            case Rem:
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


    private Value<?> putsFunction(Environment env){
        Variable param = instances.get(op).getParams().get(0); //Obtenha o primeiro parâmetro
        System.out.println(param.expr(env));

        return AtomValue.OK;  
    }

    private Value<?> readFunction(Environment env) {
        Variable param = instances.get(op).getParams().get(0); //Obtenha o primeiro parâmetro
        System.out.print(param.expr(env));

        String line = input.nextLine();
        return new StringValue(line);
    }

    private Value<?> intFunction(Environment env){
        Variable param = instances.get(op).getParams().get(0);  //Obtenha o primeiro parâmetro
        Value<?> val = param.expr(env);

        if(val instanceof StringValue)
            return new IntValue(Integer.parseInt(((StringValue)val).value()));
        else
            return new IntValue(0);
    }

    private Value<?> strFunction(Environment env){
        Variable param = instances.get(op).getParams().get(0);  //Obtenha o primeiro parâmetro
        Value<?> val = param.expr(env);

        return new StringValue(val.toString());
    }

    private Value<?> lengthFunction(Environment env){
        Variable param = instances.get(op).getParams().get(0);  //Obtenha o primeiro parâmetro
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
        Variable param = instances.get(op).getParams().get(0);  //Obtenha o primeiro parâmetro
        Value<?> val = param.expr(env);

        if(val instanceof ListValue){
            Value<?> head = ((ListValue) val).value().head();
            if(head != null)
                return head;
            else
                return AtomValue.ERROR;
        } 
        else if (val instanceof TupleValue){
            TupleItem item = ((TupleValue)val).value().head();
            if(item != null){
                ListLiteral head = new ListLiteral();
                head.add(item.key);
                head.add(item.value);
    
                return new ListValue(head);
            } else{
                return AtomValue.ERROR;
            }
        }
        throw LanguageException.instance(0, LanguageException.Error.InvalidOperation);
    }

    private Value<?> tlFunction(Environment env){
        Variable param = instances.get(op).getParams().get(0);  //Obtenha o primeiro parâmetro
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
        Variable param1 = instances.get(op).getParams().get(0);  //Obtenha o primeiro parâmetro
        Variable param2 = instances.get(op).getParams().get(1);  //Obtenha o segundo parâmetro
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
        Variable param1 = instances.get(op).getParams().get(0);  //Obtenha o primeiro parâmetro
        Variable param2 = instances.get(op).getParams().get(1);  //Obtenha o primeiro parâmetro
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

    public String toString() {
        return "fn<" + this.op.getName() + ">";
    }

}
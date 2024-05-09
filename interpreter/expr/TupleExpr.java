package interpreter.expr;

import java.util.ArrayList;
import java.util.List;

import interpreter.Environment;
import interpreter.literal.TupleLiteral;
import interpreter.value.Value;
import interpreter.value.TupleValue;

public class TupleExpr extends Expr {
    private List<TupleItem> list; 
    
    public TupleExpr(int line){
        super(line);
        list = new ArrayList<TupleItem>();
    }

    public void add(Expr key, Expr value){
        TupleItem item = new TupleItem(key, value);
        list.add(item);
    }

    public Value<?> expr(Environment env){
        TupleLiteral tupleLiteral = new TupleLiteral();

        for(TupleItem item : this.list){

            Value<?> v1 = item.key.expr(env);
            Value<?> v2 = item.value.expr(env);

            tupleLiteral.add(new interpreter.literal.TupleItem(v1, v2));
        }
        
        return new TupleValue(tupleLiteral);
    }
}

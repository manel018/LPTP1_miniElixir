package interpreter.expr;

import java.util.List;

import interpreter.Environment;
import interpreter.value.Value;

public class TupleExpr extends Expr {
    private List<TupleItem> list; 
    
    public TupleExpr(int line){
        super(line);
    }

    public void add(Expr key, Expr value){
        TupleItem item = new TupleItem(key, value);
        list.add(item);
    }

    public Value<?> expr(Environment env){
        //TODO: Implement me!
        return null;
    }
}

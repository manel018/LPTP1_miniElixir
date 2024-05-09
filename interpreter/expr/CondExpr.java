package interpreter.expr;

import java.util.ArrayList;
import java.util.List;

import interpreter.Environment;
import interpreter.value.AtomValue;
import interpreter.value.Value;

public class CondExpr extends Expr{
    private List<CondItem> items;

    public CondExpr(int line){
        super(line);
        items = new ArrayList<CondItem>();
    }

    public void add(Expr cond, Expr body){
        CondItem item = new CondItem(cond, body);
        items.add(item);
    }

    public Value<?> expr(Environment env){
        for(CondItem cItem : items){
            if(cItem.cond.expr(env).eval())
                return cItem.body.expr(env);
        }
        return AtomValue.NIL;
    }
}

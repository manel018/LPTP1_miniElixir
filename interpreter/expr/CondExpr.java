package interpreter.expr;

import java.util.ArrayList;
import java.util.List;

import interpreter.Environment;
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
        //TODO: Implement me!
        throw new UnsupportedOperationException("Unimplemented method 'expr'");
    }
}

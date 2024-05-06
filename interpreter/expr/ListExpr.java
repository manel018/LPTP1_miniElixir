package interpreter.expr;

import java.util.ArrayList;
import java.util.List;

import interpreter.Environment;
import interpreter.literal.ListLiteral;
import interpreter.value.ListValue;
import interpreter.value.Value;

public class ListExpr extends Expr {
    private List<Expr> list;

    public ListExpr(int line){
        super(line);
        list = new ArrayList<Expr>();
    }

    public void add(Expr expr){
        list.add(expr);
    }

    public List<Expr> getList(){
        return list;
    }

    public Value<?> expr(Environment env){
        Value<?> listValue = null;
        ListLiteral listLiteral = new ListLiteral();

        for(Expr expr : list){
            listLiteral.add(expr.expr(env));
        }

        listValue = new ListValue(listLiteral);
        
        return listValue;
    }
}

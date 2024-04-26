package interpreter.expr;

public class TupleItem {
    public Expr key;
    public Expr value;

    public TupleItem(Expr key, Expr value){
        this.key = key;
        this.value = value;
    }

    
}

package interpreter.expr;

import error.LanguageException;
import interpreter.Environment;
import interpreter.value.ListValue;
import interpreter.value.Value;

public class AssignExpr extends Expr {

    private Expr lhs;
    private Expr rhs;

    public AssignExpr(int line, Expr lhs, Expr rhs) {
        super(line);

        this.lhs = lhs;
        this.rhs = rhs;
    }
    
    public Value<?> expr(Environment env) {
        Value<?> v = null; 

        if (lhs instanceof Variable)
            v = assignVar(env);
        else
            v = assignList(env);

        return v;
    }

    private Value<?> assignVar(Environment env){
        Variable var = (Variable) lhs;
        Value<?> value = rhs.expr(env);

        var.setValue(env, value);

        return value;
    }

    private Value<?> assignList(Environment env){
        Value<?> value = rhs.expr(env);

        //As expresões devem ser listas
        if(lhs instanceof ListExpr && value instanceof ListValue){
            ListExpr listExpr = (ListExpr) lhs;
            ListValue listValue = (ListValue) rhs.expr(env);

            int size = listExpr.getList().size();
            int i = 0;

            // As listas devem ter o mesmo tamanho
            if(listValue.value().size() == size){
                
                for(Expr elementExpr: listExpr.getList()){
                    
                    // Todos elementos de lhs devem ser instâncias de Variable
                    if(elementExpr instanceof Variable){
                        Variable var = (Variable) elementExpr;

                        var.setValue(env, listValue.value().get(i));
                    } else
                        throw LanguageException.instance(super.getLine(), LanguageException.Error.InvalidOperation);
                    i++;
                }
                
                return listValue;
            }
        }
        throw LanguageException.instance(super.getLine(), LanguageException.Error.InvalidOperation);
    }
}
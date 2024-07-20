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
        return assignment(env, lhs, rhs.expr(env));    
    }

    private Value<?> assignment(Environment env, Expr leftExpr, Value<?> value){
        if (leftExpr instanceof Variable){
            Variable var = (Variable) leftExpr;
            return assignVar(env, var, value);
        }
        else
            return assignList(env, leftExpr, value);
    }

    private Value<?> assignVar(Environment env, Variable var, Value<?> value){
        var.setValue(env, value);
        return value;
    }

    private Value<?> assignList(Environment env, Expr leftExpr, Value<?> value){
        //A expresão à esquerda e o valor à direita devem ser listas
        if(leftExpr instanceof ListExpr && value instanceof ListValue){
            ListExpr leftList = (ListExpr) leftExpr;
            ListValue rightList = (ListValue) value;

            int size = leftList.getList().size();
            int i = 0;

            // As listas devem ter o mesmo tamanho
            if(rightList.value().size() == size){
                for(Expr elementExpr : leftList.getList()){
                    //Chame recursivamente a função de atribuição
                    assignment(env, elementExpr, rightList.value().get(i));
                    i++;
                }
            }
            return value;
        }
        throw LanguageException.instance(super.getLine(), LanguageException.Error.InvalidOperation);
    }
}
package interpreter.expr;

import java.util.List;

import error.LanguageException;
import interpreter.Environment;
import interpreter.literal.ListLiteral;
import interpreter.value.ListValue;
import interpreter.value.Value;

public class ForExpr extends Expr{
    private Variable var;
    private Expr expr;
    private List<Expr> filters;
    private Expr body;

    public ForExpr(int line, Variable var, Expr expr, List<Expr> filters, Expr body){
        super(line);
        this.var = var;
        this.expr = expr;
        this.filters = filters;
        this.body = body;
    }

    public Value<?> expr(Environment env) {
        Value<?> value = expr.expr(env);
        ListLiteral result = new ListLiteral();
        
        if(value instanceof ListValue){
            ListLiteral list = ((ListValue) value).value();
            Environment newEnv = new Environment(env);
            boolean filterFlag;
            
            for(Value<?> v : list){
                filterFlag = true;
                var.setValue(newEnv, v);    

                for(Expr cond : filters){
                // Se alguma condição do filtro não for satisfeita,
                // então o valor não é adicionado à lista
                    if(!cond.expr(newEnv).eval()){
                        filterFlag = false;
                        break;
                    }
                }
                if(filterFlag)
                    result.add(body.expr(newEnv));
            }
            return new ListValue(result);
        } 
        else
            throw LanguageException.instance(super.getLine(),LanguageException.Error.InvalidOperation);
    }


}

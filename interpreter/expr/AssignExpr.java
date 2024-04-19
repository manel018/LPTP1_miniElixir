package interpreter.expr;

import error.LanguageException;
import interpreter.Environment;
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
        Value<?> v = this.rhs.expr(env);

        if (lhs instanceof Variable) {
            Variable var = (Variable) lhs;
            var.setValue(env, v);

            return v;
        // } else if (lhs instanceof ListExpr) {
        //     throw new RuntimeException("Implement me!");
        } else {
            throw LanguageException.instance(super.getLine(), LanguageException.Error.InvalidOperation);
        }
    }
}

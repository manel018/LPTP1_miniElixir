package interpreter.expr;

import error.LanguageException;
import interpreter.Environment;
import interpreter.value.Value;
import lexical.Token;

public class Variable extends Expr {

    private Token name;
    private boolean anonymous;

    public Variable(Token name) {
        super(name.line);
        this.name = name;
        this.anonymous = name.lexeme.startsWith("_");
    }

    public String getName() {
        return name.lexeme;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public Value<?> expr(Environment env) {
        if (this.isAnonymous())
            throw LanguageException.instance(super.getLine(), LanguageException.Error.InvalidOperation);
            
        Value<?> v = env.get(this.name);
        return v;
    }
    
    public void setValue(Environment env, Value<?> value) {
        if (!this.isAnonymous())
            env.set(this.name, value);
    }

}

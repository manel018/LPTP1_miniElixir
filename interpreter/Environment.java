package interpreter;

import static error.LanguageException.Error.UndeclaredVariable;

import java.util.HashMap;
import java.util.Map;

import error.LanguageException;
import interpreter.value.Value;
import lexical.Token;

public class Environment {

    private final Environment enclosing;    //Escopo 'pai'
    private final Map<String, Value<?>> memory;

    public Environment() {
        this(null);
    }

    public Environment(Environment enclosing) {
        this.memory = new HashMap<String, Value<?>>();
        this.enclosing = enclosing;
    }

    //Procura o Token do escopo mais interno para o mais externo e retorna seu valor
    public Value<?> get(Token name) {
        if (memory.containsKey(name.lexeme))
            return memory.get(name.lexeme);

        //'enclosing' igual a null significa que estamos no escopo mais externo do programa.
        //Portanto, se o Token não for encontrado na memória do escopo mais externo, lança uma exceção    
        else if (enclosing != null) 
            return enclosing.get(name);
        else
            throw LanguageException.instance(name.line, UndeclaredVariable, name.lexeme);
    }

    public void set(Token name, Value<?> value) {
        memory.put(name.lexeme, value);
    }

}

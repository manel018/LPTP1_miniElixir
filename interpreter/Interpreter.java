package interpreter;

import interpreter.expr.Expr;
import interpreter.value.FunctionValue;
import interpreter.value.Value;
import lexical.Token;

public class Interpreter {

    public final static Environment globals =
        new Environment();

    static{
        globals.set(new Token("puts", null, null), FunctionValue.PUTS_FN);
        globals.set(new Token("read", null, null), FunctionValue.READ_FN);
        globals.set(new Token("int", null, null), FunctionValue.INT_FN);
        globals.set(new Token("str", null, null), FunctionValue.STR_FN);
        globals.set(new Token("length", null, null), FunctionValue.LENGTH_FN);
        globals.set(new Token("hd", null, null), FunctionValue.HD_FN);
        globals.set(new Token("tl", null, null), FunctionValue.TL_FN);
        globals.set(new Token("at", null, null), FunctionValue.AT_FN);
        globals.set(new Token("rem", null, null), FunctionValue.REM_FN);
    }

    private Interpreter() {
    }

    public static Value<?> interpret(Expr expr) {
        return expr.expr(globals);
    }

}

package interpreter.expr;

import error.LanguageException;
import interpreter.Environment;
import interpreter.literal.ListLiteral;
import interpreter.literal.TupleItem;
import interpreter.literal.TupleLiteral;
import interpreter.value.AtomValue;
import interpreter.value.IntValue;
import interpreter.value.ListValue;
import interpreter.value.StringValue;
import interpreter.value.TupleValue;
import interpreter.value.Value;

public class BinaryExpr extends Expr {

    public static enum Op {
        And,            // &&
        Or,             // ||
        Equal,          // ==
        NotEqual,       // !=
        LowerThan,      // <
        LowerEqual,     // <=
        GreaterThan,    // >
        GreaterEqual,   // >=
        ListSubtract,   // --
        ListConcat,     // ++
        StringConcat,   // <>
        Add,            // +
        Sub,            // -
        Mul,            // *
        Div             // /
    }

    private Expr left;
    private Op op;
    private Expr right;

    public BinaryExpr(int line, Expr left, Op op, Expr right) {
        super(line);

        this.left = left;
        this.op = op;
        this.right = right;
    }

    @Override
    public Value<?> expr(Environment env) {
        switch (op) {
            case And:
                return andOp(env);
            case Or:
                return orOp(env);
            case Equal:
                return equalOp(env);
            case NotEqual:
                return notEqualOp(env);
            case LowerThan:
                return lowerThanOp(env);
            case GreaterThan:
                return greaterThanOp(env);
            case LowerEqual:
                return lowerEqualOp(env);
            case GreaterEqual:
                return greaterEqualOp(env);
            case ListSubtract:
                return listSubtractOp(env);
            case ListConcat:
                return listConcatOp(env);
            case StringConcat:
                return stringConcatOp(env);
            case Add:
                return addOp(env);
            case Sub:
                return subOp(env);
            case Mul:
                return mulOp(env);
            case Div:
                return divOp(env);
            default:
                throw new RuntimeException("Unreachable");
        }
    }

    private Value<?> andOp(Environment env) {
        Value<?> v1 = right.expr(env);
        Value<?> v2 = left.expr(env);

        // Retorna o valor do último termo avaliado na expressão
        // Se v1 for falso, retorna v1
        if(!v1.eval())
            return v1;
        else    // Se v1 for verdadeiro, retorna v2
            return v2;
    }

    private Value<?> orOp(Environment env) {
        Value<?> v1 = right.expr(env);
        Value<?> v2 = left.expr(env);

        // Retorna o valor do último termo avaliado na expressão
        // Se v1 for verdadeiro, retorna v1
        if(v1.eval())
            return v1;
        else    // Se v1 for falso, retorna v2
            return v2;
    }

    private Value<?> equalOp(Environment env) {
        Value<?> v1 = left.expr(env);
        Value<?> v2 = right.expr(env);

        // Os tipos devem ser iguais
        if(v1.getClass() == v2.getClass() && v1.equals(v2))
            return AtomValue.TRUE;
        else
            return AtomValue.FALSE;
    }

    private Value<?> notEqualOp(Environment env) {
        Value<?> v1 = right.expr(env);
        Value<?> v2 = left.expr(env);
        
        // Os tipos devem ser iguais
        if(v1.getClass() == v2.getClass() && !v1.equals(v2))
            return AtomValue.TRUE;
        else
            return AtomValue.FALSE;
    }

    private Value<?> lowerThanOp(Environment env) {
        Value<?> v1 = right.expr(env);
        Value<?> v2 = left.expr(env);

        if(v1 instanceof IntValue && v2 instanceof IntValue){
            IntValue iv1 = (IntValue) v1;
            IntValue iv2 = (IntValue) v2;

            if (iv2.value().intValue() < iv1.value().intValue())
                return AtomValue.TRUE;
        }
        
        return AtomValue.FALSE;
    }

    private Value<?> greaterThanOp(Environment env) {
        Value<?> v1 = right.expr(env);
        Value<?> v2 = left.expr(env);

        if(v1 instanceof IntValue && v2 instanceof IntValue){
            IntValue iv1 = (IntValue) v1;
            IntValue iv2 = (IntValue) v2;

            if(iv2.value().intValue() > iv1.value().intValue())
                return AtomValue.TRUE;
        }

        return AtomValue.FALSE;
    }

    private Value<?> lowerEqualOp(Environment env) {
        Value<?> v1 = right.expr(env);
        Value<?> v2 = left.expr(env);

        if(v1 instanceof IntValue && v2 instanceof IntValue){
            IntValue iv1 = (IntValue) v1;
            IntValue iv2 = (IntValue) v2;

            if(iv2.value().intValue() <= iv1.value().intValue())
                return AtomValue.TRUE;
        }

        return AtomValue.FALSE;
    }

    private Value<?> greaterEqualOp(Environment env) {
        Value<?> v1 = right.expr(env);
        Value<?> v2 = left.expr(env);

        if(v1 instanceof IntValue && v2 instanceof IntValue){
            IntValue iv1 = (IntValue) v1;
            IntValue iv2 = (IntValue) v2;

            if(iv2.value().intValue() >= iv1.value().intValue())
                return AtomValue.TRUE;
        }

        return AtomValue.FALSE;
    }

    private Value<?> listSubtractOp(Environment env) {
        Value<?> v1 = right.expr(env);
        Value<?> v2 = left.expr(env);

        // Subtrai Listas
        if(v1 instanceof ListValue && v2 instanceof ListValue){
            ListLiteral listLit1 = ((ListValue) v1).value();
            ListLiteral listLit2 = ((ListValue) v2).value();

            ListLiteral subtractList = new ListLiteral();
            for(Value<?> value : listLit2)
            // Inclua o valor na lista resultante somente
            // se ele não estiver contido na lista da direita
                if(!listLit1.contains(value))
                    subtractList.add(value);

            return new ListValue(subtractList);
            
        // Subtrai Tuplas
        } else if(v1 instanceof TupleValue && v2 instanceof TupleValue){
            TupleLiteral tupleV1 = ((TupleValue) v1).value();
            TupleLiteral tupleV2 = ((TupleValue) v2).value();
            
            TupleLiteral subtractTuple = new TupleLiteral();
            for(TupleItem item : tupleV2)
            // Inclua o item na tupla resultante somente
            // se ele não estiver contido na lista da direita
                if(!tupleV1.contains(item))
                    subtractTuple.add(item);

            return new TupleValue(subtractTuple);
        }else
            throw LanguageException.instance(super.getLine(), LanguageException.Error.InvalidOperation);
    }

    private Value<?> listConcatOp(Environment env) {
        Value<?> v1 = right.expr(env);
        Value<?> v2 = left.expr(env);

        // Concatena Listas
        if(v1 instanceof ListValue && v2 instanceof ListValue){
            ListLiteral listV1 = ((ListValue) v1).value();
            ListLiteral listV2 = ((ListValue) v2).value();
            
            ListLiteral concatList = new ListLiteral();
            for(Value<?> value : listV2)
                concatList.add(value);
            for(Value<?> value : listV1)
                concatList.add(value);

            return new ListValue(concatList);

        // Concatena Tuplas
        } else if(v1 instanceof TupleValue && v2 instanceof TupleValue){
            TupleLiteral tupleV1 = ((TupleValue) v1).value();
            TupleLiteral tupleV2 = ((TupleValue) v2).value();

            TupleLiteral concatTuple = new TupleLiteral();
            for(TupleItem item : tupleV2)
                concatTuple.add(item);
            for(TupleItem item : tupleV1)
                concatTuple.add(item);

            return new TupleValue(concatTuple);
            
        } else
            throw LanguageException.instance(super.getLine(), LanguageException.Error.InvalidOperation);
    }

    private Value<?> stringConcatOp(Environment env) {
        Value<?> v1 = left.expr(env);
        Value<?> v2 = right.expr(env);

        if (v1 instanceof StringValue && v2 instanceof StringValue){
            StringValue sv1 = (StringValue) v1;
            StringValue sv2 = (StringValue) v2;

            String concat =  sv1.value() + sv2.value();

            return new StringValue(concat);
        } else
            throw LanguageException.instance(super.getLine(), LanguageException.Error.InvalidOperation);
    }

    private Value<?> addOp(Environment env) {
        Value<?> v1 = left.expr(env);
        Value<?> v2 = right.expr(env);

        if(v1 instanceof IntValue && v2 instanceof IntValue){
            IntValue iv1 = (IntValue) v1;
            IntValue iv2 = (IntValue) v2;

            int n = iv1.value() + iv2.value();

            return new IntValue(n);
        } else
            throw LanguageException.instance(super.getLine(), LanguageException.Error.InvalidOperation);
    }

    private Value<?> subOp(Environment env) {
        Value<?> v1 = left.expr(env);
        Value<?> v2 = right.expr(env);

        if (v1 instanceof IntValue && v2 instanceof IntValue){
            IntValue iv1 = (IntValue) v1;
            IntValue iv2 = (IntValue) v2;

            int n = iv1.value() - iv2.value();

            return new IntValue(n);
        } else
            throw LanguageException.instance(super.getLine(), LanguageException.Error.InvalidOperation);
    }

    private Value<?> mulOp(Environment env) {
        Value<?> v1 = this.left.expr(env);
        Value<?> v2 = this.right.expr(env);

        if (v1 instanceof IntValue && v2 instanceof IntValue) {
            IntValue iv1 = (IntValue) v1;
            IntValue iv2 = (IntValue) v2;

            int n = iv1.value() * iv2.value();
            return new IntValue(n);
        } else
            throw LanguageException.instance(super.getLine(), LanguageException.Error.InvalidOperation);
    }

    private Value<?> divOp(Environment env) {
        Value<?> v1 = this.left.expr(env);
        Value<?> v2 = this.right.expr(env);

        if (v1 instanceof IntValue && v2 instanceof IntValue) {
            IntValue iv1 = (IntValue) v1;
            IntValue iv2 = (IntValue) v2;

            int n = iv1.value() / iv2.value();
            return new IntValue(n);
        } else
            throw LanguageException.instance(super.getLine(), LanguageException.Error.InvalidOperation);
    }

}

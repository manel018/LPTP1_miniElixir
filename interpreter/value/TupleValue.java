package interpreter.value;

import interpreter.literal.TupleLiteral;

public class TupleValue extends Value<TupleLiteral> {

    private TupleLiteral value;

    public TupleValue(TupleLiteral value) {
        this.value = value;
    }

    @Override
    public TupleLiteral value() {
        return this.value;
    }

    @Override
    public boolean eval() {
        return true;
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof TupleValue) {
            return this.value.equals(((TupleValue) obj).value);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return this.value.toString();
    }
    
}

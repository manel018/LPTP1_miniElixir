package interpreter.literal;

import interpreter.value.Value;

public class TupleItem {
    
    public Value<?> key;
    public Value<?> value;

    public TupleItem(Value<?> key, Value<?> value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + this.key.hashCode();
        result = prime * result + this.value.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof TupleItem) {
            return this.key.equals(((TupleItem) obj).key) &&
                this.value.equals(((TupleItem) obj).value);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.key.toString());
        sb.append(":");
        sb.append(this.value.toString());
        return sb.toString();
    }

}

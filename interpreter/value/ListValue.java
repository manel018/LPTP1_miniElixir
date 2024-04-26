package interpreter.value;

import interpreter.literal.ListLiteral;

public class ListValue extends Value<ListLiteral> {

    private ListLiteral value;

    public ListValue(ListLiteral value) {
        this.value = value;
    }

    public ListLiteral value() {
        return this.value;
    }

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
        } else if (obj instanceof ListValue) {
            return this.value.equals(((ListValue) obj).value);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        // Opção melhorada:
        // return this.value.toString();

        StringBuffer sb = new StringBuffer();
        sb.append("[");

        for (Value<?> v : this.value) {
            sb.append(v.toString());
            sb.append(",");
        }

        if (sb.length() > 1)
            sb.setLength(sb.length() - 1);

        sb.append("]");
        return sb.toString();
    }

}

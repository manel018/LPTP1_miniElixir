package interpreter.literal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import interpreter.value.Value;

public class ListLiteral implements Iterable<Value<?>> {

    private List<Value<?>> list;

    public ListLiteral() {
        this.list = new ArrayList<Value<?>>();
    }

    public boolean add(Value<?> v) {
        return this.list.add(v);
    }

    public Value<?> get(int index) {
        return (index >= 0 && index < this.size()) ?
            this.list.get(index) : null;
    }

    public int size() {
        return this.list.size();
    }

    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    public boolean contains(Value<?> v) {
        return this.list.contains(v);
    }

    public Value<?> head() {
        return this.isEmpty() ? null : this.list.get(0);
    }

    public List<Value<?>> tail() {
        return this.isEmpty() ? null : this.list.subList(1, this.list.size());
    }

    public Iterator<Value<?>> iterator() {
        return this.list.iterator();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + this.list.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof ListLiteral) {
            return this.list.equals(((ListLiteral) obj).list);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[");

        for (Value<?> v : this) {
            sb.append(v);
            sb.append(",");
        }

        if (sb.length() > 1)
            sb.setLength(sb.length() - 1);

        sb.append("]");
        return sb.toString();
    }

}

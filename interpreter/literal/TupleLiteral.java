package interpreter.literal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TupleLiteral implements Iterable<TupleItem> {

    private List<TupleItem> list;

    public TupleLiteral() {
        this.list = new ArrayList<TupleItem>();
    }

    public boolean add(TupleItem item) {
        for (TupleItem tmp : this) {
            if (tmp.key.equals(item.key)) {
                tmp.value = item.value;
                return true;
            }
        }

        this.list.add(item);
        return true;
    }

    public TupleItem get(int index) {
        return (index >= 0 && index < this.size()) ?
                this.list.get(index) : null;
    }

    public int size() {
        return this.list.size();
    }

    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    public boolean contains(TupleItem e) {
        return this.list.contains(e);
    }

    public TupleItem head() {
        return this.isEmpty() ? null : this.list.get(0);
    }

    public List<TupleItem> tail() {
        return this.isEmpty() ? null : this.list.subList(1, this.list.size());
    }

    public Iterator<TupleItem> iterator() {
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
        } else if (obj instanceof TupleLiteral) {
            return this.list.equals(((TupleLiteral) obj).list);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("{");

        for (TupleItem item : this) {
            sb.append(item);
            sb.append(",");
        }

        if (sb.length() > 1)
            sb.setLength(sb.length() - 1);

        sb.append("}");
        return sb.toString();
    }

}

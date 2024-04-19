package interpreter.value;

public class IntValue extends Value<Integer> {

    private Integer value;

    public IntValue(Integer value) {
        this.value = value;
    }

    @Override
    public Integer value() {
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
        } else if (obj instanceof IntValue) {
            return this.value.intValue() == ((IntValue) obj).value.intValue();
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return this.value.toString();
    }

}

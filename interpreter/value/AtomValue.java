package interpreter.value;

import interpreter.literal.AtomLiteral;

public class AtomValue extends Value<AtomLiteral> {

    private AtomLiteral value;

    public static final AtomValue NIL;
    public static final AtomValue FALSE;
    public static final AtomValue TRUE;
    public static final AtomValue OK;
    public static final AtomValue ERROR;

    static {
        NIL = new AtomValue(AtomLiteral.instance(":nil"));
        FALSE = new AtomValue(AtomLiteral.instance(":false"));
        TRUE = new AtomValue(AtomLiteral.instance(":true"));
        OK = new AtomValue(AtomLiteral.instance(":ok"));
        ERROR = new AtomValue(AtomLiteral.instance(":error"));
    }

    public AtomValue(AtomLiteral value) {
        this.value = value;
    }

    @Override
    public AtomLiteral value() {
        return this.value;
    }

    @Override
    public boolean eval() {
        String atom = this.value.getAtom();
        return !(atom.equals(":nil") || atom.equals(":false") || atom.equals(":error"));
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof AtomValue) {
            return this.value.getAtom().equals(((AtomValue) obj).value.getAtom());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return this.value.getAtom();
    }

}

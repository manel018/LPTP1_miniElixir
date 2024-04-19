package interpreter.literal;

import java.util.HashMap;
import java.util.Map;

public class AtomLiteral {

    private String atom;

    private static Map<String,AtomLiteral> instances =
        new HashMap<String,AtomLiteral>();

    private AtomLiteral(String atom) {
        this.atom = atom;
    }

    public String getAtom() {
        return this.atom;
    }

    public static AtomLiteral instance(String atom) {
        AtomLiteral a = instances.get(atom);
        if (a == null) {
            a = new AtomLiteral(atom);
            instances.put(atom, a);
        }

        return a;
    }
}

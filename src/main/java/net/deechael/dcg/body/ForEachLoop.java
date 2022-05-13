package net.deechael.dcg.body;

import net.deechael.dcg.JExecutable;
import net.deechael.dcg.JType;
import net.deechael.dcg.items.Var;

public final class ForEachLoop implements Operation {

    private final String type;
    private final String varName;
    private final Var iterable;
    private final JExecutable body;

    public ForEachLoop(JType type, String varName, Var iterable, JExecutable body) {
        this.type = type != null ? type.typeString() : null;
        this.varName = varName;
        this.iterable = iterable;
        this.body = body;
    }

    @Override
    public String getString() {
        return "for (" + type + " " + varName + " : " + iterable.varString() + ") {\n"
                + body.getString() + "\n"
                + "}";
    }

}

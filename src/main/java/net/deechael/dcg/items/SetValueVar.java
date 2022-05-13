package net.deechael.dcg.items;

import net.deechael.dcg.JType;

final class SetValueVar implements Var {

    private final String originalTarget;
    private final Var value;

    private boolean got = false;

    public SetValueVar(String originalTarget, Var value) {
        this.originalTarget = originalTarget;
        this.value = value;
    }

    @Override
    public JType getType() {
        throw new RuntimeException("No type");
    }

    @Override
    public String getName() {
        throw new RuntimeException("No name");
    }

    @Override
    public String varString() {
        if (got) {
            return originalTarget;
        } else {
            return "(" + originalTarget + " = " + value.varString() + ")";
        }
    }

}

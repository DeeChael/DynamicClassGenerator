package net.deechael.dcg.items;

import net.deechael.dcg.JType;

final class ReferringVar implements Var {

    private final JType type;
    private final String name;

    public ReferringVar(JType type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public JType getType() {
        return this.type;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String varString() {
        return getName();
    }

}

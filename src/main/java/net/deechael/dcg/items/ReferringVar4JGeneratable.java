package net.deechael.dcg.items;

import net.deechael.dcg.JType;

final class ReferringVar4JGeneratable implements Var {

    private final JType type;
    private final String name;

    public ReferringVar4JGeneratable(JType type, String name) {
        this.type = type;
        this.name = name;
    }

    public JType getJGeneratableType() {
        return this.type;
    }

    @Override
    public JType getType() {
        throw new RuntimeException("Please invoke getJGeneratableType()");
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

package net.deechael.dcg.items;

import net.deechael.dcg.JGeneratable;

final class ReferringVar4JGeneratable implements Var {

    private final JGeneratable type;
    private final String name;

    public ReferringVar4JGeneratable(JGeneratable type, String name) {
        this.type = type;
        this.name = name;
    }

    public JGeneratable getJGeneratableType() {
        return this.type;
    }

    @Override
    public Class<?> getType() {
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

package net.deechael.dcg.items;

final class ReferringVar implements Var {

    private final Class<?> type;
    private final String name;

    public ReferringVar(Class<?> type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public Class<?> getType() {
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

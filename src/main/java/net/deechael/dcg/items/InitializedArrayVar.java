package net.deechael.dcg.items;

import net.deechael.dcg.JGeneratable;

final class InitializedArrayVar implements Var {

    private final String type;
    private final int length;

    public InitializedArrayVar(Class<?> type, int length) {
        this.type = type.getName();
        this.length = length;
    }

    public InitializedArrayVar(JGeneratable type, int length) {
        this.type = type.getName();
        this.length = length;
    }

    @Override
    public Class<?> getType() {
        throw new RuntimeException("No type");
    }

    @Override
    public String getName() {
        throw new RuntimeException("No name");
    }

    @Override
    public String varString() {
        return "new " + type + "[" + length + "]";
    }

}

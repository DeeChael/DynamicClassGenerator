package net.deechael.dcg.items;

import net.deechael.dcg.JType;

final class InitializedArrayVar implements Var {

    private final String type;
    private final int length;

    public InitializedArrayVar(JType type, int length) {
        this.type = type.typeString();
        this.length = length;
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
        return "new " + type + "[" + length + "]";
    }

}

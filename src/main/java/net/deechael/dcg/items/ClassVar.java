package net.deechael.dcg.items;

import net.deechael.dcg.JType;

final class ClassVar implements Var {

    private final JType type;

    public ClassVar(JType type) {
        this.type = type;
    }

    @Override
    public String getName() {
        throw new RuntimeException("Class var doesn't have name!");
    }

    @Override
    public String varString() {
        return type.typeString() + ".class";
    }

}

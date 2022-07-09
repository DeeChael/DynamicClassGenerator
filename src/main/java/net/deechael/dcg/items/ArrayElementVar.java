package net.deechael.dcg.items;

import net.deechael.dcg.JType;

final class ArrayElementVar implements Var {

    private final Var element;
    private final Var index;

    public ArrayElementVar(Var element, Var index) {
        this.element = element;
        this.index = index;
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
        return element.varString() + "[" + index.varString() + "]";
    }

}

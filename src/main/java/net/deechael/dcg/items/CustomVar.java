package net.deechael.dcg.items;

import net.deechael.dcg.JType;

final class CustomVar implements Var {

    private final String varValue;

    public CustomVar(String varValue) {
        this.varValue = varValue;
    }

    @Override
    public JType getType() {
        throw new RuntimeException("CustomVar not has type");
    }

    @Override
    public String getName() {
        throw new RuntimeException("CustomVar not has name");
    }

    @Override
    public String varString() {
        return varValue;
    }

}

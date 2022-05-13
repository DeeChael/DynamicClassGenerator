package net.deechael.dcg.items;

import net.deechael.dcg.JType;

final class ConstructorVar implements Var {

    private final JType type;
    private final String bodyString;

    public ConstructorVar(JType type, String bodyString) {
        this.type = type;
        this.bodyString = bodyString;
    }

    @Override
    public JType getType() {
        return this.type;
    }

    @Override
    public String getName() {
        throw new RuntimeException("This var doesn't have name!");
    }

    @Override
    public String varString() {
        return "new " + getType().typeString() + "(" + bodyString + ")";
    }

}

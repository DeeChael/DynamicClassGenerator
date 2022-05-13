package net.deechael.dcg.items;

import net.deechael.dcg.JType;

final class ConstructorVar4JGeneratable implements Var {

    private final JType type;
    private final String bodyString;

    public ConstructorVar4JGeneratable(JType type, String bodyString) {
        this.type = type;
        this.bodyString = bodyString;
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
        throw new RuntimeException("This var doesn't have name!");
    }

    @Override
    public String varString() {
        return "new " + this.type.typeString() + "(" + bodyString + ")";
    }

}

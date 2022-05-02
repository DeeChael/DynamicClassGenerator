package net.deechael.dcg.items;

import net.deechael.dcg.JGeneratable;

final class ConstructorVar4JGeneratable implements Var {

    private final JGeneratable type;
    private final String bodyString;

    public ConstructorVar4JGeneratable(JGeneratable type, String bodyString) {
        this.type = type;
        this.bodyString = bodyString;
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
        throw new RuntimeException("This var doesn't have name!");
    }

    @Override
    public String varString() {
        return "new " + getType().getName() + "(" + bodyString + ")";
    }

}

package net.deechael.dcg.items;

import net.deechael.dcg.JType;

public final class LambdaVar implements Var {

    private final String type;
    private final String content;

    public LambdaVar(JType clazz, String content) {
        this.type = clazz.typeString();
        this.content = content;
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
        return type + "::" + content;
    }

}

package net.deechael.dcg.items;

import net.deechael.dcg.JGeneratable;

public final class LambdaVar implements Var {

    private final String type;
    private final String content;

    public LambdaVar(Class<?> clazz, String content) {
        this.type = clazz.getName();
        this.content = content;
    }

    public LambdaVar(JGeneratable clazz, String content) {
        this.type = clazz.getName();
        this.content = content;
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
        return type + "::" + content;
    }

}

package net.deechael.dcg.items;

import net.deechael.dcg.JType;

final class InvokeMethodDirectlyAsVar implements Var {

    private final String methodName;
    private final String body;

    public InvokeMethodDirectlyAsVar(String methodName, String body) {
        this.methodName = methodName;
        this.body = body;
    }

    @Override
    public JType getType() {
        throw new RuntimeException("InvokeMethodDirectlyAsVar cannot get type!");
    }

    @Override
    public String getName() {
        throw new RuntimeException("InvokeMethodDirectlyAsVar cannot have a name!");
    }

    @Override
    public String varString() {
        return methodName + "(" + body + ")";
    }

}


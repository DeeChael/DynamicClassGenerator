package net.deechael.dcg.items;

import net.deechael.dcg.JType;

final class InvokeMethodAsVar implements Var {

    private final String varName;
    private final String methodName;
    private final String body;

    public InvokeMethodAsVar(String varName, String methodName, String body) {
        this.varName = varName;
        this.methodName = methodName;
        this.body = body;
    }

    @Override
    public JType getType() {
        throw new RuntimeException("InvokeMethodAsVar cannot get type!");
    }

    @Override
    public String getName() {
        throw new RuntimeException("InvokeMethodAsVar cannot have a name!");
    }

    @Override
    public String varString() {
        return varName + "." + methodName + "(" + body + ")";
    }

}

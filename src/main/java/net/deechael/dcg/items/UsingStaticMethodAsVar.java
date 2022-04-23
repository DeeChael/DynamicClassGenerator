package net.deechael.dcg.items;

import java.lang.reflect.Method;

class UsingStaticMethodAsVar extends Var {

    private final String varName;
    private final String methodName;
    private final String body;

    public UsingStaticMethodAsVar(Method method, String body) {
        super(method.getReturnType(), null);
        this.varName = method.getDeclaringClass().getName();
        this.methodName = method.getName();
        this.body = body;
    }

    @Override
    public String getName() {
        throw new RuntimeException("UsingStaticMethodAsVar cannot have a name!");
    }

    @Override
    public String varString() {
        return varName + "." + methodName + "(" + body + ")";
    }

}

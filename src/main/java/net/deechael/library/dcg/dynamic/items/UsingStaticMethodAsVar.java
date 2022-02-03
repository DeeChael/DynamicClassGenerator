package net.deechael.library.dcg.dynamic.items;

import net.deechael.library.dcg.dynamic.body.Requirement;

import java.lang.reflect.Method;

public class UsingStaticMethodAsVar extends Var implements Requirement {

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


    @Override
    public String getString() {
        return varName + "." + methodName + "(" + body + ")";
    }

}

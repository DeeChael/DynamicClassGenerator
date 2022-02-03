package net.deechael.library.dcg.dynamic.items;

import net.deechael.library.dcg.dynamic.body.Requirement;

import java.lang.reflect.Method;

public class UsingMethodAsVar extends Var implements Requirement {

    private final String varName;
    private final String methodName;
    private final String body;

    public UsingMethodAsVar(String varName, Method method, String body) {
        super(method.getReturnType(), null);
        this.varName = varName;
        this.methodName = method.getName();
        this.body = body;
    }

    @Override
    public String getName() {
        throw new RuntimeException("UsingMethodAsVar cannot have a name!");
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

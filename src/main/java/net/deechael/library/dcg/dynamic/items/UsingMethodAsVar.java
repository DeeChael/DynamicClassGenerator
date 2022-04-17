package net.deechael.library.dcg.dynamic.items;

import net.deechael.library.dcg.dynamic.body.Requirement;

public class UsingMethodAsVar extends Var implements Requirement {

    private final String varName;
    private final String methodName;
    private final String body;

    public UsingMethodAsVar(String varName, String methodName, String body) {
        super(null, null);
        this.varName = varName;
        this.methodName = methodName;
        this.body = body;
    }

    @Override
    public Class<?> getType() {
        throw new RuntimeException("UsingMethodAsVar cannot get type!");
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

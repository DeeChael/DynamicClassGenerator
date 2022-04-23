package net.deechael.dcg.items;

class UsingMethodAsVar extends Var {

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

}

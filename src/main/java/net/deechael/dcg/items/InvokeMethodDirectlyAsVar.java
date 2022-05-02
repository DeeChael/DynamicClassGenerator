package net.deechael.dcg.items;

final class InvokeMethodDirectlyAsVar extends Var {

    private final String methodName;
    private final String body;

    public InvokeMethodDirectlyAsVar(String methodName, String body) {
        super(null, null);
        this.methodName = methodName;
        this.body = body;
    }

    @Override
    public Class<?> getType() {
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


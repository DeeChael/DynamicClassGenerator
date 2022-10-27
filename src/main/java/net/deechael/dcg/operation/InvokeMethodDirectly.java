package net.deechael.dcg.operation;

public final class InvokeMethodDirectly implements Operation {
    private final String methodName;
    private final String body;

    public InvokeMethodDirectly(String methodName, String body) {
        this.methodName = methodName;
        this.body = body;
    }

    @Override
    public String getString() {
        return methodName + "(" + body + ");";
    }

}

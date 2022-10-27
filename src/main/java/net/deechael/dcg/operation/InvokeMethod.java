package net.deechael.dcg.operation;

public final class InvokeMethod implements Operation {

    private final String varName;
    private final String methodName;
    private final String body;

    public InvokeMethod(String varName, String methodName, String body) {
        this.varName = varName;
        this.methodName = methodName;
        this.body = body;
    }

    @Override
    public String getString() {
        return varName + "." + methodName + "(" + body + ");";
    }

}

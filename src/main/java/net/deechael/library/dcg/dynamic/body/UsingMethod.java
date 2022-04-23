package net.deechael.library.dcg.dynamic.body;

public final class UsingMethod implements Operation {

    private final String varName;
    private final String methodName;
    private final String body;

    public UsingMethod(String varName, String methodName, String body) {
        this.varName = varName;
        this.methodName = methodName;
        this.body = body;
    }

    @Override
    public String getString() {
        return varName + "." + methodName + "(" + body + ");";
    }

}

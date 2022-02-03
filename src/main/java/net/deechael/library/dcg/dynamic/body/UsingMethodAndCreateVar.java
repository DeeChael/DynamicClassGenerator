package net.deechael.library.dcg.dynamic.body;

public class UsingMethodAndCreateVar implements Operation {

    private final String returnClassName;
    private final String newVarName;
    private final String varName;
    private final String methodName;
    private final String body;

    public UsingMethodAndCreateVar(String returnClassName, String newVarName, String varName, String methodName, String body) {
        this.returnClassName = returnClassName;
        this.newVarName = newVarName;
        this.varName = varName;
        this.methodName = methodName;
        this.body = body;
    }

    @Override
    public String getString() {
        return returnClassName + " " + newVarName + " = " + varName + "." + methodName + "(" + body + ");";
    }

}

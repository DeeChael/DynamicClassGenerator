package net.deechael.library.dcg.dynamic.body;

public class UsingStaticMethodAndCreateVar implements Operation {

    private final String returnClassName;
    private final String newVarName;
    private final String className;
    private final String methodName;
    private final String body;

    public UsingStaticMethodAndCreateVar(String returnClassName, String newVarName, String className, String methodName, String body) {
        this.returnClassName = returnClassName;
        this.newVarName = newVarName;
        this.className = className;
        this.methodName = methodName;
        this.body = body;
    }

    @Override
    public String getString() {
        return returnClassName + " " + newVarName + " = " + className + "." + methodName + "(" + body + ");";
    }

}

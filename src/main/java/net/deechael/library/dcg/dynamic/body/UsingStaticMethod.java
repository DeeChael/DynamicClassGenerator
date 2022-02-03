package net.deechael.library.dcg.dynamic.body;

public class UsingStaticMethod implements Operation {

    private final String className;
    private final String methodName;
    private final String body;

    public UsingStaticMethod(String className, String methodName, String body) {
        this.className = className;
        this.methodName = methodName;
        this.body = body;
    }

    @Override
    public String getString() {
        return className + "." + methodName + "(" + body + ");";
    }

}

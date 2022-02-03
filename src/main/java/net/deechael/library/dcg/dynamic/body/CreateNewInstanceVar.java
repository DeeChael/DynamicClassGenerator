package net.deechael.library.dcg.dynamic.body;

public class CreateNewInstanceVar implements Operation {

    private final Class<?> type;
    private final String varName;
    private final String body;

    public CreateNewInstanceVar(Class<?> type, String varName, String body) {
        this.type = type;
        this.varName = varName;
        this.body = body;
    }

    @Override
    public String getString() {
        return type.getName() + " " + varName + " = new " + type.getName() + "(" + body + ");";
    }

}

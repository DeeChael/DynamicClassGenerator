package net.deechael.library.dcg.dynamic.body;

public class CreateNewObjectsFieldVar implements Operation {

    private final Class<?> type;
    private final String newVarName;

    private final String varName;
    private final String fieldName;

    public CreateNewObjectsFieldVar(Class<?> type, String newVarName, String varName, String fieldName) {
        this.type = type;
        this.newVarName = newVarName;
        this.varName = varName;
        this.fieldName = fieldName;
    }

    @Override
    public String getString() {
        return type.getName() + " " + newVarName + " = " + varName + "." + fieldName + ";";
    }

}

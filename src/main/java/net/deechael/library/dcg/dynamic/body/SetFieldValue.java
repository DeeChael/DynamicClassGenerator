package net.deechael.library.dcg.dynamic.body;

public class SetFieldValue implements Operation {

    private final String fieldName;
    private final String varString;

    public SetFieldValue(String fieldName, String varString) {
        this.fieldName = fieldName;
        this.varString = varString;
    }

    @Override
    public String getString() {
        return "this." + this.fieldName + " = (" + varString + ");";
    }

}

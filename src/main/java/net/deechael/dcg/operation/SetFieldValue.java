package net.deechael.dcg.operation;

public final class SetFieldValue implements Operation {

    private final String parentName;
    private final String owner;
    private final String field;
    private final String varString;

    private final boolean isStatic;

    public SetFieldValue(String parentName, String owner, String field, String varString, boolean isStatic) {
        this.parentName = parentName;
        this.owner = owner;
        this.field = field;
        this.varString = varString;
        this.isStatic = isStatic;
    }

    @Override
    public String getString() {
        if (isStatic) {
            return parentName + "." + field + " = (" + varString + ");";
        } else {
            String base = "";
            if (owner != null) {
                base = this.owner + ".";
            }
            return base + this.field + " = (" + varString + ");";
        }
    }

}

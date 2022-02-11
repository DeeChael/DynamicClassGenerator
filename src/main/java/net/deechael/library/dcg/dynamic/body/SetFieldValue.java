package net.deechael.library.dcg.dynamic.body;

import net.deechael.library.dcg.dynamic.JField;

public class SetFieldValue implements Operation {

    private final JField field;
    private final String varString;

    public SetFieldValue(JField field, String varString) {
        this.field = field;
        this.varString = varString;
    }

    @Override
    public String getString() {
        if (field.isStatic()) {
            return field.getParent().getName() + "." + field.getName() + " = (" + varString + ");";
        } else {
            return field.getParent().getName() + "." + "this." + this.field.getName() + " = (" + varString + ");";
        }
    }

}

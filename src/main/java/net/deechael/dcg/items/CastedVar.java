package net.deechael.dcg.items;

import net.deechael.dcg.JType;

final class CastedVar implements Var {

    private final String type;
    private final String originalVarString;

    public CastedVar(JType type, String originalVarString) {
        this.type = type.typeString();
        this.originalVarString = originalVarString;
    }

    @Override
    public JType getType() {
        throw new RuntimeException("No type");
    }

    @Override
    public String getName() {
        throw new RuntimeException("This var doesn't have name!");
    }

    @Override
    public String varString() {
        return "((" + type + ") (" + originalVarString + "))";
    }

    private String deal(String typeName) {
        if (typeName.startsWith("[L")) {
            return typeName.substring(2) + "[]";
        } else if (typeName.startsWith("[")) {
            return typeName.substring(1) + "[]";
        }
        return typeName;
    }

}

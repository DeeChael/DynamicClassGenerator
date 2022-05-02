package net.deechael.dcg.items;

import net.deechael.dcg.JGeneratable;

final class CastedVar implements Var {

    private final String type;
    private final String originalVarString;

    public CastedVar(Class<?> type, String originalVarString) {
        String typeName = type.getName();
        while (typeName.contains("[")) {
            typeName = deal(typeName);
        }
        this.type = typeName;
        this.originalVarString = originalVarString;
    }

    public CastedVar(JGeneratable type, String originalVarString) {
        this.type = type.getName();
        this.originalVarString = originalVarString;
    }


    @Override
    public Class<?> getType() {
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

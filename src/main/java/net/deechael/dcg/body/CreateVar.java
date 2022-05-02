package net.deechael.dcg.body;

public final class CreateVar implements Operation {

    private final Class<?> type;
    private final String varName;
    private final String body;

    public CreateVar(Class<?> type, String varName, String body) {
        this.type = type;
        this.varName = varName;
        this.body = body;
    }

    @Override
    public String getString() {
        String typeName = type.getName();
        while (typeName.contains("[")) {
            typeName = deal(typeName);
        }
        return typeName + " " + varName + " = " + "(" + body + ");";
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

package net.deechael.dcg.body;

import java.util.Arrays;
import java.util.Iterator;

public final class CreateVar implements Operation {

    private final Class<?> type;
    private final String varName;
    private final String body;

    private final String[] types;

    public CreateVar(Class<?> type, String varName, String body) {
        this.type = type;
        this.varName = varName;
        this.body = body;
        this.types = null;
    }

    public CreateVar(Class<?> type, String[] types, String varName, String body) {
        this.type = type;
        this.varName = varName;
        this.body = body;
        this.types = types;
    }

    @Override
    public String getString() {
        String typeName = type.getName();
        while (typeName.contains("[")) {
            typeName = deal(typeName);
        }
        if (types != null) {
            Iterator<String> iterator = Arrays.stream(types).iterator();
            StringBuilder innerTypes = new StringBuilder();
            while (iterator.hasNext()) {
                innerTypes.append(innerTypes);
                if (iterator.hasNext()) {
                    innerTypes.append(", ");
                }
            }
            return typeName + "<" + innerTypes + "> " + varName + " = " + "(" + body + ");";
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

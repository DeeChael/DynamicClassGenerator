package net.deechael.dcg.body;

import net.deechael.dcg.JType;

import java.util.Arrays;
import java.util.Iterator;

public final class CreateVar implements Operation {

    private final String type;
    private final String varName;
    private final String body;

    public CreateVar(JType type, String varName, String body) {
        this.type = type.typeString();
        this.varName = varName;
        this.body = body;
    }

    @Override
    public String getString() {
        String typeName = type;
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

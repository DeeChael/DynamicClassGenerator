package net.deechael.dcg.items;

import net.deechael.dcg.JType;

import java.util.Arrays;

final class InitializedContentArrayVar implements Var {

    private final String type;
    private final Var[] vars;

    public InitializedContentArrayVar(JType type, Var... vars) {
        this.type = type.typeString();
        this.vars = vars;
    }

    @Override
    public JType getType() {
        throw new RuntimeException("No type");
    }

    @Override
    public String getName() {
        throw new RuntimeException("No name");
    }

    @Override
    public String varString() {
        String typeName = type;
        while (typeName.contains("[")) {
            typeName = deal(typeName);
        }
        String contentString = Arrays.toString(Arrays.stream(this.vars).map(Var::varString).toArray(String[]::new));
        return "new " + typeName + "[] {" + contentString.substring(1, contentString.length() - 1) + "}";
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

package net.deechael.dcg.items;

import net.deechael.dcg.JType;

final class FieldDirectVar implements Var {

    private final String fieldName;

    public FieldDirectVar(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public JType getType() {
        throw new RuntimeException("This var doesn't have type!");
    }

    @Override
    public String getName() {
        throw new RuntimeException("This var doesn't have name!");
    }

    @Override
    public String varString() {
        return fieldName;
    }

}

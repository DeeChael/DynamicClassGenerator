package net.deechael.dcg.items;

final class FieldVar implements Var {

    private final String varName;
    private final String fieldName;

    public FieldVar(String varName, String fieldName) {
        this.varName = varName;
        this.fieldName = fieldName;
    }

    @Override
    public Class<?> getType() {
        throw new RuntimeException("This var doesn't have type!");
    }

    @Override
    public String getName() {
        throw new RuntimeException("This var doesn't have name!");
    }

    @Override
    public String varString() {
        return varName + "." + fieldName;
    }

}

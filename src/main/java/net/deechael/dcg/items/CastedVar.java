package net.deechael.dcg.items;

final class CastedVar implements Var {

    private final Class<?> type;
    private final String originalVarString;

    public CastedVar(Class<?> type, String originalVarString) {
        this.type = type;
        this.originalVarString = originalVarString;
    }

    @Override
    public Class<?> getType() {
        return this.type;
    }

    @Override
    public String getName() {
        throw new RuntimeException("This var doesn't have name!");
    }

    @Override
    public String varString() {
        return "((" + type.getName() + ") (" + originalVarString + "))";
    }

}

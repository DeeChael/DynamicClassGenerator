package net.deechael.library.dcg.dynamic.items;

class CastingVar extends Var {

    private final Class<?> type;
    private final String originalVarString;

    public CastingVar(Class<?> type, String originalVarString) {
        super(type, null);
        this.type = type;
        this.originalVarString = originalVarString;
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

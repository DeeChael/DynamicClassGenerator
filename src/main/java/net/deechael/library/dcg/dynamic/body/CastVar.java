package net.deechael.library.dcg.dynamic.body;

public class CastVar implements Operation {

    private final Class<?> type;
    private final String varName;
    private final String originalVarString;

    public CastVar(Class<?> type, String varName, String originalVarString) {
        this.type = type;
        this.varName = varName;
        this.originalVarString = originalVarString;
    }

    @Override
    public String getString() {
        return type.getName() + " " + varName + " = " + "((" + type.getName() + ") (" + originalVarString + "));";
    }

}

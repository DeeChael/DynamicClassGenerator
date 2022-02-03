package net.deechael.library.dcg.dynamic.body;

public class BooleanVarCheck implements Requirement {

    private final String varName;

    public BooleanVarCheck(String varName) {
        this.varName = varName;
    }

    @Override
    public String getString() {
        return varName;
    }

}

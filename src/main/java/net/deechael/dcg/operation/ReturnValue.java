package net.deechael.dcg.operation;

public final class ReturnValue implements Operation {

    private final String varName;

    public ReturnValue(String varName) {
        this.varName = varName;
    }

    @Override
    public String getString() {
        return "return (" + varName + ");";
    }

}

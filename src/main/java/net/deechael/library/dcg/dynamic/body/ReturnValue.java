package net.deechael.library.dcg.dynamic.body;

public class ReturnValue implements Operation {

    private final String varName;

    public ReturnValue(String varName) {
        this.varName = varName;
    }

    @Override
    public String getString() {
        return "return (" + varName + ");";
    }

}

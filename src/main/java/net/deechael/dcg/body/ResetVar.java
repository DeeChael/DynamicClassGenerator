package net.deechael.dcg.body;

public final class ResetVar implements Operation {

    private final String varName;
    private final String body;

    public ResetVar(String varName, String body) {
        this.varName = varName;
        this.body = body;
    }

    @Override
    public String getString() {
        return varName + " = " + "(" + body + ");";
    }


}

package net.deechael.dcg.operation;

public final class ContinueToLabel implements Operation {

    private final String labelName;

    public ContinueToLabel(String labelName) {
        this.labelName = labelName;
    }

    @Override
    public String getString() {
        return "continue " + labelName + ";";
    }

}

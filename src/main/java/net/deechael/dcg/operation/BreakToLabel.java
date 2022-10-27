package net.deechael.dcg.operation;

public final class BreakToLabel implements Operation {

    private final String labelName;

    public BreakToLabel(String labelName) {
        this.labelName = labelName;
    }

    @Override
    public String getString() {
        return "break " + labelName + ";";
    }

}

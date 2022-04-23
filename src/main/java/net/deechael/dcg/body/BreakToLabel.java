package net.deechael.dcg.body;

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

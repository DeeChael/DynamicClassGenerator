package net.deechael.library.dcg.dynamic.body;

public class BreakToLabel implements Operation {

    private final String labelName;

    public BreakToLabel(String labelName) {
        this.labelName = labelName;
    }

    @Override
    public String getString() {
        return "break " + labelName + ";";
    }
}

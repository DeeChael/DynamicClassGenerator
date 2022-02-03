package net.deechael.library.dcg.dynamic.body;

import net.deechael.library.dcg.dynamic.JExecutable;

public class IfOnly implements Operation {

    private final Requirement requirement;
    private final JExecutable ifExecuting;

    public IfOnly(Requirement requirement, JExecutable ifExecuting) {
        this.requirement = requirement;
        this.ifExecuting = ifExecuting;
    }

    @Override
    public String getString() {
        if (requirement instanceof UsingMethod) {
            String requirementString = requirement.getString();
            return "if (" + requirementString.substring(0, requirementString.length() - 1) + ") {\n"
                    + ifExecuting.getString() + "\n"
                    + "}\n";
        }
        return "if (" + requirement.getString() + ") {\n"
                + ifExecuting.getString() + "\n"
                + "}";
    }

}

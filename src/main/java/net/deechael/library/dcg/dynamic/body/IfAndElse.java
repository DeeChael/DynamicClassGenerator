package net.deechael.library.dcg.dynamic.body;

import net.deechael.library.dcg.dynamic.JExecutable;

public class IfAndElse implements Operation {

    private final Requirement requirement;
    private final JExecutable ifExecuting;
    private final JExecutable elseExecuting;

    public IfAndElse(Requirement requirement, JExecutable ifExecuting, JExecutable elseExecuting) {
        this.requirement = requirement;
        this.ifExecuting = ifExecuting;
        this.elseExecuting = elseExecuting;
    }

    @Override
    public String getString() {
        return "if (" + requirement.getString() + ") {\n"
                + ifExecuting.getString() + "\n"
                + "} else {\n"
                + elseExecuting.getString() + "\n"
                + "}\n";
    }

}

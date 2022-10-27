package net.deechael.dcg.operation;

import net.deechael.dcg.JExecutable;
import net.deechael.dcg.Requirement;

public final class WhileLoop implements Operation {

    private final Requirement requirement;
    private final JExecutable whileExecuting;

    public WhileLoop(Requirement requirement, JExecutable whileExecuting) {
        this.requirement = requirement;
        this.whileExecuting = whileExecuting;
    }

    @Override
    public String getString() {
        return "while (" + requirement.getString() + ") {\n" +
                whileExecuting.getString() +
                "\n}\n";
    }

}

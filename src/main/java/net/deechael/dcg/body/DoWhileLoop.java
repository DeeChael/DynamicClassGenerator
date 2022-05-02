package net.deechael.dcg.body;

import net.deechael.dcg.JExecutable;
import net.deechael.dcg.Requirement;

public final class DoWhileLoop implements Operation {

    private final JExecutable doExecuting;

    private final Requirement requirement;

    public DoWhileLoop(JExecutable doExecuting, Requirement requirement) {
        this.doExecuting = doExecuting;
        this.requirement = requirement;
    }

    @Override
    public String getString() {
        return "do {\n" + doExecuting.getString() +
                "\n} while (" + requirement.getString() + ");";
    }

}

package net.deechael.library.dcg.dynamic.body;

import net.deechael.library.dcg.dynamic.JExecutable;
import net.deechael.library.dcg.dynamic.Requirement;

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

package net.deechael.dcg.operation;

import net.deechael.dcg.JExecutable;
import net.deechael.dcg.JType;
import net.deechael.dcg.Requirement;
import net.deechael.dcg.Var;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ForLoop implements Operation {

    private final String type;
    private final String varName;
    private final Var initialized;
    private final Requirement requirement;
    private final Var operation;

    private final JExecutable body;

    public ForLoop(@Nullable JType type, @Nullable String varName, @Nullable Var initialized, @Nullable Requirement requirement, @Nullable Var operation, @NotNull JExecutable body) {
        this.type = type != null ? type.typeString() : null;
        this.varName = varName;
        this.initialized = initialized;
        this.requirement = requirement;
        this.operation = operation;
        this.body = body;
    }

    @Override
    public String getString() {
        String first = "";
        if (type != null && varName != null && initialized != null) {
            first = type + " " + varName + " = " + initialized.varString();
        }
        return "for (" + first + "; " + (requirement != null ? requirement.getString() : "") + "; " + (operation != null ? operation.varString() : "") + ") {\n"
                + body.getString() + "\n"
                + "}";
    }

}

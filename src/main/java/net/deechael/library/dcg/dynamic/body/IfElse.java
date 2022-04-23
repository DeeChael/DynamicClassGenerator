package net.deechael.library.dcg.dynamic.body;

import net.deechael.library.dcg.dynamic.JExecutable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class IfElse implements Operation {

    private final Requirement ifRequirement;
    private final JExecutable ifExecuting;
    
    private final JExecutable elseExecuting;

    private final List<Map.Entry<Requirement, JExecutable>> elseifExecutings = new ArrayList<>();

    public IfElse(@NotNull Requirement ifRequirement, @NotNull JExecutable ifExecuting, @Nullable List<Map.Entry<Requirement, JExecutable>> elseifExecutings, @Nullable JExecutable elseExecuting) {
        this.ifRequirement = ifRequirement;
        this.ifExecuting = ifExecuting;
        this.elseExecuting = elseExecuting;
        if (elseifExecutings != null) {
            this.elseifExecutings.addAll(elseifExecutings);
        }
    }

    @Override
    public String getString() {
        StringBuilder base = new StringBuilder();
        base.append("if (").append(ifRequirement.getString()).append(") {\n")
                .append(ifExecuting.getString()).append("\n")
                .append("}");
        if (!elseifExecutings.isEmpty()) {
            for (Map.Entry<Requirement, JExecutable> entry : elseifExecutings) {
                base.append(" else if (").append(entry.getKey().getString()).append(") {\n")
                        .append(entry.getValue().getString()).append("\n")
                        .append("}");
            }
        }
        if (elseExecuting != null) {
            base.append(" else {\n")
                    .append(elseExecuting.getString()).append("\n")
                    .append("}");
        }
        base.append("\n");
        return base.toString();
    }

}

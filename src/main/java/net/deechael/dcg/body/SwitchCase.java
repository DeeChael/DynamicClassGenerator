package net.deechael.dcg.body;

import net.deechael.dcg.JExecutable;
import net.deechael.dcg.items.Var;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class SwitchCase implements Operation {

    private final Var toBeCased;

    private final List<Map.Entry<Var, JExecutable>> caseBodies = new ArrayList<>();

    private final JExecutable defaultCaseBody;

    public SwitchCase(@NotNull Var toBeCased, @Nullable List<Map.Entry<Var, JExecutable>> caseBodies, @Nullable JExecutable defaultCaseBody) {
        this.toBeCased = toBeCased;
        if (caseBodies != null) {
            this.caseBodies.addAll(caseBodies);
        }
        this.defaultCaseBody = defaultCaseBody;
    }

    @Override
    public String getString() {
        StringBuilder base = new StringBuilder();
        base.append("switch (").append(toBeCased.varString()).append(") {\n");
        for (Map.Entry<Var, JExecutable> entry : caseBodies) {
            base.append("case ").append(entry.getKey()).append(":\n    ").append(entry.getValue().getString().replace("\n", "\n    ")).append("\n");
        }
        if (defaultCaseBody != null) {
            base.append("default:\n    ").append(this.defaultCaseBody.getString().replace("\n", "\n    ")).append("\n");
        }
        base.append("}\n");
        return base.toString();
    }

}

package net.deechael.library.dcg.dynamic.body;

import net.deechael.library.dcg.dynamic.JExecutable;
import net.deechael.useless.objs.DuObj;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class TryCatchInner implements Operation {

    private final JExecutable tryBody;
    private final List<Map.Entry<Class<? extends Throwable>[], DuObj<String, JExecutable>>> catches = new ArrayList<>();
    private final JExecutable finallyBody;

    private final String varString;

    public TryCatchInner(@NotNull JExecutable tryBody, @Nullable List<Map.Entry<Class<? extends Throwable>[], DuObj<String, JExecutable>>> catches, @Nullable JExecutable finallyBody, @NotNull String varString) {
        this.tryBody = tryBody;
        if (catches != null) {
            this.catches.addAll(catches);
        }
        this.finallyBody = finallyBody;
        this.varString = varString;
    }

    @Override
    public String getString() {
        StringBuilder base = new StringBuilder();
        base.append("try (").append(varString).append(") {\n")
                .append(tryBody.getString()).append("\n")
                .append("}");
        for (Map.Entry<Class<? extends Throwable>[], DuObj<String, JExecutable>> entry : catches) {
            Class<? extends Throwable>[] throwings = entry.getKey();
            DuObj<String, JExecutable> duObj = entry.getValue();
            base.append(" catch (");
            if (throwings.length < 1) continue;
            Iterator<Class<? extends Throwable>> iterator = Arrays.stream(throwings).iterator();
            while (iterator.hasNext()) {
                base.append(iterator.next().getName());
                if (iterator.hasNext()) {
                    base.append(" | ");
                }
            }
            base.append(" ").append(duObj.getFirst()).append(") {\n")
                    .append(duObj.getSecond().getString()).append("\n")
                    .append("}");
        }
        if (finallyBody != null) {
            base.append(" finally {\n")
                    .append(finallyBody.getString()).append("\n")
                    .append("}");
        }
        base.append("\n");
        return base.toString();
    }

}

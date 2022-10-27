package net.deechael.dcg.operation;

import net.deechael.dcg.JStringVar;
import net.deechael.dcg.JType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class CreateVar implements Operation {

    private final Map<JType, Map<String, JStringVar>> annotations;
    private final boolean isFinal;
    private final String type;
    private final String varName;
    private final String body;

    public CreateVar(Map<JType, Map<String, JStringVar>> annotations, boolean isFinal, JType type, String varName, String body) {
        this.annotations = annotations;
        this.isFinal = isFinal;
        this.type = type.typeString();
        this.varName = varName;
        this.body = body;
    }

    @Override
    public String getString() {
        StringBuilder builder = new StringBuilder();
        if (annotations != null) {
            for (Map.Entry<JType, Map<String, JStringVar>> entry : annotations.entrySet()) {
                builder.append("@").append(entry.getKey().typeString());
                if (!entry.getValue().isEmpty()) {
                    builder.append("(");
                    List<Map.Entry<String, JStringVar>> jStringVars = new ArrayList<>(entry.getValue().entrySet());
                    for (int i = 0; i < jStringVars.size(); i++) {
                        Map.Entry<String, JStringVar> subEntry = jStringVars.get(i);
                        builder.append(subEntry.getKey()).append("=").append(subEntry.getValue().varString());
                        if (i != jStringVars.size() - 1) {
                            builder.append(", ");
                        }
                    }
                    builder.append(")\n");
                } else {
                    builder.append("\n");
                }
            }
        }
        if (isFinal)
            builder.append("final ");
        builder.append(type).append(" ").append(varName).append(" = ").append("(").append(body).append(");");
        return builder.toString();
    }

}

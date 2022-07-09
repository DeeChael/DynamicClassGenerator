package net.deechael.dcg;

import net.deechael.dcg.items.Var;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class JConstructor extends JExecutableParametered {

    private final Level level;
    private final String className;
    private boolean isSuper = false;
    private boolean isThis = false;
    private String[] parentVars = new String[0];

    JConstructor(Level level, ConstructorOwnable parent) {
        this.level = level;
        this.className = parent.getSimpleName();
    }

    public void superConstructor(Var... superParameters) {
        List<String> list = new ArrayList<>();
        for (Var var : superParameters) {
            list.add(var.varString());
        }
        this.isSuper = true;
        this.isThis = false;
        parentVars = list.toArray(new String[list.size()]);
    }

    public void thisConstructor(Var... thisParameters) {
        List<String> list = new ArrayList<>();
        for (Var var : thisParameters) {
            list.add(var.varString());
        }
        this.isSuper = false;
        this.isThis = true;
        parentVars = list.toArray(new String[list.size()]);
    }

    @Override
    public String getString() {
        StringBuilder base = new StringBuilder();
        base.append(this.annotationString()).append(level.getString()).append(" ").append(className).append(" (");
        List<Map.Entry<String, String>> entrySet = new ArrayList<>(getParameters().entrySet());
        for (int i = 0; i < entrySet.size(); i++) {
            Map.Entry<String, String> entry = entrySet.get(i);
            base.append(entry.getValue()).append(" ").append(entry.getKey());
            if (i != entrySet.size() - 1) {
                base.append(", ");
            }
        }
        base.append(")");
        List<String> throwables = getThrowings();
        if (throwables.size() > 0) {
            base.append(" throws ");
            for (int i = 0; i < throwables.size(); i++) {
                base.append(throwables.get(i));
                if (i != throwables.size() - 1) {
                    base.append(", ");
                }
            }
        }
        base.append(" {\n");
        if (isSuper || isThis) {
            StringBuilder bodyBuilder = new StringBuilder();
            for (int i = 0; i < parentVars.length; i++) {
                String parameterName = parentVars[i];
                bodyBuilder.append(parameterName);
                if (i != parentVars.length - 1) {
                    bodyBuilder.append(", ");
                }
            }
            base.append(isThis ? "this(" : "super(")
                    .append(bodyBuilder)
                    .append(");\n");
        }
        this.getOperations().forEach(operation -> base.append(operation.getString()).append("\n"));
        base.append("}");
        return base.toString();
    }

    /*
    public String toString() {
        StringBuilder parametersString = new StringBuilder();
        if (!parameters.isEmpty()) {
            List<Map.Entry<String, Class<?>>> entries = new ArrayList<>(parameters.entrySet());
            for (int i = 0; i < entries.size(); i++) {
                Map.Entry<String, Class<?>> entry = entries.get(i);
                parametersString.append("{" + "\"type\": \"").append(entry.getValue().getName()).append("\"").append("\"name\": \"").append(entry.getKey()).append("\"").append("}");
                if (i < entries.size() - 1) {
                    parametersString.append(",");
                }
            }
        }
        return "JConstructor: {" +
                "\"parameters\": [" +
                parametersString +
                "]" +
                "}";
    }
    */

    public String getClassName() {
        return className;
    }

}

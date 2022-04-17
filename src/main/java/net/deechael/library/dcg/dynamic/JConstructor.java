package net.deechael.library.dcg.dynamic;

import net.deechael.library.dcg.dynamic.body.Operation;
import net.deechael.library.dcg.dynamic.items.Var;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class JConstructor extends JExecutableParametered {

    private final Level level;
    private final String className;
    private boolean isSuper = false;
    private boolean isThis = false;
    private String[] parentVars = new String[0];

    JConstructor(Level level, JClass parent) {
        super(parent);
        this.level = level;
        this.className = parent.getSimpleName();
    }

    public void superConstructor(Var... superParameters) {
        List<String> list = new ArrayList<>();
        for (Var var : superParameters) {
            if (!containsParameter(var)) {
                throw new RuntimeException("The parameters not exists! Type: " + var.getType().getName() + "; Parameter Name: " + var.getName());
            }
            list.add(var.varString());
        }
        this.isSuper = true;
        this.isThis = false;
        parentVars = list.toArray(new String[list.size()]);
    }

    public void thisConstructor(Var... thisParameters) {
        List<String> list = new ArrayList<>();
        for (Var var : thisParameters) {
            if (!containsParameter(var)) {
                throw new RuntimeException("The parameters not exists! Type: " + var.getType().getName() + "; Parameter Name: " + var.getName());
            }
            list.add(var.varString());
        }
        this.isSuper = false;
        this.isThis = true;
        parentVars = list.toArray(new String[list.size()]);
    }

    @Override
    public String getString() {
        StringBuilder base = new StringBuilder();
        Map<Class<?>, Map<String, JStringVar>> map = getAnnotations();
        if (!map.isEmpty()) {
            for (Map.Entry<Class<?>, Map<String, JStringVar>> entry : map.entrySet()) {
                base.append("@").append(entry.getKey().getName());
                if (!entry.getValue().isEmpty()) {
                    base.append("(");
                    List<Map.Entry<String, JStringVar>> jStringVars = new ArrayList<>(entry.getValue().entrySet());
                    for (int i = 0; i < jStringVars.size(); i++) {
                        Map.Entry<String, JStringVar> subEntry = jStringVars.get(i);
                        base.append(subEntry.getKey()).append("=").append(subEntry.getValue().varString());
                        if (i != jStringVars.size() - 1) {
                            base.append(", ");
                        }
                    }
                    base.append(")\n");
                } else {
                    base.append("\n");
                }
            }
        }
        base.append(level.getString()).append(" ");
        base.append(className).append(" (");
        List<Map.Entry<String, Class<?>>> entrySet = new ArrayList<>(getParameters().entrySet());
        for (int i = 0; i < entrySet.size(); i++) {
            Map.Entry<String, Class<?>> entry = entrySet.get(i);
            base.append(entry.getValue().getName()).append(" ").append(entry.getKey());
            if (i != entrySet.size() - 1) {
                base.append(", ");
            }
        }
        base.append(") ");
        List<Class<?>> throwables = getThrowings();
        if (throwables.size() > 0) {
            base.append("throws ");
            for (int i = 0; i < throwables.size(); i++) {
                base.append(throwables.get(i).getName());
                if (i != throwables.size() - 1) {
                    base.append(", ");
                }
            }
            base.append(" ");
        }
        base.append("{\n");
        if (isSuper || isThis) {
            StringBuilder bodyBuilder = new StringBuilder("");
            for (int i = 0; i < parentVars.length; i++) {
                String parameterName = parentVars[i];
                bodyBuilder.append(parameterName);
                if (i != parentVars.length - 1) {
                    bodyBuilder.append(", ");
                }
            }
            if (isThis) {
                base.append("this(");
            } else {
                base.append("super(");
            }
            base.append(bodyBuilder).append(");\n");
        }
        for (Operation operation : this.getOperations()) {
            base.append(operation.getString()).append("\n");
        }
        base.append("}");
        return base.toString();
    }

    @Override
    public void addAnnotation(Class<?> annotation, Map<String, JStringVar> values) {
        if (!annotation.isAnnotation()) throw new RuntimeException("The class is not an annotation!");
        Target target = annotation.getAnnotation(Target.class);
        if (target != null) {
            boolean hasConstructor = false;
            for (ElementType elementType : target.value()) {
                if (elementType == ElementType.CONSTRUCTOR) {
                    hasConstructor = true;
                    break;
                }
            }
            if (!hasConstructor) throw new RuntimeException("This annotation is not for constructor!");
        }
        super.addAnnotation(annotation, values);
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

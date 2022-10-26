package net.deechael.dcg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class JInterfaceMethod implements JObject, InterfaceMethod {

    private final String returnType;

    private final String name;

    private final Map<String, Class<?>> parameters = new HashMap<>();
    private final List<Class<?>> throwings = new ArrayList<>();
    Map<JType, Map<String, JStringVar>> annotations = new HashMap<>();

    public JInterfaceMethod(String methodName) {
        this(JType.classType(void.class), methodName);
    }

    public JInterfaceMethod(JType returnType, String methodName) {
        this.returnType = returnType.typeString();
        this.name = methodName;
    }

    @SafeVarargs
    public final void throwing(Class<? extends Throwable>... throwables) {
        if (throwables.length == 0) return;
        for (Class<? extends Throwable> throwable : throwables) {
            if (!throwings.contains(throwable)) {
                throwings.add(throwable);
            }
        }
    }

    private Map<String, Class<?>> getParameters() {
        return parameters;
    }

    public List<Class<?>> getThrowings() {
        return new ArrayList<>(throwings);
    }

    private List<Class<?>> getRequirementTypes() {
        return new ArrayList<>(parameters.values());
    }

    @Override
    public String getString() {
        StringBuilder base = new StringBuilder();
        base.append(this.annotationString(getAnnotations())).append("\n").append(this.returnType).append(" ").append(this.name).append("(");
        List<Map.Entry<String, Class<?>>> entries = new ArrayList<>(this.getParameters().entrySet());
        for (int i = 0; i < entries.size(); i++) {
            Map.Entry<String, Class<?>> entry = entries.get(i);
            base.append(entry.getValue().getName()).append(" ").append(entry.getKey());
            if (i != entries.size() - 1) {
                base.append(", ");
            }
        }
        base.append(")");
        List<Class<?>> throwables = getThrowings();
        if (throwables.size() > 0) {
            base.append(" throws ");
            for (int i = 0; i < throwables.size(); i++) {
                base.append(throwables.get(i).getName());
                if (i != throwables.size() - 1) {
                    base.append(", ");
                }
            }
        }
        base.append(";");
        return base.toString();
    }

    @Override
    public void addAnnotation(JType annotation, Map<String, JStringVar> values) {
        getAnnotations().put(annotation, values);
    }

    @Override
    public Map<JType, Map<String, JStringVar>> getAnnotations() {
        return annotations;
    }


}

package net.deechael.dcg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class JNativeMethod implements ClassMethod, NonStructureMethod, JObject {

    private final String returnType;

    private final String name;

    private final Map<String, JType> parameters = new HashMap<>();
    private final List<Class<?>> throwings = new ArrayList<>();
    private final Level level;
    private final boolean isFinal;
    private final boolean isStatic;
    Map<JType, Map<String, JStringVar>> annotations = new HashMap<>();

    public JNativeMethod(Level level, String methodName, boolean isFinal, boolean isStatic) {
        this(level, JType.classType(void.class), methodName, isFinal, isStatic);
    }

    public JNativeMethod(Level level, JType returnType, String methodName, boolean isFinal, boolean isStatic) {
        this.returnType = returnType.typeString();
        this.name = methodName;
        this.level = level;
        this.isFinal = isFinal;
        this.isStatic = isStatic;
    }

    private String deal(String typeName) {
        if (typeName.startsWith("[L")) {
            return typeName.substring(2) + "[]";
        } else if (typeName.startsWith("[")) {
            return typeName.substring(1) + "[]";
        }
        return typeName;
    }


    public void throwing(Class<? extends Throwable>... throwables) {
        if (throwables.length == 0) return;
        for (Class<? extends Throwable> throwable : throwables) {
            if (!throwings.contains(throwable)) {
                throwings.add(throwable);
            }
        }
    }

    public Var addParameter(JType clazz, String parameterName) {
        if (parameterName == null) return null;
        parameterName = "jparam_" + parameterName;
        this.parameters.put(parameterName, clazz);
        return Var.referringVar(clazz, parameterName);
    }

    public List<Class<?>> getThrowings() {
        return new ArrayList<>(throwings);
    }

    @Override
    public String getString() {
        StringBuilder base = new StringBuilder();
        base.append(this.annotationString(getAnnotations())).append("\n").append(this.level.getString());
        if (isFinal) {
            base.append(" final");
        }
        if (isStatic) {
            base.append(" static");
        }
        base.append(" native ").append(this.returnType).append(" ").append(this.name).append("(");
        List<Map.Entry<String, JType>> entries = new ArrayList<>(this.parameters.entrySet());
        for (int i = 0; i < entries.size(); i++) {
            Map.Entry<String, JType> entry = entries.get(i);
            base.append(entry.getValue().typeString()).append(" ").append(entry.getKey());
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

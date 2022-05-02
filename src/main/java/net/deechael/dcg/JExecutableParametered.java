package net.deechael.dcg;

import net.deechael.dcg.items.Var;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class JExecutableParametered extends JExecutable {

    private final Map<String, String> parameters = new HashMap<>();

    private final List<String> throwings = new ArrayList<>();

    private String deal(String typeName) {
        if (typeName.startsWith("[L")) {
            return typeName.substring(2) + "[]";
        } else if (typeName.startsWith("[")) {
            return typeName.substring(1) + "[]";
        }
        return typeName;
    }

    public Var addParameter(Class<?> clazz, String parameterName) {
        if (parameterName == null) return null;
        parameterName = "jparam_" + parameterName;
        String className = clazz.getName();
        while (className.contains("[")) {
            className = deal(className);
        }
        this.parameters.put(parameterName, className);
        return Var.referringVar(clazz, parameterName);
    }

    public Var addParameter(JGeneratable clazz, String parameterName) {
        if (parameterName == null) return null;
        parameterName = "jparam_" + parameterName;
        this.parameters.put(parameterName, clazz.getName());
        return Var.referringVar(clazz, parameterName);
    }

    protected Map<String, String> getParameters() {
        return parameters;
    }

    public void throwing(Class<? extends Throwable>... throwables) {
        if (throwables.length == 0) return;
        for (Class<? extends Throwable> throwable : throwables) {
            if (!throwings.contains(throwable.getName())) {
                throwings.add(throwable.getName());
            }
        }
    }

    public void throwing(JClass... throwables) {
        if (throwables.length == 0) return;
        for (JClass throwable : throwables) {
            if (!throwings.contains(throwable.getName())) {
                throwings.add(throwable.getName());
            }
        }
    }

    public List<String> getThrowings() {
        return new ArrayList<>(throwings);
    }

}

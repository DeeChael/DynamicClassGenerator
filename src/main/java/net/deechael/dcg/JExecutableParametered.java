package net.deechael.dcg;

import net.deechael.dcg.items.Var;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class JExecutableParametered extends JExecutable {

    protected final Map<String, String> parameters = new HashMap<>();
    protected final List<String> finalledParameters = new ArrayList<>();
    protected final Map<String, Map<JType, Map<String, JStringVar>>> annotatedParameters = new HashMap<>();

    private final List<String> throwings = new ArrayList<>();

    public Var addParameter(JType clazz, String parameterName) {
        if (parameterName == null) return null;
        parameterName = "jparam_" + parameterName;
        this.parameters.put(parameterName, clazz.typeString());
        return Var.referringVar(clazz, parameterName);
    }

    public Var addParameter(JType clazz, String parameterName, boolean isFinal) {
        if (parameterName == null) return null;
        parameterName = "jparam_" + parameterName;
        this.parameters.put(parameterName, clazz.typeString());
        if (isFinal)
            this.finalledParameters.add(parameterName);
        return Var.referringVar(clazz, parameterName);
    }

    public Var addParameter(JType clazz, String parameterName, Map<JType, Map<String, JStringVar>> annotations) {
        if (parameterName == null) return null;
        parameterName = "jparam_" + parameterName;
        this.parameters.put(parameterName, clazz.typeString());
        this.annotatedParameters.put(parameterName, annotations);
        return Var.referringVar(clazz, parameterName);
    }

    public Var addParameter(JType clazz, String parameterName, boolean isFinal, Map<JType, Map<String, JStringVar>> annotations) {
        if (parameterName == null) return null;
        parameterName = "jparam_" + parameterName;
        this.parameters.put(parameterName, clazz.typeString());
        if (isFinal)
            this.finalledParameters.add(parameterName);
        this.annotatedParameters.put(parameterName, annotations);
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

    @Override
    public String getString() {
        return null;
    }

}

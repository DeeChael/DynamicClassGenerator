package net.deechael.dcg;

import net.deechael.dcg.items.Var;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class JExecutableParametered extends JExecutable {

    private final Map<String, Class<?>> parameters = new HashMap<>();

    private final List<Class<?>> throwings = new ArrayList<>();

    public Var addParameter(Class<?> clazz, String parameterName) {
        if (parameterName == null) return null;
        parameterName = "jparam_" + parameterName;
        return new Var(clazz, parameterName);
    }

    protected Map<String, Class<?>> getParameters() {
        return parameters;
    }

    public void throwing(Class<? extends Throwable>... throwables) {
        if (throwables.length == 0) return;
        for (Class<? extends Throwable> throwable : throwables) {
            if (!throwings.contains(throwable)) {
                throwings.add(throwable);
            }
        }
    }

    public List<Class<?>> getThrowings() {
        return new ArrayList<>(throwings);
    }

    @Override
    protected List<Class<?>> getRequirementTypes() {
        List<Class<?>> list = super.getRequirementTypes();
        list.addAll(parameters.values());
        return list;
    }

}

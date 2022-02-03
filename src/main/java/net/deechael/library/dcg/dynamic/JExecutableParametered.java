package net.deechael.library.dcg.dynamic;

import net.deechael.library.dcg.dynamic.items.UsingMethodAsVar;
import net.deechael.library.dcg.dynamic.items.UsingStaticMethodAsVar;
import net.deechael.library.dcg.dynamic.items.Var;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public abstract class JExecutableParametered extends JExecutable {

    private final Map<String, Class<?>> parameters = new HashMap<>();

    private final List<Class<?>> throwings = new ArrayList<>();

    protected JExecutableParametered(JClass parent) {
        super(parent);
    }

    public Var addParameter(Class<?> clazz, String parameterName) {
        if (parameterName == null) return null;
        if (!Pattern.matches("^[A-Za-z_$]+[A-Za-z_$\\d]+$", parameterName)) return null;
        parameterName = "jparam_" + parameterName;
        if (parameters.containsKey(parameterName)) return vars.get(parameterName);
        parameters.put(parameterName, clazz);
        Var var = new Var(clazz, parameterName);
        vars.put(parameterName, var);
        return var;
    }

    protected Map<String, Class<?>> getParameters() {
        return parameters;
    }

    public boolean containsParameter(Var var) {
        return parameters.containsKey(var.getName()) && vars.containsKey(var.getName()) && parameters.get(var.getName()) == var.getType();
    }

    protected boolean isVarExists(Var var) {
        if (var instanceof UsingMethodAsVar || var instanceof UsingStaticMethodAsVar) {
            return true;
        }
        if (var instanceof JStringVar) {
            return true;
        }
        if (var instanceof JField) {
            if (((JField) var).parent != this.parent) {
                throw new RuntimeException("The field not exists in the class!");
            }
            return true;
        }
        return vars.containsValue(var) || (vars.containsKey(var.getName()) && vars.get(var.getName()).getType().getName().equals(var.getType().getName())) || containsParameter(var);
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

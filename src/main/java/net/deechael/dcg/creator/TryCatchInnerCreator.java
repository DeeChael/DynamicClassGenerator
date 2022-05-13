package net.deechael.dcg.creator;

import net.deechael.dcg.JExecutable;
import net.deechael.dcg.JType;
import net.deechael.dcg.body.TryCatchInner;
import net.deechael.dcg.items.Var;
import net.deechael.useless.function.parameters.DuParameter;
import net.deechael.useless.function.parameters.Parameter;
import net.deechael.useless.objs.DuObj;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class TryCatchInnerCreator {

    private final JExecutable owner;

    private final JExecutable tryBody;

    private final List<Map.Entry<Class<? extends Throwable>[], DuObj<String, JExecutable>>> catches = new ArrayList<>();

    private JExecutable finallyBody;

    private boolean editable = true;

    private final String varType;
    private final String varName;
    private final Var varValue;

    private String deal(String typeName) {
        if (typeName.startsWith("[L")) {
            return typeName.substring(2) + "[]";
        } else if (typeName.startsWith("[")) {
            return typeName.substring(1) + "[]";
        }
        return typeName;
    }

    public TryCatchInnerCreator(JExecutable executable, DuParameter<JExecutable, Var> tryExecuting, JType clazz, String varName, Var var) {
        varName = "jvar_" + varName;
        this.owner = executable;
        JExecutable.JExecutable4InnerStructure tryBody = new JExecutable.JExecutable4InnerStructure();
        tryExecuting.apply(tryBody, Var.referringVar(clazz, varName));
        this.tryBody = tryBody;
        this.varType = clazz.typeString();
        this.varName = varName;
        this.varValue = var;
    }

    public TryCatchInnerCreator catchIt(String throwableObjectName, DuParameter<JExecutable, Var> catchExecuting, Class<? extends Throwable>... throwings) {
        if (!editable) return this;
        if (throwings.length == 0) return this;
        throwableObjectName = "jthrowable_" + throwableObjectName;
        JExecutable.JExecutable4InnerStructure catchBody = new JExecutable.JExecutable4InnerStructure();
        Var var = Var.referringVar(null, throwableObjectName);
        catchExecuting.apply(catchBody, var);
        this.catches.add(new AbstractMap.SimpleEntry<>(throwings, new DuObj<>(throwableObjectName, catchBody)));
        return this;
    }

    public void setFinally(Parameter<JExecutable> finallyExecuting) {
        if (!editable) return;
        JExecutable.JExecutable4InnerStructure finallyBody = new JExecutable.JExecutable4InnerStructure();
        finallyExecuting.apply(finallyBody);
        this.finallyBody = finallyBody;
        done();
    }

    public void done() {
        if (!editable) return;
        this.editable = false;
        this.owner.addOperation(new TryCatchInner(tryBody, catches, finallyBody, varType + " " + varName + " = " + varValue.varString()));
    }

}

package net.deechael.library.dcg.dynamic.creator;

import net.deechael.library.dcg.dynamic.JExecutable;
import net.deechael.library.dcg.dynamic.body.TryCatch;
import net.deechael.library.dcg.dynamic.body.TryCatchInner;
import net.deechael.library.dcg.dynamic.items.Var;
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


    private final Class<?> varType;
    private final String varName;
    private final Var varValue;

    public TryCatchInnerCreator(JExecutable executable, DuParameter<JExecutable, Var> tryExecuting, Class<?> clazz, String varName, Var var) {
        varName = "jvar_" + varName;
        this.owner = executable;
        JExecutable.JExecutable4InnerStructure tryBody = new JExecutable.JExecutable4InnerStructure();
        tryExecuting.apply(tryBody, new Var(clazz, varName));
        this.tryBody = tryBody;
        this.varType = clazz;
        this.varName = varName;
        this.varValue = var;
    }

    public TryCatchInnerCreator catchIt(String throwableObjectName, DuParameter<JExecutable, Var> catchExecuting, Class<? extends Throwable>... throwings) {
        if (!editable) return this;
        throwableObjectName = "jthrowable_" + throwableObjectName;
        JExecutable.JExecutable4InnerStructure catchBody = new JExecutable.JExecutable4InnerStructure();
        Var var = new Var(null, throwableObjectName);
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
        this.owner.addOperation(new TryCatchInner(tryBody, catches, finallyBody, varType.getName() + " " + varName + " = " + varValue.varString()));
    }

}

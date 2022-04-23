package net.deechael.dcg.creator;

import net.deechael.dcg.JExecutable;
import net.deechael.dcg.body.TryCatch;
import net.deechael.dcg.items.Var;
import net.deechael.useless.function.parameters.DuParameter;
import net.deechael.useless.function.parameters.Parameter;
import net.deechael.useless.objs.DuObj;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class TryCatchCreator {

    private final JExecutable owner;

    private final JExecutable tryBody;

    private final List<Map.Entry<Class<? extends Throwable>[], DuObj<String, JExecutable>>> catches = new ArrayList<>();

    private JExecutable finallyBody;

    private boolean editable = true;

    public TryCatchCreator(JExecutable executable, Parameter<JExecutable> tryExecuting) {
        this.owner = executable;
        JExecutable.JExecutable4InnerStructure tryBody = new JExecutable.JExecutable4InnerStructure();
        tryExecuting.apply(tryBody);
        this.tryBody = tryBody;
    }

    public TryCatchCreator catchIt(String throwableObjectName, DuParameter<JExecutable, Var> catchExecuting, Class<? extends Throwable>... throwings) {
        if (!editable) return this;
        if (throwings.length == 0) return this;
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
        this.owner.addOperation(new TryCatch(tryBody, catches, finallyBody));
    }

}

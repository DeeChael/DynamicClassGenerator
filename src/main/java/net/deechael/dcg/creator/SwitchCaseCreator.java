package net.deechael.dcg.creator;

import net.deechael.dcg.JExecutable;
import net.deechael.dcg.Var;
import net.deechael.dcg.operation.SwitchCase;
import net.deechael.useless.function.parameters.Parameter;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class SwitchCaseCreator {

    private final JExecutable owner;

    private final Var toBeCased;
    private final List<Map.Entry<Var[], JExecutable>> caseBodies = new ArrayList<>();
    private boolean editable = true;
    private JExecutable defaultCaseBody;

    public SwitchCaseCreator(JExecutable executable, Var toBeCased) {
        this.owner = executable;
        this.toBeCased = toBeCased;
    }

    public SwitchCaseCreator caseVar(Var var, Parameter<JExecutable.JExecutable4Switch> parameter) {
        return this.caseVar(new Var[]{var}, parameter);
    }


    public SwitchCaseCreator caseVar(Var[] var, Parameter<JExecutable.JExecutable4Switch> parameter) {
        if (!editable) return this;
        JExecutable.JExecutable4Switch caseBody = new JExecutable.JExecutable4Switch();
        parameter.apply(caseBody);
        caseBodies.add(new AbstractMap.SimpleEntry<>(var, caseBody));
        return this;
    }

    public SwitchCaseCreator defaultCase(Parameter<JExecutable.JExecutable4Switch> parameter) {
        if (!editable) return this;
        JExecutable.JExecutable4Switch caseBody = new JExecutable.JExecutable4Switch();
        parameter.apply(caseBody);
        this.defaultCaseBody = caseBody;
        return this;
    }

    public void done() {
        if (!editable) return;
        this.owner.addOperation(new SwitchCase(toBeCased, caseBodies, defaultCaseBody));
        this.editable = false;
    }

}

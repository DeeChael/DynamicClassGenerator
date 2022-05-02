package net.deechael.dcg.creator;

import net.deechael.dcg.JExecutable;
import net.deechael.dcg.items.Var;
import net.deechael.useless.function.parameters.Parameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class SwitchCreator {

    private final JExecutable owner;

    private final Var toBeCased;

    private boolean editable = true;

    private final List<Map.Entry<Var, JExecutable>> caseBodies = new ArrayList<>();

    public SwitchCreator(JExecutable executable, Var toBeCased) {
        this.owner = executable;
        this.toBeCased = toBeCased;
    }

    public SwitchCreator caseVar(Var var, Parameter<JExecutable.JExecutable4Switch> parameter) {
        JExecutable.JExecutable4Switch caseBody = new JExecutable.JExecutable4Switch();
        parameter.apply(caseBody);

        return this;
    }

}

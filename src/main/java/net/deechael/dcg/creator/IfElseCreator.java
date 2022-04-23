package net.deechael.dcg.creator;

import net.deechael.dcg.JExecutable;
import net.deechael.dcg.body.IfElse;
import net.deechael.dcg.Requirement;
import net.deechael.useless.function.parameters.Parameter;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class IfElseCreator {

    private final JExecutable owner;

    private final Requirement ifRequirement;
    private final JExecutable ifBody;

    private JExecutable elseBody;

    private final List<Map.Entry<Requirement, JExecutable>> elseifBodies = new ArrayList<>();

    private boolean editable = true;

    public IfElseCreator(JExecutable executable, Requirement requirement, Parameter<JExecutable> ifExecuting) {
        this.owner = executable;
        JExecutable.JExecutable4InnerStructure ifBody = new JExecutable.JExecutable4InnerStructure();
        ifExecuting.apply(ifBody);
        this.ifRequirement = requirement;
        this.ifBody = ifBody;
    }

    public IfElseCreator elseif(Requirement requirement, Parameter<JExecutable> elseifExecuting) {
        if (!editable) return this;
        JExecutable.JExecutable4InnerStructure elseifBody = new JExecutable.JExecutable4InnerStructure();
        elseifExecuting.apply(elseifBody);
        this.elseifBodies.add(new AbstractMap.SimpleEntry<>(requirement, elseifBody));
        return this;
    }

    public void setElse(Parameter<JExecutable> elseExecuting) {
        if (!editable) return;
        JExecutable.JExecutable4InnerStructure elseBody = new JExecutable.JExecutable4InnerStructure();
        elseExecuting.apply(elseBody);
        this.elseBody = elseBody;
        done();
    }

    public void done() {
        if (!editable) return;
        this.editable = false;
        this.owner.addOperation(new IfElse(ifRequirement, ifBody, elseifBodies, elseBody));
    }

}

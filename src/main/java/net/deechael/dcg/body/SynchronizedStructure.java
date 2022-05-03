package net.deechael.dcg.body;

import net.deechael.dcg.JExecutable;
import net.deechael.dcg.items.Var;

public final class SynchronizedStructure implements Operation {

    private final Var var;
    private final JExecutable.JExecutable4InnerStructure executable;

    public SynchronizedStructure(Var var, JExecutable.JExecutable4InnerStructure executableStructure) {
        this.var = var;
        this.executable = executableStructure;
    }

    public String getString() {
        return "synchronized (" + var.varString() + ") {\n" + this.executable.getString() + "\n}\n";
    }

}

package net.deechael.library.dcg.dynamic.body;

import net.deechael.library.dcg.dynamic.JExecutable;

public class TryAndCatch implements Operation {

    private final Class<?> throwing;
    private final String varName;
    private final JExecutable tryExecuting;
    private final JExecutable catchExecuting;

    public TryAndCatch(Class<?> throwing, String varName, JExecutable tryExecuting, JExecutable catchExecuting) {
        this.throwing = throwing;
        this.varName = varName;
        this.tryExecuting = tryExecuting;
        this.catchExecuting = catchExecuting;
    }

    @Override
    public String getString() {
        return "try {\n"
                + tryExecuting.getString() + "\n"
                + "} catch (" + throwing.getName() + " " + varName + ") {\n"
                + catchExecuting.getString() + "\n"
                + "}\n";
    }

}

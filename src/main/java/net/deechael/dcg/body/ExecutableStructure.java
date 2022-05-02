package net.deechael.dcg.body;

import net.deechael.dcg.JExecutable;

public final class ExecutableStructure implements Operation {

    private final JExecutable.JExecutable4InnerStructure executable;

    public ExecutableStructure(JExecutable.JExecutable4InnerStructure executableStructure) {
        this.executable = executableStructure;
    }

    public String getString() {
        return "{\n" + this.executable.getString() + "\n}\n";
    }

}

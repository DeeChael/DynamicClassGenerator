package net.deechael.library.dcg.dynamic.body;

public class InstanceofCheck implements Requirement {

    private final String varName;
    private final Class<?> type;

    public InstanceofCheck(String varName, Class<?> type) {
        this.varName = varName;
        this.type = type;
    }

    @Override
    public String getString() {
        return varName + " instanceof " + type.getName();
    }

}

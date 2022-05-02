package net.deechael.dcg.items;

final class CustomVar extends Var {

    private final String varValue;

    public CustomVar(String varValue) {
        super(null, null);
        this.varValue = varValue;
    }

    @Override
    public String varString() {
        return varValue;
    }

}

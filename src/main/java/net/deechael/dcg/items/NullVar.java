package net.deechael.dcg.items;

final class NullVar extends Var {

    public NullVar() {
        super(null, null);
    }

    @Override
    public String varString() {
        return "null";
    }

}

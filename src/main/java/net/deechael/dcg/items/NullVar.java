package net.deechael.dcg.items;

class NullVar extends Var {

    public NullVar() {
        super(null, null);
    }

    @Override
    public String varString() {
        return "null";
    }

}

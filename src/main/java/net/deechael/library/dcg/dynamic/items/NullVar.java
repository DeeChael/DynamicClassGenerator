package net.deechael.library.dcg.dynamic.items;

class NullVar extends Var {

    public NullVar() {
        super(null, null);
    }

    @Override
    public String varString() {
        return "null";
    }

}

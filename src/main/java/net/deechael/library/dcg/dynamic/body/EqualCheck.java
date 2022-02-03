package net.deechael.library.dcg.dynamic.body;

import net.deechael.library.dcg.dynamic.items.Var;

public class EqualCheck implements Requirement {

    private final Var left;
    private final Var right;

    public EqualCheck(Var left, Var right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String getString() {
        return left.varString() + " == " + right.varString();
    }

}

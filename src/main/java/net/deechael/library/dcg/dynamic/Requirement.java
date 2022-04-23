package net.deechael.library.dcg.dynamic;

import net.deechael.library.dcg.dynamic.items.Var;

public interface Requirement {

    String getString();

    static Requirement isInstanceof(Var var, Class<?> clazz) {
        return () -> var.varString() + " instanceof " + clazz.getName();
    }

    static Requirement isEqual(Var var, Var another) {
        return () -> var.varString() + " == " + another.varString();
    }

    static Requirement usingMethod(Var var, String methodName, Var... parameters) {
        return () -> Var.usingMethod(var, methodName, parameters).varString();
    }

    static Requirement usingMethod(Class<?> clazz, String methodName, Var... parameters) {
        return () -> Var.usingMethod(clazz, methodName, parameters).varString();
    }

    static Requirement usingThisMethod(String methodName, Var... parameters) {
        return () -> Var.usingThisMethod(methodName, parameters).varString();
    }

    static Requirement usingSuperMethod(String methodName, Var... parameters) {
        return () -> Var.usingSuperMethod(methodName, parameters).varString();
    }

    static Requirement booleanVar(Var var) {
        return var::varString;
    }

    static Requirement not(Requirement requirement) {
        return () -> "!(" + requirement.getString() + ")";
    }

    static Requirement notEqual(Var var, Var another) {
        return () -> var.varString() + " != " + another.varString();
    }

    static Requirement notNull(Var var) {
        return notEqual(var, Var.nullVar());
    }

    static Requirement isNull(Var var) {
        return isEqual(var, Var.nullVar());
    }

    static Requirement and(Requirement one, Requirement another) {
        return () -> "(" + one.getString() + ") && (" + another.getString() + ")";
    }

    static Requirement or(Requirement one, Requirement another) {
        return () -> "(" + one.getString() + ") || (" + another.getString() + ")";
    }

    static Requirement isGreater(Var one, Var another) {
        return () -> "(" + one.varString() + ") > (" + another.varString() + ")";
    }

    static Requirement isGreaterOrEqual(Var one, Var another) {
        return () -> "(" + one.varString() + ") >= (" + another.varString() + ")";
    }

    static Requirement isSmaller(Var one, Var another) {
        return () -> "(" + one.varString() + ") < (" + another.varString() + ")";
    }

    static Requirement isSmallerOrEqual(Var one, Var another) {
        return () -> "(" + one.varString() + ") <= (" + another.varString() + ")";
    }

}

package net.deechael.dcg;

import net.deechael.dcg.items.Var;

public interface Requirement {

    static Requirement isInstanceof(Var var, JType clazz) {
        return () -> var.varString() + " instanceof " + clazz.typeString();
    }

    static Requirement isEqual(Var var, Var another) {
        return () -> var.varString() + " == " + another.varString();
    }

    static Requirement invokeMethod(Var var, String methodName, Var... parameters) {
        return () -> Var.invokeMethod(var, methodName, parameters).varString();
    }

    static Requirement invokeMethod(JType clazz, String methodName, Var... parameters) {
        return () -> Var.invokeMethod(clazz, methodName, parameters).varString();
    }

    static Requirement invokeThisMethod(String methodName, Var... parameters) {
        return () -> Var.invokeThisMethod(methodName, parameters).varString();
    }

    static Requirement invokeSuperMethod(String methodName, Var... parameters) {
        return () -> Var.invokeSuperMethod(methodName, parameters).varString();
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

    String getString();

}

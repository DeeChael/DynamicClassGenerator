package net.deechael.library.dcg.dynamic.body;

import net.deechael.library.dcg.dynamic.items.Var;

public interface Requirement {

    String getString();

    static Requirement isInstanceof(Var var, Class<?> clazz) {
        return new InstanceofCheck(var.varString(), clazz);
    }

    static Requirement isEqual(Var var, Var another) {
        return new EqualCheck(var, another);
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

}

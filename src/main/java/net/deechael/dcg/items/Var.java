package net.deechael.dcg.items;

import org.jetbrains.annotations.NotNull;

public interface Var {

    Class<?> getType();

    String getName();

    String varString();

    static Var objectsField(Var var, String fieldName) {
        return new FieldVar(var.varString(), fieldName);
    }

    static Var staticField(Class<?> clazz, String fieldName) {
        return new FieldVar(clazz.getName(), fieldName);
    }

    static Var castObject(Var originalVar, Class<?> castToClass) {
        return new CastedVar(castToClass, originalVar.varString());
    }

    static Var invokeMethod(@NotNull Var var, @NotNull String methodName, Var... arguments) {
        StringBuilder bodyBuilder = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            bodyBuilder.append(arguments[i].varString());
            if (i != arguments.length - 1) {
                bodyBuilder.append(", ");
            }
        }
        return new InvokeMethodAsVar(var.varString(), methodName, bodyBuilder.toString());
    }

    static Var invokeMethod(@NotNull Class<?> clazz, @NotNull String methodName, Var... arguments) {
        StringBuilder bodyBuilder = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            bodyBuilder.append(arguments[i].varString());
            if (i != arguments.length - 1) {
                bodyBuilder.append(", ");
            }
        }
        return new InvokeMethodAsVar(clazz.getName(), methodName, bodyBuilder.toString());
    }

    static Var invokeThisMethod(@NotNull String methodName, Var... arguments) {
        StringBuilder bodyBuilder = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            bodyBuilder.append(arguments[i].varString());
            if (i != arguments.length - 1) {
                bodyBuilder.append(", ");
            }
        }
        return new InvokeMethodAsVar("this", methodName, bodyBuilder.toString());
    }

    static Var invokeSuperMethod(@NotNull String methodName, Var... arguments) {
        StringBuilder bodyBuilder = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            bodyBuilder.append(arguments[i].varString());
            if (i != arguments.length - 1) {
                bodyBuilder.append(", ");
            }
        }
        return new InvokeMethodAsVar("super", methodName, bodyBuilder.toString());
    }

    static Var constructor(Class<?> type, Var... arguments) {
        StringBuilder bodyBuilder = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            bodyBuilder.append(arguments[i].varString());
            if (i != arguments.length - 1) {
                bodyBuilder.append(", ");
            }
        }
        return new ConstructorVar(type, bodyBuilder.toString());
    }

    static Var custom(String content) {
        return new CustomVar(content);
    }

    static Var nullVar() {
        return new CustomVar("this");
    }

    static Var referringVar(Class<?> type, String name) {
        return new ReferringVar(type, name);
    }

    static Var thisVar() {
        return new CustomVar("this");
    }

}

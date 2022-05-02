package net.deechael.dcg.items;

import org.jetbrains.annotations.NotNull;

public class Var {

    private final Class<?> type;

    private final String name;

    public Var(Class<?> type, String name) {
        this.type = type;
        this.name = name;
    }

    public Class<?> getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String varString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Var var = (Var) o;

        if (type != null ? !type.getName().equals(var.type.getName()) : var.type != null) return false;
        return name.equals(var.name);
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    public static Var objectsField(Var var, String fieldName) {
        return new FieldVar(var.varString(), fieldName);
    }

    public static Var staticField(Class<?> clazz, String fieldName) {
        return new FieldVar(clazz.getName(), fieldName);
    }

    public static Var castObject(Var originalVar, Class<?> castToClass) {
        return new CastedVar(castToClass, originalVar.varString());
    }

    public static Var invokeMethod(@NotNull Var var, @NotNull String methodName, Var... arguments) {
        StringBuilder bodyBuilder = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            bodyBuilder.append(arguments[i].varString());
            if (i != arguments.length - 1) {
                bodyBuilder.append(", ");
            }
        }
        return new InvokeMethodAsVar(var.varString(), methodName, bodyBuilder.toString());
    }

    public static Var invokeMethod(@NotNull Class<?> clazz, @NotNull String methodName, Var... arguments) {
        StringBuilder bodyBuilder = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            bodyBuilder.append(arguments[i].varString());
            if (i != arguments.length - 1) {
                bodyBuilder.append(", ");
            }
        }
        return new InvokeMethodAsVar(clazz.getName(), methodName, bodyBuilder.toString());
    }

    public static Var invokeThisMethod(@NotNull String methodName, Var... arguments) {
        StringBuilder bodyBuilder = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            bodyBuilder.append(arguments[i].varString());
            if (i != arguments.length - 1) {
                bodyBuilder.append(", ");
            }
        }
        return new InvokeMethodAsVar("this", methodName, bodyBuilder.toString());
    }

    public static Var invokeSuperMethod(@NotNull String methodName, Var... arguments) {
        StringBuilder bodyBuilder = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            bodyBuilder.append(arguments[i].varString());
            if (i != arguments.length - 1) {
                bodyBuilder.append(", ");
            }
        }
        return new InvokeMethodAsVar("super", methodName, bodyBuilder.toString());
    }

    public static Var constructor(Class<?> type, Var... arguments) {
        StringBuilder bodyBuilder = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            bodyBuilder.append(arguments[i].varString());
            if (i != arguments.length - 1) {
                bodyBuilder.append(", ");
            }
        }
        return new ConstructorVar(type, bodyBuilder.toString());
    }

    public static Var custom(String content) {
        return new CustomVar(content);
    }

    public static Var nullVar() {
        return new NullVar();
    }


}

package net.deechael.dcg.items;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

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
        return new ObjectsFieldVar(var.varString(), fieldName);
    }

    public static Var castObject(Var originalVar, Class<?> castToClass) {
        return new CastingVar(castToClass, originalVar.varString());
    }

    public static Var invokeMethod(@NotNull Var var, @NotNull String methodName, Var... arguments) {
        StringBuilder bodyBuilder = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            bodyBuilder.append(arguments[i].varString());
            if (i != arguments.length - 1) {
                bodyBuilder.append(", ");
            }
        }
        return new UsingMethodAsVar(var.varString(), methodName, bodyBuilder.toString());
    }

    public static Var invokeThisMethod(@NotNull String methodName, Var... arguments) {
        StringBuilder bodyBuilder = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            bodyBuilder.append(arguments[i].varString());
            if (i != arguments.length - 1) {
                bodyBuilder.append(", ");
            }
        }
        return new UsingMethodAsVar("this", methodName, bodyBuilder.toString());
    }

    public static Var invokeSuperMethod(@NotNull String methodName, Var... arguments) {
        StringBuilder bodyBuilder = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            bodyBuilder.append(arguments[i].varString());
            if (i != arguments.length - 1) {
                bodyBuilder.append(", ");
            }
        }
        return new UsingMethodAsVar("super", methodName, bodyBuilder.toString());
    }

    public static Var invokeMethod(@NotNull Class<?> clazz, @NotNull String methodName, Var... arguments) {
        boolean hasMethod = false;
        Method result = null;
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(methodName) && !Modifier.isStatic(method.getModifiers())) {
                hasMethod = true;
                result = method;
                break;
            }
        }
        if (!hasMethod) {
            throw new RuntimeException("Unknown method of the class " + clazz.getName() + ": " + methodName + "(...);");
        }
        StringBuilder bodyBuilder = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            bodyBuilder.append(arguments[i].varString());
            if (i != arguments.length - 1) {
                bodyBuilder.append(", ");
            }
        }
        return new UsingStaticMethodAsVar(result, bodyBuilder.toString());
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

    public static Var nullVar() {
        return new NullVar();
    }


}

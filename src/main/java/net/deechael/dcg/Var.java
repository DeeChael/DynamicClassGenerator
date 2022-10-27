package net.deechael.dcg;

import net.deechael.useless.function.parameters.DuParameter;
import org.jetbrains.annotations.NotNull;

public interface Var {

    static Var directField(Var var, String fieldName) {
        return new DefaultVars.FieldVar(var.varString(), fieldName);
    }

    static Var objectsField(Var var, String fieldName) {
        return new DefaultVars.FieldVar(var.varString(), fieldName);
    }

    static Var staticField(Class<?> clazz, String fieldName) {
        return new DefaultVars.FieldVar(clazz.getName(), fieldName);
    }

    static Var staticField(JGeneratable clazz, String fieldName) {
        return new DefaultVars.FieldVar(clazz.getName(), fieldName);
    }

    static Var castObject(Var originalVar, JType castToClass) {
        return new DefaultVars.CastedVar(castToClass, originalVar.varString());
    }

    static Var invokeMethod(@NotNull Var var, @NotNull String methodName, Var... arguments) {
        StringBuilder bodyBuilder = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            bodyBuilder.append(arguments[i].varString());
            if (i != arguments.length - 1) {
                bodyBuilder.append(", ");
            }
        }
        return new DefaultVars.InvokeMethodAsVar(var.varString().replace("$", "."), methodName, bodyBuilder.toString());
    }

    static Var invokeMethodDirectly(@NotNull String methodName, Var... arguments) {
        StringBuilder bodyBuilder = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            bodyBuilder.append(arguments[i].varString());
            if (i != arguments.length - 1) {
                bodyBuilder.append(", ");
            }
        }
        return new DefaultVars.InvokeMethodDirectlyAsVar(methodName, bodyBuilder.toString());
    }

    static Var invokeMethod(@NotNull JType clazz, @NotNull String methodName, Var... arguments) {
        StringBuilder bodyBuilder = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            bodyBuilder.append(arguments[i].varString());
            if (i != arguments.length - 1) {
                bodyBuilder.append(", ");
            }
        }
        return new DefaultVars.InvokeMethodAsVar(clazz.typeString(), methodName, bodyBuilder.toString());
    }

    static Var invokeThisMethod(@NotNull String methodName, Var... arguments) {
        StringBuilder bodyBuilder = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            bodyBuilder.append(arguments[i].varString());
            if (i != arguments.length - 1) {
                bodyBuilder.append(", ");
            }
        }
        return new DefaultVars.InvokeMethodAsVar("this", methodName, bodyBuilder.toString());
    }

    static Var invokeSuperMethod(@NotNull String methodName, Var... arguments) {
        StringBuilder bodyBuilder = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            bodyBuilder.append(arguments[i].varString());
            if (i != arguments.length - 1) {
                bodyBuilder.append(", ");
            }
        }
        return new DefaultVars.InvokeMethodAsVar("super", methodName, bodyBuilder.toString());
    }

    static Var constructor(JType type, Var... arguments) {
        StringBuilder bodyBuilder = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            bodyBuilder.append(arguments[i].varString());
            if (i != arguments.length - 1) {
                bodyBuilder.append(", ");
            }
        }
        return new DefaultVars.ConstructorVar4JGeneratable(type, bodyBuilder.toString());
    }

    static Var initializedArray(JType componentType, int length) {
        return new DefaultVars.InitializedArrayVar(componentType, length);
    }

    static Var initializedArray(JType componentType, Var... vars) {
        return new DefaultVars.InitializedContentArrayVar(componentType, vars);
    }

    static Var custom(String content) {
        return new DefaultVars.CustomVar(content);
    }

    static Var nullVar() {
        return JStringVar.nullVar();
    }

    static Var referringVar(JType type, String name) {
        return new DefaultVars.ReferringVar4JGeneratable(type, name);
    }

    static Var thisVar() {
        return new DefaultVars.CustomVar("this");
    }

    static Var add(Var var, Var another) {
        return new DefaultVars.DigitOperatedVar(var, DigitOperator.PLUS, another);
    }

    static Var subtract(Var var, Var another) {
        return new DefaultVars.DigitOperatedVar(var, DigitOperator.MINUS, another);
    }

    static Var multiply(Var var, Var another) {
        return new DefaultVars.DigitOperatedVar(var, DigitOperator.MULTIPLY, another);
    }

    static Var divide(Var var, Var another) {
        return new DefaultVars.DigitOperatedVar(var, DigitOperator.DIVIDE, another);
    }

    static Var remainder(Var var, Var another) {
        return new DefaultVars.DigitOperatedVar(var, DigitOperator.REMINDER, another);
    }

    static Var bitwiseOR(Var var, Var another) {
        return new DefaultVars.DigitOperatedVar(var, DigitOperator.BITWISE_OR, another);
    }

    static Var bitwiseAND(Var var, Var another) {
        return new DefaultVars.DigitOperatedVar(var, DigitOperator.BITWISE_AND, another);
    }

    static Var bitwiseXOR(Var var, Var another) {
        return new DefaultVars.DigitOperatedVar(var, DigitOperator.BITWISE_XOR, another);
    }

    static Var bitwiseLeftShift(Var var, Var another) {
        return new DefaultVars.DigitOperatedVar(var, DigitOperator.BITWISE_LEFT_SHIFT, another);
    }

    static Var bitwiseRightShift(Var var, Var another) {
        return new DefaultVars.DigitOperatedVar(var, DigitOperator.BITWISE_RIGHT_SHIFT, another);
    }

    static Var bitwiseRightLogicalShift(Var var, Var another) {
        return new DefaultVars.DigitOperatedVar(var, DigitOperator.BITWISE_RIGHT_LOGICAL_SHIFT, another);
    }

    static Var bitwiseNOT(Var var) {
        return new DefaultVars.DigitOperatedVar(var, DigitOperator.BITWISE_NOT, null);
    }

    static Var increaseLeft(Var var) {
        return new DefaultVars.DigitOperatedVar(null, DigitOperator.INCREASE, var);
    }

    static Var increaseRight(Var var) {
        return new DefaultVars.DigitOperatedVar(var, DigitOperator.INCREASE, null);
    }

    static Var decreaseLeft(Var var) {
        return new DefaultVars.DigitOperatedVar(null, DigitOperator.DECREASE, var);
    }

    static Var decreaseRight(Var var) {
        return new DefaultVars.DigitOperatedVar(var, DigitOperator.DECREASE, null);
    }

    static Var setValueVar(Var referringVar, Var any) {
        if (!(referringVar instanceof DefaultVars.ReferringVar || referringVar instanceof DefaultVars.ReferringVar4JGeneratable)) {
            throw new RuntimeException("Unacceptable var");
        }
        return new DefaultVars.SetValueVar(referringVar.getName(), any);
    }

    static JAnonymousClass anonymousClass(@NotNull JType type, @NotNull Var[] arguments) {
        return new JAnonymousClass(type, arguments);
    }

    static Var enumVar(@NotNull JEnum enumClass, @NotNull String enumItemName) {
        return new DefaultVars.EnumVar(enumClass, enumItemName);
    }

    static Var lambda(JType clazz) {
        return new DefaultVars.LambdaVar(clazz, "new");
    }

    static Var lambda(JType clazz, String method) {
        return new DefaultVars.LambdaVar(clazz, method);
    }

    static Var lambda(String[] parameters, DuParameter<Var[], JExecutable> executable) {
        return new DefaultVars.FunctionVar(parameters, executable);
    }

    static Var lambda(String[] parameters, Var returnValue) {
        return new DefaultVars.FunctionReturnVar(parameters, returnValue);
    }

    static Var arrayElement(Var arrayVar, int index) {
        return arrayElement(arrayVar, JStringVar.intVar(index));
    }

    static Var arrayElement(Var arrayVar, Var index) {
        return new DefaultVars.ArrayElementVar(arrayVar, index);
    }

    static Var classVar(JType type) {
        return new DefaultVars.ClassVar(type);
    }

    JType getType();

    String getName();

    String varString();

}

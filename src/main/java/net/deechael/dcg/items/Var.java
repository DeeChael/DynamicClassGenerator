package net.deechael.dcg.items;

import net.deechael.dcg.*;
import org.jetbrains.annotations.NotNull;

public interface Var {

    static Var directField(Var var, String fieldName) {
        return new FieldVar(var.varString(), fieldName);
    }

    static Var objectsField(Var var, String fieldName) {
        return new FieldVar(var.varString(), fieldName);
    }

    static Var staticField(Class<?> clazz, String fieldName) {
        return new FieldVar(clazz.getName(), fieldName);
    }

    static Var staticField(JGeneratable clazz, String fieldName) {
        return new FieldVar(clazz.getName(), fieldName);
    }

    static Var castObject(Var originalVar, JType castToClass) {
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
        return new InvokeMethodAsVar(var.varString().replace("$", "."), methodName, bodyBuilder.toString());
    }

    static Var invokeMethod(@NotNull JType clazz, @NotNull String methodName, Var... arguments) {
        StringBuilder bodyBuilder = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            bodyBuilder.append(arguments[i].varString());
            if (i != arguments.length - 1) {
                bodyBuilder.append(", ");
            }
        }
        return new InvokeMethodAsVar(clazz.typeString(), methodName, bodyBuilder.toString());
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

    static Var constructor(JType type, Var... arguments) {
        StringBuilder bodyBuilder = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            bodyBuilder.append(arguments[i].varString());
            if (i != arguments.length - 1) {
                bodyBuilder.append(", ");
            }
        }
        return new ConstructorVar4JGeneratable(type, bodyBuilder.toString());
    }

    static Var initializedArray(JType componentType, int length) {
        return new InitializedArrayVar(componentType, length);
    }

    static Var initializedArray(JType componentType, Var... vars) {
        return new InitializedContentArrayVar(componentType, vars);
    }

    static Var custom(String content) {
        return new CustomVar(content);
    }

    static Var nullVar() {
        return JStringVar.nullVar();
    }

    static Var referringVar(JType type, String name) {
        return new ReferringVar4JGeneratable(type, name);
    }

    static Var thisVar() {
        return new CustomVar("this");
    }

    static Var add(Var var, Var another) {
        return new DigitOperatedVar(var, DigitOperator.PLUS, another);
    }

    static Var subtract(Var var, Var another) {
        return new DigitOperatedVar(var, DigitOperator.MINUS, another);
    }

    static Var multiply(Var var, Var another) {
        return new DigitOperatedVar(var, DigitOperator.MULTIPLY, another);
    }

    static Var divide(Var var, Var another) {
        return new DigitOperatedVar(var, DigitOperator.DIVIDE, another);
    }

    static Var remainder(Var var, Var another) {
        return new DigitOperatedVar(var, DigitOperator.REMINDER, another);
    }

    static Var bitwiseOR(Var var, Var another) {
        return new DigitOperatedVar(var, DigitOperator.BITWISE_OR, another);
    }

    static Var bitwiseAND(Var var, Var another) {
        return new DigitOperatedVar(var, DigitOperator.BITWISE_AND, another);
    }

    static Var bitwiseXOR(Var var, Var another) {
        return new DigitOperatedVar(var, DigitOperator.BITWISE_XOR, another);
    }

    static Var bitwiseLeftShift(Var var, Var another) {
        return new DigitOperatedVar(var, DigitOperator.BITWISE_LEFT_SHIFT, another);
    }

    static Var bitwiseRightShift(Var var, Var another) {
        return new DigitOperatedVar(var, DigitOperator.BITWISE_RIGHT_SHIFT, another);
    }

    static Var bitwiseRightLogicalShift(Var var, Var another) {
        return new DigitOperatedVar(var, DigitOperator.BITWISE_RIGHT_LOGICAL_SHIFT, another);
    }

    static Var bitwiseNOT(Var var) {
        return new DigitOperatedVar(var, DigitOperator.BITWISE_NOT, null);
    }

    static Var increaseLeft(Var var) {
        return new DigitOperatedVar(null, DigitOperator.INCREASE, var);
    }

    static Var increaseRight(Var var) {
        return new DigitOperatedVar(var, DigitOperator.INCREASE, null);
    }

    static Var decreaseLeft(Var var) {
        return new DigitOperatedVar(null, DigitOperator.DECREASE, var);
    }

    static Var decreaseRight(Var var) {
        return new DigitOperatedVar(var, DigitOperator.DECREASE, null);
    }

    static Var setValueVar(Var referringVar, Var any) {
        if (!(referringVar instanceof ReferringVar || referringVar instanceof ReferringVar4JGeneratable)) {
            throw new RuntimeException("Unacceptable var");
        }
        return new SetValueVar(referringVar.getName(), any);
    }

    static JAnonymousClass anonymousClass(@NotNull JType type, @NotNull Var[] arguments) {
        return new JAnonymousClass(type, arguments);
    }

    static Var enumVar(@NotNull JEnum enumClass, @NotNull String enumItemName) {
        return new EnumVar(enumClass, enumItemName);
    }

    static Var lambda(JType clazz, String method) {
        return new LambdaVar(clazz, method);
    }

    static Var lambda(JType clazz) {
        return new LambdaVar(clazz, "new");
    }

    static Var arrayElement(Var arrayVar, int index) {
        return arrayElement(arrayVar, JStringVar.intVar(index));
    }

    static Var arrayElement(Var arrayVar, Var index) {
        return new ArrayElementVar(arrayVar, index);
    }

    JType getType();

    String getName();

    String varString();

}

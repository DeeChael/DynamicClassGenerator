package net.deechael.dcg;

import net.deechael.useless.function.parameters.DuParameter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

final class DefaultVars {

    static final class ArrayElementVar implements Var {

        private final Var element;
        private final Var index;

        public ArrayElementVar(Var element, Var index) {
            this.element = element;
            this.index = index;
        }

        @Override
        public JType getType() {
            throw new RuntimeException("No type");
        }

        @Override
        public String getName() {
            throw new RuntimeException("No name");
        }

        @Override
        public String varString() {
            return element.varString() + "[" + index.varString() + "]";
        }

    }

    static final class CastedVar implements Var {

        private final String type;
        private final String originalVarString;

        public CastedVar(JType type, String originalVarString) {
            this.type = type.typeString();
            this.originalVarString = originalVarString;
        }

        @Override
        public JType getType() {
            throw new RuntimeException("No type");
        }

        @Override
        public String getName() {
            throw new RuntimeException("This var doesn't have name!");
        }

        @Override
        public String varString() {
            return "((" + type + ") (" + originalVarString + "))";
        }

        private String deal(String typeName) {
            if (typeName.startsWith("[L")) {
                return typeName.substring(2) + "[]";
            } else if (typeName.startsWith("[")) {
                return typeName.substring(1) + "[]";
            }
            return typeName;
        }

    }


    static final class ClassVar implements Var {

        private final JType type;

        public ClassVar(JType type) {
            this.type = type;
        }

        @Override
        public JType getType() {
            return JType.CLASS;
        }

        @Override
        public String getName() {
            throw new RuntimeException("Class var doesn't have name!");
        }

        @Override
        public String varString() {
            return type.typeString() + ".class";
        }

    }

    static final class ConstructorVar implements Var {

        private final JType type;
        private final String bodyString;

        public ConstructorVar(JType type, String bodyString) {
            this.type = type;
            this.bodyString = bodyString;
        }

        @Override
        public JType getType() {
            return this.type;
        }

        @Override
        public String getName() {
            throw new RuntimeException("This var doesn't have name!");
        }

        @Override
        public String varString() {
            return "new " + getType().typeString() + "(" + bodyString + ")";
        }

    }

    static final class ConstructorVar4JGeneratable implements Var {

        private final JType type;
        private final String bodyString;

        public ConstructorVar4JGeneratable(JType type, String bodyString) {
            this.type = type;
            this.bodyString = bodyString;
        }

        public JType getJGeneratableType() {
            return this.type;
        }

        @Override
        public JType getType() {
            throw new RuntimeException("Please invoke getJGeneratableType()");
        }

        @Override
        public String getName() {
            throw new RuntimeException("This var doesn't have name!");
        }

        @Override
        public String varString() {
            return "new " + this.type.typeString() + "(" + bodyString + ")";
        }

    }

    static final class CustomVar implements Var {

        private final String varValue;

        public CustomVar(String varValue) {
            this.varValue = varValue;
        }

        @Override
        public JType getType() {
            throw new RuntimeException("CustomVar not has type");
        }

        @Override
        public String getName() {
            throw new RuntimeException("CustomVar not has name");
        }

        @Override
        public String varString() {
            return varValue;
        }

    }

    static final class DigitOperatedVar implements Var {

        private final Var first;
        private final Var second;

        private final DigitOperator operator;

        public DigitOperatedVar(Var first, @NotNull DigitOperator operator, Var second) {
            this.first = first;
            this.second = second;

            this.operator = operator;
        }

        @Override
        public JType getType() {
            return JType.classType(int.class);
        }

        @Override
        public String getName() {
            throw new RuntimeException("Digit has no name");
        }

        @Override
        public String varString() {
            if (operator.isMath()) {
                return "(" + first.varString() + " " + operator.getSymbol() + " " + second.varString() + ")";
            } else if (operator == DigitOperator.BITWISE_NOT) {
                return "(~" + first.varString() + ")";
            } else if (operator == DigitOperator.INCREASE || operator == DigitOperator.DECREASE) {
                return first != null ? "(" + first.varString() + "++)" : (second != null ? "(++" + second.varString() + ")" : "0");
            } else {
                throw new RuntimeException("HOW!!!");
            }
        }

    }

    static final class EnumVar implements Var {

        private final String enumClass;
        private final String enumItemName;

        public EnumVar(JEnum enumClass, String enumItemName) {
            this.enumClass = enumClass.getName();
            this.enumItemName = enumItemName;
        }

        @Override
        public JType getType() {
            throw new RuntimeException("No type");
        }

        @Override
        public String getName() {
            throw new RuntimeException("No name");
        }

        @Override
        public String varString() {
            return enumClass + "." + enumItemName;
        }

    }

    static final class FieldDirectVar implements Var {

        private final String fieldName;

        public FieldDirectVar(String fieldName) {
            this.fieldName = fieldName;
        }

        @Override
        public JType getType() {
            throw new RuntimeException("This var doesn't have type!");
        }

        @Override
        public String getName() {
            throw new RuntimeException("This var doesn't have name!");
        }

        @Override
        public String varString() {
            return fieldName;
        }

    }

    static final class FieldVar implements Var {

        private final String varName;
        private final String fieldName;

        public FieldVar(String varName, String fieldName) {
            this.varName = varName;
            this.fieldName = fieldName;
        }

        @Override
        public JType getType() {
            throw new RuntimeException("This var doesn't have type!");
        }

        @Override
        public String getName() {
            throw new RuntimeException("This var doesn't have name!");
        }

        @Override
        public String varString() {
            return varName + "." + fieldName;
        }

    }

    static final class FunctionVar implements Var {

        private final String[] parameters;
        private final DuParameter<Var[], JExecutable> function;

        public FunctionVar(String[] parameters, DuParameter<Var[], JExecutable> function) {
            this.parameters = parameters;
            this.function = function;
        }

        @Override
        public JType getType() {
            throw new RuntimeException("Functional Interface not have type");
        }

        @Override
        public String getName() {
            throw new RuntimeException("Functional Interface not have name");
        }

        @Override
        public String varString() {
            Var[] vars = new Var[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                vars[i] = Var.referringVar(null, "jfunc_" + parameters[i]);
            }
            JExecutable executable = new JExecutable.JExecutable4InnerStructure();
            this.function.apply(vars, executable);

            StringBuilder builder = new StringBuilder();
            builder.append("(");
            for (int i = 0; i < parameters.length; i++) {
                builder.append("jfunc_").append(parameters[i]);
                if (i != parameters.length - 1) {
                    builder.append(", ");
                }
            }
            builder.append(") -> {\n").append(executable.getString()).append("\n}");
            return null;
        }
    }

    static final class FunctionReturnVar implements Var {

        private final String[] parameters;
        private final Var returnValue;

        public FunctionReturnVar(String[] parameters, Var returnValue) {
            this.parameters = parameters;
            this.returnValue = returnValue;
        }

        @Override
        public JType getType() {
            throw new RuntimeException("Functional Interface not have type");
        }

        @Override
        public String getName() {
            throw new RuntimeException("Functional Interface not have name");
        }

        @Override
        public String varString() {
            StringBuilder builder = new StringBuilder();
            builder.append("(");
            for (int i = 0; i < parameters.length; i++) {
                builder.append("jfunc_").append(parameters[i]);
                if (i != parameters.length - 1) {
                    builder.append(", ");
                }
            }
            builder.append(") -> \n").append(this.returnValue.varString()).append("\n");
            return null;
        }
    }

    static final class InitializedArrayVar implements Var {

        private final String type;
        private final int length;

        public InitializedArrayVar(JType type, int length) {
            this.type = type.typeString();
            this.length = length;
        }

        @Override
        public JType getType() {
            throw new RuntimeException("No type");
        }

        @Override
        public String getName() {
            throw new RuntimeException("No name");
        }

        @Override
        public String varString() {
            return "new " + type + "[" + length + "]";
        }

    }

    static final class InitializedContentArrayVar implements Var {

        private final String type;
        private final Var[] vars;

        public InitializedContentArrayVar(JType type, Var... vars) {
            this.type = type.typeString();
            this.vars = vars;
        }

        @Override
        public JType getType() {
            throw new RuntimeException("No type");
        }

        @Override
        public String getName() {
            throw new RuntimeException("No name");
        }

        @Override
        public String varString() {
            String typeName = type;
            while (typeName.contains("[")) {
                typeName = deal(typeName);
            }
            String contentString = Arrays.toString(Arrays.stream(this.vars).map(Var::varString).toArray(String[]::new));
            return "new " + typeName + "[] {" + contentString.substring(1, contentString.length() - 1) + "}";
        }

        private String deal(String typeName) {
            if (typeName.startsWith("[L")) {
                return typeName.substring(2) + "[]";
            } else if (typeName.startsWith("[")) {
                return typeName.substring(1) + "[]";
            }
            return typeName;
        }

    }

    static final class InvokeMethodAsVar implements Var {

        private final String varName;
        private final String methodName;
        private final String body;

        public InvokeMethodAsVar(String varName, String methodName, String body) {
            this.varName = varName;
            this.methodName = methodName;
            this.body = body;
        }

        @Override
        public JType getType() {
            throw new RuntimeException("InvokeMethodAsVar cannot get type!");
        }

        @Override
        public String getName() {
            throw new RuntimeException("InvokeMethodAsVar cannot have a name!");
        }

        @Override
        public String varString() {
            return varName + "." + methodName + "(" + body + ")";
        }

    }

    static final class InvokeMethodDirectlyAsVar implements Var {

        private final String methodName;
        private final String body;

        public InvokeMethodDirectlyAsVar(String methodName, String body) {
            this.methodName = methodName;
            this.body = body;
        }

        @Override
        public JType getType() {
            throw new RuntimeException("InvokeMethodDirectlyAsVar cannot get type!");
        }

        @Override
        public String getName() {
            throw new RuntimeException("InvokeMethodDirectlyAsVar cannot have a name!");
        }

        @Override
        public String varString() {
            return methodName + "(" + body + ")";
        }

    }

    public static final class LambdaVar implements Var {

        private final String type;
        private final String content;

        public LambdaVar(JType clazz, String content) {
            this.type = clazz.typeString();
            this.content = content;
        }

        @Override
        public JType getType() {
            throw new RuntimeException("No type");
        }

        @Override
        public String getName() {
            throw new RuntimeException("No name");
        }

        @Override
        public String varString() {
            return type + "::" + content;
        }

    }

    static final class ReferringVar implements Var {

        private final JType type;
        private final String name;

        public ReferringVar(JType type, String name) {
            this.type = type;
            this.name = name;
        }

        @Override
        public JType getType() {
            return this.type;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public String varString() {
            return getName();
        }

    }

    static final class ReferringVar4JGeneratable implements Var {

        private final JType type;
        private final String name;

        public ReferringVar4JGeneratable(JType type, String name) {
            this.type = type;
            this.name = name;
        }

        public JType getJGeneratableType() {
            return this.type;
        }

        @Override
        public JType getType() {
            throw new RuntimeException("Please invoke getJGeneratableType()");
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public String varString() {
            return getName();
        }

    }

    static final class SetValueVar implements Var {

        private final String originalTarget;
        private final Var value;

        private boolean got = false;

        public SetValueVar(String originalTarget, Var value) {
            this.originalTarget = originalTarget;
            this.value = value;
        }

        @Override
        public JType getType() {
            throw new RuntimeException("No type");
        }

        @Override
        public String getName() {
            throw new RuntimeException("No name");
        }

        @Override
        public String varString() {
            if (got) {
                return originalTarget;
            } else {
                got = true;
                return "(" + originalTarget + " = " + value.varString() + ")";
            }
        }

    }

    private DefaultVars() {
    }

}

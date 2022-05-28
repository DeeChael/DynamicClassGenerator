package net.deechael.dcg;

import net.deechael.dcg.body.Operation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class JMethod extends JExecutableParametered implements ClassMethod {

    private final String returnType;
    private final Level level;
    private final MethodOwnable parent;
    private final String methodName;

    private final boolean isStatic;
    private final boolean isFinal;
    private final boolean isSynchronized;

    JMethod(Level level, MethodOwnable clazz, String methodName, boolean isStatic, boolean isFinal, boolean isSynchronized) {
        this(JType.classType(void.class), level, clazz, methodName, isStatic, isFinal, isSynchronized);
    }

    JMethod(JType returnType, Level level, MethodOwnable clazz, String methodName, boolean isStatic, boolean isFinal, boolean isSynchronized) {
        this.returnType = returnType.typeString();
        this.level = level;
        this.parent = clazz;
        this.methodName = methodName;
        this.isStatic = isStatic;
        this.isFinal = isFinal;
        this.isSynchronized = isSynchronized;
    }

    private String deal(String typeName) {
        if (typeName.startsWith("[L")) {
            return typeName.substring(2) + "[]";
        } else if (typeName.startsWith("[")) {
            return typeName.substring(1) + "[]";
        }
        return typeName;
    }

    @Override
    public String getString() {
        StringBuilder base = new StringBuilder();
        base.append(this.annotationString()).append(level.getString()).append(" ");
        if (isFinal) {
            base.append("final ");
        }
        if (isStatic) {
            base.append("static ");
        }
        if (isSynchronized) {
            base.append("synchronized ");
        }
        base.append(returnType).append(" ").append(methodName).append("(");
        List<Map.Entry<String, String>> entries = new ArrayList<>(this.getParameters().entrySet());
        for (int i = 0; i < entries.size(); i++) {
            Map.Entry<String, String> entry = entries.get(i);
            base.append(entry.getValue()).append(" ").append(entry.getKey());
            if (i != entries.size() - 1) {
                base.append(", ");
            }
        }
        base.append(")");
        List<String> throwables = getThrowings();
        if (throwables.size() > 0) {
            base.append(" throws ");
            for (int i = 0; i < throwables.size(); i++) {
                base.append(throwables.get(i));
                if (i != throwables.size() - 1) {
                    base.append(", ");
                }
            }
        }
        base.append(" {\n");
        for (Operation operation : this.getOperations()) {
            base.append(operation.getString()).append("\n");
        }
        base.append("}").append("\n");
        return base.toString();
    }


}

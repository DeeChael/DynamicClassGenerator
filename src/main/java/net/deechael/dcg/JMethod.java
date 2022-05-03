package net.deechael.dcg;

import net.deechael.dcg.body.Operation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JMethod extends JExecutableParametered {

    private final String returnType;
    private final Level level;
    private final JClass parent;
    private final String methodName;

    private final boolean isStatic;
    private final boolean isFinal;
    private final boolean isSynchronized;

    JMethod(Level level, JClass clazz, String methodName, boolean isStatic, boolean isFinal, boolean isSynchronized) {
        this(void.class, level, clazz, methodName, isStatic, isFinal, isSynchronized);
    }

    JMethod(Class<?> returnType, Level level, JClass clazz, String methodName, boolean isStatic, boolean isFinal, boolean isSynchronized) {
        String returnTypeString = returnType.getName();
        while (returnTypeString.contains("[")) {
            returnTypeString = deal(returnTypeString);
        }
        this.returnType = returnTypeString;
        this.level = level;
        this.parent = clazz;
        this.methodName = methodName;
        this.isStatic = isStatic;
        this.isFinal = isFinal;
        this.isSynchronized = isSynchronized;
    }

    JMethod(JGeneratable returnType, Level level, JClass clazz, String methodName, boolean isStatic, boolean isFinal, boolean isSynchronized) {
        this.returnType = returnType.getName();
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
        if (isStatic) {
            base.append("static ");
        } else if (isFinal) {
            base.append("final ");
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

    @Override
    public void addAnnotation(Class<?> annotation, Map<String, JStringVar> values) {
        if (!annotation.isAnnotation()) throw new RuntimeException("The class is not an annotation!");
        Target target = annotation.getAnnotation(Target.class);
        if (target != null) {
            boolean hasConstructor = false;
            for (ElementType elementType : target.value()) {
                if (elementType == ElementType.METHOD) {
                    hasConstructor = true;
                    break;
                }
            }
            if (!hasConstructor) throw new RuntimeException("This annotation is not for method!");
        }
        super.addAnnotation(annotation, values);
    }

}

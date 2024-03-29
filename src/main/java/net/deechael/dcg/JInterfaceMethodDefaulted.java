package net.deechael.dcg;

import net.deechael.dcg.operation.Operation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class JInterfaceMethodDefaulted extends JExecutableParametered implements InterfaceMethod {

    private final String returnType;
    private final String methodName;

    JInterfaceMethodDefaulted(String methodName) {
        this(JType.classType(void.class), methodName);
    }

    JInterfaceMethodDefaulted(JType returnType, String methodName) {
        this.returnType = returnType.typeString();
        this.methodName = methodName;
    }

    @Override
    public String getString() {
        StringBuilder base = new StringBuilder();
        base.append(this.annotationString(getAnnotations()))
                .append("default ")
                .append(returnType)
                .append(" ")
                .append(methodName)
                .append("(");
        List<Map.Entry<String, String>> entries = new ArrayList<>(this.parameters.entrySet());
        for (int i = 0; i < entries.size(); i++) {
            Map.Entry<String, String> entry = entries.get(i);
            if (annotatedParameters.containsKey(entry.getKey()))
                base.append(this.annotationString(annotatedParameters.get(entry.getKey())));
            if (finalledParameters.contains(entry.getKey()))
                base.append("final ");
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

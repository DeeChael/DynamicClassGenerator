package net.deechael.dcg;

import java.util.Arrays;
import java.util.List;

final class JVMTypedType implements JType {

    private final Class<?> clazz;
    private final List<JType> types;

    JVMTypedType(Class<?> clazz, JType... types) {
        this.clazz = clazz;
        this.types = Arrays.asList(types);
    }

    @Override
    public String typeString() {
        StringBuilder builder = new StringBuilder();
        Class<?> tempClass = clazz;
        while (tempClass.isArray()) {
            builder.append("[]");
            tempClass = tempClass.getComponentType();
        }
        StringBuilder another = new StringBuilder();
        another.append(tempClass.getName().replace("$", "."));
        int loopTimes = types.size();
        if (loopTimes > 0) {
            another.append("<");
            for (int i = 0; i < loopTimes; i++) {
                another.append(types.get(i).typeString());
                if (i != (loopTimes - 1)) {
                    another.append(", ");
                }
            }
            another.append(">");
        }
        return another.append(builder).toString();
    }

}
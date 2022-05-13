package net.deechael.dcg;

final class JVMType implements JType {

    private final Class<?> clazz;

    JVMType(Class<?> clazz) {
        this.clazz = clazz;
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
        if (clazz.getTypeParameters().length > 0) another.append("<>");
        return another.append(builder).toString();
    }

}

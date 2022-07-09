package net.deechael.dcg;

public interface JType {

    // Default provided JTypes
    JType VOID = classType(void.class);
    JType INT = classType(int.class);
    JType BOOLEAN = classType(boolean.class);
    JType DOUBLE = classType(double.class);
    JType FLOAT = classType(float.class);
    JType LONG = classType(long.class);
    JType SHORT = classType(short.class);
    JType BYTE = classType(byte.class);
    JType CHAR = classType(char.class);
    JType OBJECT = classType(Object.class);
    JType STRING = classType(String.class);
    JType CLASS = classType(Class.class);

    static JType classType(Class<?> clazz) {
        return new JVMType(clazz);
    }

    static JType typedClassType(Class<?> clazz, JType... types) {
        return new JVMTypedType(clazz, types);
    }

    static JType typeType(String typeId) {
        return new CustomType(typeId);
    }

    String typeString();

}

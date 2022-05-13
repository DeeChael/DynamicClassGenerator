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

    String typeString();

    static JType classType(Class<?> clazz) {
        return new JVMType(clazz);
    }

    static JType typedClassType(Class<?> clazz, JType... types) {
        return new JVMTypedType(clazz,  types);
    }

    /**
     * I don't know how to explain
     * Example:
     * If you generated a class like this:
     * public class ClassYouGenerated<E> {
     *
     *     public List<E> returnList() {
     *         ....
     *     }
     *
     * }
     *
     * To implement "public List<E> returnList()":
     * jClassObject.addMethod(JType.typedClassType(List.class, JType.typeType("E")), Level.PUBLIC, "returnList", false, false, false);
     *
     * @since 1.03.5 Create typed class is not allowed right now, will be updated later
     *
     * @param typeId The name of the type
     * @return A type whose typeString() returns typeId
     */
    static JType typeType(String typeId) {
        return new CustomType(typeId);
    }


}

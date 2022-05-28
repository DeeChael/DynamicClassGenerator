package net.deechael.dcg;

import net.deechael.dcg.items.Var;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public final class JStringVar implements Var {

    private final Type type;
    private final JType dcgType;
    private final String value;

    private JStringVar(Type type, String value) {
        this.type = type;
        this.dcgType = type.getDcgType();
        this.value = value;
    }

    public String getValueString() {
        return value;
    }

    @Override
    public JType getType() {
        return this.dcgType;
    }

    public Type getJStringVarType() {
        return this.type;
    }

    @Override
    public String getName() {
        return null;
    }

    public String varString() {
        return getValueString();
    }

    public static JStringVar nullVar() {
        return new JStringVar(null, null);
    }

    public static JStringVar stringVar(String value) {
        return new JStringVar(Type.STRING, "\"" + value + "\"");
    }

    public static JStringVar intVar(int value) {
        return new JStringVar(Type.INT, String.valueOf(value));
    }

    public static JStringVar byteVar(byte value) {
        return new JStringVar(Type.BYTE, String.valueOf(value));
    }

    public static JStringVar charVar(char value) {
        return new JStringVar(Type.CHAR, String.valueOf(value));
    }

    public static JStringVar shortVar(short value) {
        return new JStringVar(Type.SHORT, String.valueOf(value));
    }

    public static JStringVar longVar(long value) {
        return new JStringVar(Type.LONG, String.valueOf(value));
    }

    public static JStringVar floatVar(float value) {
        return new JStringVar(Type.FLOAT, String.valueOf(value));
    }

    public static JStringVar doubleVar(double value) {
        return new JStringVar(Type.DOUBLE, String.valueOf(value));
    }

    public static JStringVar booleanVar(boolean value) {
        return new JStringVar(Type.BOOLEAN, String.valueOf(value));
    }

    public static JStringVar classVar(Class<?> value) {
        return new JStringVar(Type.CLASS, value.getName());
    }

    public static JStringVar classVar(JGeneratable generatable) {
        return new JStringVar(Type.CLASS, generatable.getName());
    }

    public static JStringVar enumVar(Object value) {
        if (!value.getClass().isEnum()) throw new RuntimeException("The value is not a enum type");
        return new JStringVar(Type.ENUM, value.getClass().getName() + "." + value);
    }

    public static JStringVar enumVar(Class<?> enumClass, String name) {
        if (!enumClass.isEnum()) throw new RuntimeException("The value is not a enum type");
        try {
            enumClass.getMethod("valueOf", String.class).invoke(null, name);
            return new JStringVar(Type.CLASS, enumClass.getName() + "." + name);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static JStringVar arrayVar(String[] array) {
        return arrayVar((Object[]) array);
    }

    public static JStringVar arrayVar(String[][] array) {
        return arrayVar((Object[][]) array);
    }

    public static JStringVar arrayVar(String[][][] array) {
        return arrayVar(Arrays.stream(array).toArray());
    }

    public static JStringVar arrayVar(int[] array) {
        return arrayVar(Arrays.stream(array).boxed().toArray());
    }

    public static JStringVar arrayVar(int[][] array) {
        return arrayVar(Arrays.stream(array).toArray());
    }

    public static JStringVar arrayVar(int[][][] array) {
        return arrayVar(Arrays.stream(array).toArray());
    }

    public static JStringVar arrayVar(byte[] array) {
        Byte[] bytes = new Byte[array.length];
        for (int i = 0; i < array.length; i++) {
            bytes[i] = array[i];
        }
        return arrayVar(Arrays.stream(bytes).toArray());
    }

    public static JStringVar arrayVar(byte[][] array) {
        return arrayVar(Arrays.stream(array).toArray());
    }

    public static JStringVar arrayVar(byte[][][] array) {
        return arrayVar(Arrays.stream(array).toArray());
    }

    public static JStringVar arrayVar(short[] array) {
        Short[] shorts = new Short[array.length];
        for (int i = 0; i < array.length; i++) {
            shorts[i] = array[i];
        }
        return arrayVar(Arrays.stream(shorts).toArray());
    }

    public static JStringVar arrayVar(short[][] array) {
        return arrayVar(Arrays.stream(array).toArray());
    }

    public static JStringVar arrayVar(short[][][] array) {
        return arrayVar(Arrays.stream(array).toArray());
    }

    public static JStringVar arrayVar(char[] array) {
        Character[] chars = new Character[array.length];
        for (int i = 0; i < array.length; i++) {
            chars[i] = array[i];
        }
        return arrayVar(Arrays.stream(chars).toArray());
    }

    public static JStringVar arrayVar(char[][] array) {
        return arrayVar(Arrays.stream(array).toArray());
    }

    public static JStringVar arrayVar(char[][][] array) {
        return arrayVar(Arrays.stream(array).toArray());
    }

    public static JStringVar arrayVar(long[] array) {
        return arrayVar(Arrays.stream(array).boxed().toArray());
    }

    public static JStringVar arrayVar(long[][] array) {
        return arrayVar(Arrays.stream(array).toArray());
    }

    public static JStringVar arrayVar(long[][][] array) {
        return arrayVar(Arrays.stream(array).toArray());
    }

    public static JStringVar arrayVar(float[] array) {
        Float[] floats = new Float[array.length];
        for (int i = 0; i < array.length; i++) {
            floats[i] = array[i];
        }
        return arrayVar(Arrays.stream(floats).toArray());
    }

    public static JStringVar arrayVar(float[][] array) {
        return arrayVar(Arrays.stream(array).toArray());
    }

    public static JStringVar arrayVar(float[][][] array) {
        return arrayVar(Arrays.stream(array).toArray());
    }

    public static JStringVar arrayVar(double[] array) {
        return arrayVar(Arrays.stream(array).boxed().toArray());
    }

    public static JStringVar arrayVar(double[][] array) {
        return arrayVar(Arrays.stream(array).toArray());
    }

    public static JStringVar arrayVar(double[][][] array) {
        return arrayVar(Arrays.stream(array).toArray());
    }

    public static JStringVar arrayVar(boolean[] array) {
        Boolean[] booleans = new Boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            booleans[i] = array[i];
        }
        return arrayVar(Arrays.stream(booleans).toArray());
    }

    public static JStringVar arrayVar(boolean[][] array) {
        return arrayVar(Arrays.stream(array).toArray());
    }

    public static JStringVar arrayVar(boolean[][][] array) {
        return arrayVar(Arrays.stream(array).toArray());
    }

    public static JStringVar arrayVar(Class[] array) {
        return arrayVar(Arrays.stream(array).toArray());
    }

    public static JStringVar arrayVar(Class[][] array) {
        return arrayVar(Arrays.stream(array).toArray());
    }

    public static JStringVar arrayVar(Class[][][] array) {
        return arrayVar(Arrays.stream(array).toArray());
    }

    public static JStringVar arrayVar(Object[] array) {
        Class<?> clazz = array.getClass().getComponentType();
        if (!clazz.isEnum()) throw new RuntimeException("The value is not a enum type");
        StringBuilder base = new StringBuilder();
        base.append("{");
        for (int i = 0; i < array.length; i++) {
            Object enumObj = array[i];
            base.append(clazz.getName()).append(".").append(enumObj);
            if (i != array.length - 1) {
                base.append(", ");
            }
        }
        base.append("}");
        return new JStringVar(Type.ARRAY, base.toString());
    }

    public static JStringVar arrayVar(Object[][] array) {
        Class<?> clazz = array.getClass().getComponentType();
        if (!clazz.getComponentType().isEnum()) throw new RuntimeException("The value is not a enum type");
        StringBuilder base = new StringBuilder();
        base.append("{");
        for (int i = 0; i < array.length; i++) {
            Object[] enumArrayObj = array[i];
            StringBuilder inner = new StringBuilder();
            inner.append("{");
            for (int o = 0; o < enumArrayObj.length; o++) {
                Object enumObj = enumArrayObj[o];
                inner.append(clazz.getName()).append(".").append(enumObj);
                if (o != enumArrayObj.length - 1) {
                    inner.append(", ");
                }
            }
            inner.append("}");
            base.append(inner);
            if (i != array.length - 1) {
                base.append(", ");
            }
        }
        base.append("}");
        return new JStringVar(Type.ARRAY, base.toString());
    }

    public static boolean isSupport(Class<?> clazz) {
        Class<?> type = clazz;
        while (type.isArray()) {
            type = type.getComponentType();
        }
        return type == Double.class || type == Float.class || type == Long.class || type == Class.class || type == Character.class || type == Short.class || type == Byte.class || type == Integer.class || type == String.class || type.isEnum() || type.isPrimitive();
    }

    public static enum Type {
        STRING(JType.STRING),
        INT(JType.INT),
        BOOLEAN(JType.BOOLEAN),
        DOUBLE(JType.DOUBLE),
        FLOAT(JType.FLOAT),
        LONG(JType.LONG),
        SHORT(JType.SHORT),
        BYTE(JType.BYTE),
        CHAR(JType.CHAR),
        CLASS(JType.CLASS),
        ENUM(null),
        ARRAY(null);

        private final JType dcgType;

        Type(JType jType) {
            this.dcgType = jType;
        }

        public JType getDcgType() {
            return dcgType;
        }

    }

}

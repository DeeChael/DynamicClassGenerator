package net.deechael.dcg;

import net.deechael.dcg.items.Var;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;

public class JStringVar implements Var {

    private final Class<?> type;
    private final String value;

    private JStringVar(Class<?> type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getValueString() {
        return value;
    }

    @Override
    public Class<?> getType() {
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
        return new JStringVar(String.class, "\"" + value + "\"");
    }

    public static JStringVar intVar(int value) {
        return new JStringVar(int.class, String.valueOf(value));
    }

    public static JStringVar byteVar(byte value) {
        return new JStringVar(byte.class, String.valueOf(value));
    }

    public static JStringVar charVar(char value) {
        return new JStringVar(char.class, String.valueOf(value));
    }

    public static JStringVar shortVar(short value) {
        return new JStringVar(short.class, String.valueOf(value));
    }

    public static JStringVar longVar(long value) {
        return new JStringVar(long.class, String.valueOf(value));
    }

    public static JStringVar floatVar(float value) {
        return new JStringVar(float.class, String.valueOf(value));
    }

    public static JStringVar doubleVar(double value) {
        return new JStringVar(double.class, String.valueOf(value));
    }

    public static JStringVar booleanVar(boolean value) {
        return new JStringVar(boolean.class, String.valueOf(value));
    }

    public static JStringVar classVar(Class<?> value) {
        return new JStringVar(Class.class, value.getName());
    }

    public static JStringVar classVar(JGeneratable generatable) {
        return new JStringVar(Class.class, generatable.getName());
    }

    public static JStringVar enumVar(Object value) {
        if (!value.getClass().isEnum()) throw new RuntimeException("The value is not a enum type");
        return new JStringVar(value.getClass(), value.getClass().getName() + "." + value);
    }

    public static JStringVar enumVar(Class<?> enumClass, String name) {
        if (!enumClass.isEnum()) throw new RuntimeException("The value is not a enum type");
        try {
            enumClass.getMethod("valueOf", String.class).invoke(null, name);
            return new JStringVar(enumClass, enumClass.getName() + "." + name);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static JStringVar arrayVar(String[] array) {
        return arrayVar(array);
    }

    public static JStringVar arrayVar(String[][] array) {
        return arrayVar(array);
    }

    public static JStringVar arrayVar(String[][][] array) {
        return arrayVar(array);
    }

    public static JStringVar arrayVar(int[] array) {
        return arrayVar(array);
    }

    public static JStringVar arrayVar(int[][] array) {
        return arrayVar(array);
    }

    public static JStringVar arrayVar(int[][][] array) {
        return arrayVar(array);
    }

    public static JStringVar arrayVar(byte[] array) {
        return arrayVar(array);
    }

    public static JStringVar arrayVar(byte[][] array) {
        return arrayVar(array);
    }

    public static JStringVar arrayVar(byte[][][] array) {
        return arrayVar(array);
    }

    public static JStringVar arrayVar(short[] array) {
        return arrayVar(array);
    }

    public static JStringVar arrayVar(short[][] array) {
        return arrayVar(array);
    }

    public static JStringVar arrayVar(short[][][] array) {
        return arrayVar(array);
    }

    public static JStringVar arrayVar(char[] array) {
        return arrayVar(array);
    }

    public static JStringVar arrayVar(char[][] array) {
        return arrayVar(array);
    }

    public static JStringVar arrayVar(char[][][] array) {
        return arrayVar(array);
    }

    public static JStringVar arrayVar(long[] array) {
        return arrayVar(array);
    }

    public static JStringVar arrayVar(long[][] array) {
        return arrayVar(array);
    }

    public static JStringVar arrayVar(long[][][] array) {
        return arrayVar(array);
    }

    public static JStringVar arrayVar(float[] array) {
        return arrayVar(array);
    }

    public static JStringVar arrayVar(float[][] array) {
        return arrayVar(array);
    }

    public static JStringVar arrayVar(float[][][] array) {
        return arrayVar(array);
    }

    public static JStringVar arrayVar(double[] array) {
        return arrayVar(array);
    }

    public static JStringVar arrayVar(double[][] array) {
        return arrayVar(array);
    }

    public static JStringVar arrayVar(double[][][] array) {
        return arrayVar(array);
    }

    public static JStringVar arrayVar(boolean[] array) {
        return arrayVar(array);
    }

    public static JStringVar arrayVar(boolean[][] array) {
        return arrayVar(array);
    }

    public static JStringVar arrayVar(boolean[][][] array) {
        return arrayVar(array);
    }

    public static JStringVar arrayVar(Class[] array) {
        return arrayVar(array);
    }

    public static JStringVar arrayVar(Class[][] array) {
        return arrayVar(array);
    }

    public static JStringVar arrayVar(Class[][][] array) {
        return arrayVar(array);
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
        return new JStringVar(Array.newInstance(clazz, 0).getClass(), base.toString());
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
        return new JStringVar(Array.newInstance(Array.newInstance(clazz, 0).getClass(), 0).getClass(), base.toString());
    }

    public static boolean isSupport(Class<?> clazz) {
        Class<?> type = clazz;
        while (type.isArray()) {
            type = type.getComponentType();
        }
        return type == Double.class || type == Float.class || type == Long.class || type == Class.class || type == Character.class || type == Short.class || type == Byte.class || type == Integer.class || type == String.class || type.isEnum() || type.isPrimitive();
    }

}

package net.deechael.dcg.generator;

import java.util.HashMap;
import java.util.Map;

final class JClassLoader extends ClassLoader {

    private final Map<String, Class<?>> generated_classes = new HashMap<>();

    private final static JClassLoader instance = new JClassLoader();

    private JClassLoader() {
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (generated_classes.containsKey(name)) {
            return generated_classes.get(name);
        }
        return super.loadClass(name);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (generated_classes.containsKey(name)) {
            return generated_classes.get(name);
        }
        return super.findClass(name);
    }

    public Class<?> generate0(String className, byte[] bytes) {
        Class<?> clazz = defineClass(className, bytes, 0, bytes.length);
        this.generated_classes.put(className, clazz);
        return clazz;
    }

    public static JClassLoader getInstance() {
        return instance;
    }

    public static Class<?> generate(String className, byte[] bytes) {
        return getInstance().generate0(className, bytes);
    }

}

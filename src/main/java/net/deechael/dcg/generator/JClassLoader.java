package net.deechael.dcg.generator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class JClassLoader extends ClassLoader {

    private final static List<ClassLoader> contextLoaders = new ArrayList<>();
    private final static JClassLoader instance = new JClassLoader();
    private final Map<String, Class<?>> generated_classes = new HashMap<>();

    private JClassLoader() {
    }

    public static JClassLoader getInstance() {
        return instance;
    }

    public static Class<?> generate(String className, byte[] bytes) {
        return getInstance().generate0(className, bytes);
    }

    public static void addLoader(ClassLoader classLoader) {
        if (!contextLoaders.contains(classLoader)) {
            contextLoaders.add(classLoader);
        }
    }

    @Override
    public Class<?> loadClass(String name) {
        if (generated_classes.containsKey(name)) {
            return generated_classes.get(name);
        }
        for (ClassLoader loader : contextLoaders) {
            try {
                return loader.loadClass(name);
            } catch (ClassNotFoundException ignored) {
            }
        }
        try {
            return super.loadClass(name);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("DCG can't load the class");
        }
    }

    @Override
    protected Class<?> findClass(String name) {
        if (generated_classes.containsKey(name)) {
            return generated_classes.get(name);
        }
        for (ClassLoader loader : contextLoaders) {
            try {
                Method method = ClassLoader.class.getDeclaredMethod("findClass", String.class);
                method.setAccessible(true);
                return (Class<?>) method.invoke(loader, name);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
            }
        }
        try {
            return super.findClass(name);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("DCG can't find the class");
        }
    }

    public Class<?> generate0(String className, byte[] bytes) {
        Class<?> clazz = defineClass(className, bytes, 0, bytes.length);
        this.generated_classes.put(className, clazz);
        return clazz;
    }

}

package net.deechael.library.dcg.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class DClass {

    private final Class<?> clazz;

    private Object instance = null;

    public DClass(Class<?> clazz) {
        this.clazz = clazz;
    }

    public void newInstance(Class<?>[] requirements, Object[] arguments) {
        if (instance != null) {
            throw new RuntimeException("Ths instance has been created! Try to remove instance first!");
        }
        try {
            Constructor<?> constructor = clazz.getConstructor(requirements);
            Object tempInstance = constructor.newInstance(arguments);
            instance = tempInstance;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("The constructor not exists!");
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Failed to create new instance!");
        }
    }

    public Object removeInstance() {
        if (instance == null) throw new RuntimeException("Ths instance hasn't been initialized yet!");
        Object temp = instance;
        instance = null;
        return temp;
    }

    public Object getInstance() {
        if (instance == null) throw new RuntimeException("Ths instance hasn't been initialized yet!");
        return instance;
    }

}

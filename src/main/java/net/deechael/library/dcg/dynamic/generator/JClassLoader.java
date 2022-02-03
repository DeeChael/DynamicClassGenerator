package net.deechael.library.dcg.dynamic.generator;

public class JClassLoader extends ClassLoader {

    private final String className;

    private final byte[] bytes;

    public JClassLoader(String className, byte[] bytes) {
        this.className = className;
        this.bytes = bytes;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return defineClass(className, bytes, 0, bytes.length);
    }

}

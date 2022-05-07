package net.deechael.dcg.generator;

import javax.tools.*;
import java.util.*;

final class JJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {

    private JJavaFileObject javaFileObject;

    private final List<JJavaFileObject> cache = new ArrayList<>();

    public JJavaFileManager(JavaFileManager fileManager) {
        super(fileManager);
    }

    public JJavaFileObject getLastJavaFileObject() {
        this.cache.remove(this.javaFileObject);
        return javaFileObject;
    }

    public List<JJavaFileObject> readCache() {
        List<JJavaFileObject> caches = new ArrayList<>(this.cache);
        this.cache.clear();
        return caches;
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) {
        JJavaFileObject javaFileObject = new JJavaFileObject(className, kind);
        this.cache.add(javaFileObject);
        return this.javaFileObject = javaFileObject;
    }

}

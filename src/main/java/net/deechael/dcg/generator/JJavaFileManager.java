package net.deechael.dcg.generator;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import java.util.ArrayList;
import java.util.List;

final class JJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {

    private final List<JJavaFileObject> cache = new ArrayList<>();
    private JJavaFileObject javaFileObject;

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

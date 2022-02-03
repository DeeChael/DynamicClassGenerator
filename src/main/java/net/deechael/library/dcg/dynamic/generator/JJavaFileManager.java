package net.deechael.library.dcg.dynamic.generator;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import java.io.IOException;

public class JJavaFileManager extends ForwardingJavaFileManager {

    private JJavaFileObject javaFileObject;

    public JJavaFileManager(JavaFileManager fileManager) {
        super(fileManager);
    }

    public JJavaFileObject getJavaFileObject() {
        return javaFileObject;
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
        return javaFileObject = new JJavaFileObject(className, kind);
    }

}

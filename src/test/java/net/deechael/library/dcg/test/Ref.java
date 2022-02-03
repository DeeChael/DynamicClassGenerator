package net.deechael.library.dcg.test;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

public final class Ref {

    public static Class<?> createClass(String className, String input) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager javaFileManager = compiler.getStandardFileManager(null, null, null);
        UnsafeJavaFileManager unsafeJavaFileManager = new UnsafeJavaFileManager(javaFileManager);
        try {
            JavaCompiler.CompilationTask task = compiler.getTask(null, unsafeJavaFileManager, null, null, null, Collections.singletonList(new StringObject(new URI(className + ".java"), JavaFileObject.Kind.SOURCE, input)));
            if (task.call()) {
                UnsafeJavaFileObject javaFileObject = unsafeJavaFileManager.getJavaFileObject();
                ClassLoader classLoader = new UnsafeClassLoader(className, javaFileObject.getBytes());
                return classLoader.loadClass(className);
            }
        } catch (URISyntaxException | ClassNotFoundException ignored) {
        }
        return null;
    }

    private static class UnsafeJavaFileManager extends ForwardingJavaFileManager {

        private UnsafeJavaFileObject javaFileObject;

        protected UnsafeJavaFileManager(JavaFileManager fileManager) {
            super(fileManager);
        }

        public UnsafeJavaFileObject getJavaFileObject() {
            return javaFileObject;
        }

        @Override
        public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
            return javaFileObject = new UnsafeJavaFileObject(className, kind);
        }

    }

    private static class UnsafeJavaFileObject extends SimpleJavaFileObject {

        private ByteArrayOutputStream byteArrayOutputStream;

        protected UnsafeJavaFileObject(String className, Kind kind) {
            super(URI.create(className + kind.extension), kind);
            this.byteArrayOutputStream = new ByteArrayOutputStream();
        }

        @Override
        public OutputStream openOutputStream() throws IOException {
            return byteArrayOutputStream;
        }

        public byte[] getBytes() {
            return this.byteArrayOutputStream.toByteArray();
        }

    }

    private static class StringObject extends SimpleJavaFileObject {

        private final String content;

        protected StringObject(URI uri, Kind kind, String content) {
            super(uri, kind);
            this.content = content;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            return content;
        }

    }

    private static class UnsafeClassLoader extends ClassLoader {

        private final String className;

        private final byte[] bytes;

        private UnsafeClassLoader(String className, byte[] bytes) {
            this.className = className;
            this.bytes = bytes;
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            return defineClass(className, bytes, 0, bytes.length);
        }
    }

}

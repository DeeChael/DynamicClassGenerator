package net.deechael.dcg.generator;

import org.springframework.boot.loader.jar.JarFile;

import javax.tools.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.stream.Collectors;

public final class JJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {

    private JJavaFileObject javaFileObject;

    final Map<String, List<JavaFileObject>> classObjectPackageMap = new HashMap<>();

    private final static Map<String, List<JavaFileObject>> CLASS_OBJECT_PACKAGE_MAP = new HashMap<>();

    public JJavaFileManager(JavaFileManager fileManager) {
        super(fileManager);
    }

    public JJavaFileObject getLastJavaFileObject() {
        return javaFileObject;
    }

    public static void loadLibrary(File file) {
        try {
            JarFile jarFile = new JarFile(file);
            List<JarEntry> entries = jarFile.stream().filter(jarEntry -> jarEntry.getName().endsWith(".jar")).collect(Collectors.toList());
            JarFile libTempJarFile = null;
            List<JavaFileObject> onePackgeJavaFiles = null;
            String packgeName = null;
            for (JarEntry entry : entries) {
                libTempJarFile = jarFile.getNestedJarFile(jarFile.getEntry(entry.getName()));
                if (libTempJarFile.getName().contains("tools.jar")) {
                    continue;
                }
                Enumeration<JarEntry> tempEntriesEnum = libTempJarFile.entries();
                while (tempEntriesEnum.hasMoreElements()) {
                    JarEntry jarEntry = tempEntriesEnum.nextElement();
                    String classPath = jarEntry.getName().replace("/", ".");
                    if (!classPath.endsWith(".class")) {
                        continue;
                    }
                    packgeName = classPath.substring(0, jarEntry.getName().lastIndexOf("/"));
                    onePackgeJavaFiles = CLASS_OBJECT_PACKAGE_MAP.containsKey(packgeName) ? CLASS_OBJECT_PACKAGE_MAP.get(packgeName) : new ArrayList<>();
                    onePackgeJavaFiles.add(new LibraryJavaObject(jarEntry.getName().replace("/", ".").replace(".class", ""),
                            new URL(libTempJarFile.getUrl(), jarEntry.getName()).toURI()));
                    CLASS_OBJECT_PACKAGE_MAP.put(packgeName, onePackgeJavaFiles);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Iterable<JavaFileObject> list(Location location,
                                         String packageName,
                                         Set<JavaFileObject.Kind> kinds,
                                         boolean recurse)
            throws IOException {


        if ("CLASS_PATH".equals(location.getName())) {
            List<JavaFileObject> result = CLASS_OBJECT_PACKAGE_MAP.get(packageName);
            if (result != null) {
                return result;
            }
        }

        Iterable<JavaFileObject> it = super.list(location, packageName, kinds, recurse);

        if (kinds.contains(JavaFileObject.Kind.CLASS)) {
            final List<JavaFileObject> javaFileObjectList = classObjectPackageMap.get(packageName);
            if (javaFileObjectList != null) {
                if (it != null) {
                    for (JavaFileObject javaFileObject : it) {
                        javaFileObjectList.add(javaFileObject);
                    }
                }
                return javaFileObjectList;
            } else {
                return it;
            }
        } else {
            return it;
        }
    }

    @Override
    public String inferBinaryName(Location location, JavaFileObject file) {
        if (file instanceof LibraryJavaObject) {
            return ((LibraryJavaObject) file).inferBinaryName();
        }
        return super.inferBinaryName(location, file);
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
        return javaFileObject = new JJavaFileObject(className, kind);
    }

    static class LibraryJavaObject extends SimpleJavaFileObject {
        private final String className;
        private URL url;

        LibraryJavaObject(String className, URI uri) {
            super(uri, Kind.CLASS);
            this.className = className;
            try {
                this.url = uri.toURL();
            } catch (MalformedURLException ignored) {
            }
        }

        @Override
        public Kind getKind() {
            return JavaFileObject.Kind.valueOf("CLASS");
        }

        @Override
        public URI toUri() {
            try {
                return url.toURI();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public String getName() {
            return className;
        }

        @Override
        public InputStream openInputStream() {
            try {
                return url.openStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public OutputStream openOutputStream() throws IOException {
            return null;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            return null;
        }

        @Override
        public Writer openWriter() throws IOException {
            return null;
        }

        @Override
        public long getLastModified() {
            return 0;
        }

        @Override
        public boolean delete() {
            return false;
        }

        public String inferBinaryName() {
            return className;
        }

        @Override
        public boolean equals(Object o) {
            return false;
        }

        @Override
        public int hashCode() {
            return 0;
        }


        @Override
        public boolean isNameCompatible(String simpleName, Kind kind) {
            return false;
        }
    }

}

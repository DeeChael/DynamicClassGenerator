package net.deechael.dcg.generator;

import net.deechael.dcg.JGeneratable;
import net.deechael.dcg.annotation.TestFeature;
import net.deechael.useless.objs.DuObj;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

public final class JGenerator {

    private final static JavaCompiler COMPILER = ToolProvider.getSystemJavaCompiler();
    private final static JJavaFileManager JAVA_FILE_MANAGER = new JJavaFileManager(COMPILER.getStandardFileManager(null, null, null));

    private final static List<File> libraries = new ArrayList<>();

    public static void loadLibrary(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                if (file.getName().endsWith(".jar")) {
                    libraries.add(file);
                }
            }
        }
    }

    /**
     * If you have multiple classes, and they are using each other, generate your JClasses with this method
     *
     * @param generatables Classes to be generated
     * @return Generated classes
     */
    @TestFeature
    public static List<Class<?>> generate(JGeneratable[] generatables) {
        Iterable<String> options = null;
        if (libraries.size() > 0) {
            StringBuilder classpath_option = new StringBuilder();
            for (File library : libraries) {
                classpath_option.append(library.getPath()).append(";");
            }
            options = Arrays.asList("-classpath", classpath_option.toString());
        }
        JavaCompiler.CompilationTask task = COMPILER.getTask(null, JAVA_FILE_MANAGER, null, options, null, Arrays.stream(generatables).map(generatable -> new DuObj<>(generatable.getSimpleName(), generatable.getString())).map(obj -> {
            try {
                return new StringObject(new URI(obj.getFirst() + ".java"), JavaFileObject.Kind.SOURCE, obj.getSecond());
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList()));
        if (task.call()) {
            List<Class<?>> classes = new ArrayList<>();
            for (JJavaFileObject javaFileObject : JAVA_FILE_MANAGER.readCache()) {
                classes.add(JClassLoader.generate(javaFileObject.getName().replace(".class", ""), javaFileObject.getBytes()));
            }
            return classes;
        } else {
            throw new RuntimeException("Failed to generate the class!");
        }
    }

    public static Class<?> generate(JGeneratable generatable) throws URISyntaxException {
        Iterable<String> options = null;
        if (libraries.size() > 0) {
            StringBuilder classpath_option = new StringBuilder();
            for (File library : libraries) {
                classpath_option.append(library.getPath()).append(";");
            }
            options = Arrays.asList("-classpath", classpath_option.toString());
        }
        System.out.println(generatable.getString());
        JavaCompiler.CompilationTask task = COMPILER.getTask(null, JAVA_FILE_MANAGER, null, options, null, Collections.singletonList(new StringObject(new URI(generatable.getSimpleName() + ".java"), JavaFileObject.Kind.SOURCE, generatable.getString())));
        if (task.call()) {
            JJavaFileObject javaFileObject = JAVA_FILE_MANAGER.getLastJavaFileObject();
            return JClassLoader.generate(generatable.getName(), javaFileObject.getBytes());
        } else {
            throw new RuntimeException("Failed to generate the class!");
        }
    }

    private JGenerator() {}

}

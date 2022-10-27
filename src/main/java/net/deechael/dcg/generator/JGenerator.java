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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class JGenerator {

    private final static JavaCompiler COMPILER = ToolProvider.getSystemJavaCompiler();
    private final static JJavaFileManager JAVA_FILE_MANAGER = new JJavaFileManager(COMPILER.getStandardFileManager(null, null, null));

    private final static List<File> libraries = new ArrayList<>();

    private JGenerator() {
    }

    public static void loadLibrary(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                if (file.getName().endsWith(".jar")) {
                    libraries.add(file);
                }
            }
        }
    }

    public static void addLoader(ClassLoader classLoader) {
        JClassLoader.addLoader(classLoader);
    }

    public static List<Class<?>> generate(Iterable<JGeneratable> generatables) {
        List<JGeneratable> generatableList = new ArrayList<>();
        generatables.iterator().forEachRemaining(generatableList::add);
        return generate(generatableList.toArray(new JGeneratable[0]));
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
                return new StringObject(new URI(obj.getObject() + ".java"), JavaFileObject.Kind.SOURCE, obj.getSecond());
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

    /**
     * Compile the JClass, JInterface, JEnum, JAnnotation to a jvm Class in cache
     *
     * @param generatable the class
     * @return if there are no inner or anonymous classes the class, will return a list whose size is 1, or else is over 1
     * @since v2.00.0
     */
    public static List<Class<?>> generate(JGeneratable generatable) {
        return generate(new JGeneratable[]{generatable});
    }

}

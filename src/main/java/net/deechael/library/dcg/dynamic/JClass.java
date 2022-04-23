package net.deechael.library.dcg.dynamic;

import net.deechael.library.dcg.dynamic.generator.JClassLoader;
import net.deechael.library.dcg.dynamic.generator.JJavaFileManager;
import net.deechael.library.dcg.dynamic.generator.JJavaFileObject;
import net.deechael.library.dcg.dynamic.generator.StringObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public final class JClass implements JObject {

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

    Map<Class<?>, Map<String, JStringVar>> annotations = new HashMap<>();

    private final String packageName;

    private final String className;

    private final Level level;

    private final List<String> imports = new ArrayList<>();

    private final List<JField> fields = new ArrayList<>();
    private final List<JConstructor> constructors = new ArrayList<>();
    private final List<JMethod> methods = new ArrayList<>();

    private int extending = 0;
    private Class<?> extending_original = null;
    private JClass extending_generated = null;
    private final List<Class<?>> implementations = new ArrayList<>();

    private Class<?> generated = null;

    public JClass(@Nullable String packageName, String className, Level level) {
        this.packageName = packageName;
        this.className = className;
        this.level = level;
    }

    public void importClass(String className) {
        if (imports.contains(className)) return;
        try {
            Class<?> clazz = Class.forName(className);
            if (clazz.isPrimitive()) return;
            imports.add(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("The class not exists!");
        }
    }

    public void importClass(@NotNull JClass clazz) {
        if (imports.contains(clazz.className)) return;
        imports.add(clazz.className);
    }

    public void importClass(@NotNull Class<?> clazz) {
        if (clazz.isPrimitive()) return;
        while (clazz.isArray()) {
            clazz = clazz.getComponentType();
        }
        importClass(clazz.getName());
    }

    public String getPackage() {
        return packageName;
    }

    public String getSimpleName() {
        return className;
    }

    public JField addField(Level level, Class<?> type, String name, boolean isFinal, boolean isStatic) {
        name = "jfield_" + name;
        JField field = new JField(level, type, this, name, isFinal, isStatic);
        this.fields.add(field);
        return field;
    }

    public JConstructor addConstructor(Level level) {
        JConstructor constructor = new JConstructor(level, this);
        this.constructors.add(constructor);
        return constructor;
    }

    public JMethod addMethod(Level level, String name) {
        JMethod method = new JMethod(level, this, name);
        this.methods.add(method);
        return method;
    }

    public JMethod addMethod(Class<?> returnType, Level level, String name) {
        JMethod method = new JMethod(returnType, level, this, name);
        this.methods.add(method);
        return method;
    }

    public String getName() {
        return packageName != null ? (packageName.endsWith(".") ? packageName + className : packageName + "." + className) : className;
    }

    public JField getField(String name) {
        if (name.startsWith("jfield_")) {
            for (JField field : this.fields) {
                if (Objects.equals(field.getName(), name)) {
                    return field;
                }
            }
            if (extending == 2) {
                if (extending_generated != null) {
                    try {
                        return getField_parentClass(name, extending_generated.generate());
                    } catch (ClassNotFoundException | URISyntaxException e) {
                        throw new RuntimeException("Cannot find the field!");
                    }
                }
            }
            throw new RuntimeException("Cannot find the field!");
        } else {
            if (extending == 1) {
                if (extending_original != null) {
                    return getField_parentClass(name, extending_original);
                } else {
                    throw new RuntimeException("Cannot find the field!");
                }
            } else {
                throw new RuntimeException("Cannot find the field!");
            }
        }
    }

    private JField getField_parentClass(String name, Class<?> clazz) {
        try {
            Field field = clazz.getDeclaredField(name);
            return new JField(Modifier.isPublic(field.getModifiers()) ? Level.PUBLIC : (Modifier.isPrivate(field.getModifiers()) ? Level.PRIVATE : (Modifier.isProtected(field.getModifiers()) ? Level.PROTECTED : Level.UNNAMED)), field.getType(), this, name, Modifier.isFinal(field.getModifiers()), Modifier.isStatic(field.getModifiers()));
        } catch (NoSuchFieldException e) {
            Class<?> parent = clazz.getSuperclass();
            if (parent == null) {
                throw new RuntimeException("Cannot find the field!");
            } else {
                if (parent == Object.class) {
                    throw new RuntimeException("Cannot find the field!");
                } else {
                    return getField_parentClass(name, parent);
                }
            }
        }
    }

    private static boolean classExists(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public void extend(Class<?> clazz) {
        this.extending = 1;
        this.extending_original = clazz;
        this.extending_generated = null;
    }

    public void extend(JClass clazz) {
        this.extending = 1;
        this.extending_original = null;
        this.extending_generated = clazz;
    }

    public void implement(Class<?> clazz) {
        if (!Modifier.isInterface(clazz.getModifiers())) throw new RuntimeException("This class is not an interface");
        implementations.add(clazz);
    }

    @Override
    public String getString() {
        if (extending > 0) {
            if (extending == 1) {
                if (extending_original == null) {
                    throw new RuntimeException("The class extended a class, but cannot find the class!");
                }
            }
            if (extending == 2) {
                if (extending_generated == null) {
                    throw new RuntimeException("The class extended a class, but cannot find the class");
                }
            }
        }
        this.fields.forEach(jConstructor -> jConstructor.getExtraClasses().forEach(this::importClass));
        this.constructors.forEach(jConstructor -> jConstructor.getRequirementTypes().forEach(this::importClass));
        this.methods.forEach(jMethod -> jMethod.getRequirementTypes().forEach(this::importClass));
        StringBuilder base = new StringBuilder();
        if (packageName != null) {
            base.append("package ").append(packageName).append(";\n");
        }
        for (String importClass : imports) {
            base.append("import ").append(importClass).append(";\n");
        }
        Map<Class<?>, Map<String, JStringVar>> map = getAnnotations();
        if (!map.isEmpty()) {
            for (Map.Entry<Class<?>, Map<String, JStringVar>> entry : map.entrySet()) {
                base.append("@").append(entry.getKey().getName());
                if (!entry.getValue().isEmpty()) {
                    base.append("(");
                    List<Map.Entry<String, JStringVar>> jStringVars = new ArrayList<>(entry.getValue().entrySet());
                    for (int i = 0; i < jStringVars.size(); i++) {
                        Map.Entry<String, JStringVar> subEntry = jStringVars.get(i);
                        base.append(subEntry.getKey()).append("=").append(subEntry.getValue().varString());
                        if (i != jStringVars.size() - 1) {
                            base.append(", ");
                        }
                    }
                    base.append(")\n");
                } else {
                    base.append("\n");
                }
            }
        }
        base.append(level.getString()).append(" class ").append(className);
        if (extending == 1) {
            base.append(" extends ");
            base.append(extending_original.getName());
        } else if (extending == 2) {
            base.append(" extends ");
            base.append(extending_generated.getName());
        }
        if (implementations.size() > 0) {
            base.append(" implements ");
            Iterator<Class<?>> iterator = implementations.iterator();
            while (iterator.hasNext()) {
                base.append(iterator.next().getName());
                if (iterator.hasNext()) {
                    base.append(", ");
                }
            }
        }
        base.append(" {\n");
        for (JField field : fields) {
            base.append(field.getString()).append("\n");
        }
        for (JConstructor constructor : constructors) {
            base.append(constructor.getString()).append("\n");
        }
        for (JMethod method : methods) {
            base.append(method.getString()).append("\n");
        }
        base.append("}\n");
        return base.toString();
    }

    @Override
    public void addAnnotation(Class<?> annotation, Map<String, JStringVar> values) {
        if (!annotation.isAnnotation()) throw new RuntimeException("The class is not an annotation!");
        Target target = annotation.getAnnotation(Target.class);
        if (target != null) {
            boolean hasConstructor = false;
            for (ElementType elementType : target.value()) {
                if (elementType == ElementType.TYPE) {
                    hasConstructor = true;
                    break;
                }
            }
            if (!hasConstructor) throw new RuntimeException("This annotation is not for class!");
        }
        getAnnotations().put(annotation, values);
    }

    public boolean isGenerated() {
        return generated != null;
    }

    public Class<?> generate() throws ClassNotFoundException, URISyntaxException {
        return isGenerated() ? generated : forceGenerate();
    }

    public Class<?> forceGenerate() throws ClassNotFoundException, URISyntaxException {
        Iterable<String> options = null;
        if (libraries.size() > 0) {
            StringBuilder classpath_option = new StringBuilder();
            for (File library : libraries) {
                classpath_option.append(library.getPath()).append(";");
            }
            options = Arrays.asList("-classpath", classpath_option.toString());
        }

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        JJavaFileManager jJavaFileManager = new JJavaFileManager(compiler.getStandardFileManager(null, null, null));
        JavaCompiler.CompilationTask task = compiler.getTask(null, jJavaFileManager, null, options, null, Collections.singletonList(new StringObject(new URI(className + ".java"), JavaFileObject.Kind.SOURCE, getString())));
        if (task.call()) {
            JJavaFileObject javaFileObject = jJavaFileManager.getLastJavaFileObject();
            String className;
            if (packageName != null) {
                className = packageName + "." + this.className;
            } else {
                className = this.className;
            }
            Class<?> generated = JClassLoader.generate(className, javaFileObject.getBytes());
            this.generated = generated;
            return generated;
        } else {
            throw new RuntimeException("Failed to generate the class!");
        }
    }

    @Override
    public Map<Class<?>, Map<String, JStringVar>> getAnnotations() {
        return annotations;
    }

}

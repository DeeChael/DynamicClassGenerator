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
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.regex.Pattern;

public final class JClass implements JObject {

    Map<Class<?>, Map<String, JStringVar>> annotations = new HashMap<>();

    private final String packageName;

    private final String className;

    private final Level level;

    private final List<String> imports = new ArrayList<>();

    private final List<JField> fields = new ArrayList<>();
    private final List<JConstructor> constructors = new ArrayList<>();
    private final List<JMethod> methods = new ArrayList<>();

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
        if (!Pattern.matches("^[A-Za-z_$]+[A-Za-z_$\\d]+$", name)) throw new RuntimeException("The field name not allowed!");
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
        if (!Pattern.matches("^[A-Za-z_$]+[A-Za-z_$\\d]+$", name)) throw new RuntimeException("The method name not allowed!");
        name = "jmethod_" + name;
        JMethod method = new JMethod(level, this, name);
        this.methods.add(method);
        return method;
    }

    public String getName() {
        return packageName != null ? (packageName.endsWith(".") ? packageName + className : packageName + "." + className) : className;
    }

    private static boolean classExists(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public String getString() {
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
        base.append(level.getString()).append(" class ").append(className).append(" {\n");
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

    public Class<?> generate() throws ClassNotFoundException, URISyntaxException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager javaFileManager = compiler.getStandardFileManager(null, null, null);
        JJavaFileManager jJavaFileManager = new JJavaFileManager(javaFileManager);
        JavaCompiler.CompilationTask task = compiler.getTask(null, jJavaFileManager, null, null, null, Collections.singletonList(new StringObject(new URI(className + ".java"), JavaFileObject.Kind.SOURCE, getString())));
        if (task.call()) {
            JJavaFileObject javaFileObject = jJavaFileManager.getJavaFileObject();
            String className = null;
            if (packageName != null) {
                className = packageName + "." + this.className;
            } else {
                className = this.className;
            }
            ClassLoader classLoader = new JClassLoader(className, javaFileObject.getBytes());
            return classLoader.loadClass(className);
        } else {
            throw new RuntimeException("Failed to generate the class!");
        }
    }

    @Override
    public Map<Class<?>, Map<String, JStringVar>> getAnnotations() {
        return annotations;
    }

}

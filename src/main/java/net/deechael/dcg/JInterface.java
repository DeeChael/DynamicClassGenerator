package net.deechael.dcg;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.Modifier;
import java.util.*;

public final class JInterface implements JGeneratable, JObject {

    private final List<String> imports = new ArrayList<>();

    private final Level level;
    private final String packageName;
    private final String className;

    Map<Class<?>, Map<String, JStringVar>> annotations = new HashMap<>();

    private final List<Class<?>> extensions = new ArrayList<>();

    private final List<InterfaceMethod> methods = new ArrayList<>();

    public JInterface(Level level, @Nullable String packageName, String className) {
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

    public void importClass(@NotNull Class<?> clazz) {
        if (clazz.isPrimitive()) return;
        while (clazz.isArray()) {
            clazz = clazz.getComponentType();
        }
        importClass(clazz.getName());
    }

    public JInterfaceMethod addMethod(Class<?> returnType, String methodName) {
        JInterfaceMethod method = new JInterfaceMethod(returnType, methodName);
        this.methods.add(method);
        return method;
    }

    public JInterfaceMethod addMethod(String methodName) {
        JInterfaceMethod method = new JInterfaceMethod(methodName);
        this.methods.add(method);
        return method;
    }

    public JInterfaceMethodDefaulted addDefaultedMethod(Class<?> returnType, String methodName) {
        JInterfaceMethodDefaulted method = new JInterfaceMethodDefaulted(returnType, methodName);
        this.methods.add(method);
        return method;
    }

    public JInterfaceMethodDefaulted addDefaultedMethod(String methodName) {
        JInterfaceMethodDefaulted method = new JInterfaceMethodDefaulted(methodName);
        this.methods.add(method);
        return method;
    }


    public String getPackage() {
        return packageName;
    }

    public String getSimpleName() {
        return className;
    }

    public void extend(Class<?> interfaceClass) {
        if (!Modifier.isInterface(interfaceClass.getModifiers())) throw new RuntimeException("This class is not an interface");
        extensions.add(interfaceClass);
    }

    @Override
    public String getString() {
        StringBuilder base = new StringBuilder();
        if (packageName != null) base.append("package ").append(packageName).append(";\n");
        for (String importClass : imports) {
            base.append("import ").append(importClass).append(";\n");
        }
        base.append(this.annotationString()).append(level.getString()).append(" class ").append(className);
        if (extensions.size() > 0) {
            base.append(" extends ");
            Iterator<Class<?>> iterator = extensions.iterator();
            while (iterator.hasNext()) {
                base.append(iterator.next().getName());
                if (iterator.hasNext()) {
                    base.append(", ");
                }
            }
        }
        base.append(" {\n");
        methods.forEach(jInterfaceMethod -> base.append(jInterfaceMethod.getString()).append("\n"));
        base.append("}\n");
        return base.toString();
    }

    @Override
    public String getName() {
        return packageName != null ? packageName + "." + className : className;
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

    @Override
    public Map<Class<?>, Map<String, JStringVar>> getAnnotations() {
        return annotations;
    }

}

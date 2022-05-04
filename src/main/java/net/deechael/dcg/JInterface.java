package net.deechael.dcg;

import net.deechael.dcg.items.Var;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.Modifier;
import java.util.*;

public final class JInterface implements JGeneratable, JObject, Var {

    private final List<String> imports = new ArrayList<>();

    Level level;
    private final String packageName;
    private final String className;

    Map<Class<?>, Map<String, JStringVar>> annotations = new HashMap<>();

    private final List<String> extensions = new ArrayList<>();

    private final List<InterfaceMethod> methods = new ArrayList<>();

    private final List<JGeneratable> innerClasses = new ArrayList<>();
    private boolean inner = false;

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

    public void importClass(@NotNull JGeneratable clazz) {
        if (imports.contains(clazz.getName())) return;
        this.imports.add(clazz.getName());
    }

    public JInterfaceMethod addMethod(Class<?> returnType, String methodName) {
        JInterfaceMethod method = new JInterfaceMethod(returnType, methodName);
        this.methods.add(method);
        return method;
    }

    public JInterfaceMethod addMethod(JGeneratable returnType, String methodName) {
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

    public JInterfaceMethodDefaulted addDefaultedMethod(JGeneratable returnType, String methodName) {
        JInterfaceMethodDefaulted method = new JInterfaceMethodDefaulted(returnType, methodName);
        this.methods.add(method);
        return method;
    }

    public JInterfaceMethodDefaulted addDefaultedMethod(String methodName) {
        JInterfaceMethodDefaulted method = new JInterfaceMethodDefaulted(methodName);
        this.methods.add(method);
        return method;
    }

    void setInner() {
        this.inner = true;
    }

    public void extend(Class<?> interfaceClass) {
        if (!Modifier.isInterface(interfaceClass.getModifiers())) throw new RuntimeException("This class is not an interface");
        if (extensions.contains(interfaceClass.getName())) return;
        extensions.add(interfaceClass.getName());
    }

    public void extend(JInterface interfaceClass) {
        if (extensions.contains(interfaceClass.getName())) return;
        extensions.add(interfaceClass.getName());
    }

    public void addInner(JGeneratable generatable) {
        if (generatable.getLevel() == Level.PRIVATE || generatable.getLevel() == Level.PROTECTED) throw new RuntimeException("Interface not supports private or protected inner classes");
        if (generatable instanceof JClass) {
            ((JClass) generatable).setInner();
            ((JClass) generatable).level = Level.UNNAMED;
            this.innerClasses.add(generatable);
        } else if (generatable instanceof JInterface) {
            ((JInterface) generatable).setInner();
            ((JInterface) generatable).level = Level.UNNAMED;
            this.innerClasses.add(generatable);
        } else if (generatable instanceof JEnum) {
            ((JEnum) generatable).setInner();
            ((JEnum) generatable).level = Level.UNNAMED;
            this.innerClasses.add(generatable);
        }
    }
    public String getPackage() {
        return packageName;
    }

    public String getSimpleName() {
        return className;
    }

    @Override
    public Level getLevel() {
        return level;
    }

    @Override
    public String getString() {
        StringBuilder base = new StringBuilder();
        if (packageName != null) base.append("package ").append(packageName).append(";\n");
        for (String importClass : imports) {
            base.append("import ").append(importClass).append(";\n");
        }
        base.append(this.annotationString()).append(level.getString());
        if (this.inner) {
            base.append(" static");
        }
        base.append(" interface ").append(className);
        if (extensions.size() > 0) {
            base.append(" extends ");
            Iterator<String> iterator = extensions.iterator();
            while (iterator.hasNext()) {
                base.append(iterator.next().replace("$", "."));
                if (iterator.hasNext()) {
                    base.append(", ");
                }
            }
        }
        base.append(" {\n");
        methods.forEach(jInterfaceMethod -> base.append(jInterfaceMethod.getString()).append("\n"));
        innerClasses.forEach(innerClass -> base.append(innerClass.getString()).append("\n"));
        base.append("}\n");
        return base.toString();
    }

    @Override
    public Class<?> getType() {
        throw new RuntimeException("Generate JInterface to get type");
    }

    @Override
    public String getName() {
        return packageName != null ? packageName + "." + className : className;
    }

    @Override
    public String varString() {
        return this.getName() + ".class";
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

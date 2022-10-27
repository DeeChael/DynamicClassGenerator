package net.deechael.dcg;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class JInterface implements JType, JGeneratable, JObject, Var {

    private final List<String> imports = new ArrayList<>();
    private final String packageName;
    private final String className;
    private final List<String> extensions = new ArrayList<>();
    private final List<InterfaceMethod> methods = new ArrayList<>();
    private final List<JGeneratable> innerClasses = new ArrayList<>();
    Level level;
    Map<JType, Map<String, JStringVar>> annotations = new HashMap<>();
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

    public JInterfaceMethod addMethod(JType returnType, String methodName) {
        JInterfaceMethod method = new JInterfaceMethod(returnType, methodName);
        this.methods.add(method);
        return method;
    }

    public JInterfaceMethod addMethod(String methodName) {
        JInterfaceMethod method = new JInterfaceMethod(methodName);
        this.methods.add(method);
        return method;
    }

    public JInterfaceMethodDefaulted addDefaultedMethod(JType returnType, String methodName) {
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

    public void extend(JType interfaceClass) {
        if (extensions.contains(interfaceClass.typeString())) return;
        extensions.add(interfaceClass.typeString());
    }

    public void addInner(JGeneratable generatable) {
        if (generatable.getLevel() == Level.PRIVATE || generatable.getLevel() == Level.PROTECTED)
            throw new RuntimeException("Interface not supports private or protected inner classes");
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
        } else if (generatable instanceof JAnnotation) {
            ((JAnnotation) generatable).setInner();
            ((JAnnotation) generatable).level = Level.UNNAMED;
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
        base.append(this.annotationString(getAnnotations())).append(level.getString());
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
    public JType getType() {
        return JType.CLASS;
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
    public void addAnnotation(JType annotation, Map<String, JStringVar> values) {
        getAnnotations().put(annotation, values);
    }

    @Override
    public Map<JType, Map<String, JStringVar>> getAnnotations() {
        return annotations;
    }

    @Override
    public String typeString() {
        return this.getName();
    }

    public static class Builder {

        private final Level level;
        private String packageName = null;
        private String className = null;

        private Builder(Level level) {
            this.level = level;
        }

        public Builder withPackage(String name) {
            this.packageName = name;
            return this;
        }

        public Builder withName(String name) {
            this.className = name;
            return this;
        }

        public JInterface build() {
            if (className == null)
                throw new RuntimeException("The class name cannot be null!");
            return new JInterface(level, packageName, className);
        }

        public static Builder ofPublic() {
            return new Builder(Level.PUBLIC);
        }

        public static Builder ofPrivate() {
            return new Builder(Level.PRIVATE);
        }

        public static Builder ofProtected() {
            return new Builder(Level.PROTECTED);
        }

        public static Builder of() {
            return new Builder(Level.UNNAMED);
        }

    }

}

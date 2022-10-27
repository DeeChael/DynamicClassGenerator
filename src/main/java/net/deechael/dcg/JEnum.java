package net.deechael.dcg;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.Map.Entry;

public final class JEnum implements JObject, JGeneratable, JType, Var, FieldOwnable, MethodOwnable, ConstructorOwnable {

    private final String packageName;
    private final String className;
    private final List<String> imports = new ArrayList<>();
    private final List<String> importStatics = new ArrayList<>();
    private final List<JField> fields = new ArrayList<>();
    private final List<JConstructor> constructors = new ArrayList<>();
    private final List<ClassMethod> methods = new ArrayList<>();
    private final List<String> implementations = new ArrayList<>();
    private final List<JGeneratable> innerClasses = new ArrayList<>();
    private final List<Entry<String, Var[]>> elements = new ArrayList<>();
    Map<JType, Map<String, JStringVar>> annotations = new HashMap<>();
    Level level;
    private boolean inner = false;

    public JEnum(Level level, @Nullable String packageName, String className) {
        this.level = level;
        this.packageName = packageName;
        this.className = className;
    }

    public void importClass(String className) {
        if (imports.contains(className)) return;
        try {
            Class<?> clazz = Class.forName(className);
            if (clazz.isPrimitive()) return;
            imports.add(className);
        } catch (ClassNotFoundException e) {
            imports.add(className);
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

    public void importStatic(String path) {
        if (importStatics.contains(path)) return;
        importStatics.add(path);
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

    public JField addField(Level level, JType type, String name, boolean isFinal, boolean isStatic) {
        name = "jfield_" + name;
        JField field = new JField(level, type, this, name, isFinal, isStatic);
        this.fields.add(field);
        return field;
    }

    public JConstructor addConstructor() {
        JConstructor constructor = new JConstructor(Level.PRIVATE, this);
        this.constructors.add(constructor);
        return constructor;
    }

    public void addElement(String name, Var... vars) {
        for (Entry<String, Var[]> entry : this.elements) {
            if (entry.getKey().equals(name)) return;
        }
        this.elements.add(new AbstractMap.SimpleEntry<>(name, vars));
    }

    void setInner() {
        this.inner = true;
    }

    public JMethod addMethod(Level level, String name, boolean isStatic, boolean isSynchronized) {
        JMethod method = new JMethod(level, this, name, isStatic, false, isSynchronized);
        this.methods.add(method);
        return method;
    }

    public JMethod addMethod(JType returnType, Level level, String name, boolean isStatic, boolean isSynchronized) {
        JMethod method = new JMethod(returnType, level, this, name, isStatic, false, isSynchronized);
        this.methods.add(method);
        return method;
    }

    public JNativeMethod addNativeMethod(JType returnType, Level level, String name, boolean isStatic) {
        JNativeMethod method = new JNativeMethod(level, returnType, name, false, isStatic);
        this.methods.add(method);
        return method;
    }

    @Override
    public JType getType() {
        return JType.CLASS;
    }

    public String getName() {
        return packageName != null ? (packageName.endsWith(".") ? packageName + className : packageName + "." + className) : className;
    }

    @Override
    public String varString() {
        return this.getName() + ".class";
    }

    public JField getField(String name) {
        return new JField(null, null, this, name, false, false);
    }

    public void implement(JType clazz) {
        if (implementations.contains(clazz.typeString())) return;
        implementations.add(clazz.typeString());
    }

    public void addInner(JGeneratable generatable) {
        if (generatable instanceof JClass) {
            ((JClass) generatable).setInner();
            this.innerClasses.add(generatable);
        } else if (generatable instanceof JInterface) {
            ((JInterface) generatable).setInner();
            this.innerClasses.add(generatable);
        } else if (generatable instanceof JEnum) {
            ((JEnum) generatable).setInner();
            this.innerClasses.add(generatable);
        } else if (generatable instanceof JAnnotation) {
            ((JAnnotation) generatable).setInner();
            this.innerClasses.add(generatable);
        }
    }

    public Var valueOf(String name) {
        return Var.enumVar(this, name);
    }

    private void elementsSolver(StringBuilder base) {
        if (this.elements.size() > 0) {
            Iterator<Entry<String, Var[]>> iterator = this.elements.iterator();
            if (this.constructors.size() == 0) {
                while (iterator.hasNext()) {
                    base.append(iterator.next().getKey());
                    if (iterator.hasNext()) {
                        base.append(", ");
                    }
                }
            } else {
                while (iterator.hasNext()) {
                    Entry<String, Var[]> entry = iterator.next();
                    Var[] vars = entry.getValue();
                    base.append(entry.getKey());
                    if (vars.length > 0) {
                        base.append("(");
                        Iterator<Var> varIterator = Arrays.stream(vars).iterator();
                        while (varIterator.hasNext()) {
                            base.append(varIterator.next().varString());
                            if (varIterator.hasNext()) {
                                base.append(", ");
                            }
                        }
                        base.append(")");
                    }
                    if (iterator.hasNext()) {
                        base.append(", ");
                    }
                }
            }
            base.append(";");
        }
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
    public String getString() {
        StringBuilder base = new StringBuilder();
        if (packageName != null) base.append("package ").append(packageName).append(";\n");
        imports.forEach(importClass -> base.append("import ").append(importClass).append(";\n"));
        importStatics.forEach(importStatic -> base.append("import static ").append(importStatic).append(";\n"));
        base.append(this.annotationString(getAnnotations())).append(level.getString());
        if (this.inner) {
            base.append(" static");
        }
        base.append(" enum ").append(className);
        if (implementations.size() > 0) {
            base.append(" implements ");
            Iterator<String> iterator = implementations.iterator();
            while (iterator.hasNext()) {
                base.append(iterator.next().replace("$", "."));
                if (iterator.hasNext()) {
                    base.append(", ");
                }
            }
        }
        base.append(" {\n");
        elementsSolver(base);
        fields.forEach(field -> base.append(field.getString()).append("\n"));
        constructors.forEach(constructor -> base.append(constructor.getString()).append("\n"));
        methods.forEach(method -> base.append(method.getString()).append("\n"));
        innerClasses.forEach(innerClass -> base.append(innerClass.getString()).append("\n"));
        base.append("}\n");
        return base.toString();
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

        public JEnum build() {
            if (className == null)
                throw new RuntimeException("The class name cannot be null!");
            return new JEnum(level, packageName, className);
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

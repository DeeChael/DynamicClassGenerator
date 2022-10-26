package net.deechael.dcg;

import net.deechael.dcg.items.Var;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class JClass implements JObject, JGeneratable, JType, Var, FieldOwnable, MethodOwnable, ConstructorOwnable {

    private final String packageName;
    private final String className;
    private final List<String> imports = new ArrayList<>();
    private final List<String> importStatics = new ArrayList<>();
    private final List<JField> fields = new ArrayList<>();
    private final List<JConstructor> constructors = new ArrayList<>();
    private final List<ClassMethod> methods = new ArrayList<>();
    private final List<String> implementations = new ArrayList<>();
    private final List<JGeneratable> innerClasses = new ArrayList<>();
    Map<JType, Map<String, JStringVar>> annotations = new HashMap<>();
    Level level;
    boolean isAbstract;
    boolean isFinal;
    private boolean extending = false;
    private JType extended = null;
    private boolean inner = false;

    JClass(Level level, @Nullable String packageName, String className) {
        this(level, packageName, className, false, false);
    }

    JClass(Level level, @Nullable String packageName, String className, boolean isAbstract) {
        this(level, packageName, className, isAbstract, false);
    }

    public JClass(Level level, @Nullable String packageName, String className, boolean isAbstract, boolean isFinal) {
        this.level = level;
        this.packageName = packageName;
        this.className = className;
        if (isAbstract) {
            this.isAbstract = true;
            this.isFinal = false;
        } else if (isFinal) {
            this.isAbstract = false;
            this.isFinal = true;
        } else {
            this.isAbstract = false;
            this.isFinal = false;
        }
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

    public JConstructor addConstructor(Level level) {
        JConstructor constructor = new JConstructor(level, this);
        this.constructors.add(constructor);
        return constructor;
    }

    void setInner() {
        this.inner = true;
    }

    public JMethod addMethod(Level level, String name, boolean isStatic, boolean isFinal, boolean isSynchronized) {
        JMethod method = new JMethod(level, this, name, isStatic, isFinal, isSynchronized);
        this.methods.add(method);
        return method;
    }

    public JMethod addMethod(JType returnType, Level level, String name, boolean isStatic, boolean isFinal, boolean isSynchronized) {
        JMethod method = new JMethod(returnType, level, this, name, isStatic, isFinal, isSynchronized);
        this.methods.add(method);
        return method;
    }

    public JAbstractMethod addAbstractMethod(JType returnType, Level level, String name) {
        JAbstractMethod method = new JAbstractMethod(level, returnType, name);
        this.methods.add(method);
        return method;
    }

    public JNativeMethod addNativeMethod(JType returnType, Level level, String name, boolean isStatic, boolean isFinal) {
        JNativeMethod method = new JNativeMethod(level, returnType, name, isFinal, isStatic);
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

    public void extend(JType clazz) {
        this.extending = true;
        this.extended = clazz;
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

    private StringBuilder appendExtendsAndImplements(StringBuilder stringBuilder) {
        if (extending) {
            stringBuilder.append(" extends ");
            stringBuilder.append(extended.typeString());
        }
        if (implementations.size() > 0) {
            stringBuilder.append(" implements ");
            Iterator<String> iterator = implementations.iterator();
            while (iterator.hasNext()) {
                stringBuilder.append(iterator.next());
                if (iterator.hasNext()) {
                    stringBuilder.append(", ");
                }
            }
        }
        return stringBuilder;
    }

    private StringBuilder appendString(StringBuilder stringBuilder, Collection<? extends JObject> objects) {
        for (JObject object : objects) {
            stringBuilder.append(object.getString()).append("\n");
        }
        return stringBuilder;
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
        if (isAbstract) {
            base.append(" abstract");
        } else if (isFinal) {
            base.append(" final");
        }
        base.append(" class ").append(className);
        appendExtendsAndImplements(base).append(" {\n");
        appendString(base, fields);
        appendString(base, constructors);
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

        private Level level;
        private String packageName = null;
        private String className = null;
        private boolean isAbstract;
        private boolean isFinal;

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

        public Builder withAbstract() {
            this.isAbstract = true;
            return this;
        }

        public Builder withFinal() {
            this.isFinal = true;
            return this;
        }

        public JClass build() {
            if (className == null)
                throw new RuntimeException("The class name cannot be null!");
            return new JClass(level, packageName, className, isAbstract, isFinal);
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

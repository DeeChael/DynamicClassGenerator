package net.deechael.dcg;

import net.deechael.dcg.items.Var;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public final class JClass implements JObject, JGeneratable, Var, FieldOwnable, MethodOwnable, ConstructorOwnable {

    Map<Class<?>, Map<String, JStringVar>> annotations = new HashMap<>();

    private final String packageName;

    private final String className;

    Level level;

    private final List<String> imports = new ArrayList<>();
    private final List<String> importStatics = new ArrayList<>();

    private final List<JField> fields = new ArrayList<>();
    private final List<JConstructor> constructors = new ArrayList<>();
    private final List<ClassMethod> methods = new ArrayList<>();

    private int extending = 0;
    private Class<?> extending_original = null;
    private JClass extending_generated = null;
    private final List<String> implementations = new ArrayList<>();

    private final List<JGeneratable> innerClasses = new ArrayList<>();
    private boolean inner = false;

    boolean isAbstract;
    boolean isFinal;

    public JClass(Level level, @Nullable String packageName, String className) {
        this(level, packageName, className, false, false);
    }

    public JClass(Level level, @Nullable String packageName, String className, boolean isAbstract) {
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

    void setInner() {
        this.inner = true;
    }

    public JMethod addMethod(Level level, String name, boolean isStatic, boolean isFinal, boolean isSynchronized) {
        JMethod method = new JMethod(level, this, name, isStatic, isFinal, isSynchronized);
        this.methods.add(method);
        return method;
    }

    public JMethod addMethod(Class<?> returnType, Level level, String name, boolean isStatic, boolean isFinal, boolean isSynchronized) {
        JMethod method = new JMethod(returnType, level, this, name, isStatic, isFinal, isSynchronized);
        this.methods.add(method);
        return method;
    }

    public JAbstractMethod addAbstractMethod(Class<?> returnType, Level level, String name) {
        JAbstractMethod method = new JAbstractMethod(level, returnType, name);
        this.methods.add(method);
        return method;
    }

    public JNativeMethod addNativeMethod(Class<?> returnType, Level level, String name, boolean isStatic, boolean isFinal) {
        JNativeMethod method = new JNativeMethod(level, returnType, name, isFinal, isStatic);
        this.methods.add(method);
        return method;
    }

    @Override
    public Class<?> getType() {
        throw new RuntimeException("Generate JClass to get type");
    }

    public String getName() {
        return packageName != null ? (packageName.endsWith(".") ? packageName + className : packageName + "." + className) : className;
    }

    @Override
    public String varString() {
        return this.getName() + ".class";
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
                    return getField_jClass(name, extending_generated);
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

    private JField getField_jClass(String name, JClass parent) {
        return parent.getField(name);
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
        if (implementations.contains(clazz.getName())) return;
        implementations.add(clazz.getName());
    }

    public void implement(JInterface clazz) {
        if (implementations.contains(clazz.getName())) return;
        implementations.add(clazz.getName());
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
        }
    }

    private void securityCheck() {
        if (extending > 0) {
            if (extending == 1 && extending_original == null) {
                throw new RuntimeException("The class extended a class, but cannot find the class!");
            } else if (extending == 2 && extending_generated == null) {
                throw new RuntimeException("The class extended a class, but cannot find the class");
            }
        }
    }

    private StringBuilder appendExtendsAndImplements(StringBuilder stringBuilder) {
        if (extending == 1) {
            stringBuilder.append(" extends ");
            stringBuilder.append(extending_original.getName().replace("$", "."));
        } else if (extending == 2) {
            stringBuilder.append(" extends ");
            stringBuilder.append(extending_generated.getName());
        }
        if (implementations.size() > 0) {
            stringBuilder.append(" implements ");
            Iterator<String> iterator = implementations.iterator();
            while (iterator.hasNext()) {
                stringBuilder.append(iterator.next().replace("$", "."));
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

    @Override
    public String getString() {
        securityCheck();
        StringBuilder base = new StringBuilder();
        if (packageName != null) base.append("package ").append(packageName).append(";\n");
        imports.forEach(importClass -> base.append("import ").append(importClass).append(";\n"));
        importStatics.forEach(importStatic -> base.append("import static ").append(importStatic).append(";\n"));
        base.append(this.annotationString()).append(level.getString());
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

}

package net.deechael.library.dcg.dynamic;

import net.deechael.library.dcg.dynamic.items.UsingStaticMethodAsVar;
import net.deechael.library.dcg.dynamic.items.Var;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JField extends Var implements JObject {

    Map<Class<?>, Map<String, JStringVar>> annotations = new HashMap<>();

    final Level level;
    final Class<?> type;
    final JClass parent;
    final String name;
    final boolean isFinal;
    final boolean isStatic;

    private boolean needInit = false;
    private String initBody = "";

    private final List<Class<?>> extraClasses = new ArrayList<>();

    JField(Level level, Class<?> type, JClass clazz, String name, boolean isFinal, boolean isStatic) {
        super(type, name);
        this.level = level;
        this.type = type;
        this.parent = clazz;
        this.name = name;
        this.isFinal = isFinal;
        this.isStatic = isStatic;
    }

    public void initializeByValue(JStringVar value) {
        if (!JStringVar.isSupport(this.type)) {
            throw new RuntimeException("The type of the field is not support by JStringVar so this field cannot be initialized by value!");
        }
        Class<?> type = value.getType();
        while (type.isArray()) {
            type = type.getComponentType();
        }
        this.extraClasses.add(type);
        needInit = true;
        initBody = "(" + value.varString() + ")";
    }

    public void initializeByStaticMethod(Class<?> clazz, String methodName, JStringVar... arguments) {
        boolean hasMethod = false;
        Method result = null;
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(methodName) && Modifier.isStatic(method.getModifiers())) {
                hasMethod = true;
                result = method;
                break;
            }
        }
        if (!hasMethod) {
            throw new RuntimeException("Unknown method of the class " + clazz.getName() + ": " + methodName + "(...);");
        }
        StringBuilder bodyBuilder = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            bodyBuilder.append(arguments[i].varString());
            if (i != arguments.length - 1) {
                bodyBuilder.append(", ");
            }
        }
        for (JStringVar argument : arguments) {
            Class<?> type = argument.getType();
            while (type.isArray()) {
                type = type.getComponentType();
            }
            this.extraClasses.add(type);
        }
        needInit = true;
        initBody = new UsingStaticMethodAsVar(result, bodyBuilder.toString()).varString();
    }

    public void initializeByConstructor(JStringVar... vars) {
        needInit = true;
        StringBuilder argumentBody = new StringBuilder();
        for (int i = 0; i < vars.length; i++) {
            argumentBody.append("(");
            argumentBody.append(vars[i].varString());
            argumentBody.append(")");
            if (i != vars.length - 1) {
                argumentBody.append(", ");
            }
        }
        for (JStringVar argument : vars) {
            Class<?> type = argument.getType();
            while (type.isArray()) {
                type = type.getComponentType();
            }
            this.extraClasses.add(type);
        }
        initBody = "new " + type.getName() + "(" + argumentBody + ")";
    }

    @Override
    public String varString() {
        return "this." + name;
    }

    @Override
    public String getString() {
        StringBuilder base = new StringBuilder();
        base.append(level.getString());
        base.append(" ");
        if (isStatic) {
            base.append("static ");
        }
        if (isFinal) {
            base.append("final ");
        }
        base.append(type.getName());
        base.append(" ");
        base.append(name);
        if (needInit) {
            base.append(" = ");
            base.append(initBody);
        }
        base.append(";");
        return base.toString();
    }

    @Override
    public void addAnnotation(Class<?> annotation, Map<String, JStringVar> values) {
        if (!annotation.isAnnotation()) throw new RuntimeException("The class is not an annotation!");
        Target target = annotation.getAnnotation(Target.class);
        if (target != null) {
            boolean hasConstructor = false;
            for (ElementType elementType : target.value()) {
                if (elementType == ElementType.FIELD) {
                    hasConstructor = true;
                    break;
                }
            }
            if (!hasConstructor) throw new RuntimeException("This annotation is not for field!");
        }
        getAnnotations().put(annotation, values);
    }

    public Map<Class<?>, Map<String, JStringVar>> getAnnotations() {
        return annotations;
    }

    public Class<?> getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public List<Class<?>> getExtraClasses() {
        return extraClasses;
    }

}

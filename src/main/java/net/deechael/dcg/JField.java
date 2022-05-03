package net.deechael.dcg;

import net.deechael.dcg.items.Var;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JField implements JObject, Var {

    Map<Class<?>, Map<String, JStringVar>> annotations = new HashMap<>();

    final Level level;
    final Class<?> type;
    final JGeneratable jType;
    final JClass parent;
    final String name;
    final boolean isFinal;
    final boolean isStatic;

    private boolean needInit = false;
    private String initBody = "";

    JField(Level level, Class<?> type, JClass clazz, String name, boolean isFinal, boolean isStatic) {
        this.level = level;
        this.type = type;
        this.jType = null;
        this.parent = clazz;
        this.name = name;
        this.isFinal = isFinal;
        this.isStatic = isStatic;
    }

    JField(Level level, JGeneratable type, JClass clazz, String name, boolean isFinal, boolean isStatic) {
        this.level = level;
        this.type = null;
        this.jType = type;
        this.parent = clazz;
        this.name = name;
        this.isFinal = isFinal;
        this.isStatic = isStatic;
    }

    public void initialize(Var var) {
        needInit = true;
        initBody = var.varString();
    }

    public boolean isStatic() {
        return isStatic;
    }

    public JClass getParent() {
        return parent;
    }

    @Override
    public String varString() {
        if (this.isStatic()) {
            return this.parent.getName() + "." + name;
        } else {
            return "this." + name;
        }
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
        if (type != null) {
            base.append(type.getName());
        } else if (jType != null) {
            base.append(jType.getName());
        }
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
        if (this.type != null) {
            return this.type;
        } else {
            throw new RuntimeException("This is a JGeneratable type field");
        }
    }

    public String getName() {
        return name;
    }

}

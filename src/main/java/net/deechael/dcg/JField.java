package net.deechael.dcg;

import net.deechael.dcg.items.Var;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;

public class JField implements JObject, Var {

    Map<JType, Map<String, JStringVar>> annotations = new HashMap<>();

    final Level level;
    final JType type;
    final JType jType;
    final FieldOwnable parent;
    final String name;
    final boolean isFinal;
    final boolean isStatic;

    private boolean needInit = false;
    private String initBody = "";

    JField(Level level, JType type, FieldOwnable clazz, String name, boolean isFinal, boolean isStatic) {
        this.level = level;
        this.type = type;
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

    public FieldOwnable getParent() {
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
            base.append(type.typeString());
        } else if (jType != null) {
            base.append(jType.typeString());
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
    public void addAnnotation(JType annotation, Map<String, JStringVar> values) {
        getAnnotations().put(annotation, values);
    }

    @Override
    public Map<JType, Map<String, JStringVar>> getAnnotations() {
        return annotations;
    }

    public JType getType() {
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

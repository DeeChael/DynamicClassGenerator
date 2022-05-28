package net.deechael.dcg;

import net.deechael.dcg.items.Var;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JAnnotation implements JObject, JGeneratable, JType, Var {

    Map<JType, Map<String, JStringVar>> annotations = new HashMap<>();

    private final String packageName;

    private final String className;

    Level level;

    private final List<String> imports = new ArrayList<>();
    private final List<String> importStatics = new ArrayList<>();


    private boolean inner = false;


    private final List<JAnnotationParameter> parameters = new ArrayList<>();

    public JAnnotation(Level level, @Nullable String packageName, @NotNull String className) {
        this.level = level;
        this.packageName = packageName;
        this.className = className;
    }

    void setInner() {
        this.inner = true;
    }

    public JAnnotationParameter addParameter(JType type, String name) {
        JAnnotationParameter parameter = new JAnnotationParameter(type, name);
        this.parameters.add(parameter);
        return parameter;
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
        StringBuilder builder = new StringBuilder();

        if (packageName != null) builder.append("package ").append(packageName).append(";\n");
        imports.forEach(importClass -> builder.append("import ").append(importClass).append(";\n"));
        importStatics.forEach(importStatic -> builder.append("import static ").append(importStatic).append(";\n"));
        builder.append(this.annotationString()).append(level.getString());
        if (this.inner) {
            builder.append(" static");
        }
        builder.append(" class ").append(className).append(" {\n");
        parameters.forEach(parameter -> builder.append(parameter.getString()).append("\n"));
        builder.append("}\n");
        return builder.toString();
    }

    @Override
    public String typeString() {
        return this.getName();
    }

}

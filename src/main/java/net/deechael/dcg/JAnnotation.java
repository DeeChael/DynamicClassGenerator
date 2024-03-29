package net.deechael.dcg;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JAnnotation implements JObject, JGeneratable, JType, Var {

    private final String packageName;
    private final String className;
    private final List<String> imports = new ArrayList<>();
    private final List<String> importStatics = new ArrayList<>();
    private final List<JAnnotationParameter> parameters = new ArrayList<>();
    Map<JType, Map<String, JStringVar>> annotations = new HashMap<>();
    Level level;
    private boolean inner = false;

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
        builder.append(this.annotationString(getAnnotations())).append(level.getString());
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

        public JAnnotation build() {
            if (className == null)
                throw new RuntimeException("The class name cannot be null!");
            return new JAnnotation(level, packageName, className);
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

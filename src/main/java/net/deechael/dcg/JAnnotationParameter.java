package net.deechael.dcg;

import java.util.HashMap;
import java.util.Map;

public final class JAnnotationParameter implements JObject {

    private final JType type;
    private final String name;
    Map<JType, Map<String, JStringVar>> annotations = new HashMap<>();
    private JStringVar defaultValue = null;

    JAnnotationParameter(JType type, String name) {
        this.type = type;
        this.name = name;
    }

    public void setDefaultValue(JStringVar defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public String getString() {
        StringBuilder base = new StringBuilder();
        base.append(this.annotationString()).append(this.type.typeString()).append(" ").append(this.name).append("()");
        if (this.defaultValue != null) {
            base.append(" default ").append(this.defaultValue.varString());
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

}

package net.deechael.dcg;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface JObject {

    String getString();

    void addAnnotation(Class<?> annotation, Map<String, JStringVar> values);

    Map<Class<?>, Map<String, JStringVar>> getAnnotations();

    default String annotationString() {
        StringBuilder base = new StringBuilder();
        Map<Class<?>, Map<String, JStringVar>> map = getAnnotations();
        if (!map.isEmpty()) {
            for (Map.Entry<Class<?>, Map<String, JStringVar>> entry : map.entrySet()) {
                base.append("@").append(entry.getKey().getName().replace("$", "."));
                if (!entry.getValue().isEmpty()) {
                    base.append("(");
                    List<Map.Entry<String, JStringVar>> jStringVars = new ArrayList<>(entry.getValue().entrySet());
                    for (int i = 0; i < jStringVars.size(); i++) {
                        Map.Entry<String, JStringVar> subEntry = jStringVars.get(i);
                        base.append(subEntry.getKey()).append("=").append(subEntry.getValue().varString());
                        if (i != jStringVars.size() - 1) {
                            base.append(", ");
                        }
                    }
                    base.append(")\n");
                } else {
                    base.append("\n");
                }
            }
        }
        return base.toString();
    }

}

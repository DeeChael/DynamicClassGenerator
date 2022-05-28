package net.deechael.dcg;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface JObject {

    String getString();

    void addAnnotation(JType type, Map<String, JStringVar> values);

    Map<JType, Map<String, JStringVar>> getAnnotations();

    default String annotationString() {
        StringBuilder base = new StringBuilder();
        Map<JType, Map<String, JStringVar>> map = getAnnotations();
        if (!map.isEmpty()) {
            for (Map.Entry<JType, Map<String, JStringVar>> entry : map.entrySet()) {
                base.append("@").append(entry.getKey().typeString());
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
        return base.append(" ").toString();
    }

}

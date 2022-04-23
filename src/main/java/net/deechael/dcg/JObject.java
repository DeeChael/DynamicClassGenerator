package net.deechael.dcg;

import java.util.Map;

public interface JObject {

    String getString();

    void addAnnotation(Class<?> annotation, Map<String, JStringVar> values);

    Map<Class<?>, Map<String, JStringVar>> getAnnotations();
}

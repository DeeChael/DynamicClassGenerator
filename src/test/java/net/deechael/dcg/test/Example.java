package net.deechael.dcg.test;

import net.deechael.dcg.*;
import net.deechael.dcg.generator.JGenerator;
import net.deechael.dcg.Var;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;

public class Example {

    public static void main(String[] args) throws URISyntaxException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        JClass clazz = JClass.Builder
                .ofPublic()
                .withPackage("net.deechael.test")
                .withName("DynamicClassGeneratorTest")
                .build();
        JField field = clazz.addField(Level.PUBLIC, JType.classType(String.class), "parent", false, false);
        field.initialize(JStringVar.stringVar("aaaaaaaaaaaaaaaaaaaaaaa"));
        JConstructor constructor = clazz.addConstructor(Level.PUBLIC);
        Var testing = constructor.addParameter(JType.classType(String.class), "testing");
        Var human = constructor.addParameter(JType.classType(Human.class), "human");
        Var second = constructor.createVar(JType.classType(String.class), "second", Var.constructor(JType.classType(String.class), testing));
        Var age = constructor.createVar(JType.classType(int.class), "age", Var.invokeMethod(human, "getAge"));
        Var name = constructor.createVar(JType.classType(String.class), "name", Var.invokeMethod(human, "getName"));
        constructor.invokeMethod(human, "print", testing);
        constructor.invokeMethod(human, "print", second);
        constructor.invokeMethod(human, "print", human);
        constructor.invokeMethod(human, "print", age);
        constructor.invokeMethod(human, "print", name);
        constructor.ifElse(Requirement.isEqual(age, JStringVar.intVar(16)), (executable) -> {
            executable.invokeMethod(human, "print", JStringVar.stringVar("You entered if executable body"));
        }).setElse(((executable) -> {
            executable.invokeMethod(human, "print", JStringVar.stringVar("You entered else executable body"));
        }));

        JMethod method = clazz.addMethod(Level.PUBLIC, "testing", false, false, false);
        Var method_human = method.addParameter(JType.classType(Human.class), "human");
        Var method_age = method.createVar(JType.classType(int.class), "age", Var.invokeMethod(human, "getAge"));
        method.invokeMethod(method_human, "print", JStringVar.stringVar("Method testing"));
        method.ifElse(Requirement.isEqual(method_age, JStringVar.intVar(16)), (executable) -> {
            executable.invokeMethod(method_human, "print", JStringVar.stringVar("Method if body"));
        }).setElse((executable) -> {
            executable.invokeMethod(method_human, "print", JStringVar.stringVar("Method else body"));
        });
        method.invokeMethod(method_human, "print", field);
        method.setFieldValue(field, JStringVar.stringVar("bbbbbbbbbbbbbbbbbbbb"));
        method.invokeMethod(method_human, "print", field);

        Class<?> generated = JGenerator.generate(clazz).get(0);
        Constructor<?> cons = generated.getConstructor(String.class, Human.class);
        Object instance = cons.newInstance("Test message!", new Human("Name", 16));
        generated.getMethod("testing", Human.class).invoke(instance, new Human("DeeChael", 16));
        generated.getMethod("testing", Human.class).invoke(instance, new Human("DeeChael", 39));
    }

    public static class Human {

        private final String name;
        private final int age;

        public Human(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return this.name;
        }

        public int getAge() {
            return age;
        }

        public void print(Object message) {
            System.out.println(message);
        }

    }

}

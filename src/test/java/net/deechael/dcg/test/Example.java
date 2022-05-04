package net.deechael.dcg.test;

import net.deechael.dcg.*;
import net.deechael.dcg.generator.JGenerator;
import net.deechael.dcg.items.Var;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;

public class Example {

    public static void main(String[] args) throws URISyntaxException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        JClass clazz = new JClass(Level.PUBLIC, "net.deechael.test", "DynamicClassGeneratorTest");
        JField field = clazz.addField(Level.PUBLIC, String.class, "parent", false, false);
        field.initialize(JStringVar.stringVar("aaaaaaaaaaaaaaaaaaaaaaa"));
        JConstructor constructor = clazz.addConstructor(Level.PUBLIC);
        Var testing = constructor.addParameter(String.class, "testing");
        Var human = constructor.addParameter(Human.class, "human");
        Var second = constructor.createVar(String.class, "second", Var.constructor(String.class, testing));
        Var age = constructor.createVar(int.class, "age", Var.invokeMethod(human, "getAge"));
        Var name = constructor.createVar(String.class, "name", Var.invokeMethod(human, "getName"));
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
        Var method_human = method.addParameter(Human.class, "human");
        Var method_age = method.createVar(int.class, "age", Var.invokeMethod(human, "getAge"));
        method.invokeMethod(method_human, "print", JStringVar.stringVar("Method testing"));
        method.ifElse(Requirement.isEqual(method_age, JStringVar.intVar(16)), (executable) -> {
            executable.invokeMethod(method_human, "print", JStringVar.stringVar("Method if body"));
        }).setElse((executable) -> {
            executable.invokeMethod(method_human, "print", JStringVar.stringVar("Method else body"));
        });
        method.invokeMethod(method_human, "print", field);
        method.setFieldValue(field, JStringVar.stringVar("bbbbbbbbbbbbbbbbbbbb"));
        method.invokeMethod(method_human, "print", field);

        Class<?> generated = JGenerator.generate(clazz);
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

package net.deechael.library.dcg.test;

import net.deechael.library.dcg.dynamic.*;
import net.deechael.library.dcg.dynamic.items.Var;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;

public class Test {

    public static void main(String[] args) throws URISyntaxException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        JClass clazz = new JClass("net.deechael.test", "DynamicClassGeneratorTest", Level.PUBLIC);

        JField field = clazz.addField(Level.PUBLIC, String.class, "parent", false, false);
        field.initializeByValue(JStringVar.stringVar("aaaaaaaaaaaaaaaaaaaaaaa"));

        JConstructor constructor = clazz.addConstructor(Level.PUBLIC);
        Var testing = constructor.addParameter(String.class, "testing");
        Var human = constructor.addParameter(Human.class, "human");
        Var second = constructor.createNewInstanceVar(String.class, "second", testing);
        Var age = constructor.usingMethodAndCreateVar("age", human, "getAge");
        Var name = constructor.usingMethodAndCreateVar("name", human, "getName");
        constructor.usingMethod(human, "print", testing);
        constructor.usingMethod(human, "print", second);
        constructor.usingMethod(human, "print", human);
        constructor.usingMethod(human, "print", age);
        constructor.usingMethod(human, "print", name);
        constructor.ifElse_Equal(age, JStringVar.intVar(16), executable -> {
            executable.usingMethod(human, "print", JStringVar.stringVar("You entered if executable body"));
        }, executable -> {
            executable.usingMethod(human, "print", JStringVar.stringVar("You entered else executable body"));
        });

        JMethod method = clazz.addMethod(Level.PUBLIC, "testing");
        Var method_human = method.addParameter(Human.class, "human");
        Var method_age = method.usingMethodAndCreateVar("age", human, "getAge");
        method.usingMethod(method_human, "print", JStringVar.stringVar("Method testing"));
        method.ifElse_Equal(method_age, JStringVar.intVar(16), executable -> {
            executable.usingMethod(human, "print", JStringVar.stringVar("Method if body"));
        }, executable -> {
            executable.usingMethod(human, "print", JStringVar.stringVar("Method else body"));
        });

        method.usingMethod(method_human, "print", field);
        method.setFieldValue(field, JStringVar.stringVar("bbbbbbbbbbbbbbbbbbbb"));
        method.usingMethod(method_human, "print", field);

        System.out.println(clazz.getString());
        Class<?> generated = clazz.generate();
        Constructor<?> cons = generated.getConstructor(String.class, Human.class);
        Object instance = cons.newInstance("Gerry is gay!", new Human());
        System.out.println("==================Method testing part=================");
        generated.getMethod("jmethod_testing", Human.class).invoke(instance, new Human());
        /*
        JClass clazz = new JClass("net.deechael.test", "TestingFucker", Level.PUBLIC);
        JConstructor constructor = new JConstructor(Level.PUBLIC, "TestingFucker");
        Var testing = constructor.addParameter(String.class, "testing");
        Var human = constructor.addParameter(Human.class, "human");
        Var second = constructor.createNewVar(String.class, "second", testing);
        Var age = constructor.usingMethodAndCreateVar("age", human, "getAge");
        Var name = constructor.usingMethodAndCreateVar("name", human, "getName");
        constructor.usingMethod(human, "print", testing);
        constructor.usingMethod(human, "print", second);
        constructor.usingMethod(human, "print", human);
        constructor.usingMethod(human, "print", age);
        constructor.usingMethod(human, "print", name);
        clazz.addConstructor(constructor);
        System.out.println(clazz.getString());
        Class<?> generated = clazz.generate();
        Constructor<?> cons = generated.getConstructor(String.class, Human.class);
        cons.newInstance("Gerry is gay!", new Human());
        */
    }

}

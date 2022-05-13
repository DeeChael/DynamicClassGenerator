package net.deechael.dcg.test;

import net.deechael.dcg.*;
import net.deechael.dcg.generator.JGenerator;
import net.deechael.dcg.items.Var;

import java.util.List;

public class MultiplyGeneratingTest {

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        JClass class1 = new JClass(Level.PUBLIC, "io.dcg", "AClass");
        JClass class2 = new JClass(Level.PUBLIC, "io.dcg", "BClass");
        class1.importClass(class1.getName());
        JConstructor constructor = class1.addConstructor(Level.PUBLIC);
        constructor.invokeMethod(Var.staticField(System.class, "out"), "println", Var.custom("new BClass()"));
        List<Class<?>> list = JGenerator.generate(new JGeneratable[] {class1, class2});
        for (Class<?> clazz : list) {
            System.out.println(clazz.getName());
            if (clazz.getName().contains("A")) {
                clazz.newInstance();
            }
        }
    }

}

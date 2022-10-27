package net.deechael.dcg.test;

import net.deechael.dcg.JClass;
import net.deechael.dcg.JGeneratable;
import net.deechael.dcg.generator.JGenerator;

public class AnonymousClassTest {

    public static void main(String[] args) {
        JClass aaa = JClass.Builder.ofPublic().withName("Test").build();
        aaa.addInner(JClass.Builder.ofProtected().withName("Fuck").build());
        System.out.println(aaa.getString());
        System.out.println("==============");
        for (Class<?> clazz : JGenerator.generate(new JGeneratable[] {aaa})) {
            System.out.println(clazz.getName());
        }
    }

    private void a() {
        InterfaceTest a = new InterfaceTest() {
            @Override
            public String toString() {
                return super.toString();
            }

            public synchronized void a() {

            }

            final class A {

            }

        };
    }

}

package net.deechael.dcg.test;

public class AnonymousClassTest {

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

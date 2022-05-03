package net.deechael.dcg.test;

public class SynchronizedTest {

    public static String A = "";

    public static synchronized void a() {

    }

    public synchronized void b() {
        synchronized (aa()) {

        }
    }

    private String aa() {
        return "";
    }

}

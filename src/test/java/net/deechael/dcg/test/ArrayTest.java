package net.deechael.dcg.test;

import java.util.Arrays;

public class ArrayTest {

    public static void main(String[] args) {
        System.out.println(String.class.getName()); // java.lang.String
        System.out.println(String[].class.getName()); // [Ljava.lang.String;
        System.out.println(String[][].class.getName()); // [[Ljava.lang.String;
        System.out.println(Arrays.toString(new String[] {"A", "B"}));
    }

}

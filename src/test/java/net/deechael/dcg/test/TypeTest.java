package net.deechael.dcg.test;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TypeTest {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        System.out.println(ArrayList.class);
        System.out.println(list.getClass().getTypeParameters()[0].getGenericDeclaration());
        System.out.println(list.getClass().getComponentType());
        System.out.println(Arrays.toString(String.class.getTypeParameters()));
    }

}

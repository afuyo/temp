package com.nereus;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.UUID;

public class SandBox {
    public static void main(String[] arg)  {

        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("1","one");
        map.put("2","two");
        map.put("3","three");

        map.forEach((k,v)->{
            System.out.println("k "+k);
            System.out.println("v "+v);
        });

        Multimap<String,String> multimap = LinkedListMultimap.create();
        multimap.put("1","one");
        multimap.put("1","two");
        multimap.put("2","two");
        multimap.put("3","three");

        multimap.forEach((k,v)->{
            System.out.println("k "+k);
            System.out.println("v "+v);
        });

        ArrayList<String> a = new ArrayList<String>(Arrays.asList("A","B","C"));
        ArrayList<String> b = new ArrayList<String>(Arrays.asList("F","D","E"));

        boolean isTrue = a.stream().flatMap(x->b.stream().filter(y->x.equals(y)).limit(1)).findFirst().isPresent();
        System.out.println(isTrue);
        UUID.randomUUID().version();
        System.out.println("UUID " +UUID.randomUUID().hashCode());

        System.out.println(System.currentTimeMillis());

    }
}

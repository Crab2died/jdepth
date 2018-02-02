package com.github.jvm.collections;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CollectionsFeature {

    // 剃头
    public void rid(){

    }

    public static void main(String... args){
        Map<String, Object> table = new Hashtable<>();
        //table.put(null, "");
        Map<String, Object> map = new HashMap<>();
        map.put(null, "");

        Map<String, Object> mapc = new ConcurrentHashMap<>();
        //mapc.put(null, "");

        Map<String, Object> idMap = new IdentityHashMap<>();
        // 必须new String以保证地址不一样
        idMap.put(new String("test"), "test1");
        idMap.put(new String("test"), "test2");
        idMap.put("test", "test3");
        idMap.put(null, "test4");
        System.out.println(idMap);
    }
}

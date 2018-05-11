package com.github.jvm.classloader;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// asm 的ClassLoader
public class ASMClassLoader extends ClassLoader {

    // class字节码数组
    private byte[] clazzBytes;

    // 缓存
    private static Map<String, Class<?>> CACHE_CLASS = new ConcurrentHashMap<>();

    public ASMClassLoader(byte[] clazzBytes) {
        super();
        this.clazzBytes = clazzBytes;
    }
//
//    // 并发同步??
//    @Override
//    public Class<?> loadClass(String name) throws ClassNotFoundException {
//
//        Class<?> clazz = findClass(name);
//        if (null == clazz)
//            try {
//                // 委派父加载器
//                clazz = getParent().loadClass(name);
//            } catch (ClassNotFoundException e) {
//                // 父加载器无法加载再调用自定义类加载器
//                clazz = defineClass(name, clazzBytes, 0, clazzBytes.length);
//                CACHE_CLASS.put(name, clazz);
//            }
//        return clazz;
//    }

    @Override
    protected Class<?> findClass(String name) {
        Class<?> clazz = CACHE_CLASS.get(name);
        if (null != clazz)
            return clazz;
        clazz = defineClass(name, clazzBytes, 0, clazzBytes.length);
        CACHE_CLASS.put(name, clazz);
        return clazz;
    }
}

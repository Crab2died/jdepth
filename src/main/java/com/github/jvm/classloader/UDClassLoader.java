package com.github.jvm.classloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

// 自定义类加载器
public class UDClassLoader extends ClassLoader {

    private static final String CLASS_HOLDER = "D:\\clazz\\";

    private static final Map<String, Class<?>> CLASS_CACHE;

    static {
        CLASS_CACHE = new HashMap<>();
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        Class<?> clazz;
        try {
            // 委派父类加载器
            clazz = getParent().loadClass(name);
        } catch (ClassNotFoundException e) {
            clazz = CLASS_CACHE.get(name);
            if (null == clazz) {
                clazz = findClass(name);
                CLASS_CACHE.put(name, clazz);
            }
        }
        return clazz;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String classPath = CLASS_HOLDER + name.replaceAll("\\.", "/") + ".class";
        try (FileInputStream fis = new FileInputStream(new File(classPath))) {

            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[1024];
                int len;
                while ((len = fis.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
                byte[] data = out.toByteArray();
                return defineClass(name, data, 0, data.length);
            }
        } catch (IOException e) {
            throw new ClassNotFoundException();
        }
    }

    public static void main(String... args) throws Exception {

        UDClassLoader classLoader = new UDClassLoader();
        Class<?> clazz = Class.forName("com.github.jvm.asm.CircleCalc", true, classLoader);
        Object object = clazz.newInstance();
        Method method = clazz.getMethod("calcArea", double.class);
        Object rel = method.invoke(object, 10D);
        System.out.println(rel);
    }
}

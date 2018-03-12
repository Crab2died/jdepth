package com.github.jvm.classloader;

// asm 的ClassLoader
public class ASMClassLoader extends ClassLoader {

    // class字节码数组
    private byte[] clazzBytes;

    public ASMClassLoader(byte[] clazzBytes) {
        super();
        this.clazzBytes = clazzBytes;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        Class<?> clazz;
        try {
            // 委派父加载器
            clazz = getParent().loadClass(name);
        } catch (ClassNotFoundException e) {
            // 父加载器无法加载再调用自定义类加载器
            clazz = findClass(name);
        }
        return clazz;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        // 加载类字节码
        return defineClass(name, clazzBytes, 0, clazzBytes.length);
    }

}

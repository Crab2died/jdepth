package com.github.jvm;

import sun.misc.Unsafe;
import sun.misc.VM;

import java.lang.reflect.Field;

/**
 * {@link sun.misc.Unsafe}
 */
public class UnsafeTest {

    private static Unsafe UNSAFE;

    static {
        try {
            // Unsafe是不安全操作，只能被BootstrapClassLoader加载
            // 但可以通过反射来获取Unsafe实例
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            UNSAFE = (Unsafe) f.get(null);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public static void main(String... args) {


        UnsafeTest.UNSAFE.park(false, 1000L);

        System.out.println(VM.isSystemDomainLoader(Unsafe.class.getClassLoader()));
        System.out.println(VM.isSystemDomainLoader(UnsafeTest.class.getClassLoader()));
    }
}

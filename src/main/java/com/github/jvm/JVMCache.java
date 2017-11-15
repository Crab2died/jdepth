package com.github.jvm;

/**
 * JVM 自动缓存了-128 ~ 127的数值对象 Character、Byte、Short、Integer、Long
 * JVM 也缓存了Boolean
 *
 * @author : Crab2Died
 * 2017/11/15  16:29:23
 */
public class JVMCache {

    public static void main(String[] args) {
        // 涉及自动装包 jvm 缓存了-127 ~ 128的值
        // true
        Integer v1 = -128;
        Integer v2 = -128;
        System.out.println(v1 == v2);

        // new 关键字会生成新对象
        // false
        Integer v1_1 = new Integer(-128);
        Integer v2_2 = new Integer(-128);
        System.out.println(v1_1 == v2_2);

        // 不在 -128 ~ 127范围内的值
        // false
        Integer v3 = 200;
        Integer v4 = 200;
        System.out.println(v3 == v4);

        // egg.
        // true
        Byte b1 = 'A';
        Byte b2 = 'A';
        System.out.println(b1 == b2);

        // false
        Byte b1_1 = new Byte(b1);
        Byte b2_2 = new Byte(b1);
        System.out.println(b1_1 == b2_2);

        Byte b3 = new Byte("127");
        Byte b4 = new Byte("127");
        System.out.println(b3 == b4);

        // 缓存<= 127
        // true
        Character ch1 = 127;
        Character ch2 = 127;
        System.out.println(ch1 == ch2);

        // false
        Character ch3 = 128;
        Character ch4 = 128;
        System.out.println(ch3 == ch4);
    }
}

package com.github.jvm.version.jdk8;

public class HashMapAnalysis {

    private static final int MAXIMUM_CAPACITY = 1 << 30;

    private static int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }


    public static void main(String... args) {
        System.out.println(tableSizeFor((1 << 19) + 1));
        System.out.println(4 | (4 >>> 1));
        System.out.println(Float.isNaN(0.0F));
    }

}

package com.github.jvm.classloader;

public class SuperClass {

    public static int superField = 1;

    static {
        System.out.println("super class loading...");
    }
}

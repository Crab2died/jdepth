package com.github.jvm.classloader;

public class SubClass extends SuperClass{

    public static int subField = 2;

    static {
        System.out.println("sub class loading...");
    }
}

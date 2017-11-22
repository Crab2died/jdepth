package com.github.jvm.execengine;

import java.io.Serializable;

/**
 *
 *  静态分派试验
 *  重载方法，找更合适的方法调用
 *  按编号一次注释代码运行观察结果
 *  实际自动类型转换 char->int->long->float->double
 */
public class Overload {


    // 1
    public static void sayHello(char arg) {
        System.out.println("hello char");
    }

    // 2
    public static void sayHello(int arg) {
        System.out.println("hello int");
    }

    // 3    //char->int->long->float->double
    public static void sayHello(long arg) {
        System.out.println("hello long");
    }

    // 4
    public static void sayHello(Character arg) {
        System.out.println("hello Character");
    }

    // 5
    public static void sayHello(Serializable arg) {
        System.out.println("hello Serializable");
    }

    // 6
    public static void sayHello(Object arg) {
        System.out.println("hello Object");
    }

    public static void sayHello(char... arg) {
        System.out.println("hello char...");
    }

    public static void main(String[] args) {
        sayHello('a');
    }
}

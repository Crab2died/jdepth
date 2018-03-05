package com.github.jvm.concurrent;

// final域为基本数据类型
public class FinalExample {

    private int i; // 普通变量
    private final int j; // final变量
    private static FinalExample obj;

    // 写final域的重排序规则可以确保：在对象引用为任意线程可见之前，
    // 对象的final域已经被正确初始化过了，而普通域不具有这个保障。
    public FinalExample() { // 构造函数
        i = 1; // 写普通域
        j = 2; // 写final域
    }

    public static void writer() { // 写线程A执行
        obj = new FinalExample();
    }

    // 读final域的重排序规则可以确保：在读一个对象的final域之前，
    // 一定会先读包含这个final域的对象的引用。
    public static void reader() { // 读线程B执行
        FinalExample object = obj; // 读对象引用
        int a = object.i; // 读普通域
        int b = object.j; // 读final域
        System.out.println("a = " + a + ", b = " + b);
    }

    public static void main(String... args) {
        Thread th1 = new Thread(FinalExample::writer);
        Thread th2 = new Thread(FinalExample::reader);
        th1.start();
        th2.start();
    }
}

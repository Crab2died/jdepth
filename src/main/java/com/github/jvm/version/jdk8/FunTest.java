package com.github.jvm.version.jdk8;

public class FunTest {

    public void test(Fun01 fun01) {
        fun01.fun();
    }

    public void test(Fun02 fun02) {
        fun02.fun();
    }

    public static void main(String... args) {
        FunTest test = new FunTest();
        // 此处指定参数类型 Fun01、Fun02
        test.test((Fun01) () -> System.out.println("fun01"));
        test.test((Fun02) () -> System.out.println("fun02"));
    }
}

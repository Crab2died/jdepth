package com.github.jvm;

/**
 * 内部类
 */
public class Outer {

    private static int outer;

    static class Inner {

        private int inner;

        static void print() {
            System.out.println(outer);
        }

    }

    abstract class Inner1 {
        public abstract void print(int a);
    }
}

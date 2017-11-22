package com.github.jvm.execengine;

/**
 *
 *  静态分派试验
 *  重载
 */
public class StaticDispatch {

    static class Human {

    }

    static class Man extends Human {

    }

    static class Woman extends Human {

    }

    public void sayHello(Human guy) {
        System.out.println("hello,guy！");
    }

    public void sayHello(Man guy) {
        System.out.println("hello,gentleman！");
    }

    public void sayHello(Woman guy) {
        System.out.println("hello,lady！");
    }

    public static void main(String[] args) {
        // man woman 的静态类型 为 Human
        Human man = new Man();
        Human woman = new Woman();
        StaticDispatch sr = new StaticDispatch();
        sr.sayHello(man);
        sr.sayHello(woman);
    }
}

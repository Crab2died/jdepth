package com.github.jvm;

class Singleton {

    private static Singleton singleton = new Singleton() {

        // 匿名内部类
        // 子类能重写父类方法，不能覆盖父类属性

        public int a = 2;

        @Override
        public int get() {
            // 此时的this与外层this一样
            return this.a;
        }
    };

    public int a = 0;

    public int get() {
        return this.a;
    }

    public static Singleton getInstance() {
        return singleton;
    }


    class Singleton2 extends Singleton {

        public int a = 2;

        @Override
        public int get() {
            return this.a;
        }
    }
}



public class InnerClass {

    public static void main(String[] args) {

        Singleton singleton = Singleton.getInstance();

        System.out.println(singleton.a);
        System.out.println(singleton.get());


        Singleton.Singleton2 s2 = new Singleton().new Singleton2();

        System.out.println(s2.a);
        System.out.println(s2.get());

    }

}

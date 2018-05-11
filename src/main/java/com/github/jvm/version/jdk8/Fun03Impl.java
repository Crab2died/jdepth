package com.github.jvm.version.jdk8;

public class Fun03Impl implements Fun03 {

    @Override
    public void fun() {
        System.out.println("fun03...");
    }


    public static void main(String... args){
        Fun03Impl impl = new Fun03Impl();
        impl.fun();
    }

}

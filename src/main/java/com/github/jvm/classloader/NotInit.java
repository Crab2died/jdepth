package com.github.jvm.classloader;

import java.util.ArrayList;
import java.util.List;

/**
 * 被动加载试验
 */
public class NotInit {

    public static void main(String[] args) {

        // 被动加载演示一

        // 不会加载子类
        // -XX：+TraceClassLoading 打印类加载情况
        // System.out.println(SubClass.superField);

        System.out.println("----------------------------");
        // 父子类均加载
        // System.out.println(SubClass.subField);

        // 被动加载演示二
        // 引用类型定义不初始化加载
        SuperClass[] supers = new SuperClass[10];
        List<SuperClass> list = new ArrayList<>();

    }
}

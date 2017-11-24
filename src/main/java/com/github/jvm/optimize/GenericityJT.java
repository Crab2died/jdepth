package com.github.jvm.optimize;

import java.util.List;

/**
 * 泛型是不能参与方法重载的
 * 编译器在编译阶段会实现泛型会脱泛
 */
public class GenericityJT {

    /**
     *
     * 泛型无法参与方法重载
     */
    class Genericity1 {

        public void method(List<String> list){

        }

//      无法重载
//
//        public void method(List<Integer> list){
//
//        }
    }

    // JDK1.6这种形式能重载编译
    class Genericity2 {

        public String method(List<String> list){
            System.out.println("invoke List<String> list");
            return null;
        }

//        public int method(List<Integer> list){
//            System.out.println("invoke List<Integer> list");
//            return 1;
//        }

    }
}

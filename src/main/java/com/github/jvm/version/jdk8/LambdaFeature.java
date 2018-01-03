package com.github.jvm.version.jdk8;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class LambdaFeature {

    // 实例1
    @FunctionalInterface
    interface Face {

        int run(int var1, int var2);

        default void hello(){
            System.out.println("hello");
        }
    }

    static class Faced {

        private int var1;

        private int var2;

        public Faced(int var1, int var2) {
            this.var1 = var1;
            this.var2 = var2;
        }

        static String hello() {
            return "hello";
        }

        int invoke(Face face) {
            return face.run(var1, var2);
        }

        public static void main(String... args) {
            Faced faced = new Faced(1, 2);
            int _var = faced.invoke((var1, var2) -> var1 + var2);
            System.out.println(_var);
        }
    }

    // 实例2  foreach
    static class ArrayForEach {

        public static void main(String... args) {
            List<String> list = Arrays.asList("a", "b", "c", "d");
            //list.forEach(x -> System.out.println(x));
            list.forEach(System.out::println);
        }
    }

    // 实例3  map
    static class MapTest {

        public static void main(String... args) {
            List<String> list = Arrays.asList("a", "b", "c", "d");
            Stream<String> map = list.stream().map(x -> x + "_value");
            map.forEach(System.out::println);
        }
    }

    // 实例4  reduce
    static class ReduceTest {

        public static void main(String... args) {
            List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
            Optional<Integer> reduce = list.stream().map(x -> x * 2).reduce((sum, x) -> sum + x);
            System.out.println(reduce.get());
        }
    }
}

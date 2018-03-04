package com.github.jvm.concurrent;

import java.util.Vector;

/**
 * Vector 非绝对线程安全
 */
public class VectorConcurrentTest {

    private static Vector<Integer> vector = new Vector<>();

    public static void main(String[] args) {
        for (; ; ) {
            for (int i = 0; i < 10; i++) {
                vector.add(i);
            }
            Thread thread1 = new Thread(() -> {
                // synchronized (vector){
                for (int i = 0; i < vector.size(); i++) {
                    vector.remove(i);
                }
                //  }
            });

            Thread thread2 = new Thread(() -> {
                // synchronized (vector) {
                for (int i = 0; i < vector.size(); i++) {
                    System.out.println(vector.get(i));
                }
                // }
            });

            thread1.start();
            thread2.start();

        }
    }
}
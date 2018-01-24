package com.github.jvm.concurrent;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Atomic变量自增运算测试
 */
public class AtomicTest {
    public static AtomicInteger race = new AtomicInteger(0);

    public static void increase() {
        race.incrementAndGet();
    }

    private static final int THREADS_COUNT = 5;

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < THREADS_COUNT; i++) {
            new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    //increase();
                    race.addAndGet(j);
                }
            }).start();
        }
        while (Thread.activeCount() > 2){
            System.out.println(Thread.activeCount());
            Thread.yield();
        }

        System.out.println(race);
    }
}
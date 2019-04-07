package com.github.jvm.concurrent;

import java.util.concurrent.*;

public class CopyOnWrite {

    public static void main(String... args) throws InterruptedException {

        CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch1 = new CountDownLatch(100);
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            executor.execute(() -> {
                if (finalI % 2 == 0) {
                    list.add(finalI);
                    System.out.println("add - " + finalI);
                } else {
                    System.out.println(list);
                }
                countDownLatch1.countDown();
            });
        }
        countDownLatch1.await();

        executor.shutdown();

    }

}

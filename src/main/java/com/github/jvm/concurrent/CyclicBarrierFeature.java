package com.github.jvm.concurrent;

import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * {@link java.util.concurrent.CyclicBarrier}
 */
public class CyclicBarrierFeature {

    public static void main(String... args) {

        CyclicBarrier cyclicBarrier = new CyclicBarrier(5, () ->
                System.out.println(Thread.currentThread().getName() +
                        " something go to running before the tasks starts"));

        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                Random random = new Random(10000);
                try {
                    TimeUnit.MILLISECONDS.sleep(random.nextLong());
                    cyclicBarrier.await();
                    System.out.println(Thread.currentThread().getName() + " is completed");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Thread.currentThread().interrupt();
                }
            }, "Thread-" + i).start();
        }
    }

}

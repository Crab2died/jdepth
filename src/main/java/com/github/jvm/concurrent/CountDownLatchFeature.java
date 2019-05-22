package com.github.jvm.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CountDownLatchFeature {

    public static void main(String... args) throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(100);

        ExecutorService exec = Executors.newFixedThreadPool(4);

        AtomicInteger count = new AtomicInteger(0);


        for (int i = 1; i <= 100; i++) {
            int finalI = i;
            exec.submit(() -> {
                System.out.println(count.addAndGet(finalI));
                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                    countDownLatch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    countDownLatch.countDown();
                }
            });
        }

        System.out.println(countDownLatch.await(1, TimeUnit.MILLISECONDS));
        System.out.println("end.....");
        exec.shutdown();
        System.out.println(count.get());

    }


}

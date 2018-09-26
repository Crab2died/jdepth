package com.github.usefultool.distributedlock;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ZkLockTest {

    private DistributedLock lock;

    @Before
    public void init() {
        lock = new ZkDistributedLock("localhost:2181", "lock1");
    }

    @Test
    public void test() {
        try {
            lock.lock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    @Test
    public void concurrencyTest() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                DistributedLock lock = new ZkDistributedLock("localhost:2181", "test");
                try {
                    lock.tryLock(100000, TimeUnit.MILLISECONDS);
                    System.out.println(Thread.currentThread().getName() + " get lock");
                    TimeUnit.MILLISECONDS.sleep(5000);
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + " get lock error");
                } finally {
                    lock.unlock();
                    System.out.println(Thread.currentThread().getName() + " release lock");
                    countDownLatch.countDown();
                }
            }, "thread-" + (i + 1))
                    .start();
        }
        countDownLatch.await();
    }
}

package com.github.usefultool.distributedlock;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class RedisLockTest {

    private DistributedLock lock;


    private JedisPool jedisPool;

    @Before
    public void init() {

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(50);
        config.setMaxWaitMillis(50000L);
        config.setMinIdle(20);
        config.setTestOnBorrow(true);
        jedisPool = new JedisPool(config, "localhost", 6379);
    }

    @After
    public void release() {

        jedisPool.close();
    }

    @Test
    public void lock() {
        lock = new RedisDistributedLock(jedisPool.getResource(), "lock1", 40);
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
                DistributedLock lock = new RedisDistributedLock(jedisPool.getResource(), "test");
                try {
                    lock.tryLock(10000, TimeUnit.MILLISECONDS);
                    System.out.println(Thread.currentThread().getName() + " get lock");
                    TimeUnit.MILLISECONDS.sleep(500);
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

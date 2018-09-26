package com.github.usefultool.distributedlock;

import redis.clients.jedis.Jedis;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class RedisDistributedLock implements DistributedLock {

    private Jedis jedis;

    private String lockName;

    private int ttl = 30;

    private static final String LOCK_SCRIPT =
            "if redis.call('setnx',KEYS[1],ARGV[1]) == 1 then  return redis.call('expire',KEYS[1],ARGV[2])  else return 0 end";

    private static final String UNLOCK_SCRIPT =
            "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";

    public RedisDistributedLock(Jedis jedis, String lockName) {
        this.jedis = jedis;
        this.lockName = "locks:" + lockName;
    }

    public RedisDistributedLock(Jedis jedis, String lockName, int ttl) {
        this.jedis = jedis;
        this.lockName = "locks:" + lockName;
        this.ttl = ttl;
    }

    @Override
    public void lock() throws InterruptedException {

        for (; ; ) {
            Object result = jedis.eval(LOCK_SCRIPT, Collections.singletonList(lockName), Arrays.asList("1", String.valueOf(ttl)));
            if ((long) result == 0) {
                TimeUnit.MILLISECONDS.sleep(100);
            } else {
                return;
            }
        }
    }

    @Override
    public void lock(long time, TimeUnit unit) throws InterruptedException {
        long timeout = unit.toNanos(time);
        long dieLine = System.nanoTime() + timeout;
        for (; ; ) {
            if (dieLine < System.nanoTime())
                throw new InterruptedException();
            Object result = jedis.eval(LOCK_SCRIPT, Collections.singletonList(lockName), Arrays.asList("1", String.valueOf(ttl)));
            if ((long) result == 0) {
                TimeUnit.MILLISECONDS.sleep(100);
            } else {
                return;
            }
        }
    }

    @Override
    public boolean tryLock() {

        Object result = jedis.eval(LOCK_SCRIPT, Collections.singletonList(lockName), Arrays.asList("1", String.valueOf(ttl)));

        return (long) result != 0;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        long timeout = unit.toNanos(time);
        long dieLine = System.nanoTime() + timeout;
        for (; ; ) {
            if (dieLine < System.nanoTime())
                return false;
            Object result = jedis.eval(LOCK_SCRIPT, Collections.singletonList(lockName), Arrays.asList("1", String.valueOf(ttl)));
            if ((long) result == 0) {
                TimeUnit.MILLISECONDS.sleep(100);
            } else {
                return true;
            }
        }
    }

    @Override
    public void unlock() {
        jedis.eval(UNLOCK_SCRIPT, Collections.singletonList(lockName), Collections.singletonList("1"));
        jedis.close();
    }
}

package com.github.usefultool.distributedlock;

import redis.clients.jedis.Jedis;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RedisDistributedLock implements DistributedLock {

    private Jedis jedis;

    private String lockName;

    private int ttl = 30;

    private final String uuKey;

    private int count;

    private static final String LOCK_SCRIPT =
            "if (redis.call('setnx',KEYS[1],ARGV[1]) == 1 or redis.call('get',KEYS[1]) == ARGV[1]) " +
                    "then return redis.call('expire',KEYS[1],ARGV[2]) else return 0 end";

    private static final String UNLOCK_SCRIPT =
            "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";

    public RedisDistributedLock(Jedis jedis, String lockName) {
        this.jedis = jedis;
        this.lockName = "locks:" + lockName;
        this.uuKey = UUID.randomUUID().toString();
    }

    public RedisDistributedLock(Jedis jedis, String lockName, int ttl) {
        this.jedis = jedis;
        this.lockName = "locks:" + lockName;
        this.ttl = ttl;
        this.uuKey = UUID.randomUUID().toString();
    }

    @Override
    public void lock() throws InterruptedException {

        for (; ; ) {
            long result = evalLockScript();
            if (result == 0) {
                TimeUnit.MILLISECONDS.sleep(100);
            } else {
                count++;
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
            long result = evalLockScript();
            if (result == 0) {
                TimeUnit.MILLISECONDS.sleep(100);
            } else {
                count++;
                return;
            }
        }
    }

    @Override
    public boolean tryLock() {

        long result = evalLockScript();
        if (result == 1) {
            count++;
            return true;
        }
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        long timeout = unit.toNanos(time);
        long dieLine = System.nanoTime() + timeout;
        for (; ; ) {
            if (dieLine < System.nanoTime())
                return false;
            long result = evalLockScript();
            if (result == 0) {
                TimeUnit.MILLISECONDS.sleep(100);
            } else {
                count++;
                return true;
            }
        }
    }

    @Override
    public void unlock() {
        if (count <= 0) {
            count = 0;
            return;
        }
        if (--count == 0) {
            jedis.eval(UNLOCK_SCRIPT,
                    Collections.singletonList(lockName),
                    Collections.singletonList(uuKey));
            jedis.close();
        }
    }

    private long evalLockScript() {
        return (long) jedis.eval(LOCK_SCRIPT,
                Collections.singletonList(lockName),
                Arrays.asList(uuKey, String.valueOf(ttl)));
    }

}

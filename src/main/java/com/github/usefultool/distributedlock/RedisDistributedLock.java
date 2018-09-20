package com.github.usefultool.distributedlock;

import redis.clients.jedis.Jedis;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class RedisDistributedLock implements DistributedLock {

    private Jedis jedis;

    private String lockKey;

    private int ttl = 5;

    private static final String LOCK_SCRIPT =
            "if redis.call('setnx',KEYS[1],ARGV[1]) == 1 then  return redis.call('expire',KEYS[1],ARGV[2])  else return 0 end";

    private static final String UNLOCK_SCRIPT =
            "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";

    public RedisDistributedLock() {
    }

    public RedisDistributedLock(Jedis jedis, String lockKey, int ttl) {
        this.jedis = jedis;
        this.lockKey = lockKey;
        this.ttl = ttl;
    }

    @Override
    public void lock() throws InterruptedException {

        for (; ; ) {
            Object result = jedis.eval(LOCK_SCRIPT, Collections.singletonList(lockKey), Arrays.asList("1", String.valueOf(ttl)));
            if ((long) result == 0) {
                TimeUnit.MILLISECONDS.sleep(100);
            } else {
                return;
            }
        }
    }

    @Override
    public void lock(long time, TimeUnit unit) throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {

        jedis.eval(UNLOCK_SCRIPT, 2, lockKey, "1");
    }
}

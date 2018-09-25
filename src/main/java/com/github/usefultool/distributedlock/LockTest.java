package com.github.usefultool.distributedlock;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class LockTest {

    private DistributedLock lock;

    private Jedis jedis;

    private JedisPool jedisPool;

    @Before
    public void init() {

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(100);
        config.setMaxWaitMillis(50000L);
        config.setTestOnBorrow(true);

        jedisPool = new JedisPool(config, "localhost", 6379);

        jedis = jedisPool.getResource();

        lock = new RedisDistributedLock(jedis, "lock1", 40);
    }

    @After
    public void release() {

        jedis.close();
        jedisPool.close();
    }

    @Test
    public void lock() {
        try {
            lock.lock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }


}

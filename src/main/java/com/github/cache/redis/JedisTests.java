package com.github.cache.redis;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisTests {

    private Jedis jedis;

    private JedisPool jedisPool;

    @Before
    public void connect() {

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(100);
        config.setMaxWaitMillis(50000L);
        config.setTestOnBorrow(true);

        jedisPool = new JedisPool(config, "localhost", 6379);

        jedis = jedisPool.getResource();

    }

    @After
    public void release(){

        jedis.close();
        jedisPool.close();
    }

    @Test
    public void ping() {
        Assert.assertEquals("PONG", jedis.ping());
    }

    @Test
    public void KeysTest(){

        System.out.println(jedis.keys("*"));

    }

    // String类型API
    @Test public void StringTest(){

        //
        Assert.assertEquals("OK", jedis.set("str-key", "REDIS"));
        Assert.assertEquals("REDIS", jedis.get("str-key"));
        System.out.println(jedis.strlen("str-key"));
        System.out.println(jedis.append("t-k", "so"));
        System.out.println(jedis.append("str-key", " SERVER"));
        System.out.println(jedis.get("str-key"));

        System.out.println(jedis.mset("str-key", "v1", "str-key2", "v2"));

        jedis.del("str-key", "t-k");

        System.out.println(jedis.msetnx("str-key", "v1", "str-key2", "v2"));
    }

}

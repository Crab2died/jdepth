package com.github.cache.redis;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.ScanResult;

import java.util.HashMap;
import java.util.Map;

public class JedisTests {

    private Jedis jedis;

    private JedisPool jedisPool;

    @Before public void connect() {

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(100);
        config.setMaxWaitMillis(50000L);
        config.setTestOnBorrow(true);

        jedisPool = new JedisPool(config, "localhost", 6379);

        jedis = jedisPool.getResource();

    }

    @After public void release(){

        jedis.close();
        jedisPool.close();
    }

    @Test public void ping() {
        Assert.assertEquals("PONG", jedis.ping());
    }

    @Test public void KeysTest(){

        System.out.println(jedis.keys("*"));

    }

    // String类型API
    @Test public void StringTest(){

        // 清库
        jedis.flushDB();

        // 添加，key存在则覆盖
        Assert.assertEquals("OK", jedis.set("str-key", "REDIS"));

        // 获取
        Assert.assertEquals("REDIS", jedis.get("str-key"));

        // 获取string长度
        System.out.println(jedis.strlen("str-key"));

        // 追加，key不存在则增加
        System.out.println(jedis.append("t-k", "so"));

        // 批量增加，原子操作，key存在则覆盖
        System.out.println(jedis.mset("str-key", "v1", "str-key2", "v2"));

        // 删除
        jedis.del("str-key", "t-k");

        // 批量增加，原子操作，当且进当所有key不存在才能成功
        System.out.println(jedis.msetnx("str-key", "hello world", "str-key3", "v3"));

        // 不存在的key增加，跳过的数值以空格处理
        System.out.println(jedis.setrange("null-key", 3, "redis"));
        System.out.println(jedis.setrange("str-key", 16, "redis"));
        System.out.println(jedis.get("null-key"));
        System.out.println(jedis.get("str-key"));

        // 设置生存时间
        System.out.println(jedis.setex("ttl-key", 1, "1"));

        // +1
        System.out.println(jedis.incr("incr-key"));
        // +10
        System.out.println(jedis.incrBy("incr-key", 10));
        // -9.345678
        System.out.println(jedis.incrByFloat("incr-key", -9.345678));

    }

    // hash类型API
    @Test public void HashTest(){

        // 清库
        jedis.flushDB();

        // key是否存在
        System.out.println(jedis.hexists("hash-key", "key1"));

        System.out.println(jedis.hset("hash-key", "key1", "v1"));

        // 批量插入
        Map<String, String> map = new HashMap<>();
        map.put("k1", "1");
        map.put("k2", "v2");
        System.out.println(jedis.hmset("hash-key", map));

        // 获取所有key
        System.out.println(jedis.hkeys("hash-key"));

        // 获取所有value值
        System.out.println(jedis.hvals("hash-key"));

        // 获取一个
        System.out.println(jedis.hget("hash-key", "key1"));

        // 获取多个
        System.out.println(jedis.hmget("hash-key", "k1", "k2"));

        // +10
        System.out.println(jedis.hincrBy("hash-key", "k1", 10));

        // 获取全部
        System.out.println(jedis.hgetAll("hash-key"));

        ScanResult<Map.Entry<String, String>> rel = jedis.hscan("hash-key", "89");
        for (Map.Entry<String, String> entry : rel.getResult()){
            System.out.println(entry.getKey() + " => " + entry.getValue());
        }

        // 删除
        System.out.println(jedis.hdel("hash-key", "k1", "k2", "k3"));
    }

    // list类型API
    @Test public void ListTest(){

        // 清除库
        jedis.flushDB();

        // 左入列
        System.out.println(jedis.lpush("list-key", "3", "2", "1"));

        // 右入列
        System.out.println(jedis.rpush("list-key", "4", "5", "6"));

        // 插入
        System.out.println(jedis.lset("list-key", 3, "0"));

        // 返回索引值 正数 l -> r  负数 r -> l
        System.out.println(jedis.lindex("list-key", -2));

        // 获取列表
        System.out.println(jedis.lrange("list-key", 0 , -1));

        // 阻塞左弹出
        System.out.println(jedis.blpop(100,"list-key"));

        // 阻塞右弹出
        System.out.println(jedis.brpop(100,"list-key"));

    }

    // set类型API
    @Test public void SetTest(){

        // 清除库
        jedis.flushDB();

        // 增加
        System.out.println(jedis.sadd("set-key", "1", "3", "2", "5", "4"));

        // 获取集合数量
        System.out.println(jedis.scard("set-key"));

        // 获取集合所有成员
        System.out.println(jedis.smembers("set-key"));

        //
        System.out.println(jedis.sadd("set-key2", "1", "7", "2", "6"));

        // 输出与第一个集合与后面集合的差集
        System.out.println(jedis.sdiff("set-key", "set-key2", "set-key3"));

        // 将第二个集合与后面集合的差集存入目标集合
        System.out.println(jedis.sdiffstore("set-key3", "set-key", "set-key2"));
        System.out.println(jedis.smembers("set-key3"));

        // 输出并集
        System.out.println(jedis.sinter("set-key", "set-key2"));

        // 并集插入目标集合
        System.out.println(jedis.sinterstore("set-key4", "set-key", "set-key2"));
        System.out.println(jedis.smembers("set-key4"));

        // 将源集合的成员移到目标集合
        System.out.println(jedis.smove("set-key", "set-key5", "4"));
        System.out.println(jedis.smembers("set-key5"));

        // 移除并返回一个随机元素
        System.out.println(jedis.spop("set-key"));

        // 移除多个元素
        System.out.println(jedis.srem("set-key", "1", "3", "7"));

        // 输出随机集合
        System.out.println(jedis.srandmember("set-key2", 2));

    }

    @Test public void ZSetTest(){

    }

}




package com.github.cache.redis;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.*;

import java.io.IOException;
import java.util.*;

/**
 *  Redis API
 */
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

    // key相关API
    @Test public void keysTest(){

        // 清除库
        jedis.flushDB();

        // 返回条件key
        System.out.println(jedis.keys("*"));

        jedis.set("key", "val");
        //设置key存活时间，秒
        jedis.expire("key", 3);

        // 设置存活时间的时间戳
        jedis.expireAt("key", System.currentTimeMillis() + 10000);

        // 是否存在key
        System.out.println(jedis.exists("key1", "key"));

        // 还剩存活时间，秒
        System.out.println(jedis.ttl("key"));
        // 还剩存活时间，毫秒
        System.out.println(jedis.pttl("key"));

    }

    // String类型API
    @Test public void stringTest(){

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
    @Test public void hashTest(){

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
    @Test public void listTest(){

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
    @Test public void setTest(){

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

    // zset类型API
    @Test public void zSetTest(){

        // 清除库
        jedis.flushDB();

        // 添加
        System.out.println(jedis.zadd("zset-key", 123, "1"));
        Map<String, Double> map  = new HashMap<>();
        map.put("3", 133.123);
        map.put("5", 1.134);
        map.put("2", 431.13);
        map.put("4", 134.1);
        map.put("2", 123.1);
        // 批量添加
        System.out.println(jedis.zadd("zset-key", map));

        // 返回集合大小
        System.out.println(jedis.zcard("zset-key"));

        // 返回权重范围内元素数量
        System.out.println(jedis.zcount("zset-key", 43, 143));

        // 按位置索引查找
        System.out.println(jedis.zrange("zset-key", 1, 3));

        // 按位置索引查找并返回带得分的元素
        Set<Tuple> temp = jedis.zrangeWithScores("zset-key", 1, 3);
        for (Tuple li: temp) {
            System.out.println(li.getElement() + " -> " + li.getScore());
        }

        // 按得分范围查找
        System.out.println(jedis.zrangeByScore("zset-key", 43, 143));

        // 元素得分排名
        System.out.println(jedis.zrank("zset-key", "3"));

        // 元素得分
        System.out.println(jedis.zscore("zset-key", "3"));

        // 移除元素
        System.out.println(jedis.zrem("zset-key", "1", "3"));

        // 移除得分范围内的元素
        System.out.println(jedis.zremrangeByScore("zset-key", 0, 10));
    }

    // 事物API
    @Test public void transactionTest(){

        // 清除库
        jedis.flushDB();

        // 监视key
        jedis.watch("tx-key1", "tx-key2");

        // 启用事物
        Transaction tx = jedis.multi();

        tx.lpush("tx-key1", "1", "3", "5");
        tx.set("tx-key2", "tx");

        // 执行事物
        tx.exec();

        // 取消监视key
        jedis.unwatch();

        System.out.println(jedis.lrange("tx-key1", 0, -1));
        System.out.println(jedis.get("tx-key2"));
    }

    // 集群操作
    @Test public void redisCluster() throws IOException {

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(100);
        config.setMaxWaitMillis(50000L);
        config.setTestOnBorrow(true);

        Set<HostAndPort> nodes = new LinkedHashSet<>();
        nodes.add(new HostAndPort("127.0.0.1", 7000));
        nodes.add(new HostAndPort("127.0.0.1", 7001));
        nodes.add(new HostAndPort("127.0.0.1", 7002));
        nodes.add(new HostAndPort("127.0.0.1", 7003));
        nodes.add(new HostAndPort("127.0.0.1", 7004));
        nodes.add(new HostAndPort("127.0.0.1", 7005));

        JedisCluster jedisCluster = new JedisCluster(nodes, 10000, config);

       // jedisCluster.set("test", "test");

        System.out.println(jedisCluster.get("test"));

        for (int i = 0; i < 10000; i++){
            jedisCluster.lpush("list", "node-" + i);
        }
        System.out.println(jedisCluster.lrange("list", 1000, 1010));
        jedisCluster.close();
    }
}

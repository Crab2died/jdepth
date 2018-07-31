package com.github.usefultool.redisson;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

public class DistributedLock {

    public void testLock(){
        Config config = new Config();
        config.useSingleServer().setAddress("localhost:6379");
        RedissonClient redisson = Redisson.create(config);
        RLock lock = redisson.getLock("myLock");
        try {
            lock.tryLock(10, 10, TimeUnit.MICROSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}

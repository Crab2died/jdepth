package com.github.usefultool.distributedlock;

import org.junit.Before;
import org.junit.Test;

public class ZkLockTest {

    private DistributedLock lock;

    @Before
    public void init() {
        lock = new ZkDistributedLock("localhost:2181", "lock1");
    }

    @Test
    public void test() {
        try {
            lock.tryLock();
        } finally {
            lock.unlock();
        }

    }
}

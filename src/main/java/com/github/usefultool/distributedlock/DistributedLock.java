package com.github.usefultool.distributedlock;

import java.util.concurrent.TimeUnit;

public interface DistributedLock {

    void lock() throws InterruptedException;

    void lock(long time, TimeUnit unit) throws InterruptedException;

    boolean tryLock();

    boolean tryLock(long time, TimeUnit unit) throws InterruptedException;

    void unlock();

}

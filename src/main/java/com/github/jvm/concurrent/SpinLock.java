package com.github.jvm.concurrent;

import java.util.concurrent.atomic.AtomicReference;

// 验证自旋锁不是可重入锁
public class SpinLock {

    private AtomicReference<Thread> owner =new AtomicReference<>();

    public void lock(){
        Thread current = Thread.currentThread();
        while(!owner.compareAndSet(null, current)){
        }
    }

    public void unlock (){
        Thread current = Thread.currentThread();
        owner.compareAndSet(current, null);
    }


}

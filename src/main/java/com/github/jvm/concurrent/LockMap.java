package com.github.jvm.concurrent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 基于读写锁实现的线程安全的HashMap
 *
 * @author : Crab2Died
 * 2018/03/06  15:13:55
 * @see java.util.concurrent.locks.ReentrantReadWriteLock
 */
public class LockMap<K, V> {

    private Lock readLock;

    private Lock writeLock;

    private Map<K, V> map = null;

    public LockMap() {
        this.map = new HashMap<>();
        initLock();
    }

    public LockMap(int initialCapacity) {
        this.map = new HashMap<>(initialCapacity);
        initLock();
    }

    public LockMap(int initialCapacity, float loadFactor) {
        this.map = new HashMap<>(initialCapacity, loadFactor);
        initLock();
    }

    private void initLock() {
        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        this.readLock = readWriteLock.readLock();
        this.writeLock = readWriteLock.writeLock();
    }

    public V get(K key) {
        readLock.lock();
        try {
            return map.get(key);
        } finally {
            readLock.unlock();
        }
    }

    public V put(K key, V value) {
        writeLock.lock();
        try {
            return map.put(key, value);
        } finally {
            writeLock.unlock();
        }
    }

    public void clear() {
        writeLock.lock();
        try {
            map.clear();
        } finally {
            writeLock.unlock();
        }
    }

}

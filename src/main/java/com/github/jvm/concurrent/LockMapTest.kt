package com.github.jvm.concurrent

import org.junit.Test

/**
 * 测试基于读写锁实现的线程安全的HashMap
 * @see LockMap
 */
class LockMapTest {

    @Test
    fun test() {

        // 初始化
        val map: LockMap<String, String> = LockMap()

        // 5个写线程
        (0..4)
                .map {
                    Thread(Runnable {
                        map.put("key-$it", "value-$it")
                    }, "Write-Thread-$it")
                }
                .forEach { it.start() }

        // 百个读线程
        (0..99)
                .map {
                    Thread(Runnable {
                        val key = it % 5
                        println(Thread.currentThread().name + " => key-$key = " + map.get("key-$key"))
                    }, "Read-Thread-$it")
                }
                .forEach { it.start() }

    }

}
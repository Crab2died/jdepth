package com.github.jvm.concurrent

import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * 共享锁
 * @see SharedLock
 */
class SharedLockTest {

    @Test
    fun test() {

        val lock = SharedLock(3)

        val countLock = CountDownLatch(10)
        (0..9)
                .map {
                    Thread(Runnable {
                        lock.lock()
                        try {
                            println("Thread $it get the lock")
                            TimeUnit.MILLISECONDS.sleep(100)
                        } finally {
                            lock.unlock()
                            println("Thread $it release the lock")
                            countLock.countDown()
                        }
                    })
                }
                .forEach { it.start() }

        countLock.await()
    }
}
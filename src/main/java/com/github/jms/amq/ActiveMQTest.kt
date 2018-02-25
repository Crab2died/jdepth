package com.github.jms.amq

import org.junit.Test

class ActiveMQTest {

    @Test
    fun `producer test`() {
        for (i in 1..1000)
            Producer.sendQueueMsg("test-queue", "msg" + i)
        Producer.closeConnection()
    }

    @Test
    fun `consumer test`() {
        (1..5)
                .map {
                    Thread(Runnable {
                        Consumer.getQueueMsg("test-queue")
                    }, "Thread-" + it)
                }
                .forEach { it.start() }

        System.`in`.read()
    }
}


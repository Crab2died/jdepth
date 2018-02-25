package com.github.jms.amq

import org.junit.Test

class ActiveMQTest {

    @Test
    fun `producer queue test`() {
        for (i in 1..100)
            Producer.sendQueueMsg("test-queue", "msg" + i)
        Producer.closeConnection()
    }

    @Test
    fun `consumer queue test`() {
        (1..5)
                .map {
                    Thread(Runnable {
                        Consumer.getQueueMsg("test-queue")
                    }, "Thread-" + it)
                }
                .forEach { it.start() }

        System.`in`.read()
    }

    @Test
    fun `producer topic test`() {
        for (i in 1..10)
            Producer.publishTopicMsg("test-topic", "msg" + i)
        Producer.closeConnection()
    }

    @Test
    fun `consumer topic test`() {
        (1..5)
                .map {
                    Thread(Runnable {
                        Consumer.subscribeTopicMsg("test-topic")
                    }, "Thread-" + it)
                }
                .forEach { it.start() }

        System.`in`.read()
    }
}


package com.github.jms.amq

import org.junit.Test

class ActiveMQTest {

    @Test
    fun producerQueueTest() {
        for (i in 1..100)
            Producer.sendQueueMsg("test-queue", "msg$i")
        Producer.closeConnection()
    }

    @Test
    fun consumerQueueTest() {
        (1..5)
                .map {
                    Thread(Runnable {
                        Consumer.getQueueMsg("test-queue")
                    }, "Thread-$it")
                }
                .forEach { it.start() }

        System.`in`.read()
    }

    @Test
    fun producerTopicTest() {
        for (i in 1..10)
            Producer.publishTopicMsg("test-topic", "msg$i")
        Producer.closeConnection()
    }

    @Test
    fun consumerTopicTest() {
        (1..5)
                .map {
                    Thread(Runnable {
                        Consumer.subscribeTopicMsg("test-topic")
                    }, "Thread-$it")
                }
                .forEach { it.start() }

        System.`in`.read()
    }
}


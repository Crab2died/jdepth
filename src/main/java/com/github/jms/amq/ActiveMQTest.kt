package com.github.jms.amq

import org.junit.Test

class ActiveMQTest {
    @Test
    fun `producer test`() {
        for (i in 1..100)
            Producer.sendQueueMsg("test-queue", "msg" + i)
        Producer.closeConnection()
    }

    @Test
    fun `consumer test`() {
        Consumer.getQueueMsg("test-queue")
    }
}


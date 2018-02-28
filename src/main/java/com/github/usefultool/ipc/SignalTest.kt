package com.github.usefultool.ipc

import org.junit.Test

/**
 * 进程异步通信信号测试
 */
class SignalTest {

    /**
     * 启动监听器
     */
    @Test
    fun `signal listener`() {
        SignalListener.onListen(SignalEventImpl())
        System.`in`.read()
    }

    /**
     * 发送信号
     */
    @Test
    fun `signal launch`() {
        SignalLaunch.doSend("STOP")
    }
}

class SignalEventImpl : SignalEvent {

    override fun stop() {
        println("do something when get the stop signal ")
    }

    override fun restart() {
        println("do something when get the restart signal ")
    }

    override fun status() {
        println("do something when get the status signal ")
    }

}
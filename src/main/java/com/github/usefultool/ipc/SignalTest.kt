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
    fun signalListenerTest() {
        SignalListener.onListen(SignalEventImpl())
        System.`in`.read()
    }

    /**
     * 发送信号
     */
    @Test
    fun signalLaunchTest() {
        SignalLaunch.doSend("STOP")
    }
}

// 信号事件实现
class SignalEventImpl : SignalEvent {

    override fun stop() {
        println("do something when get the stop signal ")
        System.exit(1)
    }

    override fun restart() {
        println("do something when get the restart signal ")
    }

    override fun status() {
        println("do something when get the status signal ")
    }

}
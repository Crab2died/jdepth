package com.github.usefultool.ipc;

/**
 * 信号处理事件
 * 直接实现该接口即可达到信号处理效果
 *
 * @author : Crab2Died
 * 2018/02/28  10:58:23
 */
public interface SignalEvent {

    // 关闭信号事件
    void stop();

    // 重启信号事件
    void restart();

    // 状态信号事件
    void status();
}

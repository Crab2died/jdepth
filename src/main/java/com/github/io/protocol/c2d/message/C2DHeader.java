package com.github.io.protocol.c2d.message;

import java.io.Serializable;
import java.util.Map;

/**
 * Crab2Died(C2D)协议消息头
 *
 * 2017/12/08  09:53:26
 */
public class C2DHeader implements Serializable {

    private static final long serialVersionUID = 0xC2D;

    // 魔数(32bit)
    // 魔数由 C2D(12bit,固定标志位)+主版本号(8bit)+次版本号(12bit)
    private int magic = 0xC2D01001;

    // 消息长度(32bit，包含消息头与消息体总长度)
    private int length;

    // 流水号(64bit,流水号生成器生成)
    private long serial;

    // 会话session(64bit，由session生成器生成)
    private long sessionId;

    // 消息类型(8bit，0 ~ 6)
    // 0 业务请求，1 业务响应，2 业务one way(既是请求又是响应)
    // 3 握手请求，4 握手响应，5 心跳请求，6心跳响应
    private byte signal;

    // 优先级(8bit, 0 ~ 255)
    private byte priority;

    // 消息头扩展信息(不定长)
    // map size为0表示无扩展消息
    private Map<String, Object> attachment;

    public int getMagic() {
        return magic;
    }

    public void setMagic(int magic) {
        this.magic = magic;
    }

    public long getSerial() {
        return serial;
    }

    public void setSerial(long serial) {
        this.serial = serial;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public byte getSignal() {
        return signal;
    }

    public void setSignal(byte signal) {
        this.signal = signal;
    }

    public byte getPriority() {
        return priority;
    }

    public void setPriority(byte priority) {
        this.priority = priority;
    }

    public Map<String, Object> getAttachment() {
        return attachment;
    }

    public void setAttachment(Map<String, Object> attachment) {
        this.attachment = attachment;
    }


    @Override
    public String toString() {
        return "C2DHeader{" +
                "magic=" + magic +
                ", length=" + length +
                ", serial=" + serial +
                ", sessionId=" + sessionId +
                ", signal=" + signal +
                ", priority=" + priority +
                ", attachment=" + attachment +
                '}';
    }
}

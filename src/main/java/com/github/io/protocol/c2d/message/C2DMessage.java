package com.github.io.protocol.c2d.message;

import java.io.Serializable;

/**
 * Crab2Died(C2D)协议消息
 * C2D协议消息由消息头+消息体构成
 *
 * @see C2DHeader
 * 2017/12/08  09:35:57
 */
public class C2DMessage implements Serializable {

    private static final long serialVersionUID = 0xC2D;

    // 消息头，不定长
    private C2DHeader header;

    // 消息体，不定长
    private Object body;

    public C2DHeader getHeader() {
        return header;
    }

    public void setHeader(C2DHeader header) {
        this.header = header;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "C2DMessage{" +
                "header=" + header +
                ", body=" + body +
                '}';
    }
}

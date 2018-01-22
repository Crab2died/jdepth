package com.github.jvm.io.netty.serialize;

import java.io.Serializable;

public class SubscribeResp implements Serializable {

    private static final long serialVersionUID = 2L;

    private int subReqId;

    private String respCode;

    private String desc;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getSubReqId() {
        return subReqId;
    }

    public void setSubReqId(int subReqId) {
        this.subReqId = subReqId;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "SubscribeResp{" +
                "subReqId=" + subReqId +
                ", respCode='" + respCode + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}

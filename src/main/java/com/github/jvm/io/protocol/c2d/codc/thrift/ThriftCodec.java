package com.github.jvm.io.protocol.c2d.codc.thrift;

import org.apache.thrift.transport.TIOStreamTransport;
import org.apache.thrift.transport.TTransport;

import java.io.ByteArrayOutputStream;

/**
 * Thrift序列化
 */
public class ThriftCodec {

    public byte[] encode(Object object){

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TTransport transport = new TIOStreamTransport(out);
        //TBinaryProtocol tp = new TBinaryProtocol(transport);
        //TCompactProtocol tp = new TCompactProtocol(transport);

        return out.toByteArray();
    }
}

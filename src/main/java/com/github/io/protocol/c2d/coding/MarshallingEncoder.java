package com.github.io.protocol.c2d.coding;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.Marshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MarshallingEncoder {

    private static final Logger logger = LoggerFactory.getLogger(MarshallingEncoder.class);

    private static final byte[] LENGTH_PLACEHOLDER = new byte[4];

    private Marshaller marshaller;

    public MarshallingEncoder() throws IOException {
        marshaller = MarshallingCodecFactory.buildMarshalling();
    }

    protected void encode(Object object, ByteBuf buf) throws IOException {
        try {
            int lenPost = buf.writerIndex();
            buf.writeBytes(LENGTH_PLACEHOLDER);
            ChannelBufferByteOutput output = new ChannelBufferByteOutput(buf);
            marshaller.start(output);
            marshaller.writeObject(object);
            marshaller.finish();
            buf.setInt(lenPost, buf.writerIndex() - lenPost - 4);
            logger.info("object encode completed");
        } finally {
            marshaller.close();
        }
    }

}

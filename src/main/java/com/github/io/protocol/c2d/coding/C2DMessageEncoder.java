package com.github.io.protocol.c2d.coding;

import com.github.io.protocol.c2d.message.C2DHeader;
import com.github.io.protocol.c2d.message.C2DMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class C2DMessageEncoder extends MessageToByteEncoder<C2DMessage> {

    private static final Logger logger = LoggerFactory.getLogger(C2DMessageEncoder.class);

    private MarshallingEncoder marshallingEncoder;

    public C2DMessageEncoder() throws IOException {
        this.marshallingEncoder = new MarshallingEncoder();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, C2DMessage msg, ByteBuf buf)
            throws Exception {

        if (null == msg || null == msg.getHeader()) {
            logger.warn("The encoder massage is null");
            throw new Exception("The encoder massage is null");
        }
        C2DHeader header = msg.getHeader();
        buf.writeInt(header.getMagic());
        buf.writeLong(header.getLength());
        buf.writeLong(header.getSerial());
        buf.writeByte(header.getSignal());
        buf.writeByte(header.getPriority());
        buf.writeInt(header.getAttachment() == null ? 0 : header.getAttachment().size());

        for (Map.Entry<String, Object> param : header.getAttachment().entrySet()) {
            byte[] keyArr = param.getKey().getBytes(CharsetUtil.UTF_8);
            buf.writeInt(keyArr.length);
            buf.writeBytes(keyArr);
            marshallingEncoder.encode(param.getValue(), buf);
        }
        if (null == msg.getBody())
            marshallingEncoder.encode(msg.getBody(), buf);
        else
            buf.writeInt(0);
        buf.setInt(4, buf.readableBytes());
    }

}

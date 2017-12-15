package com.github.io.protocol.c2d.codc.marshalling;

import com.github.io.protocol.c2d.message.C2DHeader;
import com.github.io.protocol.c2d.message.C2DMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class C2DMarshalMsgEncoder extends MessageToMessageEncoder<C2DMessage> {

    private static final Logger logger = LoggerFactory.getLogger(C2DMarshalMsgEncoder.class);

    private C2DMarshalEncoder marshallingEncoder;

    public C2DMarshalMsgEncoder() throws IOException {
        this.marshallingEncoder = MarshallingCodecFactory.buildMarshalling();
    }

    @Override
    public void encode(ChannelHandlerContext ctx, C2DMessage msg, List<Object> out)
            throws Exception {

        if (null == msg || null == msg.getHeader()) {
            logger.warn("The encoder massage is null");
            throw new Exception("The encoder massage is null");
        }
        ByteBuf buf = Unpooled.buffer();
        C2DHeader header = msg.getHeader();
        buf.writeInt(header.getMagic());
        buf.writeInt(header.getLength());
        buf.writeLong(header.getSerial());
        buf.writeLong(header.getSessionId());
        buf.writeByte(header.getSignal());
        buf.writeByte(header.getPriority());
        buf.writeInt(header.getAttachment() == null ? 0 : header.getAttachment().size());

        for (Map.Entry<String, Object> param : header.getAttachment().entrySet()) {
            byte[] keyArr = param.getKey().getBytes(CharsetUtil.UTF_8);
            buf.writeInt(keyArr.length);
            buf.writeBytes(keyArr);
            marshallingEncoder.encode(ctx, param.getValue(), buf);
        }
        if (null != msg.getBody())
            marshallingEncoder.encode(ctx, msg.getBody(), buf);
        else
            buf.writeInt(0);
        buf.setInt(4, buf.readableBytes());
        out.add(buf);
    }
}

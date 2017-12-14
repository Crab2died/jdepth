package com.github.io.protocol.c2d.codc.marshalling;

import com.github.io.protocol.c2d.message.C2DHeader;
import com.github.io.protocol.c2d.message.C2DMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class C2DMarshalMsgDecoder extends LengthFieldBasedFrameDecoder {


    private static final Logger logger = LoggerFactory.getLogger(C2DMarshalMsgDecoder.class);

    private C2DMarshalDecoder marshallingDecoder;

    public C2DMarshalMsgDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
                             int lengthAdjustment, int initialBytesToStrip)
            throws IOException {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
        this.marshallingDecoder = MarshallingCodecFactory.buildUnMarshalling();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {

        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if (null == frame || frame.readableBytes() <= 0) return null;
        C2DMessage message = new C2DMessage();
        C2DHeader header = new C2DHeader();
        header.setMagic(frame.readInt());
        header.setLength(frame.readInt());
        header.setSerial(frame.readLong());
        header.setSessionId(frame.readLong());
        header.setSignal(frame.readByte());
        header.setPriority(frame.readByte());

        int size = frame.readInt();
        if (size > 0) {
            Map<String, Object> attach = new HashMap<>(size);
            for (int i = 0; i < size; i++) {
                int keySize = frame.readInt();
                byte[] keyBytes = new byte[keySize];
                frame.readBytes(keyBytes);
                String key = new String(keyBytes, CharsetUtil.UTF_8);
                attach.put(key, marshallingDecoder.decode(ctx, frame));
            }
            header.setAttachment(attach);
        }
        if (frame.readableBytes() > 4)
            message.setBody(marshallingDecoder.decode(ctx, frame));

        message.setHeader(header);
        logger.info("decode message is completed : " + message);
        return message;
    }
}

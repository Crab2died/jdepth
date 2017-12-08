package com.github.io.protocol.c2d.coding;

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

public class C2DMessageDecoder extends LengthFieldBasedFrameDecoder {

    private static final Logger logger = LoggerFactory.getLogger(C2DMessageDecoder.class);

    private MarshallingDecoder marshallingDecoder;

    public C2DMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength)
            throws IOException {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
        marshallingDecoder = new MarshallingDecoder();
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {

        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if (null == frame) return null;

        //
        C2DMessage message = new C2DMessage();
        C2DHeader header = new C2DHeader();
        header.setMagic(frame.readInt());
        header.setLength(frame.readInt());
        header.setSerial(frame.readLong());
        header.setSignal(frame.readByte());
        header.setPriority(frame.readByte());

        int size = frame.readInt();
        if (size > 0){
            Map<String, Object> attach = new HashMap<>(size);
            for (int i = 0; i < size; i ++){
                int keySize = frame.readInt();
                byte[] keyBytes = new byte[keySize];
                frame.readBytes(keyBytes);
                String key = new String(keyBytes, CharsetUtil.UTF_8);
                attach.put(key, marshallingDecoder.decode(frame));
            }
            header.setAttachment(attach);
        }
        if (frame.readableBytes() > 4)
            message.setBody(marshallingDecoder.decode(frame));

        message.setHeader(header);
        logger.info("decode message is completed : " + message);
        return message;
    }
}

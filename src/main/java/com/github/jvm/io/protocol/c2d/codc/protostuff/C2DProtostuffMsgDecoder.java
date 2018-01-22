package com.github.jvm.io.protocol.c2d.codc.protostuff;

import com.github.jvm.io.protocol.c2d.message.C2DHeader;
import com.github.jvm.io.protocol.c2d.message.C2DMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 *  基于Protostuff的反序列化必须指定明确的反序列化对象才能正确反序列化成功
 *  对于指定为{@code Object.class}是不能被正确反序列化的
 *  所以应用场景的选定很重要
 *
 *  @author : Crab2Died
 * 	2017/12/15  10:21:13
 */
@Deprecated
public class C2DProtostuffMsgDecoder extends LengthFieldBasedFrameDecoder {

    private static final Logger logger = LoggerFactory.getLogger(C2DProtostuffMsgDecoder.class);

    public C2DProtostuffMsgDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
                                   int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
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
                int _len = frame.readInt();
                byte[] valBytes = new byte[_len];
                frame.readBytes(valBytes);
                attach.put(key, ProtostuffCodec.decode(valBytes, Object.class));
            }
            header.setAttachment(attach);
        }
        if (frame.readableBytes() > 4) {
            int _bodyLen = frame.readInt();
            byte[] bodyBytes = new byte[_bodyLen];
            frame.readBytes(bodyBytes);
            message.setBody(ProtostuffCodec.decode(bodyBytes, Object.class));
        }

        message.setHeader(header);
        logger.info("decode message is completed : " + message);
        return message;
    }

}

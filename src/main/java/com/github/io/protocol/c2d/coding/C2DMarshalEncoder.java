package com.github.io.protocol.c2d.coding;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingEncoder;

public class C2DMarshalEncoder extends MarshallingEncoder {


    public C2DMarshalEncoder(MarshallerProvider provider) {
        super(provider);
    }

    @Override
    public void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out)
            throws Exception {
        super.encode(ctx, msg, out);
    }

}

package com.github.jvm.io.protocol.c2d.codc.marshalling;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;

public class C2DMarshalDecoder extends MarshallingDecoder {

    public C2DMarshalDecoder(UnmarshallerProvider provider) {
        super(provider);
    }

    public C2DMarshalDecoder(UnmarshallerProvider provider, int maxObjectSize) {
        super(provider, maxObjectSize);
    }

    @Override
    public Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        return super.decode(ctx, in);
    }
}

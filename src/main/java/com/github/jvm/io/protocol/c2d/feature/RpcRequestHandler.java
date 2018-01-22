package com.github.jvm.io.protocol.c2d.feature;

import com.github.jvm.io.protocol.c2d.message.C2DMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcRequestHandler extends SimpleChannelInboundHandler<C2DMessage> {

    private final static Logger logger = LoggerFactory.getLogger(RpcRequestHandler.class);

    private C2DMessage request;

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, C2DMessage msg)
            throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        logger.error("RpcRequestHandler exception", cause);
        ctx.close();
        ctx.fireExceptionCaught(cause);
    }
}

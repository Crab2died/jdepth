package com.github.io.protocol.c2d.auth;

import com.github.io.protocol.c2d.message.C2DHeader;
import com.github.io.protocol.c2d.message.C2DMessage;
import com.github.io.protocol.c2d.message.MessageSignal;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginAuthReqHandler extends ChannelHandlerAdapter {

    private final static Logger logger = LoggerFactory.getLogger(LoginAuthReqHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(buildLoginReq());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        C2DMessage message = (C2DMessage) msg;

        // 如果是握手应答消息，需要判断是否认证成功
        if (message.getHeader() != null && message.getBody() != null
                && message.getHeader().getSignal() == MessageSignal.AUTH_RESP) {
            int loginResult = (int) message.getBody();
            if (loginResult != 0) {
                // 握手失败，关闭连接
                ctx.close();
                logger.info("Login is error : " + message);
            } else {
                logger.info("Login is ok : " + message);
                ctx.fireChannelRead(msg);
            }
        } else
            //调用下一个channel链..
            ctx.fireChannelRead(msg);
    }

    /**
     * 构建登录请求
     */
    private C2DMessage buildLoginReq() {
        C2DMessage message = new C2DMessage();
        C2DHeader header = new C2DHeader();
        header.setSignal(MessageSignal.AUTH_REQ);
        message.setHeader(header);
        return message;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        logger.error("LoginAuthReqHandler exception", cause);
        ctx.fireExceptionCaught(cause);
    }
}

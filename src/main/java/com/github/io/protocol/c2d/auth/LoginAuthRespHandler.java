package com.github.io.protocol.c2d.auth;

import com.github.io.protocol.c2d.message.C2DHeader;
import com.github.io.protocol.c2d.message.C2DMessage;
import com.github.io.protocol.c2d.message.MessageSignal;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoginAuthRespHandler extends ChannelInboundHandlerAdapter {

    private final static Logger logger = LoggerFactory.getLogger(LoginAuthRespHandler.class);

    /**
     * 本地缓存
     */
    private Map<String, Boolean> nodeCheck = new ConcurrentHashMap<>();

    private String[] whiteList = {"127.0.0.1", "localhost"};


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        C2DMessage message = (C2DMessage) msg;

        // 如果是握手请求消息，处理，其它消息透传
        if (message.getHeader() != null && message.getHeader().getSignal() == MessageSignal.AUTH_REQ) {
            String nodeIndex = ctx.channel().remoteAddress().toString();
            C2DMessage loginResp;
            // 重复登陆，拒绝
            if (nodeCheck.containsKey(nodeIndex)) {
                loginResp = buildResponse((byte) -1);
                logger.error("Refuse to repeat login");
            } else {
                InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
                String ip = address.getAddress().getHostAddress();
                boolean isOK = false;
                for (String WIP : whiteList) {
                    if (WIP.equals(ip)) {
                        isOK = true;
                        break;
                    }
                }
                loginResp = isOK ? buildResponse((byte) 0) : buildResponse((byte) -1);
                if (isOK)
                    nodeCheck.put(nodeIndex, true);
            }
            logger.info("The login response is : " + loginResp + " body [" + loginResp.getBody() + "]");
            ctx.writeAndFlush(loginResp);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    private C2DMessage buildResponse(byte result) {
        C2DMessage message = new C2DMessage();
        C2DHeader header = new C2DHeader();
        header.setSignal(MessageSignal.AUTH_RESP);
        message.setHeader(header);
        message.setBody(result);
        return message;
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        logger.error("LoginAuthRespHandler exception ", cause);
        nodeCheck.remove(ctx.channel().remoteAddress().toString());// 删除缓存
        ctx.close();
        ctx.fireExceptionCaught(cause);
    }
}

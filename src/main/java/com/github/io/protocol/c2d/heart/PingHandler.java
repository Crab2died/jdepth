package com.github.io.protocol.c2d.heart;

import com.github.io.protocol.c2d.message.C2DHeader;
import com.github.io.protocol.c2d.message.C2DMessage;
import com.github.io.protocol.c2d.message.MessageSignal;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.ScheduledFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class PingHandler extends ChannelHandlerAdapter {

    private final static Logger logger = LoggerFactory.getLogger(PingHandler.class);

    //使用定时任务发送
    private volatile ScheduledFuture<?> heartBeat;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        C2DMessage message = (C2DMessage) msg;
        // 当握手成功后，Login响应向下透传，主动发送心跳消息
        if (message.getHeader() != null
                && message.getHeader().getSignal() == MessageSignal.AUTH_RESP) {

            //NioEventLoop是一个Schedule,因此支持定时器的执行，创建心跳计时器
            heartBeat = ctx.executor().scheduleAtFixedRate(
                    new PingHandler.HeartBeatTask(ctx),
                    0,
                    60000,
                    TimeUnit.MILLISECONDS
            );
        } else if (message.getHeader() != null &&
                message.getHeader().getSignal() == MessageSignal.PONG) {
            logger.info("Client receive server heart beat message : ---> " + message);
        } else
            ctx.fireChannelRead(msg);
    }

    //Ping消息任务类
    private class HeartBeatTask implements Runnable {
        private final ChannelHandlerContext ctx;

        HeartBeatTask(final ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            C2DMessage heatBeat = buildHeatBeat();
            logger.info("Client send heart beat message to server : ---> " + heatBeat);
            ctx.writeAndFlush(heatBeat);
        }

        private C2DMessage buildHeatBeat() {
            C2DMessage message = new C2DMessage();
            C2DHeader header = new C2DHeader();
            header.setSerial(System.currentTimeMillis());
            header.setSignal(MessageSignal.PING);
            message.setHeader(header);
            return message;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        logger.error("PingHandler exception", cause);
        if (heartBeat != null) {
            heartBeat.cancel(true);
            heartBeat = null;
        }
        ctx.fireExceptionCaught(cause);
    }
}

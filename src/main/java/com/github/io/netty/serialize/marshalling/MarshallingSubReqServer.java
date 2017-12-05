package com.github.io.netty.serialize.marshalling;

import com.github.io.netty.serialize.SubscribeReq;
import com.github.io.netty.serialize.SubscribeResp;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.UUID;

public class MarshallingSubReqServer {


    private static final int PORT = 8200;

    public static void main(String... args) {
        try {
            new MarshallingSubReqServer().bind(PORT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void bind(int port) throws InterruptedException {

        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(boosGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(MarshallingCodeCFactory.buildDecoder())
                                    .addLast(MarshallingCodeCFactory.buildEncoder())
                                    .addLast(new MarshallingSubReqServerHandler());
                        }
                    });

            ChannelFuture future = bootstrap.bind(port).sync();

            future.channel().closeFuture().sync();
        } finally {
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    class MarshallingSubReqServerHandler extends ChannelHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

            SubscribeReq req = (SubscribeReq) msg;

            SubscribeResp resp = new SubscribeResp();
            resp.setRespCode(UUID.randomUUID().toString());
            resp.setSubReqId(req.getSubReqId());
            resp.setDesc("You order ID is : " + req.getSubReqId() + " address is : " + req.getAddress()
                    + " phone is : " + req.getPhoneNumber());
            ctx.writeAndFlush(resp);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }

}

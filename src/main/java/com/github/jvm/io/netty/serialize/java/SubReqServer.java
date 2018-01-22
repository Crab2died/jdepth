package com.github.jvm.io.netty.serialize.java;

import com.github.jvm.io.netty.serialize.SubscribeReq;
import com.github.jvm.io.netty.serialize.SubscribeResp;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.UUID;

public class SubReqServer {

    private static final int PORT = 8200;

    public static void main(String... args){
        try {
            new SubReqServer().bind(PORT);
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
                                    .addLast(new ObjectDecoder(
                                            1024 * 1024,
                                            ClassResolvers.weakCachingConcurrentResolver(
                                                    this.getClass().getClassLoader())
                                    ))
                                    .addLast(new ObjectEncoder())
                                    .addLast(new SubReqServerHandler());
                        }
                    });

            ChannelFuture future = bootstrap.bind(port).sync();

            future.channel().closeFuture().sync();
        }finally {
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    class SubReqServerHandler extends ChannelHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

            SubscribeReq req = (SubscribeReq) msg;

            SubscribeResp resp = new SubscribeResp();
            resp.setSubReqId(req.getSubReqId());
            resp.setRespCode(UUID.randomUUID().toString());
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

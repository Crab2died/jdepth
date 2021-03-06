package com.github.jvm.io.netty.serialize.java;

import com.github.jvm.io.netty.serialize.SubscribeReq;
import com.github.jvm.io.netty.serialize.SubscribeResp;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class SubRespClient {

    private static final String HOST = "127.0.0.1";

    private static final int PORT = 8200;

    public static void main(String... args) {

        try {
            new SubRespClient().connect(HOST, PORT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void connect(String host, int port) throws InterruptedException {

        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new ObjectEncoder())
                                    .addLast(new ObjectDecoder(
                                            1024,
                                            ClassResolvers.weakCachingConcurrentResolver(
                                                    this.getClass().getClassLoader()
                                            )))
                                    .addLast(new SubRespClientHandler());

                        }
                    });
            ChannelFuture future = bootstrap.connect(host, port).sync();
            future.channel().closeFuture().sync();

        } finally {
            group.shutdownGracefully();
        }

    }

    class SubRespClientHandler extends SimpleChannelInboundHandler<SubscribeResp> {

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {

            for (int i = 1000; i < 1010; i++) {
                SubscribeReq req = new SubscribeReq();
                req.setSubReqId(i);
                req.setUserName("螃蟹——C");
                req.setProductName("netty");
                req.setPhoneNumber("010-12122312");
                req.setAddress("Beijing TAM");
                ctx.writeAndFlush(req);
            }
        }

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, SubscribeResp msg) throws Exception {
            System.out.println("Response is : " + msg);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            ctx.flush();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }
}

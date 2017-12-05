package com.github.io.netty.serialize.marshalling;

import com.github.io.netty.serialize.SubscribeReq;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class MarshallingSubRespClient {

    private static final String HOST = "127.0.0.1";

    private static final int    PORT =        8200;

    public static void main(String... args) {

        try {
            new MarshallingSubRespClient().connect(HOST, PORT);
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
                                    .addLast(MarshallingCodeCFactory.buildDecoder())
                                    .addLast(MarshallingCodeCFactory.buildEncoder())
                                    .addLast(new MarshallingSubRespClientHandler());
                        }
                    });
            ChannelFuture future = bootstrap.connect(host, port).sync();
            future.channel().closeFuture().sync();

        } finally {
            group.shutdownGracefully();
        }
    }

    class MarshallingSubRespClientHandler extends ChannelHandlerAdapter {

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {

            for (int i = 1000; i < 1010; i++) {
                SubscribeReq req = new SubscribeReq();
                req.setSubReqId(i);
                req.setProductName("netty");
                req.setUserName("螃蟹—Marshal");
                req.setPhoneNumber("010-12122312");
                req.setAddress("Beijing TAM");
                ctx.writeAndFlush(req);
            }
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
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

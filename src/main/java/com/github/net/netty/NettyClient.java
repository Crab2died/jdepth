package com.github.net.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Scanner;

public class NettyClient {

    private final static String HOST = "127.0.0.1";

    private final static int    PORT =        8200;

    public static void main(String... args) {

        try {
            new NettyClient().connect(HOST, PORT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void connect(String host, int port) throws InterruptedException {

        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ClientHandler());
                        }
                    });

            ChannelFuture future = bootstrap.connect(host, port).sync();

            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }


    class ClientHandler extends ChannelHandlerAdapter {

        private ByteBuf reqBuf;

        ClientHandler() {
            System.out.print("Please input something:");
            Scanner scan = new Scanner(System.in);
            String msg = scan.nextLine();
            byte[] req = msg.getBytes();
            reqBuf = Unpooled.buffer(req.length);
            reqBuf.writeBytes(req);
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) {

            ctx.writeAndFlush(reqBuf);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf buf = (ByteBuf) msg;
            byte[] resp = new byte[buf.readableBytes()];
            buf.readBytes(resp);
            String respBody = new String(resp, "UTF-8");
            System.out.println("Response is : " + respBody);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            ctx.close();
        }
    }
}

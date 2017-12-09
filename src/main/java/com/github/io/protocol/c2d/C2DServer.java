package com.github.io.protocol.c2d;

import com.github.io.protocol.c2d.auth.LoginAuthRespHandler;
import com.github.io.protocol.c2d.coding.C2DMessageDecoder;
import com.github.io.protocol.c2d.coding.C2DMessageEncoder;
import com.github.io.protocol.c2d.heart.PongHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class C2DServer {

    private static final String HOST = "127.0.0.1";

    private static final int PORT = 8200;

    private final static Logger logger = LoggerFactory.getLogger(C2DServer.class);

    public static void main(String... args) {
        try {
            new C2DServer().bind(HOST, PORT);
        } catch (Exception e) {
            logger.error("start server error ", e);
        }
    }

    private void bind(String host, int port) throws InterruptedException {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new C2DMessageDecoder(1024 * 1024, 4, 4, -8, 0))
                                    .addLast("MessageEncoder", new C2DMessageEncoder())
                                    .addLast("ReadTimeoutHandler", new ReadTimeoutHandler(30))
                                    .addLast("LoginAuthResp", new LoginAuthRespHandler())
                                    .addLast("Pong", new PongHandler());
                        }
                    });
            ChannelFuture future = bootstrap.bind(host, port).sync();
            future.channel().closeFuture().sync();
            logger.info("server is started");
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}

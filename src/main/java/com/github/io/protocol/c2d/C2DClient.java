package com.github.io.protocol.c2d;

import com.github.io.protocol.c2d.auth.LoginAuthReqHandler;
import com.github.io.protocol.c2d.coding.C2DMessageDecoder;
import com.github.io.protocol.c2d.coding.C2DMessageEncoder;
import com.github.io.protocol.c2d.heart.PingHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class C2DClient {

    private static final String REMOTE_HOST = "127.0.0.1";
    private static final int REMOTE_PORT = 8200;

    private final static Logger logger = LoggerFactory.getLogger(C2DClient.class);

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    public static void main(String... args) {
        try {
            new C2DClient().connect(REMOTE_HOST, REMOTE_PORT, "127.0.0.1", 8201);
        } catch (InterruptedException e) {
            logger.error("client connect error ", e);
        }
    }

    public void connect(String remoteHost, int remotePort, String localeHost, int localPort) throws InterruptedException {

        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                    .handler(new LoggingHandler(LogLevel.WARN))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new C2DMessageDecoder(1024 * 1024, 4, 4,-8 ,0))
                                    .addLast("MessageEncoder", new C2DMessageEncoder())
                                    .addLast("ReadTimeoutHandler", new ReadTimeoutHandler(30))
                                    .addLast("LoginAuthReq", new LoginAuthReqHandler())
                                    .addLast("Ping", new PingHandler());
                        }
                    });
            // 发起异步连接操作
            ChannelFuture future = bootstrap.connect(
                    new InetSocketAddress(remoteHost, remotePort),
                    new InetSocketAddress(localeHost, localPort)
            ).sync();
            // 当对应的channel关闭的时候，就会返回对应的channel。

            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
            // 所有资源释放完成之后，清空资源，再次发起重连操作
            executor.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(5);
                    try {
                        connect(REMOTE_HOST, REMOTE_PORT, "127.0.0.1", 8201);// 发起重连操作
                    } catch (Exception e) {
                        logger.error("reconnect error", e);
                    }
                } catch (InterruptedException e) {
                    logger.error("InterruptedException", e);
                }
            });
        }
    }

}

package com.github.jvm.io.protocol.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSocketServer {

    private static final int PORT = 8201;

    public static void main(String... args) {

        try {
            new WebSocketServer().run(PORT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void run(int port) throws InterruptedException {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .channel(NioServerSocketChannel.class)
                    .group(bossGroup, workerGroup)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast("http-codec", new HttpServerCodec())
                                    .addLast("aggregator", new HttpObjectAggregator(65536))
                                    .addLast("http-chunked", new ChunkedWriteHandler())
                                    .addLast("handler", new WebSocketServerHandler());
                        }
                    });

            Channel channel = bootstrap.bind(port).sync().channel();
            channel.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

        private final Logger logger = LoggerFactory.getLogger(WebSocketServerHandler.class);

        private WebSocketServerHandshaker handshaker;

        @Override
        protected void messageReceived(ChannelHandlerContext ctx, Object msg)
                throws Exception {

            // 传统的HTTP接入
            if (msg instanceof FullHttpRequest) {
                handleHttpRequest(ctx, (FullHttpRequest) msg);
            }
            // WebSocket接入
            else if (msg instanceof WebSocketFrame) {
                handleWebSocketFrame(ctx, (WebSocketFrame) msg);
            }
        }


        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            ctx.flush();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            logger.error("exception", cause);
            ctx.close();
        }

        private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {

            // HTTP解码，失败
            if (req.decoderResult().isFailure() ||
                    !"websocket".equals(req.headers().get("Upgrade"))) {
                sendResponse(ctx, new DefaultFullHttpResponse(
                        HttpVersion.HTTP_1_1,
                        HttpResponseStatus.BAD_REQUEST)
                );
                return;
            }
            // 构造握手返回，本机测试
            WebSocketServerHandshakerFactory factory = new WebSocketServerHandshakerFactory(
                    "ws://127.0.0.1:8201/websocket",
                    null,
                    false
            );
            handshaker = factory.newHandshaker(req);
            if (null == handshaker) {
                WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            } else {
                handshaker.handshake(ctx.channel(), req);
            }
        }

        private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {

            // 判断是否关闭链路指令
            if (frame instanceof CloseWebSocketFrame) {
                handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
                return;
            }
            // 判断是否为ping指令
            if (frame instanceof PingWebSocketFrame) {
                ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
                return;
            }
            // 本例程只支持文本消息，不支持二进制消息
            if (frame instanceof TextWebSocketFrame) {
                String req = ((TextWebSocketFrame) frame).text();
                logger.info(String.format("%s received %s", ctx.channel(), req));
                ctx.channel().write(new TextWebSocketFrame(req + ", 欢迎"));
            } else {
                throw new UnsupportedOperationException("operation is not support @=。-");
            }
        }

        private void sendResponse(ChannelHandlerContext ctx, FullHttpResponse resp) {

            if (resp.status().code() != 200) {
                ByteBuf buf = Unpooled.copiedBuffer(resp.status().toString(), CharsetUtil.UTF_8);
                resp.content().writeBytes(buf);
                buf.release();
                HttpHeaderUtil.setContentLength(resp, resp.content().readableBytes());
                //return;
            }
            ChannelFuture future = ctx.channel().writeAndFlush(resp);
            if (!HttpHeaderUtil.isKeepAlive(resp) || resp.status().code() != 200) {
                future.addListener(ChannelFutureListener.CLOSE);
            }
        }
    }
}

package com.github.jvm.io.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AIOAcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AIOServerHandler> {

    @Override
    public void completed(AsynchronousSocketChannel result, AIOServerHandler attachment) {
        //继续接受其他客户端的请求
        AIOServer.clientCount++;
        System.out.println("连接的客户端数：" + AIOServer.clientCount);
        attachment.channel.accept(attachment, this);
        //创建新的Buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //异步读  第三个参数为接收消息回调的业务Handler
        result.read(buffer, buffer, new AIOServerReadHandler(result));
    }

    @Override
    public void failed(Throwable exc, AIOServerHandler attachment) {
        exc.printStackTrace();
        attachment.latch.countDown();
    }
}

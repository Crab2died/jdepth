package com.github.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioServerHandler implements Runnable {

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    private volatile boolean stop;

    public NioServerHandler(int port) {

        try {
            // 创建多路复用器selector、serverSocketChannel
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            // 设置为异步非阻塞
            serverSocketChannel.configureBlocking(false);
            // 绑定TCP端口号及最大连接数
            serverSocketChannel.socket().bind(new InetSocketAddress(port), 1024);
            // 将selector注册到channel中，设置监听操作位
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("NIO server started, port is :" + port);
        } catch (IOException e) {
            // 异常退出，如端口占用
            e.printStackTrace();
        }

    }

    public void stop(){
        this.stop = true;
    }

    @Override
    public void run() {
        while (!stop) {

            try {
                // 设置休眠时间为1s
                selector.select(1000);
                // 获取操作位集合
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                // 迭代器
                Iterator<SelectionKey> it = selectionKeys.iterator();
                SelectionKey key;
                while (it.hasNext()) {
                    key = it.next();
                    it.remove();
                    try {
                        inputHandle(key);
                    } catch (Exception e) {
                        if (null != key) {
                            key.cancel();
                            if (null != key.channel())
                                key.channel().close();
                        }
                    }
                }

            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    private void inputHandle(SelectionKey key) throws IOException {
        if (key.isValid()) {
            // 客户端连接请求处理
            if (key.isAcceptable()) {
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                SocketChannel sc = ssc.accept();
                sc.configureBlocking(false);
                sc.register(selector, SelectionKey.OP_READ);
            }
            // 消息读取
            if (key.isReadable()) {
                SocketChannel sc = (SocketChannel) key.channel();
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(readBuffer);
                if (readBytes > 0) {
                    readBuffer.flip();// 当前缓冲区limit设置为position，position为0
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String request = new String(bytes, "UTF-8");
                    System.out.println(sc.getRemoteAddress() + " request is: " + request);
                    doResponse(sc, request);
                } else if (readBytes < 0) {
                    //
                    key.cancel();
                    sc.close();
                } else {
                    //
                }
            }
        }
    }

    private void doResponse(SocketChannel socketChannel, String responseBody) throws IOException {

        if (null != responseBody) {

            byte[] bytes = responseBody.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            socketChannel.write(writeBuffer);
        }
    }
}

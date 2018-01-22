package com.github.jvm.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class NioClientHandler implements Runnable {

    private String host;

    private int port;

    private Selector selector;

    private SocketChannel socketChannel;

    private volatile boolean stop;

    public NioClientHandler(String host, int port) {
        this.host = host == null ? "127.0.0.1" : host;
        this.port = port;
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    @Override
    public void run() {

        try {
            doConnect();
        } catch (IOException e) {
            e.printStackTrace();
            // 连接失败
            System.exit(1);
        }
        while (!stop) {
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                SelectionKey key;
                while (it.hasNext()) {
                    key = it.next();
                    it.remove();
                    try {
                        inputHandle(key);
                    } catch (Exception e) {
                        if (null != key){
                            key.cancel();
                            if (null != key.channel()){
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (null != selector){
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        this.stop = true;
    }

    private void inputHandle(SelectionKey key) throws IOException {

        if (key.isValid()) {

            SocketChannel sc = (SocketChannel) key.channel();
            if (key.isConnectable()) {
                if (sc.finishConnect()) {
                    sc.register(selector, SelectionKey.OP_READ);
                    doRequest(sc);
                } else {
                    //
                    System.out.println("连接失败");
                    System.exit(1);
                }
                if (key.isReadable()) {
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                    int readBytes = sc.read(readBuffer);
                    if (readBytes > 0) {
                        byte[] bytes = new byte[readBuffer.remaining()];
                        readBuffer.get(bytes);
                        String response = new String(bytes, "UTF-8");
                        System.out.println(sc.getRemoteAddress() + " response is: " + response);
                        this.stop = true;
                    }else if (readBytes < 0){
                        key.cancel();
                        sc.close();
                    }else {
                        //
                    }
                }
            }

        }
    }

    private void doConnect() throws IOException {
        if (socketChannel.connect(new InetSocketAddress(host, port))) {
            socketChannel.register(selector, SelectionKey.OP_READ);
            // do request
            doRequest(socketChannel);
        } else {
            socketChannel.register(selector, SelectionKey.OP_CONNECT);

        }

    }

    private void doRequest(SocketChannel sc) throws IOException {

        //for (; ; ) {
            System.out.print("Please input something:");
            Scanner scan = new Scanner(System.in);
            String str = scan.nextLine();
            System.out.println(str);
            byte[] req = str.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
            writeBuffer.put(req);
            writeBuffer.flip();
            sc.write(writeBuffer);
            if (!writeBuffer.hasRemaining()) {
                System.out.println("Send request 2 server succeed");
            }
        //}

    }

}

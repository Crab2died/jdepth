package com.github.jvm.io.bio;

import java.net.ServerSocket;
import java.net.Socket;

public class BioServer {

    private final static int PORT = 8200;

    public static void main(String... args) {

        try {
            try (ServerSocket server = new ServerSocket(PORT)) {
                System.out.println("Server socket started, address is " + server.getInetAddress() + " port is " + PORT);
                for (; ; ) {
                    Socket socket = server.accept();
                    System.out.println(socket.getInetAddress() + ":" + socket.getPort() + " is connected");
                    // 为每一个连接创建一个线程跟踪处理
                    new Thread(new BioServerHandler(socket)).start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

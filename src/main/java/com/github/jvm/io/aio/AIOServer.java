package com.github.jvm.io.aio;

public class AIOServer {

    private static int DEFAULT_PORT = 12345;
    private static AIOServerHandler serverHandle;
    public volatile static long clientCount = 0;

    public static void start() {
        start(DEFAULT_PORT);
    }

    public static synchronized void start(int port) {
        if (serverHandle != null)
            return;
        serverHandle = new AIOServerHandler(port);
        new Thread(serverHandle, "Server").start();
    }

    public static void main(String[] args) {
        AIOServer.start();
    }
}

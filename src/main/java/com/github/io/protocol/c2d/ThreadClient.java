package com.github.io.protocol.c2d;

import java.io.IOException;

public class ThreadClient {

    private static final String REMOTE_HOST = "127.0.0.1";
    private static final int REMOTE_PORT = 8200;

    public static void main(String[] args) throws IOException {

        for (int i = 1; i <= 10; i ++){
            int finalI = i;
            new Thread(() -> {
                try {
                    new C2DClient().connect(REMOTE_HOST, REMOTE_PORT, "127.0.0.1", 8201 + finalI);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "Thread-Client-" + i).start();
        }
        System.in.read();
    }
}

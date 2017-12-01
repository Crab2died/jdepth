package com.github.net.nio;

public class NioServer {

    private final static int PORT = 8200;

    public static void main(String[] args){

        new Thread(new NioServerHandler(PORT), "nio-server-thread").start();

    }

}

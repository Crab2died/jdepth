package com.github.net.nio;

public class NioClient {

    public static void main(String[] args){

        new Thread(new NioClientHandler(null, 8200), "nio-client-thread").start();
    }
}

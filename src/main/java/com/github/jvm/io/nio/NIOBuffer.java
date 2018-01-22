package com.github.jvm.io.nio;

import java.nio.ByteBuffer;

public class NIOBuffer {

    public static void main(String... args) {

        ByteBuffer buffer = ByteBuffer.allocateDirect(64);  // 直接内存
        buffer.put((byte) 121);
        buffer.put((byte) 46);
        buffer.flip();
        System.out.println(buffer.limit());
        System.out.println(buffer.isDirect());
        buffer.flip();
        System.out.println(buffer.limit());
    }
}

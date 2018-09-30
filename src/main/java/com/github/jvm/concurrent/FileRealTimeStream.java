package com.github.jvm.concurrent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileRealTimeStream {

    public static void main(String... args) throws IOException {

        try (FileInputStream fis = new FileInputStream(new File("File.txt"));
             FileChannel ch = fis.getChannel()) {

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            ///ch.position(200);
            for (; ; ) {
                while (ch.size() > ch.position()) {
                    ch.read(buffer);
                    buffer.flip();
                    byte[] bytes = new byte[buffer.remaining()];
                    buffer.get(bytes);
                    System.out.print(new String(bytes));
                    buffer.clear();
                }
            }
        }

    }

}


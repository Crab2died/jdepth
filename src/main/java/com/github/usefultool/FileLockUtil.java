package com.github.usefultool;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * 文件锁机制
 */
public class FileLockUtil {

    public static void main(String... args) {
        try {
            // mod 'r'  'rw'  'rws'  'rwd'
            try (RandomAccessFile fis = new RandomAccessFile("D:\\资料\\myFile.txt", "rws");
                 FileChannel ch = fis.getChannel();
                 FileLock fl = ch.tryLock()) {
                if (null != fl) {
                    int len;
                    byte[] buf = new byte[1024];
                    while ((len = fis.read(buf)) != -1) {
                        System.out.println(new String(buf, 0, len));
                    }
                }
                fis.seek(0);
                fis.write("aaaaddda".getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

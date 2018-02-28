package com.github.usefultool.ipc;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 发送信号量
 *
 * @author : Crab2Died
 * 2018/02/28  10:50:46
 */
public class SignalLaunch {

    /**
     * 信号有三种：
     * 1. stop 信号
     * 2. restart 信号
     * 3. status 信号
     */
    public static void doSend(String signal) throws IOException {

        if ("".equals(signal) || null == signal)
            throw new RuntimeException("signal must not blank");
        signal = signal.toLowerCase();
        FileChannel fc = null;
        try {
            RandomAccessFile raf = new RandomAccessFile(".lock", "rw");
            fc = raf.getChannel();
            MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE, 0, 16);
            for (int i = 0; i < 16; i++)
                mbb.put(i, (byte) 0);
            if ("stop".equals(signal)) {
                mbb.put(0, (byte) Signal.STOP);
            }
            if ("restart".equals(signal)) {
                mbb.put(0, (byte) Signal.RESTART);
            }
            if ("status".equals(signal)) {
                mbb.put(0, (byte) Signal.STATUS);
            }
        } finally {
            if (fc != null) {
                fc.close();
            }
        }
    }
}

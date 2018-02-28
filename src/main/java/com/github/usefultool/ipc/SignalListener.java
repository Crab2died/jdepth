package com.github.usefultool.ipc;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 系统信号监听器
 *
 * @author : Crab2Died
 * 2018/02/28  10:56:09
 */
public class SignalListener {

    public static void onListen(SignalEvent event) {

        // 独立线程监听
        new Thread(new Runnable() {

            @Override
            public void run() {
                FileChannel fc = null;
                try {
                    File file = new File(".lock");
                    if (!file.exists())
                        if (!file.createNewFile())
                            throw new IOException("can not create the .lock file");
                    RandomAccessFile raf = new RandomAccessFile(".lock", "rw");
                    fc = raf.getChannel();
                    MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE, 0, 16);
                    for (int i = 0; i < 16; i++)
                        mbb.put(i, (byte) 0);
                    for (; ; ) {
                        int sign = mbb.get(0);
                        if (sign == Signal.STOP) {
                            mbb.put(0, (byte) 0);
                            event.stop();
                            break;
                        }
                        if (sign == Signal.RESTART) {
                            mbb.put(0, (byte) 0);
                            event.restart();
                        }
                        if (sign == Signal.STATUS) {
                            mbb.put(0, (byte) 0);
                            event.status();
                        }
                    }
                } catch (IOException e) {
                    System.err.println("lock file channel create error");
                    System.exit(2);
                } finally {
                    if (null != fc) {
                        try {
                            fc.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        }).start();
    }
}

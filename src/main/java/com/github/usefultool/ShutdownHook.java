package com.github.usefultool;

/**
 * 优雅停机
 * 当执行{@link System#exit(int)}时
 * 当执行kill -2 PID 时
 * 当^C时
 *
 * @author Crab2Died
 * @date 2018/11/30 10:40
 */
public class ShutdownHook {

    private volatile static boolean RUNNING = true;

    public static void main(String... args) {

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Stop...");
            synchronized (ShutdownHook.class) {
                RUNNING = false;
                ShutdownHook.class.notify();
            }
        }));

        System.out.println("Start running...");

        synchronized (ShutdownHook.class) {
            for (; RUNNING; ) {
                try {
                    ShutdownHook.class.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

package com.github.jvm.concurrent;

/**
 * 死锁例子
 */
public class DeadLock {

    /**
     * A锁
     */
    private static final String A = "A";
    /**
     * B锁
     */
    private static final String B = "B";

    public static void main(String[] args) {
        new DeadLock().deadLock();
    }

    private void deadLock() {
        Thread t1 = new Thread(() -> {
            synchronized (A) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (B) {
                    System.out.println("1");
                }
            }
        }, "dead-thread-01");

        Thread t2 = new Thread(() -> {
            synchronized (B) {
                synchronized (A) {
                    System.out.println("2");
                }
            }
        }, "dead-thread-02");
        t1.start();
        t2.start();
    }

}
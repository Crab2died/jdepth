package com.github.jvm.concurrent;

import java.util.Random;
import java.util.concurrent.*;

/**
 * 阻塞队列
 */
public class BlockingQueueFeature {

    private static BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(20);

    //private static BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(20);

    // 自增长优先级阻塞队列
    //private static BlockingQueue<Integer> queue = new PriorityBlockingQueue<>(20, Comparator.comparingInt(o -> o));

    // 生产者
    static class Producer implements Runnable {

        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() + "当前队列大小为：" + queue.size());
                queue.put(new Random().nextInt(100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    // 消费者
    static class Consumer implements Runnable {

        @Override
        public void run() {
            try {
                Integer val = queue.take();
                System.out.println(Thread.currentThread().getName() + "从队列获取了：" + val);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String... args) {

        ThreadPoolExecutor producerExec = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        producerExec.setMaximumPoolSize(3);
        //
//        ThreadPoolExecutor producerExec = new ThreadPoolExecutor(2, 5, 0L, TimeUnit.MILLISECONDS, new
//                LinkedBlockingQueue<>(), new ThreadPoolExecutor.DiscardPolicy());
        ScheduledExecutorService consumerExec = Executors.newScheduledThreadPool(3);

        for (; ; ) {

            producerExec.execute(new Producer());

            consumerExec.schedule(new Consumer(), 100L, TimeUnit.MILLISECONDS);
        }
    }
}

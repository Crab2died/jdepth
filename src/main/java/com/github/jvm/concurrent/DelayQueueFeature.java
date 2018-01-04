package com.github.jvm.concurrent;

import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * {@link java.util.concurrent.DelayQueue}
 * <p>
 * 延时队列
 *
 * @see java.util.concurrent.BlockingQueue
 */
public class DelayQueueFeature {


    public static void main(String... args) {

        DelayQueue<DelayItem> queue = new DelayQueue<>();
        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            int r = random.nextInt(10000);
            queue.offer(new DelayItem(r, "item - " + i), r, TimeUnit.MILLISECONDS);
        }
        for (int i = 0; i < 1000; i++) {
            try {
                System.out.println(queue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(queue.size());
    }

    static class DelayItem implements Delayed {

        private static final long NOW = System.nanoTime();

        private int timeOut; // 延时时间

        private String desc;

        public DelayItem() {
        }

        public DelayItem(int timeOut, String desc) {
            this.timeOut = timeOut;
            this.desc = desc;
        }

        @Override
        public long getDelay(@NotNull TimeUnit unit) {
            return unit.convert(NOW + timeOut - System.nanoTime(), TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(@NotNull Delayed o) {

            return (int) ((getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS)));
        }

        public int getTimeOut() {
            return timeOut;
        }

        public void setTimeOut(int timeOut) {
            this.timeOut = timeOut;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        @Override
        public String toString() {
            return "DelayItem{" +
                    "timeOut=" + timeOut +
                    ", desc='" + desc + '\'' +
                    '}';
        }
    }
}

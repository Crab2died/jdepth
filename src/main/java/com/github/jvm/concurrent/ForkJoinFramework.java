package com.github.jvm.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * Fork/Join框架
 *
 * @author : Crab2Died
 * 2018/03/07  16:26:18
 */
public class ForkJoinFramework {

    // 累加器
    static class CountTask extends RecursiveTask<Integer> {

        // 最大计算范围量
        private static int MIN_THRESHOLD = 100;

        // 初始值
        private int start;

        // 结束值
        private int end;

        public CountTask(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {

            int sum = 0;

            if ((end - start) > MIN_THRESHOLD) {
                for (int i = start; i <= end; i++) {
                    sum += i;
                }
            } else {
                int middle = (start + end) / 2;
                CountTask leftTask = new CountTask(start, middle);
                CountTask rightTask = new CountTask(middle + 1, end);
                leftTask.fork();
                rightTask.fork();
                sum = leftTask.join() + rightTask.join();
            }

            return sum;
        }
    }

    public static void main(String... args) throws ExecutionException, InterruptedException {

        int start = 1, end = 100000;

        long t1 = System.currentTimeMillis();
        ForkJoinPool pool = new ForkJoinPool();
        CountTask task = new CountTask(start, end);
        ForkJoinTask<Integer> result = pool.submit(task);
        long t2 = System.currentTimeMillis();
        System.out.println("Fork/Join计算结果为：" + result.get() + ", 耗时：" + (t2 - t1) + "ms");

        long t3 = System.currentTimeMillis();
        int sum = 0;
        for (int i = start; i <= end; i++) {
            sum += i;
        }
        long t4 = System.currentTimeMillis();
        System.out.println("For循环计算结果为：" + sum + ", 耗时：" + (t4 - t3) + "ms");

    }
}

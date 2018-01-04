package com.github.jvm.concurrent;

import com.github.support.A;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @see java.util.concurrent.Semaphore
 */
public class SemaphoreFeature {

    static class ObjectPool<E> {

        // 池大小
        private int size;

        // 对象集合
        private List<E> items;

        //
        private volatile boolean[] itemLabel;

        //
        private Semaphore semaphore;

        public ObjectPool(List<E> items) {
            if (null == items || items.size() <= 0)
                throw new IllegalArgumentException();
            this.items = items;
            this.size = items.size();
            this.itemLabel = new boolean[size];
            this.semaphore = new Semaphore(size, true);
        }

        public E take() throws InterruptedException {
            semaphore.acquire();
            for (int i = 0; i < itemLabel.length; i++) {
                if (!itemLabel[i]) {
                    itemLabel[i] = true;
                    System.out.println("对象获取成功");
                    return items.get(i);
                }
            }
            System.out.println("对象获取失败");
            return null;
        }

        public boolean release(E item) {

            int index = items.indexOf(item);
            if (index >= 0) {
                itemLabel[index] = false;
                semaphore.release();
                System.out.println("对象释放成功");
                return true;
            }
            System.out.println("对象释放失败");
            return false;
        }
    }


    public static void main(String... args) {
        ObjectPool<A> pool = new ObjectPool<>(Arrays.asList(
                new A("a"),
                new A("b"),
                new A("c"),
                new A("d")
        ));
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                A _a = null;
                try {
                    _a = pool.take();
                    if (null != _a) {
                        System.out.println(Thread.currentThread().getName() + " :" + _a.getA());
                        _a.setA(_a.getA() + _a.getA());
                        TimeUnit.SECONDS.sleep(3);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (null != _a)
                        pool.release(_a);
                }
            }, "Thread-" + i).start();
        }
    }
}

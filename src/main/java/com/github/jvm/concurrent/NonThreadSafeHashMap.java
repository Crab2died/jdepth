package com.github.jvm.concurrent;

import java.util.HashMap;
import java.util.UUID;

public class NonThreadSafeHashMap {

    // 可能会出现死链
    public static void main(String... args) throws InterruptedException {
        final HashMap<String, String> map = new HashMap<>(2);
        Thread t = new Thread(() -> {
            for (int i = 0; i < 10000; i++)
                new Thread(() -> map.put(UUID.randomUUID().toString(), ""), "ftf" + i).start();
        }, "ftf");
        t.start();
        t.join();
    }
}

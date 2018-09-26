package com.github.usefultool.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Test;

public class ZookeeperApi {

    private static ZooKeeper zooKeeper;
    private static final String ADDRESS = "localhost:2181";
    private static final String ROOT = "/ROOT";

    protected class Watcher1 implements Watcher {

        @Override
        public void process(WatchedEvent watchedEvent) {
            System.out.println(watchedEvent.toString());
        }
    }

    @Test
    public void test1() {
        try {
            zooKeeper = new ZooKeeper(ADDRESS, 20000, new Watcher1());
            Stat stat = zooKeeper.exists(ROOT, false);
            if (null == stat) {
                String path = zooKeeper.create(ROOT, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                System.out.println(path);
            }
            String path1 = zooKeeper.create(ROOT + "/path", "path".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            System.out.println(path1);
            String path2 = zooKeeper.create(ROOT + "/path", "path".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            System.out.println(path2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @After
    public void close() throws InterruptedException {
        if (null != zooKeeper) {
            System.out.println("Disconnected zk");
            zooKeeper.close();
        }

    }
}

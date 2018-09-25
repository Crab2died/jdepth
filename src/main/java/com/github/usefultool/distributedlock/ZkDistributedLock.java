package com.github.usefultool.distributedlock;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ZkDistributedLock implements DistributedLock, Watcher {

    private String zkAddr = "localhost:2181";

    private String lockKey;

    private ZooKeeper zk;

    private String CURRENT_LOCK;

    // 等待的前一个锁
    private String WAIT_LOCK;

    private CountDownLatch countDownLatch;

    private static final int TIMEOUT = 20000;

    private static final String ROOT_LOCK = "/LOCKS";

    public ZkDistributedLock() {
    }

    public ZkDistributedLock(String zkAddr, String lockKey) {
        this.zkAddr = zkAddr;
        this.lockKey = lockKey;

        // 连接zookeeper
        try {
            zk = new ZooKeeper(zkAddr, TIMEOUT, this);
            Stat stat = zk.exists(ROOT_LOCK, false);
            if (stat == null) {
                // 如果根节点不存在，则创建根节点
                zk.create(ROOT_LOCK, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (IOException | KeeperException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void lock() throws InterruptedException {

    }

    @Override
    public void lock(long time, TimeUnit unit) throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        try {
            String splitStr = "_lock_";
            if (lockKey.contains(splitStr)) {
                throw new IllegalArgumentException("锁名有误");
            }
            // 创建临时有序节点
            CURRENT_LOCK = zk.create(ROOT_LOCK + "/" + lockKey + splitStr, new byte[0],
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            // 取所有子节点
            List<String> subNodes = zk.getChildren(ROOT_LOCK, false);
            // 取出所有lockName的锁
            List<String> lockObjects = new ArrayList<>();
            for (String node : subNodes) {
                String _node = node.split(splitStr)[0];
                if (_node.equals(lockKey)) {
                    lockObjects.add(node);
                }
            }
            Collections.sort(lockObjects);
            // 若当前节点为最小节点，则获取锁成功
            if (CURRENT_LOCK.equals(ROOT_LOCK + "/" + lockObjects.get(0))) {
                return true;
            }
            // 若不是最小节点，则找到自己的前一个节点
            String prevNode = CURRENT_LOCK.substring(CURRENT_LOCK.lastIndexOf("/") + 1);
            WAIT_LOCK = lockObjects.get(Collections.binarySearch(lockObjects, prevNode) - 1);
        } catch (InterruptedException | KeeperException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        try {
            zk.delete(CURRENT_LOCK, -1);
            CURRENT_LOCK = null;
            zk.close();
        } catch (InterruptedException | KeeperException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (null != countDownLatch)
            countDownLatch.countDown();
    }
}

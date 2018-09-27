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

    private final String lockName;

    private ZooKeeper zk;

    private int sessionTimeout = 30000;

    private int count;

    private CountDownLatch countDownLatch;

    private String CURRENT_LOCK;

    private String WAIT_LOCK;

    private final String ROOT_LOCK;

    private static final String ROOT_NODE = "/LOCKS";

    private static final String LOCK_SPILT = "_lock_";

    public ZkDistributedLock(String address, String lockName) {
        this(address, 30000, lockName);
    }

    public ZkDistributedLock(String address, int sessionTimeout, String lockName) {

        if (lockName.contains(LOCK_SPILT)) {
            throw new IllegalArgumentException("lock name [" + lockName + "] can not contains '_lock_'");
        }
        this.lockName = lockName;
        this.sessionTimeout = sessionTimeout;
        this.ROOT_LOCK = ROOT_NODE + "/" + lockName;
        try {
            // connect zookeeper
            zk = new ZooKeeper(address, sessionTimeout, this);
            Stat stat = zk.exists(ROOT_LOCK, false);
            if (stat == null) {
                // create root node if it not exist
                zk.create(ROOT_LOCK, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (IOException | KeeperException | InterruptedException e) {
            throw new RuntimeException("create zookeeper distributed lock error", e);
        }
    }

    @Override
    public void lock() throws InterruptedException {
        if (tryLock())
            return;
        try {
            if (waitForLock(sessionTimeout, TimeUnit.MILLISECONDS)) {
                throw new InterruptedException();
            }
        } catch (KeeperException e) {
            throw new InterruptedException(e.getMessage());
        }
    }

    @Override
    public void lock(long time, TimeUnit unit) throws InterruptedException {
        if (tryLock())
            return;
        try {
            if (!waitForLock(time, unit)) {
                throw new InterruptedException();
            }
        } catch (KeeperException e) {
            throw new InterruptedException(e.getMessage());
        }
    }

    @Override
    public boolean tryLock() {
        if (count > 0) {
            count++;
            return true;
        }
        try {
            // create provisional node
            CURRENT_LOCK = zk.create(ROOT_LOCK + "/" + lockName + LOCK_SPILT, new byte[0],
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            // get all sub nodes
            List<String> subNodes = zk.getChildren(ROOT_LOCK, false);
            // get all lockName locks
            List<String> lockObjects = new ArrayList<>();
            for (String node : subNodes) {
                String _node = node.split(LOCK_SPILT)[0];
                if (_node.equals(lockName)) {
                    lockObjects.add(node);
                }
            }
            Collections.sort(lockObjects);
            // get lock success when current node is the first zk node
            if (CURRENT_LOCK.equals(ROOT_LOCK + "/" + lockObjects.get(0))) {
                count++;
                return true;
            }
            // get the prev zk node when current node is not the first node
            String prevNode = CURRENT_LOCK.substring(CURRENT_LOCK.lastIndexOf("/") + 1);
            WAIT_LOCK = lockObjects.get(Collections.binarySearch(lockObjects, prevNode) - 1);
        } catch (InterruptedException | KeeperException e) {
            return false;
        }
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        try {
            return tryLock() || waitForLock(time, unit);
        } catch (KeeperException e) {
            throw new InterruptedException(e.getMessage());
        }
    }

    @Override
    public void unlock() {
        if (--count > 0) {
            return;
        }
        try {
            zk.delete(CURRENT_LOCK, -1);
            CURRENT_LOCK = null;
            zk.close();
            count = 0;
        } catch (InterruptedException | KeeperException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * zookeeper {@link Watcher} interface implement
     */
    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
            if (null != this.countDownLatch) {
                this.countDownLatch.countDown();
                count++;
            }
        }
    }

    private boolean waitForLock(long time, TimeUnit unit) throws KeeperException, InterruptedException {

        Stat stat = zk.exists(ROOT_LOCK + "/" + WAIT_LOCK, true);
        if (stat != null) {
            this.countDownLatch = new CountDownLatch(1);
            // waiting for CountDownLatch = 0
            if (this.countDownLatch.await(time, unit)) {
                countDownLatch = null;
            } else {
                return false;
            }
        }
        return true;
    }
}

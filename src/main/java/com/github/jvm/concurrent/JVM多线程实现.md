> [返回目录](https://github.com/Crab2died/jdepth)

#                                               JVM多线程实现
----
## 1. JAVA与线程
### 1.1 线程的实现
   1. 实现方式:使用内核线程实现、 使用用户线程实现和使用用户线程加轻量级进程混合实现.
   2. JDK1.2之前是基于用户线程实现的，JDK1.2及以后是基于操作系统原生线程模型实现的.
   
### 1.2 Java线程调度
   1. 线程调度是指系统为线程分配处理器使用权的过程,主要调度方式有两种,分别是协同式线程调度(Cooperative Threads-Scheduling)
      和抢占式线程调度(Preemptive ThreadsScheduling)
   2. 协同式线程调度:线程的执行时间由线程本身来控制,线程把自己的工作执行完了之后,要主动通知系统切换到另外一个线程上;
      优点是实现简单,缺点是线程执行时间不可控制,容易线程阻塞
   3. 每个线程将由系统来分配执行时间,线程的切换不由线程本身来决定(在Java中,Thread.yield()可以让出执行时间,但是要获取执行时
      间的话,线程本身是没有什么办法的);优点是线程的执行时间是系统可控的,也不会有一个线程导致整个进程阻塞的问题.
   4. JAVA线程有10个优先级(Thread.MIN_PRIORITY至Thread.MAX_PRIORITY)
   
### 1.3 线程转换状态
   1. JAVA线程定义了6个状态:  
      * 新建(New):创建后尚未启动的线程处于这种状态.
      * 运行(Runable):Runable包括了操作系统线程状态中的Running和Ready,也就是处于此状态的线程有可能正在执行,也有可能正在
        等待着CPU为它分配执行时间.
      * 无限期等待(Waiting):处于这种状态的线程不会被分配CPU执行时间,它们要等待被  
            其他线程显式地唤醒. 以下方法会让线程陷入无限期的等待状态:  
            ●没有设置Timeout参数的Object.wait()方法.  
            ●没有设置Timeout参数的Thread.join()方法.  
            ●LockSupport.park()方法.  
      * 限期等待(Timed Waiting):处于这种状态的线程也不会被分配CPU执行时间,不过无须等待被其他线程显式地唤醒,在一定时间之后
        它们会由系统自动唤醒. 以下方法会让线程进入限期等待状态:  
            ●Thread.sleep()方法.  
            ●设置了Timeout参数的Object.wait()方法.  
            ●设置了Timeout参数的Thread.join()方法.  
            ●LockSupport.parkNanos()方法.  
            ●LockSupport.parkUntil()方法 
      * 阻塞(Blocked):该状态程序在等待获取一个排他锁，程序在同步时会在该状态 
      * 结束(Terminated):已终止线程的线程状态,线程已经结束执行.
   2. 线程状态转换关系图  
      ![线程状态转换关系](https://raw.githubusercontent.com/Crab2died/jdepth/master/src/main/java/com/github/jvm/concurrent/%E7%BA%BF%E7%A8%8B%E7%8A%B6%E6%80%81%E8%BD%AC%E6%8D%A2%E5%85%B3%E7%B3%BB.png)
   
## 2. 线程安全与锁优化
### 2.1 JAVA中的线程安全
   1. 共享数据分类
      * 不可变(Immutable):不可变对象一定是线程安全的,典型的final
      * 绝对线程安全:
      * 相对线程安全: java大部分的线程安全都是相对线程安全的
      * 线程兼容:
      * 线程对立:

### 2.2 synchronized的优化
   1. synchronized自JDK1.6后引入偏向锁和轻量级锁后大大提升了并发的性能
   2. synchronized锁升级`偏向锁 -> 轻量级锁 -> 重量级锁` 所以在锁高竞争下Lock性能更高
   
## 3. 锁介绍
### 3.1 自旋锁
   1. 自旋锁可以使线程在没有取得锁的时候，不被挂起，而转去执行一个空循环，（即所谓的自旋，就是自己执行空循环），若在若干个空循环后，线程
      如果可以获得锁，则继续执行。若线程依然不能获得锁，才会被挂起。使用自旋锁后，线程被挂起的几率相对减少，线程执行的连贯性相对加强。因
      此，对于那些锁竞争不是很激烈，锁占用时间很短的并发线程，具有一定的积极意义，但对于锁竞争激烈，单线程锁占用很长时间的并发程序，自旋
      锁在自旋等待后，往往毅然无法获得对应的锁，不仅仅白白浪费了CPU时间，最终还是免不了被挂起的操作 ，反而浪费了系统的资源。在JDK1.6中，
      Java虚拟机提供-XX:+UseSpinning参数来开启自旋锁，使用-XX:PreBlockSpin参数来设置自旋锁等待的次数。在JDK1.7开始，自旋锁的参数被
      取消，虚拟机不再支持由用户配置自旋锁，自旋锁总是会执行，自旋锁次数也由虚拟机自动调整。  
   2. 问题：  
      - 可能白占用CPU时间
      - 死锁问题，自己占用锁，还在等待锁释放
      
### 3.2 阻塞锁
   1. 让线程进入阻塞状态进行等待，当获得相应的信号（唤醒，时间） 时，才可以进入线程的准备就绪状态，准备就绪状态的所有线程，通过竞争，进
      入运行状态。JAVA中，能够进入\退出、阻塞状态或包含阻塞锁的方法有 ，synchronized 关键字（其中的重量锁），ReentrantLock，
      Object.wait()\notify() 

### 3.3 可重入锁
   1. 可重入锁，也叫做递归锁，指的是同一线程 外层函数获得锁之后 ，内层递归函数仍然有获取该锁的代码，但不受影响。    
      在JAVA环境下 ReentrantLock 和synchronized 都是 可重入锁

### 3.4 乐观锁和悲观锁
   1. 悲观锁: 每次拿数据都上锁，如行锁、表锁、读锁、写锁
   2. 乐观锁: 每次拿数据都不上锁，只是在修改前验证下数据在此期间有无更新，如版本号控制

### 3.5 轮询锁和定时锁
   1. 由tryLock实现，与无条件获取锁模式相比，它们具有更完善的错误恢复机制。可避免死锁的发生：  
      boolean tryLock()：仅在调用时锁为空闲状态才获取该锁。如果锁可用，则获取锁，并立即返回值 true。
      如果锁不可用，则此方法将立即返回值 false。
      boolean tryLock(long time, TimeUnit unit) throws InterruptedException：
      如果锁在给定的等待时间内空闲，并且当前线程未被中断，则获取锁。
      
### 3.6 显示锁和内置锁
   1. 显示锁用Lock来定义、内置锁用synchronized。
   
### 3.7 对象锁和类锁
   1. 对象锁是用于实例对象(可有多个实例对象)方法上的
   2. 类锁是作用于对象的静态方法和Class(一个类只有一个Class对象)对象上的

### 3.8 互斥锁
   1. 互斥锁, 指的是一次最多只能有一个线程持有的锁。如Java的Lock
   
### 3.9 锁粗化
   1. 将多个连续的锁操作合并成一个整体的锁

### 3.10 锁消除
   1. 通过逃逸分析，能证明堆上数据不会逃逸出当前线程，则认为是线程安全的，不必要加锁操作
  
## 4. java线程池
### 4.1 线程池实现类
   ```
      (C)ThreadPoolExecutor --->  (AC)AbstractExecutorService ---> (I)ExecutorService ---> (I)Executor
   ```

### 4.2 ThreadPoolExecutor构造参数说明
   ```
        # corePoolSize 核心线程数，当任务多于核心线程数时会进入缓冲阻塞队列workQueue
        # maximunPoolSize 线程池最大线程数
        # keepAliveTime 多于核心线程数的空闲线程最长存活时间量级与unit参数配合使用
        # unit 线程等待时间的单位级
        # workQueue 任务缓冲队列
        # threadFactory 线程工厂，用于创建线程
        # handler 表示拒接处理任务的策略有一下4种：
        #  - ThreadPoolExecutor.AbortPolicy:丢弃任务并抛出RejectedExecutionException异常
        #  - ThreadPoolExecutor.DiscardPolicy：也是丢弃任务，但是不抛出异常
        #  - ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程）
        #  - ThreadPoolExecutor.CallerRunsPolicy：由调用线程处理该任务
        
        public ThreadPoolExecutor(int corePoolSize,int maximumPoolSize,long keepAliveTime,TimeUnit unit,
                BlockingQueue<Runnable> workQueue);
     
        public ThreadPoolExecutor(int corePoolSize,int maximumPoolSize,long keepAliveTime,TimeUnit unit,
                BlockingQueue<Runnable> workQueue,ThreadFactory threadFactory);
     
        public ThreadPoolExecutor(int corePoolSize,int maximumPoolSize,long keepAliveTime,TimeUnit unit,
                BlockingQueue<Runnable> workQueue,RejectedExecutionHandler handler);
     
        public ThreadPoolExecutor(int corePoolSize,int maximumPoolSize,long keepAliveTime,TimeUnit unit,
            BlockingQueue<Runnable> workQueue,ThreadFactory threadFactory,RejectedExecutionHandler handler);
   ```

### 4.3 实现原理
   1. 线程池状态：  
   - RUNNING: 线程池初始化时就是RUNNING状态，表示线程池能够接受任务并处理，并且线程池中线程数默认为0(可以通过调用方法
   `prestartAllCoreThreads() #创建核心线程`或者`prestartCoreThread() #创建一个核心线程`来初始化线程数)
   - SHUTDOWN: 线程处于SHUTDOWN状态时,不接收新任务,但能处理已添加的任务;状态切换调用`shutdown()`时从`RUNNING-> SHUTDOWN`
   - STOP: 线程处于STOP状态时，不接收新任务，不处理已添加任务，并会终止正在执行的任务;状态切换调用`shutdownNow()`时从
     `RUNNING or SHUTDOWN -> STOP`
   - TIDYING: 当所有任务已终止，任务数量为0时，线程池会进入TIDYING状态，并且会执行钩子函数`terminated()`，用户可重载该方法
     实现自己的业务逻辑;状态切换是所有任务终止就进入TIDYING状态
   - TERMINATED: 线程池彻底终止状态;状态切换是TIDYING的钩子函数执行完毕后进入TERMINATED状态
   
   2. 任务执行过程
   - 当任务提交给线程池时，线程首先判断当前池内线程数是否大于corePoolSize(核心线程数)，如果小于这值就会创建一个新的线程来执行该任务；
   - 当线程数大于核心线程数时，则会尝试将任务放入缓冲队列(workQueue)内，若添加成功，则该任务会被等待的空闲线程取去执行，若添加失败，
     则会尝试创建新的线程去执行该任务；
   - 如果线程池内线程数达到了maximumPoolSize(最大线程数)时，则会采取handler(拒绝策略)处理
   - 如果线程池内的线程数大于corePoolSize时，当线程空闲超时keepAliveTime时，线程将被终止，直到线程数等于corePoolSize；如果允许核心
     线程数也有超时时间，则当核心线程数内的线程超时时也会被终止，直至线程数为0
   
   3. 线程池中的线程初始化
   - prestartCoreThread()：初始化一个核心线程
   - prestartAllCoreThreads()：初始化所有核心线程
   - 初始化后线程会执行workQueue的`take()`方法，该方法是阻塞的，直到有任务提交
   
   4. 任务缓存队列及排队策略
   - ArrayBlockingQueue：基于数组的FIFO阻塞队列,必须有最大容量的参数
   - LinkedBlockingQueue: 基于链表的FIFO阻塞队列,容量动态扩展
   - SynchronousQueue: 该队列不保存提交的任务，而是直接新建队列来执行任务
   
   5. 任务拒绝策略
   ```
    ThreadPoolExecutor.AbortPolicy:丢弃任务并抛出RejectedExecutionException异常
    ThreadPoolExecutor.DiscardPolicy：也是丢弃任务，但是不抛出异常
    ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程）
    ThreadPoolExecutor.CallerRunsPolicy：由调用线程处理该任务
   ```
   
   6. 线程池的关闭
   - shutdown()：不会立即终止线程池，而是要等所有任务缓存队列中的任务都执行完后才终止，但再也不会接受新的任务 
   - shutdownNow(): 立即终止线程池，并尝试打断正在执行的任务，并且清空任务缓存队列，返回尚未执行的任务
   
   7. 线程池容量动态调整
   - setCorePoolSize()：设置核心池大小
   - setMaximumPoolSize()：设置线程池最大能创建的线程数目大小

### 4.4 Executors
   - newFixedThreadPool: 定容量的线程池，核心线程数与最大线程数相等
   - newSingleThreadExecutor: 单线程线程池，线程池内核心线程数与最大线程数为1
   - newCachedThreadPool: 无线大小线程池，核心线程数为0，最大线程数为`Integer.MAX_VALUE`, 缓冲队列为`SynchronousQueue`
   - newScheduledThreadPool：创建一个ScheduledThreadPoolExecutor定时执行线程池,最大线程数为`Integer.MAX_VALUE`,内部是
     一个DelayedWorkQueue实现
   - newSingleThreadScheduledExecutor: 创建一个ScheduledThreadPoolExecutor定时执行线程池,最大线程数为`Integer.MAX_VALUE`,
     内部是一个DelayedWorkQueue实现  

## 5. AbstractQueuedSynchronizer
   内部类：
   - ConditionObject：
   - Node：存放线程信息队列
        
### 5.1 AQS之ReentrantLock独占锁源码分析
   - [AbstractQueuedSynchronizer独占锁](http://www.infoq.com/cn/articles/jdk1.8-abstractqueuedsynchronizer)
   - `ReentrantLock.lock()`保证在`ReentrantLock.unlock()`之间的代码只有一个线程在执行；ReentrantLock为可重入锁，它有一个与
     锁相关的获取计数器，如果拥有锁的某个线程再次得到锁，那么获取计数器就加1，然后锁需要被释放两次才能获得真正释放。
   - 内部类`Syn`实现了`AbstractQueuedSynchronizer`接口
   - 构造方法有公平锁和非公平锁，公平锁与非公平锁的区别在于公平锁在尝试获取锁时会放入等待队列的后面，获取锁的顺序是按先后顺序执行的，
     而非公平锁在尝试获取锁时首先会去尝试获取锁，若获取失败在进入等待队列按顺序执行。
   
### 5.2 AQS之CountDownLatch共享锁源码分析
   - [AbstractQueuedSynchronizer共享锁](http://www.infoq.com/cn/articles/java8-abstractqueuedsynchronizer)
   - `CountDownLatch.countDown()`实现锁计数-1，直到减至0是，唤醒`CountDownLatch.await()`等待线程

### 5.3 公平锁和非公平锁
   公平锁是严格按照FIFO队列获得锁，但带来了大量的线程切换的消耗，非公平锁极大的降低了线程切换带来的消耗，虽然可能造成线程饥饿的情况，
   但也提高了吞吐量。

## 6. `synchronized`与`Lock`比较
   1. `synchronized`是JVM层面实现的'重量级锁'，可通过监控工具监控`synchronized`的锁定，而且代码出现异常时会自动释放锁
   2. `Lock`是纯JAVA实现的，为多种实现留下空间，可以实现不同的调度算法、性能特性或者锁定语义，`Lock`必须自己手动的释放锁
      形如`finally{lock.unlock();}`
   3. 当锁竞争激烈时用`Lock`,锁竞争较弱时用`synchronized`

## 7. 阻塞队列`BlockingQueue`
   1. 阻塞队列是一个FIFO队列
   2. 主要方法   

   |   Ops   |    Throws Exception    |      Special Value     |           Blocks Times Out          |
   |:-------:|:----------------------:|:----------------------:|:-----------------------------------:|
   |Insert   |    add(o)              |     offer(o)           |put(o) & offer(o, timeout, timeUnit) |  
   |Remove   |   remove(o)            |     poll()             |take()	& poll(timeout, timeUnit)    |
   |Examine  |   element()            |     peek()             |                -                    |

   3. 主要实现  
   - ArrayBlockingQueue：基于数组的阻塞队列，必须指定长度
   - LinkedBlockingQueue: 基于链表的阻塞队列，长度可指定也可动态扩张，默认长度为`Integer.MAX_VALUE`
   - SynchronousQueue: 无缓冲区的阻塞队列，`put()`要阻塞等待`take()`
   - PriorityBlockingQueue: 优先级阻塞队列，队列元素必须实现`Comparator`接口，基于数组，自动扩展长度
   
## 8. ConcurrentLinkedQueue
   非阻塞线程安全的FIFO队列，基于单向链表实现，循环CAS操作实现，由于是根据Node.NEXT是否为NULL来判断是否为TAIL节点，因此
   队列的元素值不可为NULL。
   

> [返回目录](https://github.com/Crab2died/jdepth)
> [返回目录](https://github.com/Crab2died/jdepth)

#                                      JAVA性能调优监控
---
## jps(JVM Process Status Tool)
### 1. 介绍
   类似UNIX的ps命令，列出虚拟机正在运行的进程信息
### 2. 参数
   - -q: 只输出进程ID
   - -m: 输出主类启动时的参数
   - -l: 输出主类全名，如果是jar则输出jar路径
   - -v: 输出虚拟机启动时的JVM参数

## jstat(JVM Statistics Monitoring Tool)
### 1. 介绍
   用于监视虚拟机各种运行状态信息的命令行工具。 它可以显示本地或者远程虚拟机进程中的类装载、 内存、 垃圾收集、 JIT编译等运行数据 
### 2. 格式
   jstat[option vmid[interval[s|ms][count]]]
   如`jstat -class 14232(PID) 520(采样率) 4(采样次数)`
### 3. 参数
   - -class: 监控类加载数量、卸载数量、总空间以及加载所用时间
   - -gc: 监控java堆状态，包括Eden区，2个survivor区，老年代，永久代的容量、已用空间、GC时间合计等
   - -gccapacity: 与-gc基本相同，但主要关注java堆各个区域使用到的最大、最小空间
   - -gcutil: 与-gc基本相同，但主要关注java堆各个区域使用占比
   - -gccase: 与-gcutil一样，但会额外输出上一次GC的原因
   - -gcnew: 监控新生代GC状态
   - -gcnewcapacity: 与-gcnew基本相同，但主要关注的是使用到的最大、最小空间
   - -gcold: 监控老年代GC状态
   - -gcoldcapacity: 与-gcold基本相同，但主要关注的是使用道的最大、最小空间
   - -gcpermcapacity: 输出永久带使用到的最大、最小空间
   - -compiler: 输出JIT编译过的方法、耗时等信息
   - -printcompilation: 输出已被JIT编译过的方法
   
## jinfo(Configuration Info for Java)
### 1. 介绍
   实时地查看和调整虚拟机各项参数
### 2. 格式
   jinfo [option] pid 如 `jinfo -flags 14232`
### 3. 参数
   - -flag <name>: to print the value of the named VM flag(输出JVM参数名为name的参数信息)
   - -flag [+|-]<name>: to enable or disable the named VM flag(添加或除去JVM参数名为name的参数)
   - -flag <name>=<value>: to set the named VM flag to the given value(设置JVM参数，入-Xmx=1024m)
   - -flags: to print VM flags(输出JVM启动参数信息)
   - -sysprops: to print Java system properties(输出`System.getProperties()`信息)

## jmap(Memory Map for Java)
### 1. 介绍
   命令用于生成堆转储快照(一般称为heapdump或dump文件)  
   其他生成dump文件方式:通过参数`-XX：+HeapDumpOnOutOfMemoryError`OOM时生成、`-XX：+HeapDumpOnCtrlBreak`通过
   [Ctrl]+[Break]键让虚拟机生成dump文件、或者Linux通过命令kill -3 pid也能拿到dump
### 2. 格式
   jmap [option] vmid 
### 3. 参数
   - -dump: 生成java堆转存快照, 格式 -dump:[live, ],format=b,file=<filename>, live表示是否只导出存活的对象
   - -finalizerinfo: 显示在F-Queen里面等待Finalizer线程执行finalize()方法的对象，只在Linux/Solaris平台有效
   - -heap: 显示java堆详细信息，如使用哪种GC回收器，参数配置、分代状况等信息，只在Linux/Solaris平台有效
   - -histo: 显示堆中对象统计信息，包括类、实例数量及合计容量等
   - -permstat: 以ClassLoader为统计口径显示永久带内存状态，只在Linux/Solaris平台有效
   - -F: 当-dump没响应时，使用-F强制生成dump文件，只在Linux/Solaris平台有效
 
## jhat(JVM Heap Analysis Tool)
### 1. 介绍
   分析jmap生成的堆转储快照(dump文件)
### 2. 格式
   jhat [-port 7001] <dumpfile> 
### 3. 参数
   - -port: server端口，可通过host:port访问
   - <file>: dump文件
   - -J<flag>: 运行参数,如``-J-mx512m`

## jstack(Stack Trace for Java)
### 1. 介绍
   用于生成虚拟机当前时刻的线程快照(一般称为threaddump或者javacore文件)  
   线程快照就是当前虚拟机内每一条线程正在执行的方法堆栈的集合，生成线程快照的主要目的是定位线程出现长时间停顿的原因，
   如线程间死锁、 死循环、 请求外部资源导致的长时间等待等都是导致线程长时间停顿的常见原因。 线程出现停顿的时候通过
   jstack来查看各个线程的调用堆栈，就可以知道没有响应的线程到底在后台做些什么事情，或者等待着什么资源
### 2. 格式
   jstack [option] vmid 如`jstack -l 14232`
### 3. 参数
   - -F: 当正常请求无响应时，强制输出线程堆栈
   - -l: 除堆栈外，显示关于锁的附加信息
   - -m: 如果调用到本地方法时，显示C/C++堆栈信息
### 4. 日志分析
   - 线程分为New、Runnable、Running、Waiting、Timed_Waiting、Blocked、Dead等状态  
    * New: 当线程对象创建时存在的状态，此时线程不可能执行；  
    * Runnable：当调用thread.start()后，线程变成为Runnable状态。只要得到CPU，就可以执行；  
    * Running：线程正在执行；  
    * Waiting：执行thread.join()或在锁对象调用obj.wait()等情况就会进该状态，表明线程正处于等待某个资源或条件发生来唤醒自己；  
    * Timed_Waiting：执行Thread.sleep(long)、thread.join(long)或obj.wait(long)等就会进该状态，与Waiting的区别在于
      Timed_Waiting的等待有时间限制；  
    * Blocked：如果进入同步方法或同步代码块，没有获取到锁，则会进入该状态；  
    * Dead：线程执行完毕，或者抛出了未捕获的异常之后，会进入dead状态，表示该线程结束  
   - 其次，对于jstack日志，我们要着重关注如下关键信息  
    * Deadlock：表示有死锁  
    * Waiting on condition：等待某个资源或条件发生来唤醒自己。具体需要结合jstacktrace来分析，比如线程正在sleep，网络读写繁忙而等待  
    * Blocked：阻塞  
    * Waiting on monitor entry：在等待获取锁  
    * in Object.wait()：获取锁后又执行obj.wait()放弃锁  
    * 对于Waiting on monitor entry 和 inObject.wait()的详细描述：Monitor是 Java中用以实现线程之间的互斥与协作的主要手段，
      它可以看成是对象或者Class的锁。每一个对象都有，也仅有一个 monitor。从下图中可以看出，每个 Monitor在某个时刻，只能被一个
      线程拥有，该线程就是 "Active Thread"，而其它线程都是 "Waiting Thread"，分别在两个队列 "Entry Set"和 "Wait Set"里面
      等候。在 "Entry Set"中等待的线程状态是"Waiting for monitor entry"，而在 "Wait Set"中等待的线程状态是 "in Object.wait()"
### 5. 附录
   在JDK1.5中在`java.lang.Thread`类中新增了`getAllStackTraces()`方法获取虚拟机所有的线程`StackTraceElement`
   对象，实现了大部分jstack功能，实际项目中可页面展示

## VisualVM(All-in-One Java Troubleshooting Tool)
### 介绍
  是到目前为止随JDK发布的功能最强大的运行监视和故障处理程序，并且可以预见在未来一段时间内都是官方主力发展的虚拟机故障处理工具。
  官方在VisualVM的软件说明中写上了“All-in-One”的描述字样，预示着它除了运行监视、 故障处理外，还提供了很多其他方面的功能。
### 远程监控
  - 1、远程服务器启动jstatd服务
    创建配置文件jstatd.policy内容为  
    ```
        grant codebase "file:${java.home}/../lib/tools.jar" {  
           permission java.security.AllPermission;  
        };
    ```  
    启动服务:`jstatd -J-Djava.security.policy=jstatd.all.policy -p 8701`
  - 2、启动应用参数
    ``` 
        -Dcom.sun.management.jmxremote=true 
        -Dcom.sun.management.jmxremote.port=9090 
        -Dcom.sun.management.jmxremote.ssl=false 
        -Dcom.sun.management.jmxremote.authenticate=false
        -Djava.rmi.server.hostname=192.168.0.1
    ```
  - 3、启动jvisualvm,操作步骤:  
    远程 -> 添加远程主机 -> 添加JMS链接

## 案列
### 1. 利用jstack调试线程堆栈信息
   1. jps得到PID，如14232
   2. 查看进程PID的线程耗时情况,命令`ps -Lfp pid`或`top -Hp pid`找到最耗时的线程ID 如14253
   3. 获取线程ID16进制编码`print "%x\n" 14253`为37ad
   4. 用jstack获取线程堆栈信息`jstack 14232 |grep 37ad`打印如下信息:  
      `"VM Periodic Task Thread" os_prio=0 tid=0x00007f772c00f800 nid=0x37ad waiting on condition `
      通过该日志分析表示该线程在等待某个资源来唤醒
  
> [返回目录](https://github.com/Crab2died/jdepth)

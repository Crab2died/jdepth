> [返回目录](https://github.com/Crab2died/jdepth)

#                                                   JAVA内存模型(JMM)
----
## 1. 内存模型
 ![内存模型](https://raw.githubusercontent.com/Crab2died/jdepth/master/src/main/java/com/github/jvm/concurrent/java%E5%86%85%E5%AD%98%E6%A8%A1%E5%9E%8B.png)

## 2. 内存间的交互操作
### 2.1. 定义操作
   * lock(锁定)：作用于主内存的变量,它把一个变量标识为一条线程独占的状态.
   * unlock(解锁)：作用于主内存的变量,它把一个处于锁定状态的变量释放出来,释放后的变量才可以被其他线程锁定.
   * read(读取)：作用于主内存的变量,它把一个变量的值从主内存传输到线程的工作内存中,以便随后的load动作使用.
   * load(载入)：作用于工作内存的变量,它把read操作从主内存中得到的变量值放入工作内存的变量副本中.
   * use(使用)：作用于工作内存的变量,它把工作内存中一个变量的值传递给执行引擎,每当虚拟机遇到一个需要使用到
     变量的值的字节码指令时将会执行这个操作.
   * assign(赋值)：作用于工作内存的变量,它把一个从执行引擎接收到的值赋给工作内存的变量,每当虚拟机遇到一个
     给变量赋值的字节码指令时执行这个操作.
   * store(存储)：作用于工作内存的变量,它把工作内存中一个变量的值传送到主内存中,以便随后的write操作使用.
   * write(写入)：作用于主内存的变量,它把store操作从工作内存中得到的变量的值放入主内存的变量中.
   
### 2.2. 约束条件
   1. read和load、store和write必须保证顺序操作,不能单独出现,但不需要连续操作,中间可隔有其他操作
   2. 不允许一个线程丢弃它的最近的assign操作,即变量在工作内存中改变了之后必须把该变化同步回主内存.
   3. 不允许一个线程无原因地(没有发生过任何assign操作)把数据从线程的工作内存同步回主内存中.
   4. 一个新的变量只能在主内存中“诞生”,不允许在工作内存中直接使用一个未被初始化(load或assign)的变量,换句话说,
      就是对一个变量实施use、 store操作之前,必须先执行过了assign和load操作.
   5. 一个变量在同一个时刻只允许一条线程对其进行lock操作,但lock操作可以被同一条线程重复执行多次,多次执行lock后,
      只有执行相同次数的unlock操作,变量才会被解锁
   6. 如果对一个变量执行lock操作,那将会清空工作内存中此变量的值,在执行引擎使用这个变量前,
      需要重新执行load或assign操作初始化变量的值.
   7. 如果一个变量事先没有被lock操作锁定,那就不允许对它执行unlock操作,也不允许去unlock一个被其他线程锁定住的变量.
   8. 对一个变量执行unlock操作之前,必须先把此变量同步回主内存中(执行store、 write操作)
   
### 2.3. volatile
   &emsp;&emsp;假定T表示一个线程,V和W分别表示两个volatile型变量,那么在进行read、 load、 use、 assign、store和write操作时
   需要满足如下规则:  
   &emsp;&emsp;只有当线程T对变量V执行的前一个动作是load的时候,线程T才能对变量V执行use动作;并且,只有当线程T对变量V执行的后一个动作是use
   的时候,线程T才能对变量V执行load动作. 线程T对变量V的use动作可以认为是和线程T对变量V的load、 read动作相关联,必须连续一起出现
   (这条规则要求在工作内存中,每次使用V前都必须先从主内存刷新最新的值,用于保证能看见其他线程对变量V所做的修改后的值).  
   &emsp;&emsp;只有当线程T对变量V执行的前一个动作是assign的时候,线程T才能对变量V执行store动作;并且,只有当线程T对变量V执行的后一个动作
   是store的时候,线程T才能对变量V执行assign动作. 线程T对变量V的assign动作可以认为是和线程T对变量V的store、 write动作相关
   联,必须连续一起出现(这条规则要求在工作内存中,每次修改V后都必须立刻同步回主内存中,用于保证其他线程可以看到自己对变量V所做的修改)  
   &emsp;&emsp;假定动作A是线程T对变量V实施的use或assign动作,假定动作F是和动作A相关联的load或store动作,假定动作P是和动作F相应的对变量V
   的read或write动作;类似的,假定动作B是线程T对变量W实施的use或assign动作,假定动作G是和动作B相关联的load或store动作,假定动作
   Q是和动作G相应的对变量W的read或write动作. 如果A先于B,那么P先于Q(这条规则要求volatile修饰的变量不会被指令重排序优化,保证代
   码的执行顺序与程序的顺序相同).  
   &emsp;&emsp;volatile不仅保证了共享变量的可见性，还通过内存屏障保证了代码执行顺序与程序顺序相同，通过内存屏障来使变量不被指令重排优化
   
   
### 2.4. long和double的非原子性协定
  &emsp;&emsp;读写操作可分为2次32位操作,所以一定不是原子操作  
  **_注：现在商用虚拟机本身几乎都已经实现了原子操作,所以不用volatile修饰符_**
  
### 2.5 原子性
  变量操作的read、load、use、assign、store、write不保证了原子性
  通过synchronized对lock、unlock操作也保证了原子性
  
### 2.6 可见性
  volatile保证在读取共享变量之前去主内存刷最新值，还保证了最新值能及时同步至主内存
  
### 2.7 有序性
  在本线程内观察所有操作都是有序的，在另一个线程观察所有操作都是无序的
  
### 2.8 先发性
  先发生的线程对修改了共享变量的值、发送了消息或调用了方法会被后发生的线程所观察到  

### 2.9 先行发生(happens-before)规则
  《JSR-133:Java Memory Model and Thread Specification》定义了如下happens-before规则。 
  - 1.程序顺序规则：一个线程中的每个操作，happens-before于该线程中的任意后续操作。
  - 2.监视器锁规则：对一个锁的解锁，happens-before于随后对这个锁的加锁。
  - 3.volatile变量规则：对一个volatile域的写，happens-before于任意后续对这个volatile域的读。
  - 4.传递性：如果A happens-before B，且B happens-before C，那么A happens-before C。
  - 5.start()规则：如果线程A执行操作ThreadB.start()（启动线程B），那么A线程的ThreadB.start()
    操作happens-before于线程B中的任意操作。
  - 6.join()规则：如果线程A执行操作ThreadB.join()并成功返回，那么线程B中的任意操作happens-before
    于线程A从ThreadB.join()操作成功返回。
    
> [返回目录](https://github.com/Crab2died/jdepth)
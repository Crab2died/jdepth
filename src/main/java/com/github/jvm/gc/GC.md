> [返回目录](https://github.com/Crab2died/jdepth)

#                                               自动内存管理  
--- 
## 一. Java虚拟机内存区域
### 1. 运行时数据区
   ![java运行时数据区](https://raw.githubusercontent.com/Crab2died/jdepth/master/src/main/java/com/github/jvm/java%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84.png)

### 2. 程序计数器(Program Counter Register)
   - 1、程序计数器是线程内(每个线程都有唯一的、封闭的)一小块内存区域
   - 2、计数器指定的是当前虚拟机执行指令的地址
   - 3、当虚拟机执行的是Native方法时,计数器值为空(Undefined),此内存区域是唯一一个在Java虚拟机规范中没有规定任何OutOfMemoryError
        情况的区域.

### 3. Java虚拟机栈(Java Virtual Machine Stacks)
   - 1、虚拟机栈是线程内部的、封闭的
   - 2、虚拟机栈描述的是java方法执行的内存模型
   - 3、每个方法在执行的同时都会创建一个栈帧(Stack Frame)用于存储局部变量表、 操作数栈、 动态链接、 方法出口等信息
   - 4、java方法的执行就是入栈与出栈的过程
   - 5、如果虚拟机栈深度超出了虚拟机允许深度将会抛出StackOverflowError异常,现代虚拟机大多数支持动态扩展(也允许固定长度),当虚拟机申
        请扩展时申请不到足够的内存时,将会抛出OutOfMemoryError异常
   
### 4. 本地方法栈(Native Method Stack)
   - 1、为虚拟机调用本地Native方法提供服务
   - 2、也有虚拟机(譬如Sun HotSpot虚拟机)直接就把本地方法栈和虚拟机栈合二为一
   - 3、也会抛出StackOverflowError异常和OutOfMemoryError异常

### 5. Java堆(Java Heap) GC堆
   - 1、线程共享的最大一块内存区域
   - 2、此内存区域的唯一目的就是存放对象实例,几乎所有的对象实例都在这里分配内存,虚拟机规范所有的对象实例与数据都在堆上分配
   - 3、随着JIT编译器的发展与逃逸分析技术逐渐成熟,栈上分配、 标量替换优化技术将会导致一些微妙的变化发生,所有的对象都分配在堆上也渐渐
        变得不是那么“绝对”了

### 6. 方法区(Method Area)
   - 1、存储已被虚拟机加载的类信息、 常量、 静态变量、 即时编译器编译后的代码等数据
   - 2、这区域的内存回收目标主要是针对常量池的回收和对类型的卸载

### 7. 运行时常量池(Runtime Constant Pool)
   - 1、Class文件中除了有类的版本、 字段、 方法、 接口等描述信息外,还有一项信息是常量池(Constant Pool Table),用于存放编译期生成
        的各种字面量和符号引用,这部分内容将在类加载后进入方法区的运行时常量池中存放
   - 2、String.intern()也会放入运行时常量池中

### 8. 直接内存(Direct Memory)
   - 1、NIO的DirectByteBuffer对象使用直接内存,这样能在一些场景中显著提高性能,因为避免了在Java堆和Native堆中来回复制数据
   
## 二. Java对象
### 1. 对象的创建
   - 1、指针碰撞(Bump the Pointer)
   - 2、空闲列表(Free List)

### 2. 对象再内存中的布局
   - 1、分为3块区域:对象头(Header)、实例数据(Instance Data)和对齐填充(Padding)
   - 2、对象头包含:轻量级锁定、 重量级锁定、 GC标记、 可偏向
   
### 3. 对象的访问定位
   - 1、句柄访问  
      ![句柄访问对象](https://raw.githubusercontent.com/Crab2died/jdepth/master/src/main/java/com/github/jvm/gc/%E5%8F%A5%E6%9F%84%E8%AE%BF%E9%97%AE.png)
   - 2、直接指针访问   
      ![直接指针访问](https://raw.githubusercontent.com/Crab2died/jdepth/master/src/main/java/com/github/jvm/gc/%E7%9B%B4%E6%8E%A5%E6%8C%87%E9%92%88%E8%AE%BF%E9%97%AE.png)  
   
## 三. 垃圾回收与内存分配
### 1. 引用计数法(虚拟机未使用)
   - 1、互相引用将无法得到正常回收

### 2. 可达性分析算法(Reachability Analysis)
   - 1、通过GC Root节点向下搜索,搜索走过的路径称为引用链(Reference Chain),当一个对象没有一个引用链经过,则表示该对象是不可用的,可以回收
   - 2、可作为GC Root对象的有:   
        * 虚拟机栈(栈帧中的本地变量表)中引用的对象 
        * 方法区中类静态属性引用的对象  
        * 方法区中常量引用的对象  
        * 本地方法栈中JNI(即一般说的Native方法)引用的对象  
        
### 3. 引用
   - 1、强引用(Strong Reference): new 关键字
   - 2、软引用(Soft Reference):当要发生内存溢出时会将软引用对象加入回收队列中
   - 3、弱引用(Weak Reference):只能活到下次GC前
   - 4、虚引用(Phantom Reference):幽灵引用或者幻影引用
   
### 4. 回收方法区
   - 1、主要回收永久代的废弃的常量和无用的类   
   - 2、无用的类判定条件:  
        * 该类所有的实例都已经被回收,也就是Java堆中不存在该类的任何实例.
        * 加载该类的ClassLoader已经被回收.
        * 该类对应的java.lang.Class对象没有在任何地方被引用,无法在任何地方通过反射访问该类的方法.

## 四. 垃圾回收算法
### 1. 标记-清除算法(Mark-Sweep)
   - 1、标记与清除2个过程
   - 2、标记与清除效率都不高,还可能产生大量空间碎片导致大对象找不到连续可用的空间
   
### 2. 复制算法(Copying)
   - 1、将堆内存分为大小相等的2块,每次只是用其中一块,当一块内存用完时将还活着的对象移动到另一块,然后清理该块内存
   - 2、消除了内存碎片化,代价是牺牲了一半可用堆内存
   - 3、商用虚拟机都采用这种,但并不是按1:1来划分空间而是将内存分为一块较大的Eden空间和两块较小的Survivor空间,每次使用Eden和其中
        一块Survivor.当回收时,将Eden和Survivor中还存活着的对象一次性地复制到另外一块Survivor空间上,最后清理掉Eden和刚才用过的
        Survivor空间. HotSpot虚拟机默认Eden和Survivor的大小比例是8:1,也就是每次新生代中可用内存空间为整个新生代容量的
        90%(80%+10%),只有10%的内存会被“浪费”. 当然,98%的对象可回收只是一般场景下的数据,我们没有办法保证每次回收都只有不多于
        10%的对象存活,当Survivor空间不够用时,需要依赖其他内存(这里指老年代)进行分配担保(Handle Promotion).
   - 4、当存活率高时将会出现大量的内存复制操作还有可能导致进行分配担保
   
### 3. 标记-整理算法(Mark-Compact)
   - 1、老年代内存,标记可回收对象之后,将存活的对象移向一端,然后清理掉端边界以外的内存
   
### 4. 分代收集算法(Generational Collection)
   - 1、将堆内存分为老年代和新生代
   - 2、老年代对象存活率高,再采用标记-清理或标记-整理算法进行GC
   - 3、新生代存活率低,采用复制算法将少量的存活对象进行复制操作   

## 五. HotSpot算法实现
### 1. 枚举根节点
   - 1、虚拟机内OopMap存有对象引用信息，可以得到GC Root根节点

### 2. 安全点
   - 1、虚拟机会在如方法调用、 循环跳转、 异常跳转等，所以具有这些功能的指令才会产生Safepoint
   - 2、中断方式  
     * 抢先式中断(基本弃用): 给出中断指令，有线程发现未到达安全点则继续执行至下一个安全点
     * 主动式中断: 给定一个中断标志，每个线程都会去轮询该标志，为真时中断
   
### 3. 安全区域
   - 1、安全区域是指在一段代码片段之中，引用关系不会发生变化。 在这个区域中的任意地方开始GC都是安全的
   - 2、在线程执行到安全区域时首先会标记自己进入安全区域，出安全区域前必须等到枚举根节点或整个GC完成，没有则都等到可以出安全区域信号为止
   
## 六. 垃圾回收器  
   ![HotSpot虚拟机垃圾回收器](https://raw.githubusercontent.com/Crab2died/jdepth/master/src/main/java/com/github/jvm/gc/HotSpot%E5%9E%83%E5%9C%BE%E6%94%B6%E9%9B%86%E5%99%A8.png)
   
### 1. Serial收集器
   - 1、JDK1.3.1之前是虚拟机新生代收集的唯一选择
   - 2、单线程、Stop The World(STW)、复制算法
   - 3、Client模式下新生代默认的垃圾收集器
   
### 2. PerNew收集器
   - 1、Serial收集器的多线程版,其他一样
   - 2、是许多虚拟机Server模式下新生代的首选收集器
   - 3、ParNew收集器也是使用-XX:+UseConcMarkSweepGC选项后的默认新生代收集器,也可以使用-XX:+UseParNewGC选项来强制指定它
   - 4、单核下效果不一定比Serial效果好,多核更适合,-XX:ParallelGCThreads参数来限制垃圾收集的线程数
   
### 3. Parallel Scavenge收集器
   - 1、新生代收集器,采用复制算法,并行的多线程收集器,吞吐量优先   
   - 2、追求可控的吞吐量, 吞吐量=运行用户代码时间/(运行用户代码时间+垃圾收集时间)
   - 3、控制最大垃圾收集停顿时间的-XX:MaxGCPauseMillis参数以及直接设置吞吐量大小的-XX:GCTimeRatio参数.
   
### 4. Serial Old收集器
   - 1、Serial老年代版本、单线程、标记-整理算法
   - 2、给Client模式下虚拟机用

### 5. Parallel Old收集器
   - 1、Parallel Scavenge收集器的老年代版本,使用多线程和“标记-整理”算法,JDK1.6开始提供
  
### 6. CMS收集器
   - 1、CMS(Concurrent Mark Sweep)收集器是一种以获取最短回收停顿时间为目标的收集器. 
   - 2、标记-清除算法实现  
        * 初始标记(CMS initial mark)，有短时的STW
        * 并发标记(CMS concurrent mark)
        * 重新标记(CMS remark)，有短时的STW
        * 并发清除(CMS concurrent sweep)
   - 3、并发收集、 低停顿,Sun公司的一些官方文档中也称之为并发低停顿收集器
   - 4、-XX：+UseCMSCompactAtFullCollection默认开启，表示CMS进行Full GC的时候开启内存碎片的合并整理，该过程无法并发停顿时间变长
   - 5、-XX：CMSFullGCsBeforeCompaction表示执行多少次不压缩的Full GC后跟着来一次压缩的Full GC，默认是0，每次都压缩

### 7. G1收集器
   - 1、G1(Garbage-First)收集器是当今收集器技术发展的最前沿成果之一,面向服务端应用 
   - 2、特点: 
        * 并发与并行:充分利用cpu与多核等硬件优势
        * 分代收集:
        * 空间整理:标记-整理算法
        * 可预测的停顿:将堆内存分为多个区域(Region),还保留有老年代与新生代
   - 3、不计算维护Remembered Set的操作,G1收集器的运作大致可划分为以下几个步骤:  
        * 初始标记(Initial Marking)
        * 并发标记(Concurrent Marking)
        * 最终标记(Final Marking)
        * 筛选回收(Live Data Counting and Evacuation) 
        
### 8. 垃圾收集器参数  
  |        参数            |                      描述                                      |
  |:-----------------------|:--------------------------------------------------------------|
  |UseSerialGC             |Client模式下默认,使用Serial+Serial Old组合                       |
  |UseParNewGC             |ParNew+Serial Old组合                                          |
  |UseConMarkSweepGC       |ParNew+CMS+Serial Old组合,Serial Old作为CMS失败后备用            |
  |UseParallelGC           |Server模式默认,Parallel Scavenge+Serial Old(PS Mark Sweep)组合  |
  |UseParallelOldGC        |Parallel Scavenge+Parallel Old组合                             |
  |UseG1GC                 |使用G1                                                         |
  
## 七. 内存分配与回收策略
### 1. 对象优先在Eden分配
   - 对象优先在Eden新生代分配,内存不足将发生一次Minor GC

### 2. 大对象直接进入老年代
   - -XX:PretenureSizeThreshold参数,令大于这个设置值的对象直接在老年代分配,避免大量内存复制

### 3. 长期存活的对象将进入老年代
   - 对象晋升老年代的年龄阈值,可以通过参数-XX:MaxTenuringThreshold设置,默认15,每次Minor GC对象没死+1

### 4. 动态对象年龄判定
   - 如果在Survivor空间中相同年龄所有对象大小的总和大于Survivor空间的一半,年龄大于或等于该年龄的对象就可以直接进入老年代,无须等
     到MaxTenuringThreshold中要求的年龄

### 5. 空间分配担保
   - Minor GC前先判断老年代可用空间是否大于新生代对象总空间,如果大于则确保安全,如果小于则查看HandlePromotionFailure设置的值是否
     允许担保失败,若允许则会继续检查老年代最大可用的连续空间是否大于历次晋升到老年代对象的平均大小,如果大于,将尝试着进行一次Minor 
     GC,尽管这次Minor GC是有风险的;如果小于,或者HandlePromotionFailure设置不允许冒险,那这时也要改为进行一次Full GC.

### 八. Minor GC、Major GC与Full GC
   - Minor GC表示新生代GC、Major GC是指老年代GC、Full GC为全部堆内存GC。
   - 往往他们之间相互影响，相互触发

  
> [返回目录](https://github.com/Crab2died/jdepth)

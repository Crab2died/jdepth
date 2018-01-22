> [返回目录](https://github.com/Crab2died/jdepth)

#                                            NIO
---
## 1. 概述

## 2. Buffer
### 2.1 属性
``` 
    private int mark = -1;     // 
    private int position = 0;  // 写模式下写入指针，每次写入都想后移位，最大为capacity-1，切换至读模式时会置0，变为读指针
    private int limit;         // 写模式下最多能写入的数据位置指针，读模式下表示最大读取位置指针
    private int capacity;      // 缓冲区容量
```
### 2.2 方法
   - `flip()` flip方法将Buffer从写模式切换到读模式。调用flip()方法会将position设回0，并将limit设置成之前position的值
   - `clear()` position将被设回0,limit被设置成 capacity的值 & `compact()` 所有未读的数据拷贝到Buffer起始处。然后将
     position设到最后一个未读元素正后面。limit属性依然像clear()方法一样，设置成capacity
   - `rewind()` 将position置为0

## 3. Channel
### 3.1 Channel几大实现
   - FileChannel 从文件中读写数据。
   - DatagramChannel 能通过UDP读写网络中的数据。
   - SocketChannel 能通过TCP读写网络中的数据。
   - ServerSocketChannel可以监听新进来的TCP连接，像Web服务器那样。对每一个新进来的连接都会创建一个SocketChannel。

## 4. Selector

## 5. SelectorKey

## 6. Pipe

## 7. NIO与IO比较
  - IO面向流, NIO面向缓冲
  - IO阻塞, NIO非阻塞
  - NIO基于Selector可以单线程管理多通道

> [返回目录](https://github.com/Crab2died/jdepth)
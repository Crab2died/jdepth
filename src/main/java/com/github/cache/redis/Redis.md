> [返回目录](https://github.com/Crab2died/jdepth)

#                                               Redis
---
## 1. 概述  
   - 基于内存的Key-Value存储系统，单线程实现  
   - 多样的数据类型，支持的数据类型有：string(字符串)、hash(哈希)、list(链表)、set(集合)、zset(sorted set有序集合)  
   - 持久化，redis会周期性的将更新的数据写入磁盘  
   - master-slave(主从)同步  
   - 3.0后支持分布式存储，去中心化，具有线性伸缩功能
   - [Redis命令大全](http://redisdoc.com/index.html)
   
## 2. Redis数据持久化
### 2.1 RDB(Redis DataBase)
   - SNAPSHOT(快照): `save 秒数 写操作次数`如`save 9000 1`表示900s(15min)有一次写操作生成快照，也可`save ""`表示
     每次写操作即生成快照
   - 配置`stop-writes-on-bgsave-error yes/no`当后台生成快照错误是否中断redis写操作的支持
   - 配置`rdbcompression yes/no`表示是否压缩RDB文件
   - 每次回fork一份来重启另一个进程进行持久化
   
### 2.2 AOF(Append Only File)
   - 配置`appendonly no/yes`启用AOF，启动时会触发全量写文件，后面写操作是增量写文件
   - 同步策略配置`appendfsync always(每次写操作都触发同步)/everysec(每秒同步一次)/no(不同步)`
   - 重写(rewrite): 满足条件后触发重写，会对AOF文件内容优化，减少文件大小  
     auto-aof-rewrite-percentage 100 (表示超过文件的百分比)  
     auto-aof-rewrite-min-size 64mb (触发重写的最小文件大小)
   - AOF文件修复: `redis-check-aof --fix appendonly.aof`

### 2.3 比较
   - RDB安全性较差、容易丢失最近一次缓存内容，但文件较小，恢复速度较快，是Master/Slave主从复制模式下的最好补充
   - AOF更加安全、数据的完整性较强、但文件较大、恢复速度较慢，IO开支较大，比较影响性能
   - Redis启用默认是脚在AOF文件恢复数据

> [返回目录](https://github.com/Crab2died/jdepth)            
> [返回目录](https://github.com/Crab2died/jdepth)

#                                               SQL优化
----
1. 建立合适的索引,并合理使用
   * 应在`where`和`order by`列上建立索引
   * 索引列避免null值
   * 避免查询条件为!=、<>、like、in(exists代替)、not in
   * 避免or连接条件 采用union all  如:`SELECT * FROM TABLE WHERE NUM = 10 OR NUM = 20` 可优化为:
     `SELECT * FROM TABLE WHERE NUM = 10 UNION ALL SELECT * FROM TABLE WHERE NUM = 20`
   * 应尽量避免在 where 子句中对字段进行表达式操作  如:`SELECT * FROM TABLE WHERE NUM/2 = 10` 可优化为:
     `SELECT * FROM TABLE WHERE NUM = 20`
   * 在使用索引字段作为条件时,如果该索引是复合索引,那么必须使用到该索引中的第一个字段作为条件时才能保证系统使用该索引,
     否则该索引将不会被使用,并且应尽可能的让字段顺序与索引顺序相一致
2. 能用数值类型的列尽量使用
3. 不要`SELECT *` 来获取返回结果,返回所需列
4. 在新建临时表时,如果一次性插入数据量很大,那么可以使用 `select into` 形如：
   `SELECT column_name(s) INTO new_table_name [IN externaldatabase] FROM old_tablename` 代替 `create table`,
   避免造成大量 log ,以提高速度;如果数据量不大,为了缓和系统表的资源,应先`create table`,然后`insert`
5. 如果使用到了临时表,在存储过程的最后务必将所有的临时表显式删除,先 `truncate table table_name`,然后 `drop table table_name`,
   这样可以避免系统表的较长时间锁定.
6. 尽量避免游标,游标效率较差
7. 在所有的存储过程和触发器的开始处设置 `SET NOCOUNT ON` ,在结束时设置 `SET NOCOUNT OFF`.
   无需在执行存储过程和触发器的每个语句后向客户端发送 DONE_IN_PROC 消息
8. 联合索引，联合索引只在`where + and`这类条件下生效，而且索引只能在从左开始组合才生效，如`ALERT TABLE_NAME ADD INDEX INDEX_NAME 
   (col1, col2, col3)`只有`WHERE col1=? AND col2=? AND col3=?` 或 `WHERE col1=? AND col2=?` 或 `WHERE col1=?` 索引生效，
   另外,复杂度高的列有先左排列
   
> [返回目录](https://github.com/Crab2died/jdepth)
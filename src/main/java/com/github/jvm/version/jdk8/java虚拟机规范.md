#                                                       JAVA虚拟机规范
---
##  一. java虚拟机结构
### 1. java虚拟机数据类型
   - 1.1. 原始类型(primitive type, 原生类型或基本类型) 、引用类型(reference type),与之对应的为原始值(primitive value)
     和引用值(reference value)
   - 1.2. 原始类型与值
     - 1.2.1 数字类型(numeric type)
       - 1)整数类型
       - 2)浮点数类型(单精度浮点数与双精度浮点数) 
     - 1.2.2 boolean类型  
       虚拟机对他的支持很少，甚至没有提供boolean值专门的字节码指令，编译之后都是用int数据类型来代替(1 <=> true  0 <=> false)
     - 1.2.3 returnAddress类型  
       returnAddress类型会被虚拟机的jsr、ret与jsr_w指令使用，其值指向一条虚拟机指令操作码
   - 1.3. 引用类型与值    
     
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
     - 1.3.1 类类型(class type)，指向动态创建的类实例
     - 1.3.2 数组类型(array type)，指向数组实例,数组类型最外面那一维元素类型成为该数组类型的组件类型(component type)，组件类型
       也可以是数组，抽丝剥茧最终一定会得到不是数组的情况，这就是数组元素类型(element type),数组的元素类型必须是原生类型、类类型
       或者接口类型之一
     - 1.3.3 接口类型(interface type), 指向实现了某个接口的实例或数组实例
   
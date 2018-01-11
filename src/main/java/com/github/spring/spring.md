> [返回目录](https://github.com/Crab2died/jdepth)

#                                             Spring
---
## 概述
  1. Spring是一个轻量级的、一站式的JAVA开发框架
  2. 模块架构  
    ![spring模块架构](https://raw.githubusercontent.com/Crab2died/jdepth/master/src/main/java/com/github/spring/spring%E6%A1%86%E6%9E%B6.jpg)

## IoC(控制反转, Inversion of Controller)
  1. IoC是将对象及对象之间的关系交由IoC容器管理，达到了代码关系解耦及提高了代码的可测试性
  2. 控制反转是典型的工厂模式的最佳实践
  3. 高内聚，低耦合
  4. 接口继承关系  
    ![接口继承关系](https://raw.githubusercontent.com/Crab2died/jdepth/master/src/main/java/com/github/spring/IoC.png)
  5. 初始化过程
   ```
   Resource定位  ->  载入  ->  注册  
   ```
  6. spring注册对象管理信息方式：  
   - 直接编码方式：
   - 配置文件方式：
   - 元数据方式：
  7. BeanFactory注册对象与依赖绑定方式： 
   - 直接编码方式
   - 外部配置文件方式：1) Properties配置  2) XML配置
   - 注解方式
  
   
> [返回目录](https://github.com/Crab2died/jdepth)
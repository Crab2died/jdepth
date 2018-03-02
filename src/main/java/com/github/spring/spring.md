> [返回目录](https://github.com/Crab2died/jdepth)

#                                             Spring
---
## 概述
  1. Spring是一个轻量级的、一站式的JAVA开发框架
  2. 模块架构  
    ![spring模块架构](https://raw.githubusercontent.com/Crab2died/jdepth/master/src/main/java/com/github/spring/spring%E6%A1%86%E6%9E%B6.jpg)

## IoC(控制反转, Inversion of Controller)
### 描述
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
  
## AOP(面向方面编程)
### 类型
   1. 静态AOP:
   2. 动态AOP:
   
### AOP实现机制
   1. 动态代理：
   2. 动态字节码增强：
   3. java代码生成：
   4. 自定义类加载器：
   5. AOL扩展：
   
### Joinpoint
   1. 方法调用
   2. 方法调用执行
   3. 构造方法调用
   4. 构造方法执行
   5. 字段设置: 如setter
   6. 字段获取: 如getter
   7. 异常处理执行
   8. 类初始化: 如static{}代码块
   
### Pointcut
   1. 代表的是Joinpoint的表述方式,将横切逻辑织入当前系统过程中需要参照Pointcut规定的Joinpoint信息,
      才能知道往系统哪些Joinpoint上织入横切逻辑
   2. 表述方式
    - 直接指定Joinpoint所在方法名称
    - 正则表达式：Spring AOP， JBoss AOP都支持
    - 使用特定的Poincut表述语言   

### Advice
   1. Before Advice  
     是在指定的Joinpoint位置之前执行，不会中断执行(可通过抛出异常中断执行)
   2. After Advice
     - After Returning Advice: 当Joinpoint流程执行完成后执行Advice
     - After Throwing Advice(Throws Advice): 当Joinpoint抛出异常后执行
     - After Advice(Finally Advice): 无论Joinpoint是否正常执行还是抛出异常最终都会执行，如finally{}
   3. Around Advice: 可以在Joinpoint执行之前和之后都能执行

### Aspect

### 织入和织入器
   AspectJ的织入器是其专门的编译器ajc，JBoss AOP是通过其类加载器，Spring AOP是通过ProxyFactory

### Spring AOP
   1. Spring AOP是使用动态代理和字节码生成技术、用java做为AOP的实现语言(AOL)

### Spring AOP中Pointcut

### Spring AOP中Advice
   1. BeforeAdvice
   2. ThrowsAdvice
   3. AfterReturningAdvice
   4. AroundAdvice
   
### Spring AOP中Aspect
   1. DefaultPointcutAdvisor
   2. NameMatchMethodPointcutAdvisor
   3. RegexpMatchMethodPointcutAdvisor
   4. DefaultBeanFactoryPointcutAdvisor
      
## SpringMVC
### 1. spring事物的传播性(Propagation)
   1. REQUIRED：如果存在一个事物，则支持当前事物。如果没有则新建
   2. MANDATORY：支持当前事物，如果没有当前事物则抛出异常
   3. NEVER：以非事物方式执行，如果当前存在事物则抛出异常
   4. NOT_SUPPORTED：以非事物方式执行，如果当前存在事物则将当前事物挂起
   5. REQUIRES_NEW：新建事物，如果当前存在事物，则将当前事物挂起
   6. SUPPORTS：支持当前事物，如果当前没有事物，则以非事物方式执行
   7. NESTED：支持当前事物，新增savepoint，与当前事物提交或回滚
   
### 2. [spring web相关介绍](https://github.com/Crab2died/jdepth/blob/master/src/main/java/com/github/web/java-web.md)
        
> [返回目录](https://github.com/Crab2died/jdepth)
> [返回目录](https://github.com/Crab2died/jdepth)

#                                               java web
---
## 一. 介绍

## 二. 重点解析
### 1. Servlet
   1. Servlet是sun公司提供的一门用于开发动态web资源的技术,由WEB服务器负责调用
   2. 生命周期: `初始化阶段init() -> 响应请求阶段service()[调用doGet()或doPost()] -> 终止阶段destroy()`
   3. 响应过程
   - 客户端发送请求，创建一个封装了HTTP请求信息的HttpServletRequest和一个封装了HTTP响应信息的HttpServletResponse，
    Servlet调用service()方法进行响应
   - service()会调用doGet()或doPost()
   
### 2. Filter 
   1. 它是Servlet技术中最实用的技术，Web开发人员通过Filter技术，对web服务器管理的所有web资源：例如Jsp, Servlet, 静态图片文件
    或静态html 文件等进行拦截，从而实现一些特殊的功能。例如实现URL级别的权限访问控制、过滤敏感词汇、压缩响应信息等一些高级功能。
   2. Filter接口主要有init($FilterConfig)、doFilter($ServletRequest, $ServletResponse, $FilterChain)和destroy()方法
    其中FilterConfig参数主要是一些Filter名称、parameter、servletContext等信息，FilterChain提供doFilter()方法调用等。
   3. 多个Filter执行顺序是由web.xml内Filter的配置顺序决定

### 3. Intercepter
   1. 在web开发中，拦截器是经常用到的功能。它可以帮我们验证是否登陆、预先设置数据以及统计方法的执行效率等等
   2. Spring中有HandlerInterceptor和MethodInterceptor两类拦截器 

### 4. Listener
   1. 

### web.xml加载
   加载顺序是: `context-param -> Listener -> Filter -> Servlet`

> [返回目录](https://github.com/Crab2died/jdepth)

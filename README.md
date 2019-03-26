# netty-npc

**项目说明**

在**微服务**大行其道的2019，分布式系统越来越重要，实现服务化首先就要考虑服务之间的通信问题。这里面涉及序列化、反序列化、交互、连接等等问题。。所以，rpc框架的学习毋庸置疑是一个关键点。

**netty-npc**是近期本人在学习netty课程之后练手的一个rpc框架，底层采用netty进行通讯，Zookeeper作为服务发现及注册中心，api + Junit4 测试各个功能点，基于SpringBoot快速启动。
 
- 使用Netty进行底层通讯
- 支持kryo Marshalling进行序列化
- 动态代理支持JDK代理 CGLIB代理
- 服务注册发现中心使用ZooKeeper集群实现
- 基于SpringBoot快速启动

**优化列表**
- [x] 采用线程池优化channelRead阻塞操作
- [x] 加入心跳包检测

**代办列表**
- [ ] spi机制
- [ ] 多服务进程负载均衡
- [ ] 日志输出
...

**使用说明**

1.服务器或虚拟机搭建奇数个Zookeeper集群，保持互相ping通的状态
  
2.springboot快速启动，开启server服务端、client客户端

3.controller提供了api，直接使用postman进行访问

![image](https://github.com/xuyikai1/netty-rpc/blob/master/images/api.png)

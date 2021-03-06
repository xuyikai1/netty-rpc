package com.nettyserver.server.impl;

import com.nettyserver.handler.ServerHandler;
import com.nettyserver.server.Server;
import com.nettyserver.service.impl.StudentServiceImpl;
import com.nettyserver.util.NetUtil;
import common.Constant;
import entity.Request;
import entity.Response;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import serializer.Marshalling.MarshallingFactory;
import serializer.kryo.KryoDecoder;
import serializer.kryo.KryoEncoder;
import service.StudentService;
import zookeeper.Curator;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Xuyk
 * @Description:
 * @Date: Created in 10:08 2019/3/14
 */
public class NettyServer implements Server {

    /** 创建两个工作线程组：一个用于网络连接接收请求，一个用于实际处理业务的线程组 */
    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workGroup = new NioEventLoopGroup();

    private Map<String, Object> serviceMap = new HashMap<>();

    @Override
    public void start() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        try{
            serverBootstrap.group(bossGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,1024)
                    //表示缓存区动态调配(自适应)
                    .option(ChannelOption.RCVBUF_ALLOCATOR,AdaptiveRecvByteBufAllocator.DEFAULT)
                    //缓冲区池化操作
                    .option(ChannelOption.ALLOCATOR,PooledByteBufAllocator.DEFAULT)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
//                            sc.pipeline().addLast(MarshallingFactory.buildMarshallingDecoder());
//                            sc.pipeline().addLast(MarshallingFactory.buildMarshallingEncoder());
                            sc.pipeline().addLast(new KryoDecoder(1024));
                            sc.pipeline().addLast(new KryoEncoder());
                            //心跳包检测
                            sc.pipeline().addLast(new IdleStateHandler(Constant.READ_IDEL_TIME_OUT, Constant.WRITE_IDEL_TIME_OUT, Constant.ALL_IDEL_TIME_OUT, TimeUnit.MINUTES));
                            sc.pipeline().addLast(new ServerHandler(serviceMap));
                        }
                    });


            //绑定端口，同步等待请求连接
            ChannelFuture cf = serverBootstrap.bind(Constant.PORT).sync();
            System.out.println("Server Startup...");

            //连接zk
            Curator curator = new Curator(StudentService.class.getSimpleName(),NetUtil.getLocalIp(),Constant.PORT,Constant.ZK_IPS);
            //注入服务到zk
            curator.registerService("getStudent");
            serviceMap.put("StudentService",new StudentServiceImpl());
            //客户端观察services结点获取最新服务变动 --Client
            //curator.WatcheNode(curator.getServicePath());

            //异步关闭
            cf.channel().closeFuture().sync();

        } catch (Exception e) {
            System.out.println("Server Start Failed...");
            e.printStackTrace();
        }
    }

    @Override
    public void shutdown() {
        //优雅地关闭线程组
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
        System.out.println("Server ShutDown...");
    }

    public Request packageRequest(Class<?> clazz,String methodName){
        //获取类方法对应的参数列表
//        clazz.getTypeParameters()
        //获取类方法对应的参数类型列表
        Request request = new Request();

        return request;
    }

}

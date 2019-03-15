package com.nettyserver.server;

import com.nettyserver.common.Constant;
import com.nettyserver.factory.ZookeeperFactory;
import com.nettyserver.handler.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import serialization.MarshallingFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Xuyk
 * @Description:
 * @Date: Created in 10:08 2019/3/14
 */
public class NettyServer {

    public  NettyServer(){

        //1.创建两个工作线程组：一个用于网络连接接收请求，一个用于实际处理业务的线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

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
                        sc.pipeline().addLast(MarshallingFactory.buildMarshallingDecoder());
                        sc.pipeline().addLast(MarshallingFactory.buildMarshallingEncoder());
                        //心跳包检测
                        sc.pipeline().addLast(new IdleStateHandler(Constant.READ_IDEL_TIME_OUT, Constant.WRITE_IDEL_TIME_OUT, Constant.ALL_IDEL_TIME_OUT, TimeUnit.SECONDS));
                        sc.pipeline().addLast(new ServerHandler());
                    }
                });


        //绑定端口，同步等待请求连接
        ChannelFuture cf = serverBootstrap.bind(8765).sync();
        System.out.println("Server Startup...");

        //nettyServer注入进zookeeper中
        CuratorFramework client = ZookeeperFactory.create();
        InetAddress address = InetAddress.getLocalHost();
        client.create().withMode(CreateMode.EPHEMERAL).forPath(Constant.ZOOKEEPER_SERVER_PATH + address.getHostAddress());

        //异步关闭
        cf.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //优雅地关闭线程组
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
            System.out.println("Server ShutDown...");
        }
    }
}

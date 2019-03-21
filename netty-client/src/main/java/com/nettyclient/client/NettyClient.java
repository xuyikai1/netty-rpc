package com.nettyclient.client;



import com.nettyclient.handler.ClientHandler;
import com.nettyclient.service.PersonService;
import com.nettyclient.service.impl.PersonServiceImpl;
import common.Constant;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import serializer.Marshalling.MarshallingFactory;
import util.NetUtil;
import zookeeper.Curator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @Author: Xuyk
 * @Description: 构建客户端需要知道服务端的相关信息
 * @Date: Created in 11:12 2019/3/14
 */
public class NettyClient {

    private PersonServiceImpl personService = new PersonServiceImpl();

    /**优化部分：使用Map池(根据ip+端口号维护) <String,Channel> */
    private Channel channel;

    /** 链接句柄，复用链接 **/
    private Map<String,Channel> channelPool = new ConcurrentHashMap<>();

    /**1.创建一个工作线程组用于实际处理业务的线程组 */
    private EventLoopGroup workGroup = new NioEventLoopGroup();

    private ChannelFuture cf;

    public NettyClient(String host,int port){
        this.connect(host,port);
    }

    private void connect(String host, int port) {

        Bootstrap bootstrap = new Bootstrap();

        try{
            bootstrap.group(workGroup)
                    .channel(NioSocketChannel.class)
                    //表示缓存区动态调配(自适应)
                    .option(ChannelOption.RCVBUF_ALLOCATOR,AdaptiveRecvByteBufAllocator.DEFAULT)
                    //缓冲区池化操作
                    .option(ChannelOption.ALLOCATOR,PooledByteBufAllocator.DEFAULT)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            sc.pipeline().addLast(MarshallingFactory.buildMarshallingDecoder());
                            sc.pipeline().addLast(MarshallingFactory.buildMarshallingEncoder());
                            sc.pipeline().addLast(new ClientHandler());
                        }
                    });

            //绑定端口，同步等待请求连接  sync()同步阻塞
            this.cf = bootstrap.connect(host,port).sync();
            System.out.println("Client Connected...");

            //获取通道
            String key = host + ":" + port;
            Channel channel = channelPool.get(key);
            if(channel == null){
                Channel newChannel = this.cf.channel();
                this.channel = newChannel;
                channelPool.put(key,newChannel);
            }else{
                this.channel = channel;
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void sendData(){
        //获取到zk上的服务节点列表
        Curator curator = new Curator(PersonService.class.getSimpleName(),NetUtil.getLocalIp(),Constant.PORT,Constant.ZK_IPS);
        //监听zk服务节点变化 客户端操作
        curator.WatcheNode(curator.getServicePath());
        //数据发送
//        personService.sendId(1,this.channel);
    }

    public void close() throws InterruptedException {
        //异步关闭
        cf.channel().closeFuture().sync();
        workGroup.shutdownGracefully();
        System.out.println("Client ShutDown... ");
    }

}

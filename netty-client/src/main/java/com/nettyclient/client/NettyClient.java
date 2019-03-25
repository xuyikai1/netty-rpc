package com.nettyclient.client;



import codec.V2.RPCDecoder;
import codec.V2.RPCEncoder;
import com.nettyclient.handler.ClientHandler;
import common.Constant;
import entity.Request;
import entity.Response;
import entity.TranslatorData;
import entity.ZookeeperNode;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.junit.Test;
import serializer.Marshalling.MarshallingFactory;
import service.StudentService;
import util.NetUtil;
import zookeeper.Curator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @Author: Xuyk
 * @Description: 构建客户端需要知道服务端的相关信息
 * @Date: Created in 11:12 2019/3/14
 */
public class NettyClient extends SimpleChannelInboundHandler<Response>{

    /**优化部分：使用Map池(根据ip+端口号维护) <String,Channel> */
    public Channel channel;

    /** 链接句柄，复用链接 **/
    private Map<String,Channel> channelPool = new ConcurrentHashMap<>();

    /**1.创建一个工作线程组用于实际处理业务的线程组 */
    private EventLoopGroup workGroup = new NioEventLoopGroup();

    private ChannelFuture cf;

    private Response response;

    private final Object obj = new Object();

    public NettyClient(){}

    public NettyClient(String host, int port){
        connect(host,port);
    }

    /**
     * 连接、创建Channel通道
     * @param host
     * @param port
     */
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
//                            sc.pipeline().addLast(new RPCEncoder(Request.class));
//                            sc.pipeline().addLast(new RPCDecoder(Response.class));
                            sc.pipeline().addLast(NettyClient.this);
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

    public Response sendData(Request request){

        try{

            ChannelFuture future = this.channel.writeAndFlush(request);

            synchronized (obj) {
                obj.wait(); // 未收到响应，使线程等待
            }

            if (response != null) {
                future.channel().closeFuture().sync();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return response;

        //获取到zk上的服务节点列表
//        Curator curator = new Curator(StudentService.class.getSimpleName(),NetUtil.getLocalIp(),Constant.PORT,Constant.ZK_IPS);
        //监听zk服务节点变化 客户端操作
//        curator.WatcheNode(curator.getServicePath());
        //数据发送

    }

    public void close() throws InterruptedException {
        //异步关闭
        cf.channel().closeFuture().sync();
        workGroup.shutdownGracefully();
        System.out.println("Client ShutDown... ");
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Response response) throws Exception {
        this.response = response;

        synchronized (obj) {
            obj.notifyAll(); // 收到响应，唤醒线程
        }
    }


}

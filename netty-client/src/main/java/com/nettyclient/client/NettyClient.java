package com.nettyclient.client;



import com.nettyclient.cache.QueueCache;
import com.nettyclient.handler.ClientHandler;
import entity.Request;
import entity.Response;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import serializer.Marshalling.MarshallingFactory;
import serializer.kryo.KryoDecoder;
import serializer.kryo.KryoEncoder;

import java.util.Map;
import java.util.concurrent.*;


/**
 * @Author: Xuyk
 * @Description: 构建客户端需要知道服务端的相关信息
 * @Date: Created in 11:12 2019/3/14
 */
public class NettyClient{

    /**优化部分：使用Map池(根据ip+端口号维护) <String,Channel> */
    public Channel channel;

    /** channel通道链接句柄，复用链接 **/
    private Map<String,Channel> channelPool = new ConcurrentHashMap<>();

    /**1.创建一个工作线程组用于实际处理业务的线程组 */
    private EventLoopGroup workGroup = new NioEventLoopGroup();

    private ChannelFuture cf;

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
//                            sc.pipeline().addLast(MarshallingFactory.buildMarshallingDecoder());
//                            sc.pipeline().addLast(MarshallingFactory.buildMarshallingEncoder());
                            sc.pipeline().addLast(new KryoDecoder(1024));
                            sc.pipeline().addLast(new KryoEncoder());
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


    /**
     * 动态代理发送请求方法
     * @param request
     * @return
     * @throws InterruptedException
     */
    public Response send(Request request) throws InterruptedException {

        //判断channel为空的情况
        if (channel == null) {
            Response response = new Response();
            RuntimeException runtimeException = new RuntimeException("Channel is not available now");
            response.setThrowable(runtimeException);
            return response;
        }
        //把请求加入阻塞队列，获取到response返回，超过指定时间则返回null
        ClientHandler handler = new ClientHandler();
        handler.sendRequest(request,channel);
        BlockingQueue<Response> queue = QueueCache.get(request.getRequestId());
        //take()  队列为空则同步阻塞  两者都为移除并返回队列头部元素
        //poll()  队列为空返回null,超过设置超时时间也返回null
        //return queue.poll(requestTimeoutMillis, TimeUnit.MILLISECONDS);
        return queue.take();
    }

    public void close() throws InterruptedException {
        //异步关闭
        cf.channel().closeFuture().sync();
        workGroup.shutdownGracefully();
        System.out.println("Client ShutDown... ");
    }


}

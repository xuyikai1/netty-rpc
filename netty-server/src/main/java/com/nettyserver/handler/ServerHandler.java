package com.nettyserver.handler;

import com.nettyserver.common.Constant;
import com.nettyserver.common.ExecutorServicePool;
import com.nettyserver.service.impl.PersonServiceImpl;
import entity.TranslatorData;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.ExecutorService;

/**
 * @Author: Xuyk
 * @Description:
 * @Date: Created in 10:51 2019/3/14
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private static ExecutorService threadPool = ExecutorServicePool.createThreadPool(
            Constant.THREAD_POOL_NAME,
            Constant.THREAD_POOL_SIZE,
            Constant.ORDER_MAX_CORE_POOL_SIZE);

    private PersonServiceImpl personService = new PersonServiceImpl();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        threadPool.submit(() -> {
            solve(ctx,msg);
        });
    }

    /**
     * 事件已触发
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        //(心跳包检测)读空闲超过时间，则关闭channel
        if( evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) evt;
            if (IdleState.READER_IDLE.equals(event.state())) {
                System.out.println("读空闲..关闭channel..");
                ctx.channel().close();
            }
        }else{
            super.userEventTriggered(ctx, evt);
        }

    }

    public void solve(ChannelHandlerContext ctx, Object msg){
        TranslatorData request = (TranslatorData)msg;
        System.out.println("Server端: id =" + request.getId() + " name = " + request.getName()
                + " message = " +  request.getMessage() + " data = " + request.getData());

        //响应回客户端
        int id = (Integer)request.getData();
        TranslatorData response = personService.getStudent(id);

        //把对象传回给客户端，并且把数据冲刷到异步NIO通道上
        ctx.writeAndFlush(response);

        System.out.println("send...");
    }

}

package com.nettyserver.handler;

import com.nettyserver.util.ExecutorServicePoolUtil;
import com.nettyserver.service.impl.StudentServiceImpl;
import common.Constant;
import entity.Request;
import entity.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;

/**
 * @Author: Xuyk
 * @Description:
 * @Date: Created in 10:51 2019/3/14
 */
public class ServerHandler extends SimpleChannelInboundHandler<Request> {

    private static ExecutorService threadPool = ExecutorServicePoolUtil.createThreadPool(
            Constant.THREAD_POOL_NAME,
            Constant.THREAD_POOL_SIZE,
            Constant.ORDER_MAX_CORE_POOL_SIZE);

    private StudentServiceImpl personService = new StudentServiceImpl();

    private Object service;

    public ServerHandler(){}

    public ServerHandler(Object service){
        this.service = service;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Request request) throws Exception {
        threadPool.submit(() -> {
            solve(ctx,request);
        });
    }

    /**
     * 事件已触发后进行心跳包检测
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

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        ctx.channel().close();
    }

    public void solve(ChannelHandlerContext ctx, Request msg) {

        try{
            String methodName = msg.getMethod();
            Object[] params = msg.getParams();
            Class<?>[] parameterTypes = msg.getParameterTypes();
            long requestId = msg.getRequestId();
            //通过反射来获取客户端所要调用的方法并执行
            Method method = service.getClass().getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            Object invoke = method.invoke(service, params);
            Response response = new Response();
            response.setRequestId(requestId);
            response.setResponse(invoke);
            ctx.pipeline().writeAndFlush(response);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}

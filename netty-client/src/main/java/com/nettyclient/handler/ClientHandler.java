package com.nettyclient.handler;

import com.nettyclient.cache.QueueCache;
import entity.Request;
import entity.Response;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.*;

/**
 * @Author: Xuyk
 * @Description:
 * @Date: Created in 11:34 2019/3/14
 */
public class ClientHandler extends SimpleChannelInboundHandler<Response> {

    private ExecutorService threadPool = com.nettyclient.common.ExecutorServicePool.createThreadPool(
            com.nettyclient.common.Constant.THREAD_POOL_NAME,
            com.nettyclient.common.Constant.THREAD_POOL_SIZE,
            com.nettyclient.common.Constant.ORDER_MAX_CORE_POOL_SIZE);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext,Response response) throws Exception {
        // 判断是否需要线程池进行解耦,提升并发性能
        threadPool.submit(() -> {
            try {
                solve(response);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public void sendRequest(Request request, Channel channel){
        BlockingQueue<Response> queue = new ArrayBlockingQueue<>(1);
        QueueCache.put(request.getRequestId(),queue);
        //将request发送给客户端
        channel.writeAndFlush(request);
    }

    /**
     * 根据请求ID，获取到响应队列，然后把response放到返回队列中
     * @param response
     * @throws InterruptedException
     */
    private void solve(Response response) throws InterruptedException {
        BlockingQueue<Response> responseQueue = QueueCache.get(response.getRequestId());
        if (responseQueue != null) {
            responseQueue.put(response);
        } else {
            throw new RuntimeException("responseQueue is null");
        }
    }

}

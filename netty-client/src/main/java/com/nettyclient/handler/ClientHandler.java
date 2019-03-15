package com.nettyclient.handler;

import com.nettyclient.common.Constant;
import com.nettyclient.common.ExecutorServicePool;
import entity.TranslatorData;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.util.concurrent.ExecutorService;

/**
 * @Author: Xuyk
 * @Description:
 * @Date: Created in 11:34 2019/3/14
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    private static ExecutorService threadPool = ExecutorServicePool.createThreadPool(
            Constant.THREAD_POOL_NAME,
            Constant.THREAD_POOL_SIZE,
            Constant.ORDER_MAX_CORE_POOL_SIZE);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        threadPool.submit(() -> {
            solve(ctx,msg);
        });
    }

    public void solve(ChannelHandlerContext ctx, Object msg){
        System.out.println("handler...");

        try{
            TranslatorData response = (TranslatorData)msg;
            System.out.println("Client端: id =" + response.getId() + " name = " + response.getName()
                    + " message = " +  response.getMessage() + " data = " + response.getData());
        }finally {
            //用完缓存需要进行释放(Client端只读不写需要释放)
            ReferenceCountUtil.release(msg);
        }
    }


}

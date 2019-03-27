package com.nettyclient.proxy;


import com.nettyclient.client.NettyClient;
import entity.Request;
import entity.Response;
import io.netty.channel.Channel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author: Xuyk
 * @Description: JDK代理
 * @Date: Created in 13:46 2019/3/21
 */
public class JDKProxy implements InvocationHandler {

    private NettyClient client;

    /** 原子类保证id唯一*/
    private static AtomicLong atomicLong = new AtomicLong();

    private String serviceName;

    private Class<?> clazz;

    /** 绑定委托对象，并返回代理类 */
    public Object getProxy(Class clazz,NettyClient client,String serviceName){
        this.clazz = clazz;
        this.client = client;
        this.serviceName = serviceName;
        //绑定该类实现的所有接口，取得代理类
        return Proxy.newProxyInstance(clazz.getClassLoader(),
                clazz.getInterfaces(),
                this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        //封装request
        Request request = new Request();
        request.setRequestId(atomicLong.incrementAndGet());
        request.setMethod(method.getName());
        request.setParams(args);
        request.setClazz(clazz);
        request.setParameterTypes(method.getParameterTypes());
        request.setServiceName(serviceName);
        //CGLIBProxy
        Response response = client.send(request);
        if(response == null){
            return null;
        }
        return response.getResponse();
    }


}

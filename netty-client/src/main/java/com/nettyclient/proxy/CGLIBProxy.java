package com.nettyclient.proxy;

import com.nettyclient.client.NettyClient;
import entity.Request;
import entity.Response;
import io.netty.channel.Channel;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author: Xuyk
 * @Description:
 * @Date: Created in 10:25 2019/3/25
 */
public class CGLIBProxy implements MethodInterceptor {

    private NettyClient client;

    private Class<?> clazz;

    private String serviceName;

    /** 原子类保证id唯一*/
    private static AtomicLong atomicLong = new AtomicLong();

    public Object getProxy(Class clazz,NettyClient client,String serviceName) {

        this.clazz = clazz;
        this.client = client;
        this.serviceName = serviceName;

        // CGLIB enhancer增强类对象
        Enhancer enhancer = new Enhancer();
        // 设置增强类型
        enhancer.setSuperclass(clazz);
        // 定义代理逻辑对象为当前对象，要求当前对象实现MethodInterceptor方法
        enhancer.setCallback(this);
        // 生成并返回代理对象
        return enhancer.create();
    }

    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        //封装request
        Request request = new Request();
        request.setServiceName(serviceName);
        request.setRequestId(atomicLong.incrementAndGet());
        request.setMethod(method.getName());
        request.setParams(args);
        request.setClazz(clazz);
        request.setParameterTypes(method.getParameterTypes());
        //JDKProxy
        Response response = client.send(request);
        if(response == null){
            return null;
        }
        return response.getResponse();
    }

    public static void main(String[] args) {
        CGLIBProxy proxy = new CGLIBProxy();
//        Test obj = (Test)cpe.getProxy(Test.class);
//        obj.sayHello("张三");
    }

}

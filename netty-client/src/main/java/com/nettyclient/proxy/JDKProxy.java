package com.nettyclient.proxy;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author: Xuyk
 * @Description:
 * @Date: Created in 13:46 2019/3/21
 */
public class JDKProxy implements InvocationHandler {

    private String serviceName;

    private Object tar;

    /** 绑定委托对象，并返回代理类 */
    public Object bind(Object tar)
    {
        this.tar = tar;
        //绑定该类实现的所有接口，取得代理类
        return Proxy.newProxyInstance(tar.getClass().getClassLoader(),
                tar.getClass().getInterfaces(),
                this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        //这里就可以进行所谓的AOP编程
        result = method.invoke(tar,args);
        return result;
    }


}

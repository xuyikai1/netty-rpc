package com.nettyclient.client;

import entity.Response;

import java.lang.reflect.Method;

/**
 * @Author: Xuyk
 * @Description: 服务的发现与使用
 * @Date: Created in 9:24 2019/3/21
 */
public interface Client {

    Response sendMessage(Class<?> clazz, Method method, Object[] args);

    <T> T proxyInterface(Class<T> serviceInterface);

    void close();

}

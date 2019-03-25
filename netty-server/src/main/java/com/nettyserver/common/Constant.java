package com.nettyserver.common;

/**
 * @Author: Xuyk
 * @Description: 常量类
 * @Date: Created in 17:13 2019/3/14
 */
public class Constant {

    public static final String THREAD_POOL_NAME = "client-threadPool";
    /** 设置服务器的两倍cpu数量 **/
    public static final Integer ORDER_MAX_CORE_POOL_SIZE = 8;
    public static final Integer THREAD_POOL_SIZE = 8;

    /** 设置zookeeper的注册路径 **/
    public static final String ZOOKEEPER_SERVER_PATH = "/NettyServer/";

    /** 读超时 **/
    public static final Integer READ_IDEL_TIME_OUT = 1000;
    /** 写超时 **/
    public static final Integer WRITE_IDEL_TIME_OUT = 1000;
    /** 所有超时 **/
    public static final Integer ALL_IDEL_TIME_OUT = 1000;

}

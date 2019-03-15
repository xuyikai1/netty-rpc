package com.nettyserver.factory;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @Author: Xuyk
 * @Description:
 * @Date: Created in 14:23 2019/3/15
 */
public class ZookeeperFactory {

    public static CuratorFramework client;

    public static CuratorFramework create(){
        if(client == null){
            //重复三次，每次睡眠一秒钟
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
            client = CuratorFrameworkFactory.newClient("localhost:2181",retryPolicy);
            client.start();
        }
        return client;
    }

    public static void main(String[] args) throws Exception {
        CuratorFramework client = create();
        client.create().forPath("/nettyServer");
    }
}

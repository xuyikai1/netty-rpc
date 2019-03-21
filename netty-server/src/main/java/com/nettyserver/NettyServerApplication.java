package com.nettyserver;

import com.nettyserver.server.impl.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: Xuyk
 * @Description: springBoot启动类
 * @Date: Created in 15:13 2019/3/18
 */
@SpringBootApplication
public class NettyServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NettyServerApplication.class, args);
        new NettyServer().start();
    }

}

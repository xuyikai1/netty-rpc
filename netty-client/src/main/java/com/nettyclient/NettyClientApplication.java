package com.nettyclient;

import com.nettyclient.client.NettyClient;
import common.Constant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: Xuyk
 * @Description: SpringBoot启动类
 * @Date: Created in 11:34 2019/3/14
 */
@SpringBootApplication
public class NettyClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(NettyClientApplication.class, args);
    }

}

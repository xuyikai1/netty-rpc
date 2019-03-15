package com.nettyclient;

import com.nettyclient.client.NettyClient;
import com.nettyclient.service.impl.PersonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: Xuyk
 * @Description: SpringBoot启动类
 * @Date: Created in 11:34 2019/3/14
 */
@SpringBootApplication
public class NettyClientApplication {

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8765;

    public static void main(String[] args) {
        SpringApplication.run(NettyClientApplication.class, args);
        new NettyClient(HOST,PORT).sendData();
    }

}

package com.nettyclient.controller;

import com.nettyclient.proxy.JDKProxy;
import common.Constant;
import entity.Request;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zookeeper.Curator;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author: Xuyk
 * @Description:
 * @Date: Created in 14:17 2019/3/21
 */
@RestController
@RequestMapping("/service")
public class ServiceController {

    /**
     * 获取Zk上所提供的服务列表
     * @return
     */
    @GetMapping("/getServiceList")
    public List<String> registerService(){
        Curator curator = null;
        try{
            curator = new Curator();
            return curator.getChildNodes(Constant.ZOOKEEPER_SERVER_PATH);
        }finally {
            curator.close();
        }
    }


    /**
     * 模拟用户针对某服务进行调用(动态代理式本地化调用)
     * @param request
     */
    @PostMapping("/sendData")
    public void sendData(@Valid Request request){
        JDKProxy proxy = new JDKProxy();

    }
}

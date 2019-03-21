package com.nettyclient.controller;

import common.Constant;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zookeeper.Curator;

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
    @PostMapping("/getServiceList")
    public List<String> registerService(){
        Curator curator = null;
        try{
            curator = new Curator();
            return curator.getChildNodes(Constant.ZOOKEEPER_SERVER_PATH);
        }finally {
            curator.close();
        }
    }
}

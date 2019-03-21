package com.nettyserver.controller;

import com.nettyserver.entity.ResultVO;
import com.nettyserver.util.ResultVOUtil;
import common.Constant;
import entity.ServiceMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zookeeper.Curator;

import java.util.List;


/**
 * @Author: Xuyk
 * @Description:
 * @Date: Created in 11:01 2019/3/21
 */
@RestController
@RequestMapping("/service")
public class ServiceController {


    @PostMapping("/register")
    public ResultVO registerService(@RequestParam("serviceName") String serviceName,
                                    @RequestParam("serverIp") String serverIp,
                                    @RequestParam("port") int port,
                                    @RequestParam("Ips") String Ips){

        Curator curator = new Curator(serviceName,serverIp,port,Ips);
        curator.registerService();
        curator.close();
        return ResultVOUtil.success();
    }

}

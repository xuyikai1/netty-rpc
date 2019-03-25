package com.nettyclient.controller;

import com.nettyclient.client.NettyClient;
import com.nettyclient.proxy.JDKProxy;
import common.Constant;
import entity.Request;
import entity.Student;
import entity.ZookeeperNode;
import io.netty.channel.Channel;
import io.netty.channel.DefaultChannelPromise;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.StudentService;
import service.impl.StudentServiceImpl;
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
     */
    @PostMapping("/getStudent")
    public Student getStudent(){
        //注册客户端
        NettyClient client = new NettyClient("127.0.0.1",8765);
        //绑定当前节点变动情况
        Curator curator = new Curator(StudentService.class.getSimpleName(),"127.0.0.1",Constant.PORT,Constant.ZK_IPS);
        curator.WatcheNode(curator.getServicePath());
        JDKProxy proxy = new JDKProxy();
        StudentService service = (StudentService)proxy.getProxy(StudentServiceImpl.class,client);
        Student student = service.getStudent(1);
        System.out.println(student);
        return null;
    }


}

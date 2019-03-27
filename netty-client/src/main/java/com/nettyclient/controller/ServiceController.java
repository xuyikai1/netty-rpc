package com.nettyclient.controller;

import com.nettyclient.client.NettyClient;
import com.nettyclient.proxy.CGLIBProxy;
import com.nettyclient.proxy.JDKProxy;
import common.Constant;
import entity.Result;
import entity.Student;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.StudentService;
import service.impl.StudentServiceImpl;
import util.ResultUtil;
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

    private static NettyClient client = null;

    /**
     * 获取Zk上所提供的服务列表
     * @return
     */
    @GetMapping("/serviceList")
    public Result registerService(){
        Curator curator = null;
        try{
            curator = new Curator();
            List<String> list = curator.getChildNodes(Constant.SERVER_SERVICE_PATH);
            return ResultUtil.success(list);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.error(e);
        } finally {
            curator.close();
        }
    }


    /**
     * 模拟用户针对某服务进行调用(动态代理式本地化调用)
     */
    @GetMapping("/getStudentByJDKProxy")
    public Result getStudentByJDKProxy(){
        try{
            JDKProxy proxy = new JDKProxy();
            StudentService service = (StudentService)proxy.getProxy(StudentServiceImpl.class,client,"StudentService");
            Student student = service.getStudent(1);
            System.out.println(ResultUtil.success(student));
            return ResultUtil.success(student);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.error(e);
        }
    }

    /**
     * 模拟用户针对某服务进行调用(动态代理式本地化调用)
     */
    @GetMapping("/getStudentByCGLIBProxy")
    public Result getStudentByCGLIBProxy(){
        try{
            CGLIBProxy proxy = new CGLIBProxy();
            StudentService service = (StudentService)proxy.getProxy(StudentServiceImpl.class,client,"StudentService");
            Student student = service.getStudent(1);
            System.out.println(ResultUtil.success(student));
            return ResultUtil.success(student);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.error(e);
        }

    }


    /**
     * 连接服务端，保证channel is active
     */
    @PostMapping("/connect")
    public Result connect(){
        try{
            //注册客户端
            client = new NettyClient("192.168.254.1",8765);
            //绑定当前节点变动情况
            Curator curator = new Curator(StudentService.class.getSimpleName(),"192.168.254.1",Constant.PORT,Constant.ZK_IPS);
            curator.WatcheNode(curator.getServicePath());
            return ResultUtil.success();
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.error(e);
        }
    }


}

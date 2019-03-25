package com.nettyserver.controller;

import org.springframework.stereotype.Controller;
import service.StudentService;

import javax.annotation.Resource;

/**
 * @Author: Xuyk
 * @Description:
 * @Date: Created in 17:06 2019/3/15
 */
@Controller
public class PersonController {

    @Resource
    private StudentService service;

}

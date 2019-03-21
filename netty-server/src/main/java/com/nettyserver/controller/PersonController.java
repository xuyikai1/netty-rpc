package com.nettyserver.controller;

import com.nettyserver.service.PersonService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * @Author: Xuyk
 * @Description:
 * @Date: Created in 17:06 2019/3/15
 */
@Controller
public class PersonController {

    @Resource
    private PersonService service;

}

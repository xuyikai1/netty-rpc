package com.nettyclient.service;

import entity.Student;
import io.netty.channel.Channel;

/**
 * @Author: Xuyk
 * @Description:
 * @Date: Created in 14:52 2019/3/14
 */
public interface PersonService {

    Student getStudent(int id,Channel channel);

}

package com.nettyclient.service.impl;

import com.nettyclient.service.PersonService;
import entity.Student;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

/**
 * @Author: Xuyk
 * @Description:
 * @Date: Created in 14:52 2019/3/14
 */
@Service
public class PersonServiceImpl implements PersonService {


    @Override
    public Student getStudent(int id,Channel channel) {
        Student student = new Student();
        student.setName("Xuyk");
        student.setId("1");
        student.setAge(23);
        return student;
    }


}

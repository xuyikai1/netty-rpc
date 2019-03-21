package com.nettyserver.service.impl;

import com.nettyserver.service.PersonService;
import entity.Student;
import org.springframework.stereotype.Service;

/**
 * @Author: Xuyk
 * @Description:
 * @Date: Created in 14:32 2019/3/14
 */
@Service
public class PersonServiceImpl implements PersonService {

    @Override
    public Student getStudent(int id) {

        Student student = new Student();
        student.setId("" + id);
        student.setAge(20);
        student.setName("man");

        return student;
    }


}

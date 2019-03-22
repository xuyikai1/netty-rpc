package com.nettyserver.service.impl;

import com.nettyserver.service.StudentService;
import entity.Student;
import org.springframework.stereotype.Service;

/**
 * @Author: Xuyk
 * @Description:
 * @Date: Created in 14:32 2019/3/14
 */
@Service
public class StudentServiceImpl implements StudentService {

    @Override
    public Student getStudent(int id) {

        Student student = new Student();
        student.setId("" + id);
        student.setAge(20);
        student.setName("man");

        return student;
    }


}

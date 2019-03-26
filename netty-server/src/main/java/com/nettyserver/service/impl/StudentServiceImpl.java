package com.nettyserver.service.impl;

import entity.Student;
import org.springframework.stereotype.Service;
import service.StudentService;

/**
 * @Author: Xuyk
 * @Description:
 * @Date: Created in 14:32 2019/3/14
 */
@Service
public class StudentServiceImpl implements StudentService {

    @Override
    public Student getStudent(Integer id) {
        //业务处理
        Student student = new Student();
        student.setId(id);
        student.setAge(20);
        student.setName("man");
        return student;
    }

    @Override
    public void create(Student student) {
        System.out.println("创建成功");
    }
}

package service.impl;

import entity.Student;
import service.StudentService;

/**
 * @Author: Xuyk
 * @Description:
 * @Date: Created in 14:16 2019/3/25
 */
public class StudentServiceImpl implements StudentService {

    @Override
    public Student getStudent(Integer id) {
        Student student = new Student();
        student.setId(1);
        student.setName("xuyikai");
        student.setAge(24);
        return student;
    }

    @Override
    public void create(Student student) {

    }


}

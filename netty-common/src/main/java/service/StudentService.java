package service;

import entity.Student;

/**
 * @Author: Xuyk
 * @Description:
 * @Date: Created in 10:19 2019/3/25
 */
public interface StudentService {

    Student getStudent(Integer id);

    void create(Student student);
}

package com.nettyserver.service.impl;

import com.nettyserver.service.PersonService;
import entity.Student;
import entity.TranslatorData;
import org.springframework.stereotype.Service;

/**
 * @Author: Xuyk
 * @Description:
 * @Date: Created in 14:32 2019/3/14
 */
@Service
public class PersonServiceImpl implements PersonService {

    @Override
    public TranslatorData getStudent(int id) {
        TranslatorData translatorData = new TranslatorData();
        translatorData.setId("" + id);
        translatorData.setName("Person Service");

        if(id == 1){
            Student student = new Student();
            student.setId("" + id);
            student.setAge(20);
            student.setName("man");
            translatorData.setMessage("success");
            translatorData.setData(student);
        }else{
            translatorData.setMessage("false");
            translatorData.setData(null);
        }

        return translatorData;
    }


}

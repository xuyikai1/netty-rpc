package com.nettyclient.service.impl;

import com.nettyclient.service.PersonService;
import entity.TranslatorData;
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
    public void sendId(int id,Channel channel) {
        TranslatorData request = new TranslatorData();
        request.setId("" + id);
        request.setName("请求消息名称" + id);
        request.setMessage("请求消息内容" + id);
        request.setData(id);

        channel.writeAndFlush(request);
    }

}

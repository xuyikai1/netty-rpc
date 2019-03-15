package com.nettyclient.service;

import io.netty.channel.Channel;

/**
 * @Author: Xuyk
 * @Description:
 * @Date: Created in 14:52 2019/3/14
 */
public interface PersonService {

    void sendId(int id,Channel channel);
}

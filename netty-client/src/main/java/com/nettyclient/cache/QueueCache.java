package com.nettyclient.cache;

import entity.Response;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Xuyk
 * @Description: 存储requestId和阻塞队列Queue映射关系 作为缓存便于获取
 * @Date: Created in 11:36 2019/3/26
 */
public class QueueCache {

    private static Map<Long,BlockingQueue<Response>> queueMap = new ConcurrentHashMap<>();

    public static BlockingQueue<Response> get(long requestId){
        return queueMap.get(requestId);
    }

    public static void put(long RequestId,BlockingQueue<Response> queue){
        queueMap.put(RequestId,queue);
    }

}

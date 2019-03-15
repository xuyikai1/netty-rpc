package com.nettyserver.common;


import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * @Author: Xuyk
 * @Description:
 * @Date: Created in 16:53 2019/3/14
 */
public class ExecutorServicePool {

    private static ThreadFactory factory = null;
    private static ScheduledExecutorService scheduledExecutorService;
    private static ExecutorService executorService = null;
    private static final Long KEEP_ALIVE_TIME = 0L;
    private static final Integer CAPACITY = 10000;

    /**
     * 创建定长线程池，支持定时和周期执行
     */
    public static ScheduledExecutorService createScheduledThreadPool(
            String poolName, int corePoolSize) {
        factory = setFactory(poolName);
        scheduledExecutorService = new ScheduledThreadPoolExecutor(corePoolSize, factory);
        return scheduledExecutorService;
    }

    /**
     * 创建线程池
     */
    public static ExecutorService createThreadPool(
            String poolName, int corePoolSize, int maximumPoolSize) {
        factory = setFactory(poolName);
        executorService =
                new ThreadPoolExecutor(
                        corePoolSize,
                        maximumPoolSize,
                        KEEP_ALIVE_TIME,
                        TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<Runnable>(CAPACITY),
                        factory,
                        new ExecutorServicePool.MyAbortPolicy());
        return executorService;
    }

    private static ThreadFactory setFactory(String poolName) {
        if (factory == null) {
            factory = new ThreadFactoryBuilder().setNameFormat(poolName).build();
        }
        return factory;
    }

    /**
     * 自定义AbortPolicy类(即自定义饱和策略的异常处理)
     */
    private static class MyAbortPolicy implements RejectedExecutionHandler {

        public MyAbortPolicy() {
        }

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            throw new RejectedExecutionException(
                    "【线程默认饱和策略】:" + r.toString() + "线程被" + executor.toString() + "线程执行器拒绝");
        }
    }

    public static void main(String[] args) {
        ExecutorService threadPool = ExecutorServicePool.createThreadPool(
                Constant.THREAD_POOL_NAME,
                Constant.THREAD_POOL_SIZE,
                Constant.ORDER_MAX_CORE_POOL_SIZE);
        System.out.println(threadPool);
    }
}

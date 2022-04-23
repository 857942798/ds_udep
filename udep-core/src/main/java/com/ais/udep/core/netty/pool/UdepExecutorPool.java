package com.ais.udep.core.netty.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: dongsheng
 * @CreateTime: 2022/4/21
 * @Description:
 */
public class UdepExecutorPool {

    private static LinkedBlockingQueue<Runnable> queues = new LinkedBlockingQueue<>(1000);

    private static ThreadPoolExecutor excutor;

    public static ExecutorService getExcutor() {
        if (excutor == null) {
            synchronized (UdepExecutorPool.class) {
                if (excutor == null) {
                    excutor = new ThreadPoolExecutor(10,20,3L, TimeUnit.SECONDS,queues,new UdepRejectedPolicy());
                    excutor.prestartAllCoreThreads();
                }
            }
        }
        return excutor;
    }


}

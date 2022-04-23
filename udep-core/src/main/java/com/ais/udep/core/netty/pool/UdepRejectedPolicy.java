package com.ais.udep.core.netty.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 自定义线程拒绝策略
 * @author dongsheng
 */
@Slf4j
public class UdepRejectedPolicy implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        //列如可以发送短信或者邮件告警
        log.warn("线程池满了");
    }
}

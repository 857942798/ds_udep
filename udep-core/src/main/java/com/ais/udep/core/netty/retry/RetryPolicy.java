package com.ais.udep.core.netty.retry;

/**
 * @author: dongsheng
 * @CreateTime: 2022/4/12
 * @Description:
 */
public interface RetryPolicy {

    /**
     * 当操作由于某种原因失败时调用。此方法应返回 true 以进行另一次尝试。
     *
     * @param retryCount 到目前为止重试的次数（第一次为 0）
     * @return true/false
     */
    boolean allowRetry(int retryCount);

    /**
     * 以毫秒为单位获取当前重试计数的睡眠时间。
     *
     * @param retryCount 当前重试次数
     * @return the time to sleep
     */
    long getSleepTimeMs(int retryCount);
}

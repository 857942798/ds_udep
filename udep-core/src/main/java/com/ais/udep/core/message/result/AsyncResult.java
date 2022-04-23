package com.ais.udep.core.message.result;

import com.ais.udep.core.message.result.UdepResult;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author: dongsheng
 * @CreateTime: 2022/4/13
 * @Description:
 *
 *  异步响应结果
 */
@Slf4j
public class AsyncResult implements UdepResult {

    private final CompletableFuture<?> future;

    public AsyncResult(CompletableFuture<?> future) {
        this.future = future;
    }

    @Override
    public boolean isSuccess() {
        return !future.isCompletedExceptionally();
    }

    @Override
    public Object getData() {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("getData error.", e);
        }
        return null;
    }
}

package com.ais.udep.core.message;

import cn.hutool.json.JSONUtil;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: dongsheng
 * @CreateTime: 2022/4/18
 * @Description: 未完成的请求，等待对端返回数据
 */
public class UnFinishedRequests {
    private static final Map<Long, CompletableFuture<UdepResponse<?>>> FUTURE_MAP = new ConcurrentHashMap<>();

    public static void put(long requestId, CompletableFuture<UdepResponse<?>> future) {
        FUTURE_MAP.put(requestId, future);
    }

    /**
     * 完成响应
     * @param udepResponse 响应内容
     */
    public static void complete(long requestId,UdepResponse<?> udepResponse) {
        CompletableFuture<UdepResponse<?>> future = FUTURE_MAP.remove(requestId);
        if (future != null) {
            future.complete(udepResponse);
        } else {
            throw new IllegalStateException("future is null. udepResponse=" + JSONUtil.toJsonStr(udepResponse));
        }
    }
}

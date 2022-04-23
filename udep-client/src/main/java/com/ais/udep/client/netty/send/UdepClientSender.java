package com.ais.udep.client.netty.send;

import cn.hutool.core.util.IdUtil;
import com.ais.udep.client.netty.client.UdedClient;
import com.ais.udep.core.exception.UdepException;
import com.ais.udep.core.message.UdepRequest;
import com.ais.udep.core.message.UdepResponse;
import com.ais.udep.core.message.UnFinishedRequests;
import com.ais.udep.core.message.result.AsyncResult;
import com.ais.udep.core.netty.send.AbstractClientSender;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * @author: dongsheng
 * @CreateTime: 2022/4/19
 * @Description:
 */
@Slf4j
@Component
public class UdepClientSender extends AbstractClientSender {

    @Autowired
    UdedClient udedClient;

    @Override
    public AsyncResult doSendToServer(UdepRequest udepRequest) throws UdepException {
        Channel channel = udedClient.getChannel();
        if (channel.isActive()) {
            CompletableFuture<UdepResponse<?>> resultFuture = new CompletableFuture<>();
            // 构建消息，此处会构建 requestId
            udepRequest.setRequestId(IdUtil.getSnowflake(udedClient.getId(),1).nextId());
            UnFinishedRequests.put(udepRequest.getRequestId(), resultFuture);
            channel.writeAndFlush(udepRequest).addListener((ChannelFutureListener) future -> {
                if (!future.isSuccess()) {
                    // 如果消息发送不成功，则断开连接
                    future.channel().close();
                    resultFuture.completeExceptionally(future.cause());
                    log.error("send failed:", future.cause());
                }
            });
            return new AsyncResult(resultFuture);
        } else {
            throw new UdepException("channel is not active. address=" +channel.remoteAddress().toString());
        }
    }
}

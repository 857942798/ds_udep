package com.ais.udep.server.netty.send;

import cn.hutool.core.util.IdUtil;
import com.ais.udep.core.exception.UdepException;
import com.ais.udep.core.message.UdepRequest;
import com.ais.udep.core.message.UdepResponse;
import com.ais.udep.core.message.UnFinishedRequests;
import com.ais.udep.core.message.result.AsyncResult;
import com.ais.udep.core.netty.send.AbstractServerSender;
import com.ais.udep.server.netty.channel.ChannelManage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * @author: dongsheng
 * @CreateTime: 2022/4/19
 * @Description:
 */
@Slf4j
@Component
public class UdepServerSender extends AbstractServerSender {
    private ChannelManage channelManage=ChannelManage.getInstance();

    @Override
    protected AsyncResult doSendToClient(Long clientId, UdepRequest udepRequest) throws UdepException {
        Channel channel = channelManage.getChannelByClientId(clientId.toString());
        if (channel.isActive()) {
            CompletableFuture<UdepResponse<?>> resultFuture = new CompletableFuture<>();
            // 构建消息，此处会构建 requestId
            udepRequest.setRequestId(IdUtil.getSnowflake(clientId.longValue(),1).nextId());
            UnFinishedRequests.put(udepRequest.getRequestId(), resultFuture);
            channel.writeAndFlush(udepRequest).addListener((ChannelFutureListener) future -> {
                if (!future.isSuccess()) {
                    // 发送失败时关闭连接
                    future.channel().close();
                    resultFuture.completeExceptionally(future.cause());
                    log.error("udep-server send msg to client failed:", future.cause());
                }
            });
            return new AsyncResult(resultFuture);
        } else {
            throw new UdepException("channel is not active. address="+channel.remoteAddress().toString());
        }
    }
}

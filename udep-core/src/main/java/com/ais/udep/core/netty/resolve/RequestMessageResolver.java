package com.ais.udep.core.netty.resolve;

import com.ais.udep.core.message.UdepRequest;
import com.ais.udep.core.message.UdepResponse;
import com.ais.udep.core.message.type.UdepMessageType;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: dongsheng
 * @CreateTime: 2022/4/13
 * @Description:
 */
// request类型的消息
public class RequestMessageResolver implements Resolver {

    @Override
    public boolean support(UdepRequest message) {
        return message.getType() == UdepMessageType.REQUEST.getValue();
    }

    @Override
    public UdepResponse resolve(ChannelHandlerContext ctx,UdepRequest request) {
        // 接收到request消息之后，将请求类消息放入真正的处理队列中，进行处理
        // do something
        // 处理完成后，生成一个响应消息返回
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return UdepResponse.success(request.getRequestId());
    }
}

package com.ais.udep.client.netty.resolve;

import com.ais.udep.core.message.UdepRequest;
import com.ais.udep.core.message.UdepResponse;
import com.ais.udep.core.message.type.UdepMessageType;
import com.ais.udep.core.netty.resolve.Resolver;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: dongsheng
 * @CreateTime: 2022/4/18
 * @Description:
 */
public class ClientInitMessageResolver implements Resolver {

    @Override
    public boolean support(UdepRequest message) {
        return message.getType()== UdepMessageType.INIT.getValue();
    }

    @Override
    public UdepResponse resolve(ChannelHandlerContext ctx, UdepRequest request) {
        request.setType(UdepMessageType.EMPTY.getValue());
        return UdepResponse.success(null);
    }
}

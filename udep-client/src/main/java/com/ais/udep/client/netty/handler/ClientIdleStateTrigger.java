package com.ais.udep.client.netty.handler;

import com.ais.udep.core.message.UdepRequest;
import com.ais.udep.core.message.type.UdepMessageType;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: dongsheng
 * @CreateTime: 2022/4/12
 * @Description:
 *   <p>
 *    用于捕获{@link IdleState#WRITER_IDLE}事件（未在指定时间内向服务器发送数据），然后向<code>Server</code>端发送一个心跳包。
 *   </p>
 */
@Slf4j
public class ClientIdleStateTrigger extends ChannelInboundHandlerAdapter {
    static UdepRequest HEARTBEAT= new UdepRequest().heartbeat();

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE) {
                Channel channel = ctx.channel();
                // 在规定时间内没有向server发送数据，则主动发送一个心跳信息
                channel.writeAndFlush(HEARTBEAT);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

}

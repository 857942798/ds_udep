package com.ais.udep.server.netty.handler;

/**
 * @author: dongsheng
 * @CreateTime: 2022/4/12
 * @Description: 断连触发器
 */

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * <p>在规定时间内未收到客户端的任何数据包, 将主动断开该连接</p>
 */
public class ServerIdleStateTrigger extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 读写超时事件
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                Channel channel = ctx.channel();
                // 在规定时间内没有收到客户端的上行数据, 主动断开连接
                channel.disconnect();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}

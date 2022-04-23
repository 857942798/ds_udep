package com.ais.udep.server.netty.handler;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RuntimeUtil;
import com.ais.udep.core.netty.codec.UdepMessageDecoder;
import com.ais.udep.core.netty.codec.UdepMessageEncoder;
import com.ais.udep.core.netty.ssl.SecureSslContextFactory;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;

import javax.net.ssl.SSLEngine;

public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {
    DefaultEventExecutorGroup serviceHandlerGroup = new DefaultEventExecutorGroup(
            RuntimeUtil.getProcessorCount() * 2,
            ThreadUtil.newNamedThreadFactory("service-handler-group", false)
    );;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        SSLEngine engine = SecureSslContextFactory.getServerContext("classpath*:/certs/udepServerCerts.jks").createSSLEngine();
        engine.setUseClientMode(false); //设置为服务端模式
        pipeline.addFirst("ssl", new SslHandler(engine));//ssl添加到最前面
        // 超时检测,readerIdleTimeSeconds：读超时（s），writerIdleTimeSeconds：写超时（s）
        // allIdleTimeSeconds:读/写超时,即当在指定的时间间隔内没有读或写操作时, 会触发一个 ALL_IDLE 的 IdleStateEvent事件
        pipeline.addLast("serverIdleStateHandler", new IdleStateHandler(30, 0, 0));
        // 超时触发器
        pipeline.addLast("idleStateTrigger", new ServerIdleStateTrigger());
        // 添加自定义协议消息的编码和解码处理器
        pipeline.addLast(new UdepMessageEncoder());
        pipeline.addLast(new UdepMessageDecoder());
        // 消息处理器，和线程组进行绑定
        pipeline.addLast(serviceHandlerGroup,new UdedServerHandler());
    }
}


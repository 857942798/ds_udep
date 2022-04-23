package com.ais.udep.client.netty.handler;

import com.ais.udep.core.netty.codec.UdepMessageDecoder;
import com.ais.udep.core.netty.codec.UdepMessageEncoder;
import com.ais.udep.core.netty.ssl.SecureSslContextFactory;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.IdleStateHandler;

import javax.net.ssl.SSLEngine;

public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        SSLEngine engine = SecureSslContextFactory.getClientContext("classpath*:/certs/udepClientCerts.jks").createSSLEngine();
        engine.setUseClientMode(true); //设置为客户端模式
        pipeline.addFirst("ssl", new SslHandler(engine));//ssl添加到最前面
        // 断线重连处理器
        pipeline.addLast("",new ReconnectHandler());
        // 超时检测,readerIdleTimeSeconds：读超时（s），writerIdleTimeSeconds：写超时（s）
        // allIdleTimeSeconds:读/写超时,即当在指定的时间间隔内没有读或写操作时, 会触发一个 ALL_IDLE 的 IdleStateEvent事件
        pipeline.addLast("clientIdleStateHandler", new IdleStateHandler(0, 10, 0));
        // 超时触发器
        pipeline.addLast("clientIdleStateTrigger", new ClientIdleStateTrigger());
        // 添加自定义协议消息的编码和解码处理器
        pipeline.addLast(new UdepMessageEncoder());
        pipeline.addLast(new UdepMessageDecoder());
        // 消息处理器
        pipeline.addLast(new UdedClientHandler());
    }


}


package com.ais.udep.server.netty.server;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RuntimeUtil;
import com.ais.udep.server.netty.handler.NettyServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author: dongsheng
 * @CreateTime: 2022/3/7
 * @Description: 报文交换服务端
 */
@Component
public class UdedServer {
    @Value("${config.udep.server.port}")
    private int port;

    // BOSS pool
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    // work pool
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    Channel channel=null;

    @PostConstruct
    public void startUdedServer() throws Exception {
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new NettyServerInitializer())
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        // 绑定指定端口并接收连接
        channel = b.bind(port).sync().channel();
    }

    @PreDestroy
    public void destroy() throws InterruptedException {
        // 优雅地关闭
        // 释放线程池资源
        workerGroup.shutdownGracefully().sync();
        bossGroup.shutdownGracefully().sync();
    }
}

package com.ais.udep.client.netty.client;

import cn.hutool.core.util.StrUtil;
import com.ais.udep.client.netty.handler.NettyClientInitializer;
import com.ais.udep.core.exception.UdepException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author: dongsheng
 * @CreateTime: 2022/3/7
 * @Description: 报文交换客户端
 */
@Slf4j
@Component
@DependsOn("udedServer")
public class UdedClient {
    @Value("${config.udep.server.port}")
    private int port;
    @Value("${config.udep.server.ip}")
    private String ip;
    @Value("${config.udep.client.id}")
    private Long id;

    // 与服务端建立连接的通道
    Channel channel=null;
    private Bootstrap bootstrap;
    private static UdedClient instance = null;

    public static UdedClient getInstance() {
        return instance;
    }
    public Long getId() {
        return id;
    }
    /**
     * 获取和指定地址连接的 channel，如果获取不到，则连接
     *
     * @return channel
     */
    public Channel getChannel() {
        if (channel == null || !channel.isActive()) {
            channel = connect();
        }
        return channel;
    }

    /**
     * 建立连接
     * @return channel
     */
    @PostConstruct
    public Channel connect() {
        this.instance=this;
        InetSocketAddress address = new InetSocketAddress(ip,port);
        try {
            log.info("Try to connect server [{}]", address);
            this.bootstrap = new Bootstrap()
                    .group(new NioEventLoopGroup())
                    .channel(Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    // 程序进程非正常退出，内核需要一定的时间才能够释放此端口，不设置 SO_REUSEADDR 就无法正常使用该端口。
                    .option(ChannelOption.SO_REUSEADDR, Boolean.TRUE)
                    // 开启 TCP 底层心跳机制
                    .option(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
                    // 对于延时敏感型，同时数据传输量比较小的应用，开启TCP_NODELAY选项无疑是一个正确的选择
                    .option(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                    .handler(new NettyClientInitializer());
            CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
            ChannelFuture connect = bootstrap.connect(address);
            connect.addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    completableFuture.complete(future.channel());
                } else {
                    throw new IllegalStateException(StrUtil.format("connect fail. address:", address.toString()));
                }
            });
            this.channel=completableFuture.get(10, TimeUnit.SECONDS);
            return channel;
        } catch (Exception ex) {
            throw new UdepException(address + " connect fail.", ex);
        }
    }

}

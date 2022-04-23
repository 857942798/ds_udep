package com.ais.udep.server.netty.resolver;

import com.ais.udep.core.message.UdepRequest;
import com.ais.udep.core.netty.resolve.Resolver;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author: dongsheng
 * @CreateTime: 2022/4/13
 * @Description: 消息处理器工厂类
 */
public final class UdepServerMessageResolverFactory {

    // 创建一个工厂类实例
    private static final UdepServerMessageResolverFactory serverRsolverFactory = new UdepServerMessageResolverFactory();
    private static final List<Resolver> serverResolvers = new CopyOnWriteArrayList<>();

    private UdepServerMessageResolverFactory() {}

    // 使用单例模式实例化当前工厂类实例
    public static UdepServerMessageResolverFactory getInstance() {
        return serverRsolverFactory;
    }

    public void registerResolver(Resolver resolver) {
        serverResolvers.add(resolver);
    }

    // 根据解码后的消息，在工厂类处理器中查找可以处理当前消息的处理器
    public Resolver getRequestResolver(UdepRequest request) {
        for (Resolver resolver : serverResolvers) {
            if (resolver.support(request)) {
                return resolver;
            }
        }
        throw new RuntimeException("cannot find resolver, message type: " + request.getType());
    }

}

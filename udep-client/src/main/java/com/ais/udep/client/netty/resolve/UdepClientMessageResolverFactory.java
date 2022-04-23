package com.ais.udep.client.netty.resolve;

import com.ais.udep.core.message.UdepRequest;
import com.ais.udep.core.netty.resolve.Resolver;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author: dongsheng
 * @CreateTime: 2022/4/21
 * @Description:
 */
public class UdepClientMessageResolverFactory {
    // 创建一个工厂类实例
    private static final UdepClientMessageResolverFactory clientResolverFactory = new UdepClientMessageResolverFactory();
    private static final List<Resolver> clientResolvers = new CopyOnWriteArrayList<>();

    private UdepClientMessageResolverFactory() {}

    // 使用单例模式实例化当前工厂类实例
    public static UdepClientMessageResolverFactory getInstance() {
        return clientResolverFactory;
    }

    public void registerResolver(Resolver resolver) {
        clientResolvers.add(resolver);
    }

    // 根据解码后的消息，在工厂类处理器中查找可以处理当前消息的处理器
    public Resolver getRequestResolver(UdepRequest request) {
        for (Resolver resolver : clientResolvers) {
            if (resolver.support(request)) {
                return resolver;
            }
        }
        throw new RuntimeException("cannot find resolver, message type: " + request.getType());
    }
}

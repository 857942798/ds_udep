package com.ais.udep.server.netty.handler;

import com.ais.udep.core.message.UdepRequest;
import com.ais.udep.core.message.UdepResponse;
import com.ais.udep.core.message.UnFinishedRequests;
import com.ais.udep.core.message.type.UdepMessageType;
import com.ais.udep.core.netty.pool.UdepExecutorPool;
import com.ais.udep.core.netty.resolve.RequestMessageResolver;
import com.ais.udep.core.netty.resolve.Resolver;
import com.ais.udep.server.netty.resolver.ServerInitMessageResolver;
import com.ais.udep.server.netty.resolver.UdepServerMessageResolverFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.ssl.NotSslRecordException;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;

/**
 * @author: dongsheng
 * @CreateTime: 2022/3/7
 * @Description: 报文交换服务处理类
 */
@Slf4j
public class UdedServerHandler extends SimpleChannelInboundHandler<UdepRequest> {
    // 获取一个消息处理器工厂类实例
    private UdepServerMessageResolverFactory serverResolverFactory = UdepServerMessageResolverFactory.getInstance();
    private ExecutorService excutor= UdepExecutorPool.getExcutor();
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Established connection with the client.");
        // do something
        ctx.fireChannelActive();
    }

    /**
     * 1. 如果是请求类型的消息，说明需要处理。交由处理器处理后得到处理结果返回给客户端
     * 2. 如果是ping类型的消息，回复类型改为pong后返回
     * 3. 如果是pong类型或empty类型的消息，什么也不做
     * 4. 其他类型的消息，由处理器作出处理
     * @param ctx
     * @param udepRequest
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, UdepRequest udepRequest) { // (2)
        try {
            // 心跳类型消息不做处理
            if (udepRequest.getType() == UdepMessageType.HEARTBEAT.getValue()) {
                return;
            }
            // 如果是回复类型的消息，接受到请求后去除future，表示一个请求周期结束
            if (udepRequest.getType() == UdepMessageType.RESPONSE.getValue()) {
                UdepResponse<?> response = (UdepResponse<?>) udepRequest.getData();
                UnFinishedRequests.complete(udepRequest.getRequestId(),response);
            }else{
                // 不在hander中做耗时处理，而是提交到线程池中处理
                // 如果执行耗时较为明确则可直接执行返回数据
                excutor.submit(new Runnable() {
                    @Override
                    public void run() {
                        Resolver resolver = serverResolverFactory.getRequestResolver(udepRequest);// 获取消息处理器
                        UdepResponse result = resolver.resolve(ctx,udepRequest);	// 对消息进行处理并获取响应数据
                        if(udepRequest.getType()==UdepMessageType.REQUEST.getValue()){
                            // 如果是请求消息，将消息类型变更为回复类型返回，请求头中除了body长度外其它不变
                            udepRequest.setType(UdepMessageType.RESPONSE.getValue());
                        }
                        udepRequest.setData(result);
                        ctx.writeAndFlush(udepRequest);	// 将响应数据写入到处理器中
                    }
                });
            }
        } finally {
            // 请求周期结束，释放当前请求
            ReferenceCountUtil.release(udepRequest);
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        serverResolverFactory.registerResolver(new RequestMessageResolver());	// 注册request消息处理器
        serverResolverFactory.registerResolver(new ServerInitMessageResolver());	// 注册init初始化消息处理器
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // 当引发异常时关闭连接
        cause.printStackTrace();
        if(cause instanceof NotSslRecordException){
            log.error("出现一个非ssl的连接："+ctx.name());
        }
        ctx.close();
    }
}

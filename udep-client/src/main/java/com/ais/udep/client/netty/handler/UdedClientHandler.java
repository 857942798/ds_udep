package com.ais.udep.client.netty.handler;

import com.ais.udep.client.netty.client.UdedClient;
import com.ais.udep.client.netty.resolve.ClientInitMessageResolver;
import com.ais.udep.client.netty.resolve.UdepClientMessageResolverFactory;
import com.ais.udep.core.bean.ClientInfo;
import com.ais.udep.core.message.UdepRequest;
import com.ais.udep.core.message.UdepResponse;
import com.ais.udep.core.message.UnFinishedRequests;
import com.ais.udep.core.message.type.UdepMessageType;
import com.ais.udep.core.netty.pool.UdepExecutorPool;
import com.ais.udep.core.netty.resolve.Resolver;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;

/**
 * @author: dongsheng
 * @CreateTime: 2022/3/7
 * @Description: 报文交换服务处理类
 * 1. 客户端启动，发送初始化消息
 * 2. 注册相应的消息类型的处理器
 * 3. 等待初始化消息结果的到来，初始化成功才进行消息的交换
 *
 * 客户端id可由ude-server的进行维护，ude-client在向ude-server注册时，
 * ude-server会为每一个ude-client分配一个实例id，范围为1～1023
 */
@Slf4j
public class UdedClientHandler extends SimpleChannelInboundHandler<UdepRequest> {
    // 获取一个消息处理器工厂类实例
    private UdepClientMessageResolverFactory clientResolverFactory = UdepClientMessageResolverFactory.getInstance();
    private ExecutorService excutor= UdepExecutorPool.getExcutor();
    private UdedClient udedClient=UdedClient.getInstance();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 建立连接后，发送一个初始化消息，使得服务端将通道信息和客户端id进行绑定
        initMsg(ctx);
        super.channelActive(ctx);
    }

    private void initMsg(ChannelHandlerContext ctx) {
        // 连接成功发送初始化消息
        // 连接成功，发送初始化消息
        UdepRequest udepRequest=new UdepRequest();
        udepRequest.setType(UdepMessageType.INIT.getValue());
        ClientInfo clientInfo=ClientInfo.builder()
                .id(udedClient.getId())
                .build();
        udepRequest.setData(clientInfo);
        ctx.channel().writeAndFlush(udepRequest);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, UdepRequest udepRequest) {
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
                        Resolver resolver = clientResolverFactory.getRequestResolver(udepRequest);// 获取消息处理器
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
        clientResolverFactory.registerResolver(new ClientInitMessageResolver());	// 注册客户端初始化类型消息处理器
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // 当引发异常时关闭连接
        cause.printStackTrace();
        ctx.close();
    }

}

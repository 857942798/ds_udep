package com.ais.udep.client.netty.handler;

import com.ais.udep.client.netty.client.UdedClient;
import com.ais.udep.client.netty.retry.ReconnectPolicy;
import com.ais.udep.core.netty.retry.RetryPolicy;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author: dongsheng
 * @CreateTime: 2022/4/12
 *
 * Be aware that this class is marked as {@link Sharable} and so the implementation must be safe to be re-used.
 */
public class ReconnectHandler extends ChannelInboundHandlerAdapter {

    private int retries = 0;
    private RetryPolicy retryPolicy;
    // ReconnectHandler 为new出的对象，手动注入客户端保证唯一
    private UdedClient udedClient=UdedClient.getInstance();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Successfully established a connection to the server.");
        retries = 0;
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (retries == 0) {
            System.err.println("Lost the connection with the udep-server.");
            ctx.close();
        }

        boolean allowRetry = getRetryPolicy().allowRetry(retries);
        if (allowRetry) {

            long sleepTimeMs = getRetryPolicy().getSleepTimeMs(retries);

            System.out.println(String.format("Try to reconnect to the udep-server after %dms. Retry count: %d.", sleepTimeMs, ++retries));

            final EventLoop eventLoop = ctx.channel().eventLoop();
            eventLoop.schedule(() -> {
                System.out.println("Reconnecting ...");
                try {
                    udedClient.connect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, sleepTimeMs, TimeUnit.MILLISECONDS);
        }
        ctx.fireChannelInactive();
    }


    private RetryPolicy getRetryPolicy() {
        if (this.retryPolicy == null) {
            this.retryPolicy =new ReconnectPolicy(1000, Integer.MAX_VALUE, 60 * 1000);
        }
        return this.retryPolicy;
    }
}

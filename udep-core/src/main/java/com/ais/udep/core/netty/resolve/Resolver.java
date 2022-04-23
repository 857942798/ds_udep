package com.ais.udep.core.netty.resolve;

import com.ais.udep.core.message.UdepRequest;
import com.ais.udep.core.message.UdepResponse;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author: dongsheng
 * @CreateTime: 2022/4/13
 * @Description:
 */
public interface Resolver {
     /**
      * 根据消息判断是否由该处理器进行处理，一般是根据消息类型判断
      * @param request {@link UdepRequest}
      * @return
      */
     boolean support(UdepRequest request) ;

     /**
      * 消息处理的具体逻辑
      * @param request {@link UdepRequest}
      * @return
      */
     UdepResponse resolve(ChannelHandlerContext ctx,UdepRequest request) ;
}

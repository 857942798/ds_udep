package com.ais.udep.core.netty.codec;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.ais.udep.core.message.UdepRequest;
import com.ais.udep.core.message.type.UdepMessageType;
import com.ais.udep.core.netty.serialize.serializer.JsonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;


/**
 * @author: dongsheng
 * @CreateTime: 2022/4/14
 * @Description: 消息编码器
 */
@Slf4j
public class UdepMessageEncoder extends MessageToByteEncoder<UdepRequest> {
    /**
     * @param ctx
     * @param udepRequest {@link UdepRequest}
     * @param out
     * 消息体定义
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, UdepRequest udepRequest, ByteBuf out) {
        out.writeShort(udepRequest.getMagic());	    // 魔数
        out.writeByte(udepRequest.getVersion());	// 消息的版本
        out.writeByte(udepRequest.getSerializeType());	// 序列化协议
        out.writeByte(udepRequest.getCompressType());	// 压缩方式
        out.writeByte(udepRequest.getType());	// 消息类型
        if(ObjectUtil.isNull(udepRequest.getRequestId())){
            Snowflake snowflake = IdUtil.getSnowflake(1, 1);
            long requestId =snowflake.nextId();
            out.writeLong(requestId); // requestId
        }else {
            out.writeLong(udepRequest.getRequestId());
        }
        writeBody(udepRequest,out); //写入body
    }

    private void writeBody(UdepRequest udepRequest, ByteBuf out) {
        byte messageType = udepRequest.getType();
        // 如果是心跳类型的，没有 body，直接返回头部长度
        if (messageType == UdepMessageType.HEARTBEAT.getValue()) {
            out.writeInt(0);
            return;
        }
        // 暂不考虑协议扩展机制
        JsonSerializer serializer =new JsonSerializer();
        // 使用序列化器对数据进行序列化
        byte[] notCompressBytes = serializer.serialize(udepRequest.getData());
        out.writeInt(notCompressBytes.length);
        // 写 body
        out.writeBytes(notCompressBytes);
    }

}
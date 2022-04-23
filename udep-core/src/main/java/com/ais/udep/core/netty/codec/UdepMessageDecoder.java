package com.ais.udep.core.netty.codec;

import com.ais.udep.core.message.UdepRequest;
import com.ais.udep.core.message.UdepResponse;
import com.ais.udep.core.message.type.UdepMessageType;
import com.ais.udep.core.netty.serialize.serializer.JsonSerializer;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

import static com.ais.udep.core.common.UdepMessageFormatConfig.*;

/**
 * @author: dongsheng
 * @CreateTime: 2022/4/13
 * @Description: 消息解码器，基于消息长度解码
 */
@Slf4j
public class UdepMessageDecoder extends LengthFieldBasedFrameDecoder {

    /**
     * 字节头中间偏移1处的2字节长度字段，去掉第一个头字段和长度字段
     * 这是上述所有示例的组合。在长度字段之前有前置头，在长度字段之后有额外的头。
     * 前置的标头影响lengthFieldOffset ，额外的标头影响lengthAdjustment 。
     * 我们还指定了一个非零的initialBytesToStrip来从帧中剥离长度字段和前置头。如果您不想剥离前置标头，您可以为initialBytesToSkip指定0 。
     *    lengthFieldOffset   = 1 (= the length of HDR1)
     *    lengthFieldLength   = 2
     *    lengthAdjustment    = 1 (= the length of HDR2)
     *    initialBytesToStrip = 3 (= the length of HDR1 + LEN)
     *
     *    BEFORE DECODE (16 bytes)                       AFTER DECODE (13 bytes)
     *    +------+--------+------+----------------+      +------+----------------+
     *    | HDR1 | Length | HDR2 | Actual Content |----->| HDR2 | Actual Content |
     *    | 0xCA | 0x000C | 0xFE | "HELLO, WORLD" |      | 0xFE | "HELLO, WORLD" |
     *    +------+--------+------+----------------+      +------+----------------+
     */
    public UdepMessageDecoder() {
        super(
                // 最大的长度，如果超过，会直接丢弃
                MAX_FRAME_LENGTH,
                // 描述消息正文长度的字段[4B]在哪个位置
                BODY_LENGTH_OFFSET,
                // 描述消息正文长度的字段[4B]本身的长度
                BODY_LENGTH,
                // lengthAdjustment 长度字段表示的就是消息正文长度，不需要进行补偿
                0,
                // initialBytesToStrip: 去除哪个位置前面的数据。我们需要的是完整的数据，故不去除标头
                0);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        Object decoded = super.decode(ctx, in);
        if (decoded instanceof ByteBuf) {
            ByteBuf msg = (ByteBuf) decoded;
            if (msg.readableBytes() >= HEADER_LENGTH) {
                try {
                    return decodeMessage(msg);
                } catch (Exception ex) {
                    log.error("Decode udepRequest error.", ex);
                } finally {
                    msg.release();
                }
            }
        }
        return decoded;
    }

    /**
     * 解码
     */
    private UdepRequest decodeMessage(ByteBuf in) {
        short magicNum= in.readShort();
        readAndCheckMagic(magicNum);
        byte version= in.readByte();
        readAndCheckVersion(version);
        byte serializeType= in.readByte();
        byte compress = in.readByte();
        byte messageType = in.readByte();
        long requestId = in.readLong();
        int bodyLength = in.readInt();

        UdepRequest udepRequest = UdepRequest.builder()
                .magic(magicNum)
                .version(version)
                .serializeType(serializeType)
                .compressType(compress)
                .type(messageType)
                .requestId(requestId)
                .length(bodyLength)
                .build();

        // 心跳类型直接返回
        if (messageType == UdepMessageType.HEARTBEAT.getValue()) {
            return udepRequest;
        }

        if (bodyLength == 0) {
            return udepRequest;
        }

        byte[] bodyBytes = new byte[bodyLength];
        in.readBytes(bodyBytes);
        // 暂不考虑协议扩展机制
        JsonSerializer serializer =new JsonSerializer();
        Object data;
        try {
            // 尝试以json格式进行转换，否则以字符串格式进行转换
            Class<?> clazz = messageType == UdepMessageType.RESPONSE.getValue() ? UdepResponse.class : JSONObject.class;
            data = serializer.deserialize(bodyBytes, clazz);
        }catch (Exception e){
            data= new String(bodyBytes, StandardCharsets.UTF_8);
        }
        udepRequest.setData(data);
        return udepRequest;
    }

    /**
     * 读取并检查魔数
     */
    private void readAndCheckMagic(short magic) {
        if (magic != MAGIC_NUMBER) {
            throw new IllegalArgumentException("Unknown magic: " + magic);
        }
    }

    /**
     * 读取并检查版本
     */
    private void readAndCheckVersion(byte version) {
        if (version != VERSON) {
            throw new IllegalArgumentException("Unknown version: " + version);
        }
    }


}

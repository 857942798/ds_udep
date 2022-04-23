package com.ais.udep.core.message;

import com.ais.udep.core.common.UdepMessageFormatConfig;
import com.ais.udep.core.message.type.UdepMessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: dongsheng
 * @CreateTime: 2022/4/13
 * @Description: 数据交换平台协议定义
 *
 *  0   1   2       3           4          5        6   7  8  9  10  11  12  13  14  15   16   17  18
 *  +---+---+-------+-----------+----------+--------+---+---+---+---+---+---+---+---+--+---+---+---+
 *  | magic |version| serialize | compress |  type  |          RequestId          | full length |
 *  +---+---+-------+---+---+---+---+-----------+---------+--------+---+---+---+---+---+---+---+---+
 *  |                                                                                              |
 *  |                                         body                                                 |
 *  |                                                                                              |
 *  |                                        ... ...                                               |
 *  +----------------------------------------------------------------------------------------------+
 * 2B magic（魔数）
 * 1B version（版本）-保留字段
 * 1B serialize（序列化类型）-保留字段
 * 1B compress（压缩类型）-保留字段
 * 1B type（消息类型）
 * 8B msgId（消息Id）
 * 4B length（消息长度）
 * body（object类型数据）
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UdepRequest {
    /**
     * magic，version，serializeType，compressType均有默认值，其他类型需要自定义实现
     */
    private Short magic=UdepMessageFormatConfig.MAGIC_NUMBER;
    private Byte version=UdepMessageFormatConfig.VERSON;
    private Byte serializeType=UdepMessageFormatConfig.SERIALIZE_TYPE;
    private Byte compressType=UdepMessageFormatConfig.COMPRESS_TYPE;
    private Byte type;
    private Long requestId;
    private Integer length;
    private Object data;

    // 默认请求构造器
    public UdepRequest(Object data) {
        this.type = UdepMessageType.REQUEST.getValue();
        this.data = data;
    }

    public UdepRequest heartbeat() {
        this.type=UdepMessageType.HEARTBEAT.getValue();
        return this;
    }
}

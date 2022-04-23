package com.ais.udep.core.message.type;

import lombok.AllArgsConstructor;

/**
 * @author: dongsheng
 * @CreateTime: 2022/4/14
 * @Description: 序列话协议
 */
@AllArgsConstructor
public enum SerializeType {
    // 消息体以json格式传输
    JSON((byte) 1);

    private final byte value;

    public byte getValue() {
        return value;
    }

    public static SerializeType get(byte value) {
        for (SerializeType type : values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new RuntimeException("unsupported udep serializeType value: " + value);
    }
}

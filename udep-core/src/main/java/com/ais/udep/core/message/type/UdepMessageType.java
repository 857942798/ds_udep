package com.ais.udep.core.message.type;

/**
 * @author: dongsheng
 * @CreateTime: 2022/4/13
 * @Description: 消息类型
 */
public enum UdepMessageType {
    // 未知类型消息
    EMPTY((byte)-1),
    // 初始化消息处理器
    INIT((byte)0),
    // 请求类型
    REQUEST((byte)1),
    // 响应类型
    RESPONSE((byte)2),
    // 心跳类型
    HEARTBEAT((byte)3);

    private final byte value;

    public byte getValue() {
        return value;
    }

    UdepMessageType(byte value) {
        this.value = value;
    }

    public static UdepMessageType get(byte value) {
        for (UdepMessageType type : values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new RuntimeException("unsupported udepMessage type value: " + value);
    }
}

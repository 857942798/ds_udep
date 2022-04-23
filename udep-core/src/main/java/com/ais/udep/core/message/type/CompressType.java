package com.ais.udep.core.message.type;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: dongsheng
 * @CreateTime: 2022/4/14
 * @Description: 消息压缩类型
 */
@AllArgsConstructor
public enum CompressType {
    // 无压缩
    NONE((byte) 0),
    GZIP((byte) 1);
    private final byte value;

    public byte getValue() {
        return value;
    }
}

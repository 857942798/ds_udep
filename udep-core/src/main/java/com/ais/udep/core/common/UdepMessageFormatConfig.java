package com.ais.udep.core.common;

/**
 * @author: dongsheng
 * @CreateTime: 2022/4/13
 * @Description: 该类只允许定义消息格式化相关的常量
 */
public interface UdepMessageFormatConfig {
    /**
     * 魔数，固定为该数值
     */
     short MAGIC_NUMBER = 0x5b4d;
    /**
     * 版本号，默认为1
     */
     byte VERSON = 1;
    /**
     * 序列化协议，默认为json
     */
    byte SERIALIZE_TYPE = 1;
    /**
     * 压缩协议，默认无压缩
     */
    byte COMPRESS_TYPE = 1;
    /**
     * 协议最大长度
     */
    int MAX_FRAME_LENGTH = 8 * 1024 * 1024;
    /**
     * 魔法数字长度
     */
    int MAGIC_LENGTH = 2;

    /**
     * 版本长度
     */
    int VERSION_LENGTH = 1;

    /**
     * 消息正文长度
     */
    int BODY_LENGTH = 4;

    /**
     * 消息类型长度
     */
    int MESSAGE_TYPE_LENGTH = 1;

    /**
     * 序列化协议长度
     */
    int SERIALIZE_TYPE_LENGTH = 1;

    /**
     * 压缩器类型长度
     */
    int COMPRESS_TYPE_LENGTH = 1;

    /**
     * 请求id 长度
     */
    int REQUEST_ID_LENGTH = 8;

    /**
     * 请求头长度
     */
    int HEADER_LENGTH =
                MAGIC_LENGTH +
                VERSION_LENGTH +
                SERIALIZE_TYPE_LENGTH +
                COMPRESS_TYPE_LENGTH +
                MESSAGE_TYPE_LENGTH +
                REQUEST_ID_LENGTH +
                BODY_LENGTH;
    /**
     * 消息正文长度偏移量
     */
    int BODY_LENGTH_OFFSET =
            MAGIC_LENGTH +
                    VERSION_LENGTH +
                    SERIALIZE_TYPE_LENGTH +
                    COMPRESS_TYPE_LENGTH +
                    MESSAGE_TYPE_LENGTH +
                    REQUEST_ID_LENGTH ;

}

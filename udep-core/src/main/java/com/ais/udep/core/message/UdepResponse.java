package com.ais.udep.core.message;

import com.ais.udep.core.common.UdepEnumCode;
import com.ais.udep.core.common.UdepMessageFormatConfig;
import com.ais.udep.core.message.type.UdepMessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: dongsheng
 * @CreateTime: 2022/4/13
 * @Description:
 * 响应消息体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UdepResponse<T> {
    /**
     * 请求是否成功
     */
    private boolean success;
    /**
     * 描述信息
     */
    private String desc;
    /**
     * 状态码
     */
    private String code;
    /**
     * 响应数据
     */
    private T data;

    public static <T> UdepResponse<T> success(T data) {
        return UdepResponse.<T>builder()
                .code(UdepEnumCode.SUCCESS.getCode())
                .desc(UdepEnumCode.SUCCESS.getDesc())
                .data(data)
                .build();
    }

    /**
     * 返回错误必须要有具体的错误码
     * @param code
     * @param <T>
     * @return
     */
    public static <T> UdepResponse<T> fail(UdepEnumCode code) {
        return UdepResponse.<T>builder()
                .code(code.getCode())
                .desc(code.getDesc())
                .build();
    }
}

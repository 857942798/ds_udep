package com.ais.udep.core.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: dongsheng
 * @CreateTime: 2022/4/18
 * @Description:
 * 错误码code，定义如下所示:
 * 一位错误级别+两位系统编码+三位业务码（表示不同业务模块的报错，取值范围001～999）+三位错误类型（取值范围000～999）。
 * - 错误级别，1位代码：1、为普通提示，2、严重错误，不可忽略 3为不提示、4隐藏性卖萌提示
 * - 数据交换平台系统编码为：99
 * - 成功码只有一个：100000000
 */
@Getter
@AllArgsConstructor
public enum UdepEnumCode {
    /*通用成功码*/
    SUCCESS("100000000", "OK"),
    // 00000 系统级错误 开始
    SYSTEM_ERROR("199000001", "系统异常"),
    SYSTEM_RUNTIME_ERROR("199000002", "运行异常"),
    PARAMETER_ERROR("199000012", "参数错误"),
    SEND_MSG_ERROR("199000013","消息发送失败");
    // 00000 系统级错误 结束

    private String code;
    private String desc;
}

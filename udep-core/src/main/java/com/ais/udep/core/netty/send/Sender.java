package com.ais.udep.core.netty.send;

import com.ais.udep.core.exception.UdepException;
import com.ais.udep.core.message.UdepRequest;
import com.ais.udep.core.message.result.UdepResult;

/**
 * @author: dongsheng
 * @CreateTime: 2022/4/13
 * @Description: 消息发送执行器，为了和invoke区分开来，叫做sender
 */
public interface Sender {

    /**
     * 执行
     * @param udepRequest 请求 {@link UdepRequest}
     * @return result
     * @throws UdepException 执行异常会抛出
     */
    default UdepResult sendToServer(UdepRequest udepRequest) {
        return null;
    }

    default UdepResult sendToClient(Long clientId,UdepRequest udepRequest) {
        return null;
    }

}

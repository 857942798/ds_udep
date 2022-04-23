package com.ais.udep.core.netty.send;

import com.ais.udep.core.exception.UdepException;
import com.ais.udep.core.message.UdepRequest;
import com.ais.udep.core.message.result.UdepResult;

/**
 * @author: dongsheng
 * @CreateTime: 2022/4/19
 * @Description:
 */
public abstract class AbstractServerSender implements Sender {

    @Override
    public UdepResult sendToClient(Long clientId,UdepRequest udepRequest) throws UdepException {
        // 预留方法，如添加负载均衡等功能
        // do something
        return doSendToClient(clientId,udepRequest);
    }

    protected abstract UdepResult doSendToClient(Long clientId,UdepRequest udepRequest) throws UdepException;
}

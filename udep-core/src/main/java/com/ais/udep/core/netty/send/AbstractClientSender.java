package com.ais.udep.core.netty.send;

import com.ais.udep.core.exception.UdepException;
import com.ais.udep.core.message.UdepRequest;
import com.ais.udep.core.message.result.UdepResult;

/**
 * @author: dongsheng
 * @CreateTime: 2022/4/19
 * @Description:
 */
public abstract class AbstractClientSender implements Sender {

    @Override
    public UdepResult sendToServer(UdepRequest message) throws UdepException {
        // 预留方法，如添加负载均衡等功能
        // do something
        return doSendToServer(message);
    }

    protected abstract UdepResult doSendToServer(UdepRequest message) throws UdepException;
}

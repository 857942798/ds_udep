package com.ais.udep.core.exception;

/**
 * @author: dongsheng
 * @CreateTime: 2021/11/10
 * @Description:
 */
public class UdepClientConnectFullException extends UdepException {
    public UdepClientConnectFullException() {
        super();
    }

    public UdepClientConnectFullException(String message) {
        super(message);
    }

    public UdepClientConnectFullException(String message, Throwable cause) {
        super(message, cause);
    }

    public UdepClientConnectFullException(Throwable cause) {
        super(cause);
    }
}

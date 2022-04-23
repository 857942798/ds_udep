package com.ais.udep.server.exception;
/**
 * @author: dongsheng
 * @CreateTime: 2022/3/8
 * @Description:
 */
public class UdepException extends RuntimeException {

    public UdepException() {
        super();
    }

    public UdepException(String message) {
        super(message);
    }

    public UdepException(Throwable cause) {
        super(cause);
    }


    public UdepException(String message, Throwable cause) {
        super(message, cause);
    }
}

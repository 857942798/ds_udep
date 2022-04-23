package com.ais.udep.core.exception;

/**
 * @author: dongsheng
 * @CreateTime: 2021/11/10
 * @Description:
 */
public class UdepMessageDecoderErrorException extends UdepException {
    public UdepMessageDecoderErrorException() {
        super();
    }

    public UdepMessageDecoderErrorException(String message) {
        super(message);
    }

    public UdepMessageDecoderErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public UdepMessageDecoderErrorException(Throwable cause) {
        super(cause);
    }
}

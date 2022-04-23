package com.ais.udep.server.exception;

/**
 * @author: dongsheng
 * @CreateTime: 2022/3/8
 * @Description:
 */
public class UdepDuplicateTableInfoException extends UdepException {
    public UdepDuplicateTableInfoException() {
        super();
    }

    public UdepDuplicateTableInfoException(String message) {
        super(message);
    }

    public UdepDuplicateTableInfoException(String message, Throwable cause) {
        super(message, cause);
    }

    public UdepDuplicateTableInfoException(Throwable cause) {
        super(cause);
    }
}

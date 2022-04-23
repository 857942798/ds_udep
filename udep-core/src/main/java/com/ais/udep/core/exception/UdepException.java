package com.ais.udep.core.exception;

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

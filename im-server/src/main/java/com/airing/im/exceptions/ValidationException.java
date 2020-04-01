package com.airing.im.exceptions;

import com.airing.im.enums.ResponseState;

public class ValidationException extends RuntimeException {

    private volatile ResponseState exception;

    public ValidationException() {
        super();
    }

    public ValidationException(String msg) {
        super(msg);
    }

    public ValidationException(String msg, Exception e) {
        super(msg, e);
    }

    public ValidationException(ResponseState exception) {
        super(exception.getMsg());
        this.exception = exception;
    }
}

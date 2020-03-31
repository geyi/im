package com.airing.im.exceptions;

import com.airing.im.enums.ResponseState;

public class RestException extends RuntimeException {
    private ResponseState excp;
    private int code;
    private String msg;

    public RestException() {
        super();
    }

    public RestException(String msg) {
        super(msg);
    }

    public RestException(String msg, Exception e) {
        super(msg, e);
    }

    public RestException(ResponseState exception) {
        super(exception.getMsg());
        this.excp = exception;
    }

    public RestException(ResponseState exception, Exception e) {
        super(exception.getMsg(), e);
        this.excp = exception;
    }

    public RestException(int code, String msg){
        super(msg);
        this.code = code;
        this.msg = msg;
    }
}

package com.airing.im.exceptions;

public class RunnerException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public RunnerException(String s) {
        super(s);
    }

    public RunnerException(String s, Throwable e) {
        super(s, e);
    }

    public RunnerException(Throwable e) {
        super(e);
    }
}

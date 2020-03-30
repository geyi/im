package com.airing.im.enums;

public enum ResultEnum {
    /** */
    SUCCESS(0,"成功"),
    /** */
    FAIL(1,"失败");

    private int code;
    private String msg;

    ResultEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

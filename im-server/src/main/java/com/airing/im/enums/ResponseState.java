package com.airing.im.enums;

public enum ResponseState {
    /** 成功标识 */
    SUCCESS(0,"成功"),
    /**失败标识 */
    FAIL(1,"失败"),

    RESULT_SYS_ERR(-1, "系统服务异常，请稍后再试"),

    PARAM_EXCPTION(100, "参数异常"),
    SEND_MSG_EXCPTION(101, "请求发消息接口异常");

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    private ResponseState(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

package com.airing.im.bean;

import com.airing.im.enums.ResponseState;

public class RetData<T> {
    private int code;
    private String msg;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public RetData() {
    }

    public RetData(ResponseState state) {
        this.code = state.getCode();
        this.msg = state.getMsg();
    }

    public RetData(ResponseState state, T data) {
        this.code = state.getCode();
        this.msg = state.getMsg();
        this.data = data;
    }
}

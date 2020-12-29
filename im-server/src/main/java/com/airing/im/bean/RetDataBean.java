package com.airing.im.bean;

import com.airing.im.enums.ResponseState;

public class RetDataBean<T> {
    private Integer code;
    private String msg;
    private T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
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

    public RetDataBean() {
    }

    public RetDataBean(ResponseState state) {
        this.code = state.getCode();
        this.msg = state.getMsg();
    }

    public RetDataBean(ResponseState state, T data) {
        this.code = state.getCode();
        this.msg = state.getMsg();
        this.data = data;
    }
}

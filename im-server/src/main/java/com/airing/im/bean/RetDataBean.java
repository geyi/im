package com.airing.im.bean;

import com.airing.im.enums.ResponseState;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RetDataBean<T> {
    private Integer code;
    private String msg;
    private T data;

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

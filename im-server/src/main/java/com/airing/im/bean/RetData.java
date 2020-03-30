package com.airing.im.bean;

import com.airing.im.enums.ResultEnum;

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

    public static RetData successResult(){
        RetData retData = new RetData();
        retData.setCode(ResultEnum.SUCCESS.getCode());
        retData.setMsg(ResultEnum.SUCCESS.getMsg());
        return retData;
    }

    public static RetData failResult(String msg){
        RetData retData = new RetData();
        retData.setCode(ResultEnum.FAIL.getCode());
        retData.setMsg(msg);
        return retData;
    }

    public static RetData failResult(Integer code,String msg){
        RetData retData = new RetData();
        retData.setCode(code);
        retData.setMsg(msg);
        return retData;
    }

    public static RetData successResult(Object data) {
        RetData retData = new RetData();
        retData.setCode(ResultEnum.SUCCESS.getCode());
        retData.setMsg(ResultEnum.SUCCESS.getMsg());
        retData.setData(data);
        return retData;
    }
}

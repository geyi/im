package com.airing.im.controller;

import com.airing.im.bean.RetDataBean;
import com.airing.im.enums.ResponseState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseController {
    private static final Logger log = LoggerFactory.getLogger(BaseController.class);

    protected <T> RetDataBean<T> successResult(T data) {
        return new RetDataBean<T>(ResponseState.SUCCESS, data);
    }

    protected RetDataBean errorResult(ResponseState error) {
        if (log.isDebugEnabled()) {
            log.debug("接口返回异常，code：{}，msg：{}", error.getCode(), error.getMsg());
        }
        return new RetDataBean(error);
    }
}

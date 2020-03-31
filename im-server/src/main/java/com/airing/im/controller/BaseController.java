package com.airing.im.controller;

import com.airing.im.bean.RetData;
import com.airing.im.enums.ResponseState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseController {
    private static final Logger log = LoggerFactory.getLogger(BaseController.class);

    protected <T> RetData<T> successResult(T data) {
        return new RetData<T>(ResponseState.SUCCESS, data);
    }

    protected RetData errorResult(ResponseState error) {
        if (log.isDebugEnabled()) {
            log.debug("接口返回异常，code：{}，msg：{}", error.getCode(), error.getMsg());
        }
        return new RetData(error);
    }
}

package com.airing.im.controller;

import com.airing.im.config.app.AppConfig;
import com.airing.im.utils.RedissonUtils;
import com.airing.im.utils.ZKUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/index")
public class IndexController {
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private ZKUtils zkUtils;
    @Autowired
    private RedissonUtils redissonUtils;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @ResponseBody
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/appConfig", method = RequestMethod.GET)
    @ResponseBody
    public String appConfig() {
        return appConfig.toString();
    }

    @RequestMapping(value = "/appNodes", method = RequestMethod.GET)
    @ResponseBody
    public Object appNodes() {
        return zkUtils.getChildren("/" + appConfig.getAppName());
    }

    @RequestMapping(value = "/redis", method = RequestMethod.GET)
    @ResponseBody
    public Object redis(String key, String value) {
        redissonUtils.set(key, value);
        return redissonUtils.get(key);
    }
}

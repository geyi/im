package com.airing.im.controller;

import com.airing.im.config.app.AppConfig;
import com.airing.im.service.route.RouteExecutor;
import com.airing.im.utils.RedissonUtils;
import com.airing.im.utils.ZKUtils;
import com.airing.im.wrapper.ServerCacheWrapper;
import java.util.List;
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
    @Autowired
    private ServerCacheWrapper serverCacheWrapper;
    @Autowired
    private RouteExecutor routeExecutor;

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
        return serverCacheWrapper.getServerList();
    }

    @RequestMapping(value = "/redis", method = RequestMethod.GET)
    @ResponseBody
    public Object redis(String key, String value) {
        redissonUtils.set(key, value);
        return redissonUtils.get(key);
    }

    @RequestMapping(value = "/route", method = RequestMethod.GET)
    @ResponseBody
    public Object route(String userId) {
        List<String> serverList = this.serverCacheWrapper.getServerList();
        return this.routeExecutor.getServer(serverList, userId);
    }
}

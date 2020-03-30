package com.airing.im.service.event.impl;

import com.airing.im.server.NettySocketHolder;
import com.airing.im.service.event.EventService;
import com.airing.im.utils.RedissonUtils;
import io.netty.channel.ChannelHandlerContext;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "onlineEventServiceImpl")
public class OnlineEventServiceImpl implements EventService {
    private static final Logger log = LoggerFactory.getLogger(OnlineEventServiceImpl.class);

    @Autowired
    private RedissonUtils redissonUtils;

    @Override
    public Map<String, Object> eventHandler(Map<String, Object> data, ChannelHandlerContext ctx) {
        String account = (String) data.get("account");
        if (StringUtils.isBlank(account)) {
            return null;
        }

        if (!NettySocketHolder.containsKey(account)) {
            NettySocketHolder.put(account, ctx.channel());
        } else {
            NettySocketHolder.get(account).close();
            NettySocketHolder.put(account, ctx.channel());
        }

        return null;
    }
}

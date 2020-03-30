package com.airing.im.service.event;

import io.netty.channel.ChannelHandlerContext;
import java.util.Map;

public interface EventService {
    /**
     * 依据不同的事件类型由不同的实现类进行相应的处理
     *
     * @param data 接收到的数据
     * @param ctx channel context
     * @return 发送给客户端的数据，没有需要发送的数据时返回null
     */
    Map<String, Object> eventHandler(Map<String, Object> data, ChannelHandlerContext ctx);
}

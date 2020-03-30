package com.airing.im.server;

import com.airing.im.enums.Event;
import com.airing.im.service.event.EventService;
import com.airing.im.utils.SpringBeanFactory;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelMatcher;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static final Logger log = LoggerFactory.getLogger(TextWebSocketFrameHandler.class);
    private final ChannelGroup group;

    public TextWebSocketFrameHandler(ChannelGroup group) {
        this.group = group;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String data = msg.text();
        Map<String, Object> dataMap = JSONObject.parseObject(data, Map.class);
        if (dataMap == null || dataMap.isEmpty()) {
            return;
        }
        String event = String.valueOf(dataMap.get("event"));
        Event eventEnum = Event.getEvent(event);
        if (eventEnum == null || StringUtils.isEmpty(eventEnum.getBeanName())) {
            log.error("未找到事件处理类，{}", data);
            return;
        } else {
            log.info("{}消息:{}", eventEnum.getDesc(), data);
        }

        EventService service = SpringBeanFactory.getBean(eventEnum.getBeanName(), EventService.class);
        Map<String, Object> result = service.eventHandler(dataMap, ctx);
        if (result == null || result.isEmpty()) {
            return;
        }
        String sendData = JSONObject.toJSONString(result);
        log.info("发送消息:{}", sendData);

        NettySocketHolder.sendMsg(String.valueOf(result.get("receiverId")), sendData);
        // 消息同时发给消息的发送者
        NettySocketHolder.sendMsg(String.valueOf(result.get("senderId")), sendData);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
            log.info("{}建立连接", ctx.channel().toString());
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        String key = NettySocketHolder.remove(incoming);
        log.info("ChatClient:" + key + "离线");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Channel incoming = ctx.channel();
        String key = NettySocketHolder.remove(incoming);
        log.info("ChatClient:" + key + "异常离线");
        // 当出现异常就关闭连接
        log.error("出现异常关闭连接", cause);
        ctx.close();
    }

    /**
     * 实现ChannelMatcher接口，用于 channel group 中channel的比较
     */
    public static class ChannelMatchers implements ChannelMatcher {
        private Channel c;

        public ChannelMatchers(Channel c) {
            this.c = c;
        }

        @Override
        public boolean matches(Channel channel) {
            if (this.c.equals(channel)) {
                return true;
            }
            return false;
        }
    }
}

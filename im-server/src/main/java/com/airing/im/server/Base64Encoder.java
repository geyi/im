package com.airing.im.server;

import com.airing.im.constant.Common;
import com.airing.im.enums.Event;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import java.util.List;
import java.util.Map;
import org.springframework.util.Base64Utils;

public class Base64Encoder extends MessageToMessageEncoder<TextWebSocketFrame> {
    @Override
    protected void encode(ChannelHandlerContext ctx, TextWebSocketFrame msg, List<Object> out) throws Exception {
        String strMsg = msg.text();
        Map<String, Object> map = JSONObject.parseObject(strMsg, Map.class);
        if (Event.codecEvent.contains(String.valueOf(map.get("event")))) {
            String text = Base64Utils.encodeToString(strMsg.getBytes(Common.DEFAULT_CHARSET));
            out.add(new TextWebSocketFrame(text));
        } else {
            out.add(new TextWebSocketFrame(strMsg));
        }
    }
}

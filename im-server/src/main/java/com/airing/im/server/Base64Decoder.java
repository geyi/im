package com.airing.im.server;

import com.airing.im.constant.Common;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import java.util.List;
import org.springframework.util.Base64Utils;

public class Base64Decoder extends MessageToMessageDecoder<TextWebSocketFrame> {
    @Override
    protected void decode(ChannelHandlerContext ctx, TextWebSocketFrame msg, List<Object> out) throws Exception {
        String strMsg = msg.text();
        byte[] base64Byte;
        try {
            base64Byte = Base64Utils.decodeFromString(strMsg);
        } catch (Exception e) {
            // 这里使用了异常控制业务逻辑，是一种不好的做法
            out.add(new TextWebSocketFrame(strMsg));
            return;
        }
        String text = new String(base64Byte, Common.DEFAULT_CHARSET);
        out.add(new TextWebSocketFrame(text));
    }
}

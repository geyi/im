package com.airing.im.service.event.impl;

import com.airing.im.config.app.AppConfig;
import com.airing.im.constant.Common;
import com.airing.im.server.NettySocketHolder;
import com.airing.im.service.chat.ChatService;
import com.airing.im.service.event.EventService;
import com.airing.im.utils.RedissonUtils;
import com.airing.im.wrapper.ServerCacheWrapper;
import com.airing.utils.ThreadPoolUtils;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatEventServiceImpl implements EventService {
    private static final Logger log = LoggerFactory.getLogger(ChatEventServiceImpl.class);

    @Autowired
    private RedissonUtils redissonUtils;
    @Autowired
    private ChatService chatService;
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private ServerCacheWrapper serverCacheWrapper;

    @Override
    public Map<String, Object> eventHandler(Map<String, Object> data, ChannelHandlerContext ctx) {
        String chatContent = (String) data.get("chatContent");
        if (StringUtils.isBlank(chatContent)) {
            return null;
        }
        // 保存消息到数据库
        ThreadPoolUtils.getSingle().execute(new SaveData(new HashMap<>(data), chatService));

        String receiverId = (String) data.get("receiverId");
        String key = String.format(Common.USER_SERVER, receiverId);
        String server = redissonUtils.get(key);
        String ip = this.serverCacheWrapper.serverIP(server);
        if (StringUtils.isBlank(ip) || appConfig.getIp().equals(ip)) {
            return data;
        } else {
            this.chatService.sendMsg2Server(server, data);
            String senderId = (String) data.get("senderId");
            NettySocketHolder.sendMsg(senderId, JSONObject.toJSONString(data));
            return null;
        }
    }

    class SaveData implements Runnable {
        private Map<String, Object> data;
        private ChatService chatService;

        public SaveData(Map<String, Object> data, ChatService chatService) {
            this.data = data;
            this.chatService = chatService;
        }

        @Override
        public void run() {
            try {
                this.execute();
            } catch (Exception e) {
                log.error("保存聊天消息异常", e);
            }
        }

        private void execute() {
            String senderId = (String) data.get("senderId");
            String receiverId = (String) data.get("receiverId");
            String chatContent = (String) data.get("chatContent");
            int chatId = this.chatService.insertChat(senderId, senderId, receiverId, chatContent);
            this.chatService.insertChatRef(senderId, receiverId, chatId);
            chatId = this.chatService.insertChat(receiverId, senderId, receiverId, chatContent);
            this.chatService.insertChatRef(receiverId, senderId, chatId);
        }
    }
}

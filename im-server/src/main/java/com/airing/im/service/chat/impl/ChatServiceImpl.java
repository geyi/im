package com.airing.im.service.chat.impl;

import com.airing.im.bean.game.chat.ChatMsgBean;
import com.airing.im.bean.game.chat.ChatParamBean;
import com.airing.im.bean.game.chat.ChatRecordBean;
import com.airing.im.bean.game.chat.ChatRefBean;
import com.airing.im.dao.game.ChatDao;
import com.airing.im.enums.ResponseState;
import com.airing.im.server.NettySocketHolder;
import com.airing.im.service.chat.ChatService;
import com.airing.im.utils.RedissonUtils;
import com.airing.im.utils.http.HttpRequestUtils;
import com.airing.im.wrapper.ServerCacheWrapper;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ChatServiceImpl implements ChatService {
    private static final Logger log = LoggerFactory.getLogger(ChatServiceImpl.class);

    @Autowired
    private ChatDao chatDao;
    @Autowired
    private RedissonUtils redissonUtils;
    @Autowired
    private ServerCacheWrapper serverCacheWrapper;

    @Override
    public List<Map<String, Object>> searchChatRefPage(Map<String, Object> params) {
        List<Map<String, Object>> refList = this.chatDao.searchChatRefPage(params);
        return refList;
    }

    @Override
    public List<Map<String, Object>> searchChatRecord(ChatParamBean chat) {
        String chatTime = chat.getChatTime();
        if (StringUtils.isNotEmpty(chatTime)) {
            chat.setCreateTimeStart(chatTime + " 00:00:00");
            chat.setCreateTimeEnd(chatTime + " 23:59:59");
        }
        List<Map<String, Object>> recordList = this.chatDao.searchChatRecord(chat);
        return recordList;
    }

    @Override
    public int insertChat(String uid, String sid, String rid, String content) {
        ChatRecordBean chatRecordBean = new ChatRecordBean();
        chatRecordBean.setUserId(uid);
        chatRecordBean.setSenderId(sid);
        chatRecordBean.setReceiverId(rid);
        chatRecordBean.setChatContent(content);
        this.chatDao.insertChat(chatRecordBean);

        return chatRecordBean.getId();
    }

    @Override
    public int insertChatRef(String uid, String friendId, int chatId) {
        ChatRefBean chatRefBean = new ChatRefBean();
        chatRefBean.setUserId(uid);
        chatRefBean.setFriendId(friendId);
        chatRefBean.setChatId(chatId);
        return this.chatDao.insertChatRef(chatRefBean);
    }

    @Override
    public void sendMsg2Server(String server, Map<String, Object> data) {
        String httpServer = this.serverCacheWrapper.httpServer(server);
        String url = httpServer + "/chat/sendMsg";
        String jsonParams = JSONObject.toJSONString(data);
        String ret = HttpRequestUtils.post(url, jsonParams , ResponseState.SEND_MSG_EXCPTION);
        log.info("{}消息发送结果{}", jsonParams, ret);
    }

    @Override
    public void sendMsg2User(ChatMsgBean msgBean) {
        NettySocketHolder.sendMsg(msgBean.getReceiverId(), JSONObject.toJSONString(msgBean));
    }
}

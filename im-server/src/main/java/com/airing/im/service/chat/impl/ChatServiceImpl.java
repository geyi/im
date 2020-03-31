package com.airing.im.service.chat.impl;

import com.airing.im.bean.RetDataBean;
import com.airing.im.bean.game.chat.ChatParamBean;
import com.airing.im.bean.game.chat.ChatRecordBean;
import com.airing.im.bean.game.chat.ChatRefBean;
import com.airing.im.dao.game.ChatDao;
import com.airing.im.service.chat.ChatService;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService {
    private static final Logger log = LoggerFactory.getLogger(ChatServiceImpl.class);

    @Autowired
    private ChatDao chatDao;

    @Override
    public RetDataBean searchChatRecord(ChatParamBean chat) {
        String chatTime = chat.getChatTime();
        if (StringUtils.isNotEmpty(chatTime)) {
            chat.setCreateTimeStart(chatTime + " 00:00:00");
            chat.setCreateTimeEnd(chatTime + " 23:59:59");
        }
        List<Map<String, Object>> recordList = this.chatDao.searchChatRecord(chat);
        RetDataBean result = new RetDataBean();
        result.setData(recordList);

        return result;
    }

    @Override
    public RetDataBean searchChatRefPage(Map<String, Object> params) {
        List<Map<String, Object>> refList = this.chatDao.searchChatRefPage(params);
        RetDataBean result = new RetDataBean();
        result.setData(refList);
        return result;
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
}

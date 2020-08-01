package com.airing.im.service.chat;

import com.airing.im.bean.game.chat.ChatMsgBean;
import com.airing.im.bean.game.chat.ChatParamBean;

import java.util.List;
import java.util.Map;

public interface ChatService {
    List<Map<String, Object>> searchChatRefPage(Map<String, Object> params);

    List<Map<String, Object>> searchChatRecord(ChatParamBean chat);

    /**
     * @Title:         insertChat
     * @Description:   插入聊天消息
     * @Author:        GEYI
     * @Date:          2019年07月04日 14:51
     * @Param:         [uid, sid, rid, content, type]
     * @return:        int 返回插入记录的自增长id
     **/
    int insertChat(String uid, String sid, String rid, String content);

    /**
     * @Title:         insertChatRef
     * @Description:   插入聊天关系
     * @Author:        GEYI
     * @Date:          2019年07月05日 17:12
     * @Param:         [csId, uid, uname, chatId]
     * @return:        int
     **/
    int insertChatRef(String uid, String friendId, int chatId);

    void sendMsg2Server(String server, Map<String, Object> data);

    void sendMsg2User(ChatMsgBean msgBean);
}

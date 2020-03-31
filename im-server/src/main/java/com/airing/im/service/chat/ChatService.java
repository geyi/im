package com.airing.im.service.chat;

import com.airing.im.bean.RetData;
import com.airing.im.bean.game.chat.Chat;
import java.util.Map;

public interface ChatService {
    RetData searchChatRef(Map<String, Object> params);

    RetData searchChatRecord(Chat chat);

    RetData updateReadStatus(String userId);

    RetData switchProcessed(String userId, int unread);

    RetData searchChatRefPage(Map<String, Object> params);

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
}

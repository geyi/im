package com.airing.im.dao.game;

import com.airing.im.bean.game.chat.Chat;
import com.airing.im.bean.game.chat.ChatRecordBean;
import com.airing.im.bean.game.chat.ChatRefBean;
import java.util.List;
import java.util.Map;

public interface ChatDao {

    List<Map<String, Object>> searchChatRef(Map<String, Object> params);

    List<Map<String, Object>> searchChatRecord(Chat chat);

    int updateChatRef(Map<String, Object> params);

    List<Map<String, Object>> searchChatRefPage(Map<String, Object> params);

    int insertChat(ChatRecordBean csChatRecordBean);

    int insertChatRef(ChatRefBean csChatRefBean);
}

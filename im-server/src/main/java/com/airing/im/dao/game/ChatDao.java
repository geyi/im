package com.airing.im.dao.game;

import com.airing.im.bean.game.chat.ChatParamBean;
import com.airing.im.bean.game.chat.ChatRecordBean;
import com.airing.im.bean.game.chat.ChatRefBean;
import java.util.List;
import java.util.Map;

public interface ChatDao {

    List<Map<String, Object>> searchChatRefPage(Map<String, Object> params);

    List<Map<String, Object>> searchChatRecord(ChatParamBean chat);

    int insertChat(ChatRecordBean csChatRecordBean);

    int insertChatRef(ChatRefBean csChatRefBean);
}

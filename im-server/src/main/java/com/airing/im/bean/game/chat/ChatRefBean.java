package com.airing.im.bean.game.chat;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ChatRefBean {
    /* 自增长id */
    private Integer id;
    /* 用户id */
    private String userId;
    /* 好友id */
    private String friendId;
    /* 聊天消息id */
    private Integer chatId;
    /* 是否有未读消息 0：没有 1：有 */
    private Integer unread;
    /* 未读消息数量 */
    private Integer unreadCount;
}

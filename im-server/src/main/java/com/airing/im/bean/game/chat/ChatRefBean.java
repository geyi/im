package com.airing.im.bean.game.chat;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }

    public Integer getUnread() {
        return unread;
    }

    public void setUnread(Integer unread) {
        this.unread = unread;
    }

    public Integer getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(Integer unreadCount) {
        this.unreadCount = unreadCount;
    }
}

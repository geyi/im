package com.airing.im.bean.game.chat;

public class ChatRecordBean {
    /* 自增长id */
    private Integer id;
    /* 用户id */
    private String userId;
    /* 消息发送者id */
    private String senderId;
    /* 消息接收者id */
    private String receiverId;
    /* 聊天消息内容 */
    private String chatContent;
    /* 聊天消息时间戳 */
    private Long timestamp;

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

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getChatContent() {
        return chatContent;
    }

    public void setChatContent(String chatContent) {
        this.chatContent = chatContent;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}

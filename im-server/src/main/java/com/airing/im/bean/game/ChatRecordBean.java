package com.airing.im.bean.game;

/**
 * @ClassName:      CSChatRecordBean
 * @Description:    聊天消息实体类
 * @Author:         GEYI
 * @CreateDate:     2019年07月04日 14:08
 * @Version:        1.0
 * @Copyright:      2019/7/4 yian Inc. All rights reserved.
 **/
public class ChatRecordBean {
    /* 自增长id */
    private int id;
    /* 用户id */
    private String userId;
    /* 消息发送者id */
    private String senderId;
    /* 消息发送者昵称 */
    private String senderName;
    /* 消息接收者id */
    private String receiverId;
    /* 聊天消息内容 */
    private String chatContent;
    /* 聊天消息类型 */
    private int chatType;
    /* 聊天消息时间戳 */
    private long timestamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
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

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

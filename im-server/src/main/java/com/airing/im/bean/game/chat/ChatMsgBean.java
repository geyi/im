package com.airing.im.bean.game.chat;

public class ChatMsgBean {
    private String senderId;
    private String receiverId;
    /** 接收人的用户名 */
    private String receiverName;
    private String chatContent;
    private int event;
    private long chatTime;

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

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getChatContent() {
        return chatContent;
    }

    public void setChatContent(String chatContent) {
        this.chatContent = chatContent;
    }

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }

    public long getChatTime() {
        return chatTime;
    }

    public void setChatTime(long chatTime) {
        this.chatTime = chatTime;
    }
}

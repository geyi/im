package com.airing.im.bean.game.chat;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
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
}

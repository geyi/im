package com.airing.im.bean.game.chat;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ChatMsgBean {
    private String senderId;
    private String receiverId;
    /** 接收人的用户名 */
    private String receiverName;
    private String chatContent;
    private Integer event;
    private Long chatTime;
}

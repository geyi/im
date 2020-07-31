package com.airing.im.bean.game.chat;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ChatParamBean {
    private String account;
    private String userId;
    private String chatTime;
    private String createTimeStart;
    private String createTimeEnd;
    private Integer offset;
    private Integer limit;
}

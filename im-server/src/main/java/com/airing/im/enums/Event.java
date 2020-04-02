package com.airing.im.enums;

import com.airing.im.exceptions.RunnerException;
import com.google.common.collect.Lists;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public enum Event {
    /* 上线 */
    ONLINE("1", "onlineEventServiceImpl", "上线"),
    /* 用户上线 */
    USER_ONLINE("2", "userOnlineEventServiceImpl", "用户上线"),
    /* 聊天 */
    CHAT("3", "chatEventServiceImpl", "聊天"),
    /* 客服已读 */
    READ("4", "readEventServiceImpl", "客服已读"),
    /* 匹配 */
    MATCH("5", "matchEventServiceImpl", "匹配"),
    /* 开始答题 */
    ANSWER_START("6", "", "开始答题"),
    /* 答题 */
    ANSWER("7", "answerEventServiceImpl", "答题"),
    /* 对战结果 */
    BATTLE_RESULT("8", "", "对战结果"),
    /* 退出对战 */
    GG("9", "ggEventServiceImpl", "退出对战"),
    /* 取消匹配 */
    CANCEL_MATCH("10", "cancelMatchEventServiceImpl", "取消匹配"),
    /* 重连 */
    RECONNECT("11", "reconnectEventServiceImpl", "重连");

    private String code;
    private String beanName;
    private String desc;

    public String getCode() {
        return code;
    }

    public String getBeanName() {
        return beanName;
    }

    public String getDesc() {
        return desc;
    }

    private Event(String code, String beanName, String desc) {
        this.code = code;
        this.beanName = beanName;
        this.desc = desc;
    }

    public static Event getEvent(String code) {
        if (StringUtils.isBlank(code)) {
            throw new RunnerException("code is blank");
        }
        for (Event event : Event.values()) {
            if (event.getCode().equals(code)) {
                return event;
            }
        }
        return null;
    }

    public static final List<String> codecEvent;
    static {
        codecEvent = Lists.newArrayList(
            MATCH.getCode(),
            ANSWER_START.getCode(),
            ANSWER.getCode(),
            BATTLE_RESULT.getCode(),
            GG.getCode(),
            CANCEL_MATCH.getCode(),
            RECONNECT.getCode()
        );
    }
}

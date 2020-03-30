package com.airing.im.constant;

public class Common {
    public static final String DEFAULT_CHARSET = "UTF-8";
    public static final long ONE_DAY_MILLI = 86400000;
    public static final long ONE_HOUR_MILLI = 3600000;
    /* 日志 */
    public static final String TRACE_ID = "traceId";
    /* 答题超时时间弹性值 */
    public static final int TIMEOUT = 5;
    /* 答题开始前的动画时间 */
    public static final int START_ANIM_TIME = 3;
    /* 答题间隔的动画时间 */
    public static final int INTERVAL_ANIM_TIME = 2;
    public static final String REDIS_KEY_FANTASY_USER_TOKEN = "AUTH_FANTASY_USER_TOKEN_";
    public static final String REDIS_KEY_FANTASY_USER_INFO = "AUTH_FANTASY_USER_INFO_";
    public static final String REDIS_KEY_CS_LIST = "CS_LIST";
    public static final String CS_RECEIVER = "cs";
    /* quiz 1V1 匹配队列前缀 */
    public static final String FTS_QUIZ_1V1 = "FTS_QUIZ_1V1_%s";
    /* 表示分配机器人 */
    public static final String FTS_QUIZ_ROBOT = "FTS_QUIZ_ROBOT";
    /* quiz 题库前缀 */
    public static final String FTS_QUIZ_QUES = "FTS_QUIZ_%s_%s";
    /* quiz 已获取的题库前缀 */
    public static final String FTS_QUIZ_GETS = "FTS_QUIZ_GETS_%s_%s";
    /* quiz 临时题库前缀 */
    public static final String FTS_QUIZ_TEMP = "FTS_QUIZ_TEMP_%s";
    /* quiz 机器人规则前缀 */
    public static final String FTS_QUIZ_ROBOT_RULE = "FTS_QUIZ_ROBOT_RULE_%s";
    /* quiz 机器人名字库前缀 */
    public static final String FTS_QUIZ_NAME = "FTS_QUIZ_NAME_%s";
    /* quiz 机器人渠道 */
    public static final String FTS_QUIZ_ROBOT_CHANNEL = "ROBOT";
    /* quiz 订单信息 */
    public static final String FTS_QUIZ_ORDER = "FTS_QUIZ_ORDER_%s";
    /* quiz 用户每天下单量前缀 */
    public static final String FTS_QUIZ_ORDER_COUNT = "FTS_QUIZ_ORDER_COUNT_%s_%s";
    /* quiz 1V1 对战结果前缀 */
    public static final String FTS_QUIZ_1V1_RET = "FTS_QUIZ_1V1_RET_%s";
    /* quiz 答题时间 */
    public static final String FTS_QUIZ_TIME = "FTS_QUIZ_TIME";
    /* quiz 1V1 机器人答题时间前缀 */
    public static final String FTS_QUIZ_ROBOT_TIME = "FTS_QUIZ_ROBOT_TIME_%s";

    /* 范特西在线用户数 */
    public static final String FTS_ONLINE_USER_COUNT = "FTS_ONLINE_USER_COUNT";

    /* MQ topic */
    public static final String FANTASY_GAME = "FANTASY_GAME";
    /* MQ 在线统计tag */
    public static final String ONLINE_STAT = "ONLINE_STAT";
    /* MQ 答题活动tag */
    public static final String QUIZ_ACTIVITY = "QUIZ_ACTIVITY";
    /* 数据上报 topic */
    public static final String NODE_DATA_REPORT = "NODE_DATA_REPORT";
    /* 数据上报quiz统计tag */
    public static final String QUIZ_ORDER_REF = "QUIZ_ORDER_REF";
    /* 数据上报quiz统计tag */
    public static final String QUIZ_ORDER = "QUIZ_ORDER";
    /* 数据上报sql类型 */
    public static final String NODE_DATA_REPORT_INSERT = "INSERT";
    public static final String NODE_DATA_REPORT_UPDATE = "UPDATE";
}

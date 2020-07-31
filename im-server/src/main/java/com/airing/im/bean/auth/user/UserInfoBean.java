package com.airing.im.bean.auth.user;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserInfoBean {
    private String userId;
    private String userName;
    private String userPic;
    /* 用户APP使用的语言 */
    private String language;
    /* 用户当前设备 */
    private String deviceCode;
}

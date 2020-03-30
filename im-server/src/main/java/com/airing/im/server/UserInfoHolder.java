package com.airing.im.server;

import com.airing.im.bean.auth.UserInfoBean;
import com.airing.im.enums.Language;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserInfoHolder {
    private static final Logger log = LoggerFactory.getLogger(UserInfoHolder.class);
    private static Map<String, UserInfoBean> MAP = new ConcurrentHashMap<>(512);

    public static void put(String id, UserInfoBean user) {
        MAP.put(id, user);
    }

    public static UserInfoBean remove(String id) {
        return MAP.remove(id);
    }

    public static UserInfoBean get(String id) {
        return MAP.get(id);
    }

    public static void putLanguage(String id, String language) {
        Language l = Language.getLanguage(language);
        if (l == null) {
            log.warn("错误的语言标识，{}，{}", id, language);
            l = Language.EN;
        }

        UserInfoBean userInfoBean = MAP.get(id);
        if (userInfoBean == null) {
            userInfoBean = new UserInfoBean();
            MAP.put(id, userInfoBean);
        }
        userInfoBean.setLanguage(l.getKey());
    }

    public static void putDeviceCode(String id, String deviceCode) {
        UserInfoBean userInfoBean = MAP.get(id);
        if (userInfoBean == null) {
            userInfoBean = new UserInfoBean();
            MAP.put(id, userInfoBean);
        }
        userInfoBean.setDeviceCode(deviceCode);
    }
}

package com.airing.im.dao.auth;


import com.airing.im.bean.auth.UserInfoBean;

public interface UserInfoDao {
    UserInfoBean loadUser(String userId);
}

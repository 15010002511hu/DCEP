package com.emop.wlt.user.management.service;


import com.emop.wlt.user.entity.User;


public interface UserService {

    User selectByPrimaryKey(String userId);

    boolean saveUserInfo(User userLoginInfo);

    boolean updateStatusByUserId(User userLoginInfo);

    boolean updateUserPwdByUserId(String userId, String userPwd, String salt);

    void deactivateUser(String phone, String userId);

}

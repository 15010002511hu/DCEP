package com.emop.wlt.user.management.manager;

import com.emop.wlt.common.util.PasswordUtils;
import com.emop.wlt.user.entity.User;
import com.emop.wlt.user.management.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class UserPasswordManager {

    @Autowired
    private UserService userService;

    public void updateUserPwd(String userId, String userPwd) {
        String salt = PasswordUtils.genSalt();
        userService.updateUserPwdByUserId(userId, PasswordUtils.encPassword(userPwd, salt), salt);
    }

    public boolean equalsUserPwd(String userId, String newPwd) {
        User userLoginInfo = userService.selectByPrimaryKey(userId);
        if (StringUtils.isBlank(userLoginInfo.getPwd()) || StringUtils.isBlank(userLoginInfo.getPwdSalt())) {
            return false;
        }
        return PasswordUtils.match(newPwd, userLoginInfo.getPwd(), userLoginInfo.getPwdSalt());
    }

}

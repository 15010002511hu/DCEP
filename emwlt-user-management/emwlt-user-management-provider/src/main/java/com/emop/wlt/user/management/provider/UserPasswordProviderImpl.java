package com.emop.wlt.user.management.provider;

import com.emop.common.enums.errorcode.BaseErrorEnum;
import com.emop.wlt.user.management.api.UserPasswordProvider;
import com.emop.wlt.user.management.exception.ExceptionCast;
import com.emop.wlt.user.management.manager.UserPasswordManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author liguangyao
 */
@DubboService
@Slf4j
public class UserPasswordProviderImpl implements UserPasswordProvider {

    @Autowired
    private UserPasswordManager userPasswordManager;

    @Autowired
    private CFCAManagerProviderImpl cfcaManagerProvider;

    @Override
    public void checkUserPwd(String userId, String userPwd) {
        boolean samePwd = userPasswordManager.equalsUserPwd(userId, userPwd);
        if (samePwd) {
            log.error("修改登录密码-设置新密码，UserId：[{}]，新旧密码不能相同", userId);
            ExceptionCast.buzCast(BaseErrorEnum.B06128);
        }
    }

    @Override
    public void updateUserPwd(String userId, String userPwd) {
        userPasswordManager.updateUserPwd(userId, userPwd);
    }

    @Override
    public void decryptAndUpdateUserPwd(String userId, String encUserPwd) {
        String userPwd = cfcaManagerProvider.decryptPassWord(encUserPwd);
        userPasswordManager.updateUserPwd(userId, userPwd);
    }

}

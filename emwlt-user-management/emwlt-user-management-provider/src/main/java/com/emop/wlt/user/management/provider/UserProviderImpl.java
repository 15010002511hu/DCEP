package com.emop.wlt.user.management.provider;

import com.emop.wlt.common.enums.AppUserStatusEnum;
import com.emop.wlt.user.entity.User;
import com.emop.wlt.user.info.api.UserInfoProvider;
import com.emop.wlt.user.management.api.UserProvider;
import com.emop.wlt.user.management.dto.UserVO;
import com.emop.wlt.user.management.manager.MessagePushManager;
import com.emop.wlt.user.management.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

@DubboService
@Slf4j
public class UserProviderImpl implements UserProvider {

    @Autowired
    private UserService userService;
    @DubboReference
    private UserInfoProvider userInfoProvider;
    @Autowired
    private MessagePushManager messagePushManager;

    @Override
    public UserVO selectByPrimaryKey(String userId) {
        User user = userService.selectByPrimaryKey(userId);
        if (Objects.isNull(user)) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public void deactivateUser(String phone, String userId) {
        userService.deactivateUser(phone, userId);
    }

    @Override
    public void reportAsLost(String userId) {
        // 踢出登录
        User dbUser = userService.selectByPrimaryKey(userId);
        String token = userInfoProvider.forceKickOutUser(userId, dbUser.getPhone());

        // 更新用户状态
        User user = new User();
        user.setUserId(userId);
        user.setStatus(AppUserStatusEnum.LOST.getValue());
        userService.updateStatusByUserId(user);
        // 发送App消息
        messagePushManager.sendForceLoginOutMsg(userId, dbUser.getPhone(), token);
        log.info("用户[{}]账号挂失成功", userId);
    }

    @Override
    public void cancelLoss(String userId) {
        User user = new User();
        user.setUserId(userId);
        user.setStatus(AppUserStatusEnum.NORMAL.getValue());
        userService.updateStatusByUserId(user);
        log.info("用户[{}]账号解挂成功", userId);
    }

}

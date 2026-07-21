package com.emop.wlt.user.query.provider;

import com.emop.wlt.user.entity.User;
import com.emop.wlt.user.query.api.UserInfoQueryProvider;
import com.emop.wlt.user.query.dto.UserInfoDTO;
import com.emop.wlt.user.query.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

/**
 * 用户信息查询
 */
@DubboService
@Slf4j
public class UserInfoQueryProviderImpl implements UserInfoQueryProvider {

    @Autowired
    private UserService userService;

    @Override
    public UserInfoDTO selectByUserId(String userId) {
        log.info("查询用户信息:{}",userId);
        try {
            User user = userService.selectByPrimaryKey(userId);
            if (Objects.isNull(user)) {
                return null;
            }
            UserInfoDTO userInfoDTO = new UserInfoDTO();
            BeanUtils.copyProperties(user, userInfoDTO);
            return userInfoDTO;
        } catch (Exception e) {
           log.error("查询异常",e);
           throw e;
        }
    }
}

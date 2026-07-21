package com.emop.wlt.user.management.manager;

import com.emop.common.enums.errorcode.BaseErrorEnum;
import com.emop.wlt.common.enums.AppUserStatusEnum;
import com.emop.wlt.user.entity.User;
import com.emop.wlt.user.info.api.UserInfoProvider;
import com.emop.wlt.user.info.dto.UserInfoLoginReqDTO;
import com.emop.wlt.user.info.dto.UserInfoLoginRespDTO;
import com.emop.wlt.user.management.exception.ExceptionCast;
import com.emop.wlt.user.management.model.AccountInfoDTO;
import com.emop.wlt.user.management.model.LoginRequestDTO;
import com.emop.wlt.user.management.service.MappingIndexManagementService;
import com.emop.wlt.user.management.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.emop.wlt.common.constant.CommonConstant.FALSE;
import static com.emop.wlt.common.constant.CommonConstant.TRUE;

@Service
@Slf4j
public class LoginManager {

    @Autowired
    private MappingIndexManagementService mappingIndexManagementService;

    @Autowired
    private UserService userService;

    @DubboReference
    private UserInfoProvider userInfoProvider;

    public AccountInfoDTO loginPreCheck(String mobileNumber) {
        //判断是否注册
        String userId = mappingIndexManagementService.selectByPhone(mobileNumber);
        if (StringUtils.isBlank(userId)) {
            ExceptionCast.cast(BaseErrorEnum.B12301);
        }

        User user = userService.selectByPrimaryKey(userId);
        //账号下钱包是否为挂失状态
        lostStatusCheck(user);

        //查询是否设置登录密码
        String isSetLoginPwd = isSetLoginPassword(user) ? TRUE : FALSE;

        AccountInfoDTO accountInfoDTO = new AccountInfoDTO();
        accountInfoDTO.setUserId(userId);
        accountInfoDTO.setIsSetLoginPwd(isSetLoginPwd);
        return accountInfoDTO;
    }

    private boolean isSetLoginPassword(User user) {
        if (Objects.isNull(user)) {
            return false;
        }
        return !StringUtils.isBlank(user.getPwd());
    }

    private void lostStatusCheck(User user) {
        if (Objects.isNull(user)) {
            user = new User();
            user.setStatus(AppUserStatusEnum.NORMAL.getValue());
        }

        //挂失状态
        if (AppUserStatusEnum.LOST.getValue().equals(user.getStatus())) {
            ExceptionCast.cast(BaseErrorEnum.B12302);
        }
    }

    public UserInfoLoginRespDTO loginLogicV2(LoginRequestDTO loginRequestDTO) {
        String userId = mappingIndexManagementService.selectByPhone(loginRequestDTO.getMobileNumber());
        if (StringUtils.isBlank(userId)) {
            ExceptionCast.cast(BaseErrorEnum.B12301);
        }

        UserInfoLoginReqDTO userInfoLoginReqDTO = new UserInfoLoginReqDTO();
        userInfoLoginReqDTO.setUserId(userId);
        userInfoLoginReqDTO.setMobileNumber(loginRequestDTO.getMobileNumber());
        userInfoLoginReqDTO.setDeviceType(loginRequestDTO.getDeviceType());
        userInfoLoginReqDTO.setDeviceId(loginRequestDTO.getDeviceId());
        userInfoLoginReqDTO.setDeviceName(loginRequestDTO.getDeviceName());
        return userInfoProvider.loginV2(userInfoLoginReqDTO);
    }

}

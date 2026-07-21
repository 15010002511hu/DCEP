package com.emop.wlt.user.management.mapi.business.impl;

import com.emop.wlt.aspect.annotation.AppResponse;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.user.entity.User;
import com.emop.wlt.user.management.mapi.business.UserInfoQueryProvider;
import com.emop.wlt.user.management.model.request.Mapp20800101Req;
import com.emop.wlt.user.management.model.response.Mapp20800101Resp;
import com.emop.wlt.user.management.service.MappingIndexManagementService;
import com.emop.wlt.user.management.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserInfoQueryProviderImpl implements UserInfoQueryProvider {
    @Autowired
    private MappingIndexManagementService mappingIndexManagementService;
    @Autowired
    private UserService userService;

    @AppResponse
    @Override
    public ResponseModel<Mapp20800101Resp> queryUserInfo(RequestModel<Mapp20800101Req> request) {
        //判断是否注册
        String userId = mappingIndexManagementService.selectByPhone(request.getMessageBody().getMobileNumber());
        if (StringUtils.isBlank(userId)) {
            return buildResponse(false);
        }
        User user = userService.selectByPrimaryKey(userId);
        if (user == null) {
            return buildResponse(false);
        }
        if (StringUtils.isNotEmpty(user.getPwd())) {
            return buildResponse(true);
        }
        return buildResponse(false);
    }

    private ResponseModel<Mapp20800101Resp> buildResponse(Boolean setPasswordFlag) {
        Mapp20800101Resp mapp20800101Resp = new Mapp20800101Resp();
        mapp20800101Resp.setSetPasswordFlag(setPasswordFlag);
        return ResponseModel.<Mapp20800101Resp>builder().messageBody(mapp20800101Resp).build();
    }
}

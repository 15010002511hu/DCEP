package com.emop.wlt.user.management.mapi.business.impl;

import com.alipay.gateway.mobileservice.invoke.MobileRpcHolder;
import com.emop.common.enums.errorcode.BaseErrorEnum;
import com.emop.wlt.aspect.annotation.AppResponse;
import com.emop.wlt.common.model.flow.FlowRespInfo;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.common.util.DeviceTypeUtils;
import com.emop.wlt.flow.annotation.MappFlowJoin;
import com.emop.wlt.flow.context.util.ResetLoginPwdConditionUtils;
import com.emop.wlt.flow.context.util.FlowContextUtils;
import com.emop.wlt.user.entity.User;
import com.emop.wlt.user.info.api.UserInfoProvider;
import com.emop.wlt.user.management.exception.ExceptionCast;
import com.emop.wlt.user.management.manager.MessagePushManager;
import com.emop.wlt.user.management.manager.ResetLoginPwdManager;
import com.emop.wlt.user.management.manager.UserPasswordManager;
import com.emop.wlt.user.management.mapi.business.ResetLoginPwdProvider;
import com.emop.wlt.user.management.model.UserWalletInfoDTO;
import com.emop.wlt.user.management.model.request.Mapp11100101Req;
import com.emop.wlt.user.management.model.request.Mapp11200101Req;
import com.emop.wlt.user.management.model.response.Mapp11100101Resp;
import com.emop.wlt.user.management.model.response.Mapp11200101Resp;
import com.emop.wlt.user.management.provider.CFCAManagerProviderImpl;
import com.emop.wlt.user.management.service.MappingIndexManagementService;
import com.emop.wlt.user.management.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

import static com.emop.wlt.common.constant.HttpHeaderConstant.DID_ANT;
import static com.emop.wlt.common.constant.HttpHeaderConstant.PLATFORM;

@Service
@Slf4j
public class ResetLoginPwdProviderImpl implements ResetLoginPwdProvider {

    @Autowired
    private MappingIndexManagementService mappingIndexManagementService;
    @Autowired
    private ResetLoginPwdManager resetLoginPwdManager;
    @Autowired
    private CFCAManagerProviderImpl cfcaManagerProvider;
    @Autowired
    private UserPasswordManager userPasswordManager;
    @Autowired
    private UserService userService;
    @Autowired
    private MessagePushManager messagePushManager;
    @DubboReference
    private UserInfoProvider userInfoProvider;


    @Override
    @MappFlowJoin(nodeCode ="MAPP111",groupNodeCode = "BNC111")
    @AppResponse
    public ResponseModel<Mapp11100101Resp> resetLoginPwdValidation(RequestModel<Mapp11100101Req> request) {
        String mobileNumber = request.getMessageBody().getMobileNumber();
        //判断是否注册
        String userId = mappingIndexManagementService.selectByPhone(mobileNumber);
        if (StringUtils.isBlank(userId)) {
            ExceptionCast.cast(BaseErrorEnum.B12301);
        }
        //判断是否设置密码
        User user = userService.selectByPrimaryKey(userId);
        if (Objects.isNull(user) || StringUtils.isBlank(user.getPwd())) {
            ExceptionCast.buzCast(BaseErrorEnum.B06130);
        }

        //检查账号下钱包等级
        UserWalletInfoDTO userWalletInfoDTO = resetLoginPwdManager.getUserHighestWalletInfo(userId);
        log.info("userWalletInfoDTO:{}",userWalletInfoDTO);
        //根据钱包等级确认验证方式
        FlowContextUtils.saveUserId(userId);
        FlowContextUtils.saveMobileNumber(mobileNumber);
        if (StringUtils.isNotEmpty(userWalletInfoDTO.getWalletId())) {
            FlowContextUtils.saveWalletId(userWalletInfoDTO.getWalletId());
        }
        if (StringUtils.isNotEmpty(userWalletInfoDTO.getIdType())) {
            FlowContextUtils.saveIdType(userWalletInfoDTO.getIdType());
        }
        Mapp11100101Resp mapp11100101Resp = new Mapp11100101Resp();
        FlowRespInfo flowRespInfo = new FlowRespInfo();
        flowRespInfo.setRouteCondition(ResetLoginPwdConditionUtils.mappingResetLoginPwdCondition(
            userWalletInfoDTO.getWalletTotalStatus(),false));
        mapp11100101Resp.setFlowRespInfo(flowRespInfo);
        return ResponseModel.<Mapp11100101Resp>builder().messageBody(mapp11100101Resp).build();
    }

    @Override
    @MappFlowJoin(nodeCode ="MAPP112",groupNodeCode = "BNC112")
    @AppResponse
    public ResponseModel<Mapp11200101Resp> resetLoginPwd(RequestModel<Mapp11200101Req> request) {
        String userId = FlowContextUtils.getUserId();
        String mobileNumber = FlowContextUtils.getMobileNumber();

        //解密密码
        String pwd = cfcaManagerProvider.decryptPassWord(request.getMessageBody().getPwdEnc());

        //重置密码
        userPasswordManager.updateUserPwd(userId, pwd);

        //踢出其他设备登录
        Map<String, String> headers = MobileRpcHolder.getHeaders();
        String token = userInfoProvider.forceKickOutFromOtherDevice(userId, mobileNumber,
            headers.get(DID_ANT), DeviceTypeUtils.getDeviceType(headers.get(PLATFORM)));
        log.info("token:{}", token);

        //有其他设备登录态时发送重置登录密码踢出提醒
        if (StringUtils.isNotBlank(token)) {
            messagePushManager.sendResetLoginPwdLogoutMessage(request.getMessageHeader().getBusinessMessageIdentifier(),
                userId, token, request.getMessageBody().getDeviceName());
        }

        //发送重置登录密码消息推送
        messagePushManager.sendResetLoginPwdMessage(request.getMessageHeader().getBusinessMessageIdentifier(),
            userId, mobileNumber);

        return ResponseModel.<Mapp11200101Resp>builder().messageBody(new Mapp11200101Resp()).build();
    }

}

package com.emop.wlt.user.management.mapi.business.impl;

import static com.emop.wlt.common.constant.HttpHeaderConstant.DID_ANT;
import static com.emop.wlt.common.constant.HttpHeaderConstant.PLATFORM;

import com.alipay.gateway.mobileservice.invoke.MobileRpcHolder;
import com.emop.wlt.aspect.annotation.AppResponse;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.common.model.flow.FlowRespInfo;
import com.emop.wlt.common.util.DeviceTypeUtils;
import com.emop.wlt.flow.annotation.MappFlowJoin;
import com.emop.wlt.flow.context.util.FlowContextUtils;
import com.emop.wlt.flow.context.util.LoginConditionUtils;
import com.emop.wlt.user.info.dto.UserInfoLoginRespDTO;
import com.emop.wlt.user.management.manager.LoginManager;
import com.emop.wlt.user.management.mapi.business.LoginProvider;
import com.emop.wlt.user.management.model.AccountInfoDTO;
import com.emop.wlt.user.management.model.LoginRequestDTO;
import com.emop.wlt.user.management.model.request.Mapp10200101Req;
import com.emop.wlt.user.management.model.request.Mapp10300101Req;
import com.emop.wlt.user.management.model.response.Mapp10200101Resp;
import com.emop.wlt.user.management.model.response.Mapp10300101Resp;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoginProviderImpl implements LoginProvider {

    @Autowired
    private LoginManager loginManager;

    @Override
    @MappFlowJoin(nodeCode ="MAPP102",groupNodeCode = "BNC102")
    @AppResponse
    public ResponseModel<Mapp10200101Resp> loginValidation(RequestModel<Mapp10200101Req> request) {
        AccountInfoDTO accountInfoDTO = loginManager.loginPreCheck(request.getMessageBody().getMobileNumber());
        String supportPwd = accountInfoDTO.getIsSetLoginPwd();
        String supportIFAA = request.getMessageBody().getIfaaOpenFlag();
        FlowContextUtils.saveMobileNumber(request.getMessageBody().getMobileNumber());
        FlowContextUtils.saveUserId(accountInfoDTO.getUserId());
        Mapp10200101Resp mapp10200101Resp = new Mapp10200101Resp();
        FlowRespInfo flowRespInfo = new FlowRespInfo();
        flowRespInfo.setRouteCondition(LoginConditionUtils.mappingLoginCondition(supportIFAA, supportPwd));
        mapp10200101Resp.setFlowRespInfo(flowRespInfo);
        mapp10200101Resp.setSetPasswordFlag(supportPwd);
        return ResponseModel.<Mapp10200101Resp>builder().messageBody(mapp10200101Resp).build();
    }

    @Override
    @MappFlowJoin(nodeCode ="MAPP103",groupNodeCode = "BNC103")
    @AppResponse
    public ResponseModel<Mapp10300101Resp> login(RequestModel<Mapp10300101Req> request) {
        Map<String, String> headers = MobileRpcHolder.getHeaders();

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setMobileNumber(FlowContextUtils.getMobileNumber());
        loginRequestDTO.setDeviceType(DeviceTypeUtils.getDeviceType(headers.get(PLATFORM)));
        loginRequestDTO.setDeviceId(headers.get(DID_ANT));
        loginRequestDTO.setDeviceName(request.getMessageBody().getDeviceName());

        UserInfoLoginRespDTO userInfoLoginRespDTO = loginManager.loginLogicV2(loginRequestDTO);

        Mapp10300101Resp mapp10300101Resp = new Mapp10300101Resp();
        mapp10300101Resp.setToken(userInfoLoginRespDTO.getToken());
        mapp10300101Resp.setExpireSeconds(String.valueOf(userInfoLoginRespDTO.getExpireSeconds()));
        mapp10300101Resp.setRefreshIntervalSeconds(String.valueOf(userInfoLoginRespDTO.getRefreshIntervalSeconds()));
        mapp10300101Resp.setUserId(userInfoLoginRespDTO.getUserId());
        mapp10300101Resp.setMobileNumber(loginRequestDTO.getMobileNumber());
        return ResponseModel.<Mapp10300101Resp>builder().messageBody(mapp10300101Resp).build();
    }
}

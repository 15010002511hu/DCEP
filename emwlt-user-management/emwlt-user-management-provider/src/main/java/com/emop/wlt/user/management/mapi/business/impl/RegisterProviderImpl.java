package com.emop.wlt.user.management.mapi.business.impl;


import com.alipay.gateway.mobileservice.invoke.MobileRpcHolder;
import com.emop.common.enums.errorcode.BaseErrorEnum;
import com.emop.wlt.aspect.annotation.AppResponse;
import com.emop.wlt.common.enums.CountryAndRegionCodeEnum;
import com.emop.wlt.common.enums.UserTypeEnum;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.common.util.DeviceTypeUtils;
import com.emop.wlt.flow.annotation.MappFlowJoin;
import com.emop.wlt.flow.context.util.RegisterContextUtils;
import com.emop.wlt.id.db.service.EmwltDBIdGenerator;
import com.emop.wlt.user.info.dto.TokenInfoDTO;
import com.emop.wlt.user.management.exception.ExceptionCast;
import com.emop.wlt.user.management.manager.RegisterManager;
import com.emop.wlt.user.management.mapi.business.RegisterProvider;
import com.emop.wlt.user.management.model.RegisterDTO;
import com.emop.wlt.user.management.model.request.Mapp10000101Req;
import com.emop.wlt.user.management.model.request.Mapp10100101Req;
import com.emop.wlt.user.management.model.response.Mapp10000101Resp;
import com.emop.wlt.user.management.model.response.Mapp10100101Resp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.emop.wlt.common.constant.HttpHeaderConstant.*;

@Service
@Slf4j
public class RegisterProviderImpl implements RegisterProvider {

    @Autowired
    private RegisterManager registerManager;

    @Autowired
    private EmwltDBIdGenerator dbIdGenerator;

    @Override
    @MappFlowJoin(nodeCode ="MAPP100",groupNodeCode = "BNC100")
    @AppResponse
    public ResponseModel<Mapp10000101Resp> registerValidation(RequestModel<Mapp10000101Req> request) {
        registerManager.registerPreCheck(request.getMessageBody().getMobileNumber(),
            request.getMessageBody().getGeolocation());

        RegisterContextUtils.saveMobileNumber(request.getMessageBody().getMobileNumber());
        RegisterContextUtils.saveCountryRegionCode(request.getMessageBody().getCountryAndRegionCode());
        CountryAndRegionCodeEnum countryAndRegionCodeEnum = CountryAndRegionCodeEnum.getEnumByLetterCode(request.getMessageBody().getCountryAndRegionCode());
        if (countryAndRegionCodeEnum == null) {
            ExceptionCast.cast(BaseErrorEnum.S02021);
        }
        if (!request.getMessageBody().getMobileNumber().contains(countryAndRegionCodeEnum.getInternationalCode())) {
            log.info("国际区号:{}",countryAndRegionCodeEnum.getInternationalCode());
            ExceptionCast.cast(BaseErrorEnum.B12106);
        }

        return ResponseModel.<Mapp10000101Resp>builder().messageBody(new Mapp10000101Resp()).build();
    }

    @Override
    @MappFlowJoin(nodeCode ="MAPP101",groupNodeCode = "BNC101")
    @AppResponse
    public ResponseModel<Mapp10100101Resp> registerAndLogin(RequestModel<Mapp10100101Req> request) {
        String countryAndRegionCode = RegisterContextUtils.getCountryRegionCode();
        CountryAndRegionCodeEnum countryAndRegionCodeEnum = CountryAndRegionCodeEnum.getEnumByLetterCode(countryAndRegionCode);
        if (countryAndRegionCodeEnum == null) {
            countryAndRegionCodeEnum =  CountryAndRegionCodeEnum.CHINA;
        }
        log.info("countryAndRegionCode:{}",countryAndRegionCodeEnum.getLetterCode());
        String userId = dbIdGenerator.getUserId(countryAndRegionCodeEnum, UserTypeEnum.PERSONAL);
        log.info("userId:{}",userId);
        Map<String, String> headers = MobileRpcHolder.getHeaders();

        RegisterDTO registerDTO = RegisterDTO.builder()
            .userId(userId)
            .mobileNumber(RegisterContextUtils.getMobileNumber())
            .deviceId(headers.get(DID_ANT))
            .deviceType(DeviceTypeUtils.getDeviceType(headers.get(PLATFORM)))
            .countryAndRegionCode(countryAndRegionCodeEnum.getLetterCode())
            .innerVersion(headers.get(APP_INNER_VERSION)).build();

        TokenInfoDTO tokenInfoDTO = new TokenInfoDTO();
        try {
            tokenInfoDTO = registerManager.register(registerDTO);
        } catch (Exception e) {
            log.error("生成token失败",e);
            ExceptionCast.cast(BaseErrorEnum.S05003);
        }

        Mapp10100101Resp mapp00100101Resp = new Mapp10100101Resp();
        mapp00100101Resp.setToken(tokenInfoDTO.getToken());
        mapp00100101Resp.setExpireSeconds(String.valueOf(tokenInfoDTO.getExpireSeconds()));
        mapp00100101Resp.setRefreshIntervalSeconds(String.valueOf(tokenInfoDTO.getRefreshIntervalSeconds()));
        mapp00100101Resp.setUserId(userId);
        mapp00100101Resp.setMobileNumber(RegisterContextUtils.getMobileNumber());
        return ResponseModel.<Mapp10100101Resp>builder().messageBody(mapp00100101Resp).build();
    }
}

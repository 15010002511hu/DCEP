package com.emop.wlt.user.management.mapi.component.impl;

import com.emop.common.enums.errorcode.BaseErrorEnum;
import com.emop.wlt.aspect.annotation.AppResponse;
import com.emop.wlt.aspect.app.util.HeaderUtils;
import com.emop.wlt.captcha.model.SendSmsDTO;
import com.emop.wlt.captcha.provider.CaptchaOperator;
import com.emop.wlt.common.constant.CommonConstant;
import com.emop.wlt.common.enums.LanguageTypeEnum;
import com.emop.wlt.common.model.mapp.EnvironmentInformation;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.flow.annotation.MappFlowJoin;
import com.emop.wlt.flow.config.NodeConfigParser;
import com.emop.wlt.flow.context.util.FlowContextUtils;
import com.emop.wlt.user.management.exception.ExceptionCast;
import com.emop.wlt.user.management.mapi.component.SmsSendProvider;
import com.emop.wlt.user.management.model.request.Mapp02100101Req;
import com.emop.wlt.user.management.model.response.Mapp02100101Resp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsSendProviderImpl implements SmsSendProvider {
    @Autowired
    private CaptchaOperator captchaOperator;

    @Override
    @MappFlowJoin(nodeCode = "MAPP021", groupNodeCode = "VNC001")
    @AppResponse
    public ResponseModel<Mapp02100101Resp> sendSms(RequestModel<Mapp02100101Req> request) {
        NodeConfigParser nodeConfigParser = new NodeConfigParser(FlowContextUtils.getConfigParam());
        String mobileNumber = nodeConfigParser.getCtxMobileNumber() ?
            FlowContextUtils.getMobileNumber() : request.getMessageBody().getMobileNumber();

        EnvironmentInformation environmentInformation = HeaderUtils.getEnvInfo();
        String language = StringUtils.isEmpty(environmentInformation.getLanguage()) ?
                LanguageTypeEnum.ZHHK.getValue() : environmentInformation.getLanguage();
        log.info("环境信息:{}",environmentInformation);
        log.info("发送短信手机号:{}", mobileNumber);
        SendSmsDTO sendSmsDTO = SendSmsDTO.builder()
                .businessId(mobileNumber)
                .contact(mobileNumber)
                .svcTp(getSvcType())
                .insNo(CommonConstant.INSTGDRCTPTY)
                .language(language)
                .build();
        captchaOperator.sendVerifyCode(sendSmsDTO);

        return ResponseModel.<Mapp02100101Resp>builder().messageBody(new Mapp02100101Resp()).build();
    }
    /**
     * 获取短信参数
     */
    private String getSvcType() {
        //读取配置，配置为空，返回null
        String configParam = FlowContextUtils.getConfigParam();
        if (StringUtils.isEmpty(configParam)) {
            ExceptionCast.cast(BaseErrorEnum.B06207);
        }
        NodeConfigParser nodeConfigParser = new NodeConfigParser(configParam);
        log.info("当前场景的svcType:{}",nodeConfigParser.getSvcType());
        if (StringUtils.isEmpty(nodeConfigParser.getSvcType())) {
            ExceptionCast.cast(BaseErrorEnum.B06207);
        }
        return nodeConfigParser.getSvcType();
    }
}

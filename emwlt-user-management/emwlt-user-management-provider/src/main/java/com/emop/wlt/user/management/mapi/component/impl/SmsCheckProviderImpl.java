package com.emop.wlt.user.management.mapi.component.impl;

import com.alibaba.fastjson.JSONObject;
import com.emop.common.enums.errorcode.BaseErrorEnum;
import com.emop.wlt.aspect.annotation.AppResponse;
import com.emop.wlt.captcha.model.CheckVerifyCodeDTO;
import com.emop.wlt.captcha.provider.CaptchaOperator;
import com.emop.wlt.common.constant.CommonConstant;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.flow.annotation.MappFlowJoin;
import com.emop.wlt.flow.config.NodeConfigParser;
import com.emop.wlt.flow.context.util.FlowContextUtils;
import com.emop.wlt.flow.exception.ExceptionCast;
import com.emop.wlt.ifaa.constant.IFAAConstant;
import com.emop.wlt.ifaa.manager.IFAAServiceLdcManager;
import com.emop.wlt.ifaa.model.BioAuthReqInfoDTO;
import com.emop.wlt.ifaa.vo.CheckIfaaResultDTO;
import com.emop.wlt.user.management.mapi.component.SmsCheckProvider;
import com.emop.wlt.user.management.model.request.Mapp02200101Req;
import com.emop.wlt.user.management.model.response.Mapp02200101Resp;
import com.emop.wlt.user.management.model.vo.BioAuthReqInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class SmsCheckProviderImpl implements SmsCheckProvider {

    @Autowired
    private CaptchaOperator captchaOperator;

    @Autowired
    private IFAAServiceLdcManager IFAAServiceLdcManager;

    @Override
    @MappFlowJoin(nodeCode ="MAPP022",groupNodeCode = "VNC001")
    @AppResponse
    public ResponseModel<Mapp02200101Resp> checkSms(RequestModel<Mapp02200101Req> request) {
        String mobileNumber = FlowContextUtils.getMobileNumber();
        log.info("验证短信手机号:{}",mobileNumber);
        String verifyCode = request.getMessageBody().getVerifyCode();
        CheckVerifyCodeDTO sendVerifyCodeDTO = CheckVerifyCodeDTO.builder()
            .businessId(mobileNumber)
            .contact(mobileNumber)
            .svcTp(getSvcType())
            .verifyCode(verifyCode)
            .insNo(CommonConstant.INSTGDRCTPTY)
            .build();
        captchaOperator.checkVerifyCode(sendVerifyCodeDTO);
        //处理ifaa扩展点
        String ifaaResult = updateIfaa(request.getMessageBody().getBioAuthReqInfo(), FlowContextUtils.getUserId());
        Mapp02200101Resp mapp02200101Resp = new Mapp02200101Resp();
        if (StringUtils.isNotEmpty(ifaaResult)) {
            mapp02200101Resp.setAuthRespMessage(ifaaResult);
        }
        return ResponseModel.<Mapp02200101Resp>builder().messageBody(mapp02200101Resp).build();
    }

    /**
     * 支持ifaa更新能力
     */
    private String updateIfaa (BioAuthReqInfoVO bioAuthReqInfoVO,String userId) {
        try {
            //读取配置，配置为空，返回null
            String configParam = FlowContextUtils.getConfigParam();
            if (StringUtils.isEmpty(configParam)) {
                return null;
            }
            NodeConfigParser nodeConfigParser = new NodeConfigParser(configParam);
            if(nodeConfigParser.getSupportIfaa() == null || !nodeConfigParser.getSupportIfaa()) {
                return null;
            }
            //如果配置了ifaa开关，则做更新
            if (Objects.isNull(bioAuthReqInfoVO)) {
                return null;
            }
            String authActionType = bioAuthReqInfoVO.getAuthActionType();
            String authMsgReq = bioAuthReqInfoVO.getAuthReqMessage();
            JSONObject jsonObject = JSONObject.parseObject(authMsgReq);
            if (Objects.isNull(jsonObject)) {
                return null;
            }
            String actionTypeInAuthMagReq = jsonObject.getString(IFAAConstant.ACTION);
            //校验authActionType与authMsgReq中的业务类型是否一致
            if (Objects.isNull(actionTypeInAuthMagReq) || !Objects.equals(actionTypeInAuthMagReq,
                    IFAAConstant.ifaaBUsinessMap.get(authActionType))) {
                ExceptionCast.cast(BaseErrorEnum.S02021);
            }
            BioAuthReqInfoDTO bioAuthReqInfoDTO = new BioAuthReqInfoDTO();
            bioAuthReqInfoDTO.setAuthDeviceId(bioAuthReqInfoVO.getAuthDeviceId());
            bioAuthReqInfoDTO.setAuthReqMessage(bioAuthReqInfoVO.getAuthReqMessage());
            bioAuthReqInfoDTO.setAuthActionType(bioAuthReqInfoVO.getAuthActionType());
            CheckIfaaResultDTO checkIfaaResultDTO = IFAAServiceLdcManager.sendIFAADubboRequest(bioAuthReqInfoDTO, userId);
            return checkIfaaResultDTO.getIfaaResult();
        } catch (Exception e) {
            log.error("更新IFAA失败",e);
            return null;
        }
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

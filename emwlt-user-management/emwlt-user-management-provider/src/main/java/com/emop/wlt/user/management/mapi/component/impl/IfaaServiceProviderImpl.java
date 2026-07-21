package com.emop.wlt.user.management.mapi.component.impl;

import com.alibaba.fastjson.JSONObject;
import com.emop.common.enums.errorcode.BaseErrorEnum;
import com.emop.wlt.aspect.annotation.AppResponse;
import com.emop.wlt.aspect.app.util.HeaderUtils;
import com.emop.wlt.captcha.model.SendSmsDTO;
import com.emop.wlt.captcha.service.SMSService;
import com.emop.wlt.captcha.util.SMSUtils;
import com.emop.wlt.common.constant.CommonConstant;
import com.emop.wlt.common.enums.CaptchaTypeEnum;
import com.emop.wlt.common.enums.LanguageTypeEnum;
import com.emop.wlt.common.model.mapp.EnvironmentInformation;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.common.util.UserUtils;
import com.emop.wlt.flow.annotation.MappFlowJoin;
import com.emop.wlt.flow.config.NodeConfigParser;
import com.emop.wlt.flow.context.util.FlowContextUtils;
import com.emop.wlt.flow.exception.ExceptionCast;
import com.emop.wlt.ifaa.constant.IFAAConstant;
import com.emop.wlt.ifaa.manager.IFAAServiceLdcManager;
import com.emop.wlt.ifaa.model.BioAuthReqInfoDTO;
import com.emop.wlt.ifaa.vo.CheckIfaaResultDTO;
import com.emop.wlt.user.management.mapi.component.IfaaServcieProvider;
import com.emop.wlt.user.management.model.request.Mapp03200101Req;
import com.emop.wlt.user.management.model.request.Mapp03400101Req;
import com.emop.wlt.user.management.model.response.Mapp03200101Resp;
import com.emop.wlt.user.management.model.response.Mapp03400101Resp;
import com.emop.wlt.user.management.model.vo.BioAuthReqInfoVO;
import com.emop.wlt.user.management.service.MappingIndexManagementService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class IfaaServiceProviderImpl implements IfaaServcieProvider {

    @Autowired
    IFAAServiceLdcManager IFAAServiceLdcManager;
    @Autowired
    private MappingIndexManagementService mappingIndexManagementService;
    @Autowired
    private SMSService smsService;

    @MappFlowJoin(nodeCode = "MAPP032", groupNodeCode = "VNC008")
    @AppResponse
    @Override
    public ResponseModel<Mapp03200101Resp> doBioAuth(RequestModel<Mapp03200101Req> request) {

        BioAuthReqInfoVO bioAuthReqInfoVO = request.getMessageBody().getBioAuthReqInfo();
        String authActionType = bioAuthReqInfoVO.getAuthActionType();
        String authMsgReq = bioAuthReqInfoVO.getAuthReqMessage();
        String userId = mappingIndexManagementService.selectByPhone(request.getMessageBody().getMobileNumber());

        //参数检查
        //读取配置，配置为空，返回null
        String configParam = FlowContextUtils.getConfigParam();
        if (StringUtils.isEmpty(configParam)) {
            log.info("IFAA流程配置参数有问题:{}",configParam);
            ExceptionCast.cast(BaseErrorEnum.B09901);
        }
        NodeConfigParser nodeConfigParser = new NodeConfigParser(configParam);
        if(StringUtils.isEmpty(nodeConfigParser.getAuthActionType()) ||
                !nodeConfigParser.getAuthActionType().equals(authActionType)) {
            log.info("IFAA流程配置参数和请求参数不一致:{}",configParam);
            ExceptionCast.cast(BaseErrorEnum.S02021);
        }

        JSONObject jsonObject = JSONObject.parseObject(authMsgReq);
        if (Objects.isNull(jsonObject)) {
            ExceptionCast.cast(BaseErrorEnum.S02021);
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

        //触发短信判断
        sendSmsWithIfaaResult(bioAuthReqInfoDTO, UserUtils.getUserPhone(),checkIfaaResultDTO);

        Mapp03200101Resp mapp03200101Resp = new Mapp03200101Resp();
        mapp03200101Resp.setAuthRespMessage(checkIfaaResultDTO.getIfaaResult());
        return ResponseModel.<Mapp03200101Resp>builder().messageBody(mapp03200101Resp).build();
    }

    /**
     * 当生物识别开通或关闭，成功后，发送短信通知
     */
    private void sendSmsWithIfaaResult(BioAuthReqInfoDTO bioAuthReqInfoDTO, String mobileNumber, CheckIfaaResultDTO checkIfaaResultDTO) {
        try {
            //不是成功结果直接返回
            if (!IFAAConstant.IFAA_RES_SUCCESS.equals(checkIfaaResultDTO.getIfaaRespCode())) {
                return;
            }
            //不是开通或关闭生物识别，直接返回
            if (!(IFAAConstant.REGISTER.equals(bioAuthReqInfoDTO.getAuthActionType()) ||
                    IFAAConstant.UNREGISTER.equals(bioAuthReqInfoDTO.getAuthActionType()))) {
                return;
            }
            String svcTp = switch (bioAuthReqInfoDTO.getAuthActionType()) {
                case IFAAConstant.REGISTER -> CaptchaTypeEnum.ST08.toString();
                case IFAAConstant.UNREGISTER -> CaptchaTypeEnum.ST09.toString();
                default -> "";
            };
            EnvironmentInformation environmentInformation = HeaderUtils.getEnvInfo();
            String language = StringUtils.isEmpty(environmentInformation.getLanguage()) ? LanguageTypeEnum.ZHHK.getValue() : environmentInformation.getLanguage();
            SendSmsDTO sendSmsDTO = SendSmsDTO.builder()
                    .businessId(mobileNumber)
                    .contact(mobileNumber)
                    .svcTp(svcTp)
                    .insNo(CommonConstant.INSTGDRCTPTY)
                    .language(language)
                    .build();
            log.info("生物识别开通或关闭，发送短信通知:{}",sendSmsDTO);
            SMSUtils.checkSendParam(sendSmsDTO);
            smsService.sendSMS(sendSmsDTO);
        } catch (Exception e) {
            log.error("生物识别开通或关闭，发送短信失败", e);
        }
    }

    @AppResponse
    @Override
    public ResponseModel<Mapp03400101Resp> doBioQuery(RequestModel<Mapp03400101Req> request) {

        BioAuthReqInfoVO bioAuthReqInfoVO = request.getMessageBody().getBioAuthReqInfo();
        String authActionType = bioAuthReqInfoVO.getAuthActionType();
        String authMsgReq = bioAuthReqInfoVO.getAuthReqMessage();
        String userId = mappingIndexManagementService.selectByPhone(request.getMessageBody().getMobileNumber());

        //如果不是查询请求AU05,直接拒绝
        if(StringUtils.isEmpty(authActionType) || !StringUtils.equals(authActionType,IFAAConstant.CAP)) {
            ExceptionCast.cast(BaseErrorEnum.S02021);
        }
        JSONObject jsonObject = JSONObject.parseObject(authMsgReq);
        if (Objects.isNull(jsonObject)) {
            ExceptionCast.cast(BaseErrorEnum.S02021);
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

        Mapp03400101Resp mapp03400101Resp = new Mapp03400101Resp();
        mapp03400101Resp.setAuthRespMessage(checkIfaaResultDTO.getIfaaResult());
        return ResponseModel.<Mapp03400101Resp>builder().messageBody(mapp03400101Resp).build();
    }
}

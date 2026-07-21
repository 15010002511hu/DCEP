package com.emop.wlt.user.management.mapi.component.impl;

import com.alibaba.fastjson.JSONObject;
import com.emop.common.enums.errorcode.BaseErrorEnum;
import com.emop.wlt.aspect.annotation.AppResponse;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.flow.annotation.MappFlowJoin;
import com.emop.wlt.flow.config.NodeConfigParser;
import com.emop.wlt.flow.context.util.FlowContextUtils;
import com.emop.wlt.ifaa.constant.IFAAConstant;
import com.emop.wlt.ifaa.manager.IFAAServiceLdcManager;
import com.emop.wlt.ifaa.model.BioAuthReqInfoDTO;
import com.emop.wlt.ifaa.vo.CheckIfaaResultDTO;
import com.emop.wlt.user.entity.User;
import com.emop.wlt.user.info.api.LoginPwdProvider;
import com.emop.wlt.user.info.dto.VerifyPwdDTO;
import com.emop.wlt.user.management.exception.ExceptionCast;
import com.emop.wlt.user.management.manager.UserManager;
import com.emop.wlt.user.management.mapi.component.LoginPwdVerifyProvider;
import com.emop.wlt.user.management.model.request.Mapp03000101Req;
import com.emop.wlt.user.management.model.response.Mapp03000101Resp;
import com.emop.wlt.user.management.model.vo.BioAuthReqInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class LoginPwdVerifyProviderImpl implements LoginPwdVerifyProvider {

    @Autowired
    private UserManager userManager;
    @DubboReference
    private LoginPwdProvider loginPwdProvider;
    @Autowired
    private IFAAServiceLdcManager IFAAServiceLdcManager;

    @Override
    @MappFlowJoin(nodeCode ="MAPP030",groupNodeCode = "VNC005")
    @AppResponse
    public ResponseModel<Mapp03000101Resp> LoginPasswordVerify(RequestModel<Mapp03000101Req> request) {
        NodeConfigParser nodeConfigParser = new NodeConfigParser(FlowContextUtils.getConfigParam());
        String mobileNumber = nodeConfigParser.getCtxMobileNumber() ?
                FlowContextUtils.getMobileNumber() : request.getMessageBody().getMobileNumber();

        User user = userManager.queryUserByPhone(mobileNumber);
        if (Objects.isNull(user)) {
            ExceptionCast.cast(BaseErrorEnum.B12301);
        }
        VerifyPwdDTO verifyPwdDTO = new VerifyPwdDTO();
        verifyPwdDTO.setUserId(user.getUserId());
        verifyPwdDTO.setPhone(user.getPhone());
        verifyPwdDTO.setPwd(user.getPwd());
        verifyPwdDTO.setPwdSalt(user.getPwdSalt());

        loginPwdProvider.loginPwdVerify(verifyPwdDTO, request.getMessageBody().getPwdEnc());

        //处理ifaa扩展点
        String ifaaResult = updateIfaa(request.getMessageBody().getBioAuthReqInfo(), FlowContextUtils.getUserId());
        Mapp03000101Resp mapp03000101Resp = new Mapp03000101Resp();
        if (StringUtils.isNotEmpty(ifaaResult)) {
            mapp03000101Resp.setAuthRespMessage(ifaaResult);
        }
        return ResponseModel.<Mapp03000101Resp>builder().messageBody(mapp03000101Resp).build();
    }

    /**
     * 支持ifaa更新能力
     */
    private String updateIfaa (BioAuthReqInfoVO bioAuthReqInfoVO, String userId) {
        try {
            //读取配置，配置为空，返回null
            String configParam = FlowContextUtils.getConfigParam();
            log.info("当前节点configParam:{}",configParam);
            if (StringUtils.isEmpty(configParam)) {
                return null;
            }
            NodeConfigParser nodeConfigParser = new NodeConfigParser(configParam);
            log.info("SupportIfaa:{}",nodeConfigParser.getSupportIfaa());
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
                com.emop.wlt.flow.exception.ExceptionCast.cast(BaseErrorEnum.S02021);
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
}

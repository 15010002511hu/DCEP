package com.emop.wlt.user.management.mapi.component.impl;

import com.emop.wlt.aspect.annotation.AppResponse;
import com.emop.wlt.common.model.flow.FlowRespInfo;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.flow.annotation.MappFlowJoin;
import com.emop.wlt.flow.config.NodeConfigParser;
import com.emop.wlt.flow.context.util.FlowContextUtils;
import com.emop.wlt.user.management.mapi.component.BankAccountSmsCheckProvider;
import com.emop.wlt.user.management.model.request.Mapp02800101Req;
import com.emop.wlt.user.management.model.response.Mapp02800101Resp;
import com.emop.wlt.user.wallet.api.RealNameInfoProvider;
import com.emop.wlt.user.wallet.api.SmsProvider;
import com.emop.wlt.user.wallet.dto.Emap304ReqParam;
import com.emop.wlt.user.wallet.dto.Emap304RespParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BankAccountSmsCheckProviderImpl implements BankAccountSmsCheckProvider {

    @DubboReference
    private SmsProvider smsProvider;

    @DubboReference
    private RealNameInfoProvider realNameInfoProvider;

    @Override
    @MappFlowJoin(nodeCode = "MAPP028", groupNodeCode = "VNC019")
    @AppResponse
    public ResponseModel<Mapp02800101Resp> bankAccountCheckSms(RequestModel<Mapp02800101Req> request) {
        NodeConfigParser nodeConfigParser = new NodeConfigParser(FlowContextUtils.getConfigParam());
        Mapp02800101Req mapp02800101Req = request.getMessageBody();

        String instContext = nodeConfigParser.getCtxInstContext() ?
            FlowContextUtils.getInstContext() : StringUtils.EMPTY;
        String walletId = nodeConfigParser.getCtxWltId() ?
            FlowContextUtils.getWalletId() : mapp02800101Req.getWalletId();
        String userId = FlowContextUtils.getUserId();

        Emap304ReqParam emap304ReqParam = Emap304ReqParam.builder()
                .userId(userId)
                .verifySceneType(mapp02800101Req.getFlowReqInfo().getVerifySceneType())
                .walletId(walletId)
                .verifyCode(request.getMessageBody().getVerifyCode())
                .contextNo(instContext).build();

        Emap304RespParam emap304RespParam = smsProvider.checkBankAccountVerifyCode(emap304ReqParam);

        //钱包解挂采用钱包升级验证方式时升级成功后将实名信息保存到钱包后台
        if (nodeConfigParser.saveRealNameInfo()) {
            try {
                realNameInfoProvider.saveRealNameInfo(userId, walletId);
            } catch (Throwable e) {
                log.error("升级成功后保存实名信息异常", e);
            }
        }

        Mapp02800101Resp mapp02800101Resp = new Mapp02800101Resp();
        mapp02800101Resp.setSigningNumber(emap304RespParam.getSigningNumber());
        mapp02800101Resp.setBankCode(emap304RespParam.getBankCode());
        FlowRespInfo flowRespInfo = new FlowRespInfo();
        flowRespInfo.setFlowVersion(request.getMessageBody().getFlowReqInfo().getFlowVersion());
        flowRespInfo.setVerifySceneType(mapp02800101Req.getFlowReqInfo().getVerifySceneType());
        mapp02800101Resp.setFlowRespInfo(flowRespInfo);

        return ResponseModel.<Mapp02800101Resp>builder().messageBody(mapp02800101Resp).build();
    }
}

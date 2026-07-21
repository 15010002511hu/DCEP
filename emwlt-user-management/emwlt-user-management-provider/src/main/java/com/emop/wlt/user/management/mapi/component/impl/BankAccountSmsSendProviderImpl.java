package com.emop.wlt.user.management.mapi.component.impl;

import com.emop.wlt.aspect.annotation.AppResponse;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.flow.annotation.MappFlowJoin;
import com.emop.wlt.flow.config.NodeConfigParser;
import com.emop.wlt.flow.context.util.FlowContextUtils;

import com.emop.wlt.user.management.mapi.component.BankAccountSmsSendProvider;
import com.emop.wlt.user.management.model.request.Mapp02700101Req;
import com.emop.wlt.user.management.model.response.Mapp02700101Resp;
import com.emop.wlt.user.wallet.api.SmsProvider;
import com.emop.wlt.user.wallet.dto.Emap303ReqParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BankAccountSmsSendProviderImpl implements BankAccountSmsSendProvider {

    @DubboReference
    private SmsProvider smsProvider;

    @Override
    @MappFlowJoin(nodeCode = "MAPP027", groupNodeCode = "VNC009")
    @AppResponse
    public ResponseModel<Mapp02700101Resp> bankAccountSendSms(RequestModel<Mapp02700101Req> request) {
        NodeConfigParser nodeConfigParser = new NodeConfigParser(FlowContextUtils.getConfigParam());
        Mapp02700101Req mapp02700101Req = request.getMessageBody();

        String instContext = nodeConfigParser.getCtxInstContext() ?
            FlowContextUtils.getInstContext() : StringUtils.EMPTY;
        String walletId = nodeConfigParser.getCtxWltId() ?
            FlowContextUtils.getWalletId() : mapp02700101Req.getWalletId();

        Emap303ReqParam emap303ReqParam = Emap303ReqParam.builder()
                .userId(FlowContextUtils.getUserId())
                .verifySceneType(mapp02700101Req.getFlowReqInfo().getVerifySceneType())
                .walletId(walletId)
                .accountNumber(mapp02700101Req.getAccountNumber())
                .bankCode(mapp02700101Req.getBankCode())
                .mobileNumber(mapp02700101Req.getMobileNumber())
                .accountType(mapp02700101Req.getAccountType())
                .contextNo(instContext).build();

        smsProvider.getBankAccountVerifyCode(emap303ReqParam);

        return ResponseModel.<Mapp02700101Resp>builder().messageBody(new Mapp02700101Resp()).build();
    }
}

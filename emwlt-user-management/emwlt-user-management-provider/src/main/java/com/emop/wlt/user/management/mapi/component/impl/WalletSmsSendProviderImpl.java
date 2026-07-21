package com.emop.wlt.user.management.mapi.component.impl;

import com.emop.wlt.aspect.annotation.AppResponse;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.flow.annotation.MappFlowJoin;
import com.emop.wlt.flow.config.NodeConfigParser;
import com.emop.wlt.flow.context.util.FlowContextUtils;
import com.emop.wlt.user.management.mapi.component.WalletSmsSendProvider;
import com.emop.wlt.user.management.model.request.Mapp02300101Req;
import com.emop.wlt.user.management.model.response.Mapp02300101Resp;
import com.emop.wlt.user.wallet.api.SmsProvider;
import com.emop.wlt.user.wallet.dto.Emap301ReqParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WalletSmsSendProviderImpl implements WalletSmsSendProvider {

    @DubboReference
    private SmsProvider smsProvider;

    @Override
    @MappFlowJoin(nodeCode = "MAPP023", groupNodeCode = "VNC003")
    @AppResponse
    public ResponseModel<Mapp02300101Resp> walletSendSms(RequestModel<Mapp02300101Req> request) {
        NodeConfigParser nodeConfigParser = new NodeConfigParser(FlowContextUtils.getConfigParam());
        Mapp02300101Req mapp02300101Req = request.getMessageBody();

        String walletId = nodeConfigParser.getCtxWltId() ?
                FlowContextUtils.getWalletId() : mapp02300101Req.getWalletId();
        String mobileNumber = nodeConfigParser.getCtxMobileNumber() ?
                FlowContextUtils.getMobileNumber() : mapp02300101Req.getMobileNumber();

        Emap301ReqParam emap301ReqParam = Emap301ReqParam.builder()
            .userId(FlowContextUtils.getUserId())
            .verifySceneType(mapp02300101Req.getFlowReqInfo().getVerifySceneType())
            .contextNo(StringUtils.EMPTY)
            .walletId(walletId)
            .mobileNumber(mobileNumber).build();

        String contextNo = smsProvider.getWalletVerifyCode(emap301ReqParam);
        FlowContextUtils.saveInstContext(contextNo);

        return ResponseModel.<Mapp02300101Resp>builder().messageBody(new Mapp02300101Resp()).build();
    }
}

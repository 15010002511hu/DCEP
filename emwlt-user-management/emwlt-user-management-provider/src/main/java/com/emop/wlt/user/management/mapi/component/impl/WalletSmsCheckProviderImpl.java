package com.emop.wlt.user.management.mapi.component.impl;

import com.emop.wlt.aspect.annotation.AppResponse;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.flow.annotation.MappFlowJoin;
import com.emop.wlt.flow.config.NodeConfigParser;
import com.emop.wlt.flow.context.util.FlowContextUtils;
import com.emop.wlt.user.management.mapi.component.WalletSmsCheckProvider;
import com.emop.wlt.user.management.model.request.Mapp02400101Req;
import com.emop.wlt.user.management.model.response.Mapp02400101Resp;
import com.emop.wlt.user.wallet.api.SmsProvider;
import com.emop.wlt.user.wallet.dto.Emap302ReqParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WalletSmsCheckProviderImpl implements WalletSmsCheckProvider {

    @DubboReference
    private SmsProvider smsProvider;

    @Override
    @MappFlowJoin(nodeCode = "MAPP024", groupNodeCode = "VNC003")
    @AppResponse
    public ResponseModel<Mapp02400101Resp> walletCheckSms(RequestModel<Mapp02400101Req> request) {
        NodeConfigParser nodeConfigParser = new NodeConfigParser(FlowContextUtils.getConfigParam());
        Mapp02400101Req mapp02400101Req = request.getMessageBody();

        String verifySceneType = mapp02400101Req.getFlowReqInfo().getVerifySceneType();
        String instContext = FlowContextUtils.getInstContext();
        String walletId = nodeConfigParser.getCtxWltId() ?
                FlowContextUtils.getWalletId() : mapp02400101Req.getWalletId();
        String mobileNumber = nodeConfigParser.getCtxMobileNumber() ?
                FlowContextUtils.getMobileNumber() : mapp02400101Req.getMobileNumber();

        Emap302ReqParam emap302ReqParam = Emap302ReqParam.builder()
            .userId(FlowContextUtils.getUserId())
            .verifySceneType(verifySceneType)
            .verifyCode(mapp02400101Req.getVerifyCode())
            .contextNo(instContext)
            .walletId(walletId)
            .mobileNumber(mobileNumber)
            .build();

        String contextNo = smsProvider.checkWalletVerifyCode(emap302ReqParam);
        FlowContextUtils.saveInstContext(contextNo);

        return ResponseModel.<Mapp02400101Resp>builder().messageBody(new Mapp02400101Resp()).build();
    }
}

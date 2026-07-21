package com.emop.wlt.user.management.mapi.component.impl;

import com.emop.wlt.aspect.annotation.AppResponse;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.flow.annotation.MappFlowJoin;
import com.emop.wlt.flow.config.NodeConfigParser;
import com.emop.wlt.flow.context.util.FlowContextUtils;
import com.emop.wlt.user.management.mapi.component.PaymentPwdVerifyProvider;
import com.emop.wlt.user.management.model.request.Mapp02900101Req;
import com.emop.wlt.user.management.model.response.Mapp02900101Resp;
import com.emop.wlt.user.wallet.api.PaymentPwdProvider;
import com.emop.wlt.user.wallet.dto.Emap305ReqParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentPwdVerifyProviderImpl implements PaymentPwdVerifyProvider {
    @DubboReference
    private PaymentPwdProvider paymentPwdProvider;

    @Override
    @MappFlowJoin(nodeCode = "MAPP029", groupNodeCode = "VNC011")
    @AppResponse
    public ResponseModel<Mapp02900101Resp> paymentPasswordVerify(RequestModel<Mapp02900101Req> request) {
        NodeConfigParser nodeConfigParser = new NodeConfigParser(FlowContextUtils.getConfigParam());
        Mapp02900101Req mapp02900101Req = request.getMessageBody();

        String verifySceneType = mapp02900101Req.getFlowReqInfo().getVerifySceneType();
        String userId = FlowContextUtils.getUserId();
        String walletId = nodeConfigParser.getCtxWltId() ?
                FlowContextUtils.getWalletId() : mapp02900101Req.getWalletId();

        Emap305ReqParam emap305ReqParam = new Emap305ReqParam();
        emap305ReqParam.setUserId(userId);
        emap305ReqParam.setVerifySceneType(verifySceneType);
        emap305ReqParam.setWalletId(walletId);
        emap305ReqParam.setPwdEnc(mapp02900101Req.getPwdEnc());
        emap305ReqParam.setContextNo(FlowContextUtils.getInstContext());
        String contextNo = paymentPwdProvider.paymentPwdInstVerify(emap305ReqParam);

        FlowContextUtils.saveInstContext(contextNo);

        return ResponseModel.<Mapp02900101Resp>builder().messageBody(new Mapp02900101Resp()).build();
    }

}

package com.emop.wlt.user.management.mapi.component.impl;

import com.emop.wlt.aspect.annotation.AppResponse;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;

import com.emop.wlt.flow.annotation.MappFlowJoin;
import com.emop.wlt.flow.context.util.FlowContextUtils;
import com.emop.wlt.user.management.mapi.component.RealNameInfoCollectProvider;
import com.emop.wlt.user.management.model.request.Mapp02500101Req;
import com.emop.wlt.user.management.model.response.Mapp02500101Resp;
import com.emop.wlt.user.wallet.api.RealNameInfoProvider;
import com.emop.wlt.user.wallet.dto.Emap306ReqParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RealNameInfoCollectProviderImpl implements RealNameInfoCollectProvider {

    @DubboReference
    private RealNameInfoProvider realNameInfoProvider;

    @Override
    @MappFlowJoin(nodeCode ="MAPP025",groupNodeCode = "VNC013")
    @AppResponse
    public ResponseModel<Mapp02500101Resp> realNameInfoCollect(RequestModel<Mapp02500101Req> request) {
        Mapp02500101Req mapp02500101Req = request.getMessageBody();

        Emap306ReqParam emap306ReqParam = new Emap306ReqParam();
        emap306ReqParam.setUserId(FlowContextUtils.getUserId());
        emap306ReqParam.setInstContext(FlowContextUtils.getInstContext());
        emap306ReqParam.setVerifySceneType(mapp02500101Req.getFlowReqInfo().getVerifySceneType());
        emap306ReqParam.setWalletId(FlowContextUtils.getWalletId());
        emap306ReqParam.setChineseNameInfo(mapp02500101Req.getChineseNameInfo());
        emap306ReqParam.setEnglishNameInfo(mapp02500101Req.getEnglishNameInfo());
        emap306ReqParam.setIdNumber(mapp02500101Req.getIdNumber());
        emap306ReqParam.setIdType(mapp02500101Req.getIdType());
        emap306ReqParam.setBirthDate(mapp02500101Req.getBirthDate());
        emap306ReqParam.setIdIssueDate(mapp02500101Req.getIdIssueDate());
        emap306ReqParam.setIdExpireDate(mapp02500101Req.getIdExpireDate());

        String contextNo = realNameInfoProvider.realNameInfoVerify(emap306ReqParam);
        FlowContextUtils.saveInstContext(contextNo);

        return ResponseModel.<Mapp02500101Resp>builder().messageBody(new Mapp02500101Resp()).build();
    }
}

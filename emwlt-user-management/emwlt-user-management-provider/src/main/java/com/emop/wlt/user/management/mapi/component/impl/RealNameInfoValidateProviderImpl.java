package com.emop.wlt.user.management.mapi.component.impl;

import com.emop.wlt.aspect.annotation.AppResponse;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.flow.annotation.MappFlowJoin;
import com.emop.wlt.flow.context.util.FlowContextUtils;
import com.emop.wlt.user.management.mapi.component.RealNameInfoValidateProvider;
import com.emop.wlt.user.management.model.request.Mapp02600101Req;
import com.emop.wlt.user.management.model.response.Mapp02600101Resp;
import com.emop.wlt.user.wallet.api.RealNameInfoProvider;
import com.emop.wlt.user.wallet.dto.Emap306ReqParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RealNameInfoValidateProviderImpl implements RealNameInfoValidateProvider {

    @DubboReference
    private RealNameInfoProvider realNameInfoProvider;

    @Override
    @MappFlowJoin(nodeCode ="MAPP026",groupNodeCode = "VNC015")
    @AppResponse
    public ResponseModel<Mapp02600101Resp> realNameInfoValidate(RequestModel<Mapp02600101Req> request) {
        Mapp02600101Req mapp02600101Req = request.getMessageBody();

        Emap306ReqParam emap306ReqParam = new Emap306ReqParam();
        emap306ReqParam.setUserId(FlowContextUtils.getUserId());
        emap306ReqParam.setInstContext(FlowContextUtils.getInstContext());
        emap306ReqParam.setVerifySceneType(mapp02600101Req.getFlowReqInfo().getVerifySceneType());
        emap306ReqParam.setWalletId(FlowContextUtils.getWalletId());
        emap306ReqParam.setChineseNameInfo(mapp02600101Req.getChineseNameInfo());
        emap306ReqParam.setEnglishNameInfo(mapp02600101Req.getEnglishNameInfo());
        emap306ReqParam.setIdNumber(mapp02600101Req.getIdNumber());
        emap306ReqParam.setIdType(mapp02600101Req.getIdType());

        String contextNo = realNameInfoProvider.realNameInfoVerify(emap306ReqParam);
        FlowContextUtils.saveInstContext(contextNo);

        return ResponseModel.<Mapp02600101Resp>builder().messageBody(new Mapp02600101Resp()).build();
    }
}

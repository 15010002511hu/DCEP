package com.emop.wlt.user.query.mapi;

import com.alipay.gateway.adapterservice.annotation.OperationType;
import com.dubbo.ldc.ZoneRouter;

import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;

import com.emop.wlt.user.query.model.request.Mapp20700101Req;
import com.emop.wlt.user.query.model.response.Mapp20700101Resp;

@ZoneRouter(ZoneRouter.Type.CZ)
public interface ProtocolSendProvider {

    /**
     * mapp.207.001.01 发送协议至电子邮箱
     * @param request
     * @return
     */
    @OperationType(value = "mapp.207.001.01", name = "发送协议至电子邮箱", desc = "mapp.207.001.01")
    ResponseModel<Mapp20700101Resp> sendProtocolByEmail(RequestModel<Mapp20700101Req> request);

}

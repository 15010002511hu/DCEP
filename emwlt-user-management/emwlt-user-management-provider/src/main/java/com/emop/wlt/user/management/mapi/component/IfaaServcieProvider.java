package com.emop.wlt.user.management.mapi.component;

import com.alipay.gateway.adapterservice.annotation.OperationType;
import com.dubbo.ldc.ZoneRouter;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.user.management.model.request.Mapp03200101Req;
import com.emop.wlt.user.management.model.request.Mapp03400101Req;
import com.emop.wlt.user.management.model.response.Mapp03200101Resp;
import com.emop.wlt.user.management.model.response.Mapp03400101Resp;

@ZoneRouter(ZoneRouter.Type.RZ)
public interface IfaaServcieProvider {

    /**
     * 生物识别通用接口
     */
    @OperationType(value = "mapp.032.001.01", name = "生物识别通用接口", desc = "mapp.032.001.01")
    ResponseModel<Mapp03200101Resp> doBioAuth(RequestModel<Mapp03200101Req> request);

    /**
     * 生物识别查询接口
     */
    @OperationType(value = "mapp.034.001.01", name = "生物识别查询接口", desc = "mapp.034.001.01")
    ResponseModel<Mapp03400101Resp> doBioQuery(RequestModel<Mapp03400101Req> request);
}

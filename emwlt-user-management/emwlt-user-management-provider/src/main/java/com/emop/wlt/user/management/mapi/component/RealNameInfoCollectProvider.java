package com.emop.wlt.user.management.mapi.component;

import com.alipay.gateway.adapterservice.annotation.OperationType;
import com.dubbo.ldc.ZoneRouter;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.user.management.model.request.Mapp02500101Req;
import com.emop.wlt.user.management.model.response.Mapp02500101Resp;


/**
 * 通用实名信息采集组件（非登录态）
 *
 * @dubbo
 */

@ZoneRouter(ZoneRouter.Type.GZ)
public interface RealNameInfoCollectProvider {

    /**
     * mapp.025.001.01 实名信息采集（非登录态）
     */
    @OperationType(value = "mapp.025.001.01", name = "实名信息采集（非登录态）", desc = "mapp.025.001.01")
    ResponseModel<Mapp02500101Resp> realNameInfoCollect(RequestModel<Mapp02500101Req> request);

}

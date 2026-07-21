package com.emop.wlt.user.management.mapi.component;

import com.alipay.gateway.adapterservice.annotation.OperationType;
import com.dubbo.ldc.ZoneRouter;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.user.management.model.request.Mapp02600101Req;
import com.emop.wlt.user.management.model.response.Mapp02600101Resp;


/**
 * 通用实名信息验证组件（非登录态）
 *
 * @dubbo
 */

@ZoneRouter(ZoneRouter.Type.GZ)
public interface RealNameInfoValidateProvider {

    /**
     * mapp.026.001.01 实名信息验证（非登录态）
     */
    @OperationType(value = "mapp.026.001.01", name = "实名信息验证（非登录态）", desc = "mapp.026.001.01")
    ResponseModel<Mapp02600101Resp> realNameInfoValidate(RequestModel<Mapp02600101Req> request);

}

package com.emop.wlt.user.management.mapi.component;

import com.alipay.gateway.adapterservice.annotation.OperationType;
import com.dubbo.ldc.ZoneRouter;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.user.management.model.request.Mapp02900101Req;
import com.emop.wlt.user.management.model.response.Mapp02900101Resp;

/**
 * 通用支付密码验证组件（非登录态）
 */

@ZoneRouter(ZoneRouter.Type.GZ)
public interface PaymentPwdVerifyProvider {

    /**
     * mapp.029.001.01 支付密码验证（非登录态）
     */
    @OperationType(value = "mapp.029.001.01", name = "支付密码验证（非登录态）", desc = "mapp.029.001.01")
    ResponseModel<Mapp02900101Resp> paymentPasswordVerify(RequestModel<Mapp02900101Req> request);

}

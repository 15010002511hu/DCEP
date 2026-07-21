package com.emop.wlt.user.management.mapi.component;

import com.alipay.gateway.adapterservice.annotation.OperationType;
import com.dubbo.ldc.ZoneRouter;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.user.management.model.request.Mapp03000101Req;
import com.emop.wlt.user.management.model.response.Mapp03000101Resp;

/**
 * 通用登录密码验证组件（非登录态）
 */

@ZoneRouter(ZoneRouter.Type.GZ)
public interface LoginPwdVerifyProvider {

    /**
     * mapp.030.001.01 登录密码验证（非登录态）
     */
    @OperationType(value = "mapp.030.001.01", name = "登录密码验证（非登录态）", desc = "mapp.030.001.01")
    ResponseModel<Mapp03000101Resp> LoginPasswordVerify(RequestModel<Mapp03000101Req> request);

}

package com.emop.wlt.user.management.mapi.business;


import com.alipay.gateway.adapterservice.annotation.OperationType;
import com.dubbo.ldc.ZoneRouter;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.user.management.model.request.Mapp11100101Req;
import com.emop.wlt.user.management.model.request.Mapp11200101Req;
import com.emop.wlt.user.management.model.response.Mapp11100101Resp;
import com.emop.wlt.user.management.model.response.Mapp11200101Resp;

/**
 * 重置登录密码（非登录态）
 *
 * @dubbo
 */

@ZoneRouter(ZoneRouter.Type.GZ)
public interface ResetLoginPwdProvider {

    /**
     * mapp.111.001.01 重置登录密码预校验（非登录态）
     * @param request
     * @return
     */
    @OperationType(value = "mapp.111.001.01", name = "重置登录密码预校验（非登录态）", desc = "mapp.111.001.01")
    ResponseModel<Mapp11100101Resp> resetLoginPwdValidation(RequestModel<Mapp11100101Req> request);

    /**
     * mapp.112.001.01 重置登录密码（非登录态）
     * @param request
     * @return
     */
    @OperationType(value = "mapp.112.001.01", name = "重置登录密码（非登录态）", desc = "mapp.112.001.01")
    ResponseModel<Mapp11200101Resp> resetLoginPwd(RequestModel<Mapp11200101Req> request);

}

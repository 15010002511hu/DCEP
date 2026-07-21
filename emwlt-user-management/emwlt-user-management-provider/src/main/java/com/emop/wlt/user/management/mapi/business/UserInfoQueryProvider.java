package com.emop.wlt.user.management.mapi.business;


import com.alipay.gateway.adapterservice.annotation.OperationType;
import com.dubbo.ldc.ZoneRouter;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.user.management.model.request.Mapp20800101Req;
import com.emop.wlt.user.management.model.response.Mapp20800101Resp;

/**
 * 用户信息查询（非登录态）
 */

@ZoneRouter(ZoneRouter.Type.GZ)
public interface UserInfoQueryProvider {

    /**
     * mapp.208.001.01 用户信息查询（非登录态）
     */
    @OperationType(value = "mapp.208.001.01", name = "用户信息查询（非登录态）", desc = "mapp.208.001.01")
    ResponseModel<Mapp20800101Resp> queryUserInfo(RequestModel<Mapp20800101Req> request);
}

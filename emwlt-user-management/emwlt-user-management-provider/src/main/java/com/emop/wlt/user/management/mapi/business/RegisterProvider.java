package com.emop.wlt.user.management.mapi.business;

import com.alipay.gateway.adapterservice.annotation.OperationType;
import com.dubbo.ldc.ZoneRouter;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.user.management.model.request.Mapp10000101Req;
import com.emop.wlt.user.management.model.request.Mapp10100101Req;
import com.emop.wlt.user.management.model.response.Mapp10000101Resp;
import com.emop.wlt.user.management.model.response.Mapp10100101Resp;

/**
 * 注册
 *
 * @dubbo
 */

@ZoneRouter(ZoneRouter.Type.GZ)
public interface RegisterProvider {

    /**
     * mapp.100.001.01 注册准入条件校验
     * @param request
     * @return
     */
    @OperationType(value = "mapp.100.001.01", name = "注册准入条件校验", desc = "mapp.100.001.01")
    ResponseModel<Mapp10000101Resp> registerValidation(RequestModel<Mapp10000101Req> request);

    /**
     * mapp.101.001.01 注册并登录
     * @param request
     * @return
     */
    @OperationType(value = "mapp.101.001.01", name = "注册并登录", desc = "mapp.101.001.01")
    ResponseModel<Mapp10100101Resp> registerAndLogin(RequestModel<Mapp10100101Req> request);
}

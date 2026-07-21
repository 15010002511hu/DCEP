package com.emop.wlt.user.management.mapi.component;

import com.alipay.gateway.adapterservice.annotation.OperationType;
import com.dubbo.ldc.ZoneRouter;

import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.user.management.model.request.Mapp02200101Req;
import com.emop.wlt.user.management.model.response.Mapp02200101Resp;


/**
 * 通用账号短信组件：验证短信验证码（非登录态）
 */

@ZoneRouter(ZoneRouter.Type.GZ)
public interface SmsCheckProvider {

    /**
     * mapp.022.001.01 验证短信验证码（非登录态）
     */
    @OperationType(value = "mapp.022.001.01", name = "验证短信验证码（非登录态）", desc = "mapp.022.001.01")
    ResponseModel<Mapp02200101Resp> checkSms(RequestModel<Mapp02200101Req> request);
}

package com.emop.wlt.user.management.mapi.component;


import com.alipay.gateway.adapterservice.annotation.OperationType;
import com.dubbo.ldc.ZoneRouter;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.user.management.model.request.Mapp02100101Req;
import com.emop.wlt.user.management.model.response.Mapp02100101Resp;

/**
 * 通用账号短信组件：发送短信验证码（非登录态）
 */

@ZoneRouter(ZoneRouter.Type.GZ)
public interface SmsSendProvider {

    /**
     * mapp.021.001.01 发送短信验证码（非登录态）
     */
    @OperationType(value = "mapp.021.001.01", name = "发送短信验证码（非登录态）", desc = "mapp.021.001.01")
    ResponseModel<Mapp02100101Resp> sendSms(RequestModel<Mapp02100101Req> request);
}

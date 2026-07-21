package com.emop.wlt.user.management.mapi.component;

import com.alipay.gateway.adapterservice.annotation.OperationType;
import com.dubbo.ldc.ZoneRouter;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.user.management.model.request.Mapp02300101Req;
import com.emop.wlt.user.management.model.response.Mapp02300101Resp;

/**
 * 通用钱包手机号短信组件：请求短信验证码（非登录态）
 *
 * @dubbo
 */

@ZoneRouter(ZoneRouter.Type.GZ)
public interface WalletSmsSendProvider {

    /**
     * mapp.023.001.01 钱包手机号发送短信验证码（非登录态）
     */
    @OperationType(value = "mapp.023.001.01", name = "钱包手机号发送短信验证码（非登录态）", desc = "mapp.023.001.01")
    ResponseModel<Mapp02300101Resp> walletSendSms(RequestModel<Mapp02300101Req> request);
}

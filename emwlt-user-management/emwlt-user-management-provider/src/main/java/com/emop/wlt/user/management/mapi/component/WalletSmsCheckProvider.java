package com.emop.wlt.user.management.mapi.component;

import com.alipay.gateway.adapterservice.annotation.OperationType;
import com.dubbo.ldc.ZoneRouter;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.user.management.model.request.Mapp02400101Req;
import com.emop.wlt.user.management.model.response.Mapp02400101Resp;

/**
 * 通用钱包手机号短信组件：验证短信验证码（非登录态）
 *
 * @dubbo
 */

@ZoneRouter(ZoneRouter.Type.GZ)
public interface WalletSmsCheckProvider {

    /**
     * mapp.024.001.01 钱包手机号验证短信验证码（非登录态）
     */
    @OperationType(value = "mapp.024.001.01", name = "钱包手机号验证短信验证码（非登录态）", desc = "mapp.024.001.01")
    ResponseModel<Mapp02400101Resp> walletCheckSms(RequestModel<Mapp02400101Req> request);

}

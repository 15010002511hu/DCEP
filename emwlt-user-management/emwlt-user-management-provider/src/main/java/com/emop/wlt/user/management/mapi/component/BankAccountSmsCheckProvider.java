package com.emop.wlt.user.management.mapi.component;

import com.alipay.gateway.adapterservice.annotation.OperationType;
import com.dubbo.ldc.ZoneRouter;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.user.management.model.request.Mapp02800101Req;
import com.emop.wlt.user.management.model.response.Mapp02800101Resp;

/**
 * 通用银行账户手机号短信组件：验证短信验证码 （非登录态）
 *
 * @dubbo
 */

@ZoneRouter(ZoneRouter.Type.GZ)
public interface BankAccountSmsCheckProvider {

    /**
     * mapp.028.001.01 银行账户手机号验证短信验证码（登录态）
     */
    @OperationType(value = "mapp.028.001.01", name = "银行账户手机号验证短信验证码（非登录态）", desc = "mapp.028.001.01")
    ResponseModel<Mapp02800101Resp> bankAccountCheckSms(RequestModel<Mapp02800101Req> request);
}

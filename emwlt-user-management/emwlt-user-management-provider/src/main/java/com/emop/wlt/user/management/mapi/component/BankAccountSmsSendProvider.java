package com.emop.wlt.user.management.mapi.component;

import com.alipay.gateway.adapterservice.annotation.OperationType;
import com.dubbo.ldc.ZoneRouter;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.user.management.model.request.Mapp02700101Req;
import com.emop.wlt.user.management.model.response.Mapp02700101Resp;

/**
 * 通用银行账户手机号短信组件：请求短信验证码（非登录态）
 *
 * @dubbo
 */

@ZoneRouter(ZoneRouter.Type.GZ)
public interface BankAccountSmsSendProvider {

    /**
     * mapp.027.001.01 银行账户手机号发送短信验证码（非登录态）
     */
    @OperationType(value = "mapp.027.001.01", name = "银行账户手机号发送短信验证码（非登录态）", desc = "mapp.027.001.01")
    ResponseModel<Mapp02700101Resp> bankAccountSendSms(RequestModel<Mapp02700101Req> request);

}

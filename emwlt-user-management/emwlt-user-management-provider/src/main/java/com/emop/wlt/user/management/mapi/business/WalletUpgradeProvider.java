package com.emop.wlt.user.management.mapi.business;

import com.alipay.gateway.adapterservice.annotation.OperationType;
import com.dubbo.ldc.ZoneRouter;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.user.management.model.request.Mapp31900101Req;
import com.emop.wlt.user.management.model.response.Mapp31900101Resp;

/**
 * 非登录态钱包升级
 */
@ZoneRouter(ZoneRouter.Type.GZ)
public interface WalletUpgradeProvider {

    /**
     * 钱包升级预校验
     */
    @OperationType(value = "mapp.319.001.01", name = "钱包升级预校验", desc = "mapp.319.001.01")
    ResponseModel<Mapp31900101Resp> walletUpgradePreCheck(RequestModel<Mapp31900101Req> request);

}

package com.emop.wlt.user.management.mapi.business;

import com.alipay.gateway.adapterservice.annotation.OperationType;
import com.dubbo.ldc.ZoneRouter;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.user.management.model.request.Mapp10200101Req;
import com.emop.wlt.user.management.model.request.Mapp10300101Req;
import com.emop.wlt.user.management.model.response.Mapp10200101Resp;
import com.emop.wlt.user.management.model.response.Mapp10300101Resp;


/**
 * 登录
 *
 * @dubbo
 */

@ZoneRouter(ZoneRouter.Type.GZ)
public interface LoginProvider {

    /**
     * mapp.102.001.01 登录校验
     * @param request
     * @return
     */
    @OperationType(value = "mapp.102.001.01", name = "登录校验", desc = "mapp.102.001.01")
    ResponseModel<Mapp10200101Resp> loginValidation(RequestModel<Mapp10200101Req> request);

    /**
     * mapp.103.001.01 登录
     * @param request
     * @return
     */
    @OperationType(value = "mapp.103.001.01", name = "登录", desc = "mapp.103.001.01")
    ResponseModel<Mapp10300101Resp> login(RequestModel<Mapp10300101Req> request);

}

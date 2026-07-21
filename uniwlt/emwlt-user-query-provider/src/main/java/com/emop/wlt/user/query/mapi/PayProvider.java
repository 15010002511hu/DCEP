package com.emop.wlt.user.query.mapi;

import com.alipay.gateway.adapterservice.annotation.OperationType;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.user.query.model.request.Mapp09500101Req;
import com.emop.wlt.user.query.model.response.Mapp09500101Resp;

/**
 * 支付相关接口
 *
 * @author liuyuntao
 * @since 1.0.0
 */
public interface PayProvider {

    /**
     * 收款码查询
     *
     * @param requestModel
     * @return
     */
    @OperationType(value = "mapp.095.001.01", name = "收款码查询", desc = "mapp.095.001.01")
    ResponseModel<Mapp09500101Resp> qrCodeQuery(RequestModel<Mapp09500101Req> requestModel);

}

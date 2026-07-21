package com.emop.wlt.user.management.mapi.business;

import com.alipay.gateway.adapterservice.annotation.OperationType;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.user.management.model.request.Mapp31300101Req;
import com.emop.wlt.user.management.model.request.Mapp31400101Req;
import com.emop.wlt.user.management.model.response.Mapp31300101Resp;
import com.emop.wlt.user.management.model.response.Mapp31400101Resp;

/**
 * 钱包挂失（非登录态）
 *
 * @author liuyuntao
 * @since 1.0.0
 */
public interface ReportAsLostProvider {

    /**
     * 钱包挂失预校验
     */
    @OperationType(value = "mapp.313.001.01", name = "钱包挂失预校验", desc = "mapp.313.001.01")
    ResponseModel<Mapp31300101Resp> preCheck(RequestModel<Mapp31300101Req> request);

    /**
     * 钱包挂失
     *
     * @param request
     * @return
     */
    @OperationType(value = "mapp.314.001.01", name = "钱包挂失", desc = "mapp.314.001.01")
    ResponseModel<Mapp31400101Resp> reportAsLost(RequestModel<Mapp31400101Req> request);

}

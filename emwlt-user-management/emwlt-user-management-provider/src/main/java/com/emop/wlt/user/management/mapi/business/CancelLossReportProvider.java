package com.emop.wlt.user.management.mapi.business;

import com.alipay.gateway.adapterservice.annotation.OperationType;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.user.management.model.request.Mapp31700101Req;
import com.emop.wlt.user.management.model.request.Mapp31800101Req;
import com.emop.wlt.user.management.model.response.Mapp31700101Resp;
import com.emop.wlt.user.management.model.response.Mapp31800101Resp;

/**
 * 钱包解挂（非登录态）
 *
 * @author liuyuntao
 * @since 1.0.0
 */
public interface CancelLossReportProvider {

    /**
     * 钱包解挂预校验
     */
    @OperationType(value = "mapp.317.001.01", name = "钱包解挂预校验", desc = "mapp.317.001.01")
    ResponseModel<Mapp31700101Resp> preCheck(RequestModel<Mapp31700101Req> request);

    /**
     * 钱包解挂
     *
     * @param request
     * @return
     */
    @OperationType(value = "mapp.318.001.01", name = "钱包解挂", desc = "mapp.318.001.01")
    ResponseModel<Mapp31800101Resp> cancelLossReport(RequestModel<Mapp31800101Req> request);

}

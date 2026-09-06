/*
 * pbcdci.cn Inc.
 * Copyright © 2025 PBCDCI All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.api;

import com.dcep.common.exception.DcepException;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.dips.wholesalepayment.dto.BizStatusDTO;
import com.dcep.dips.wholesalepayment.dto.acctrans.ZeroOutReqDTO;
import com.dcep.dips.wholesalepayment.dto.acctrans.ZeroOutRespDTO;
import com.dcep.dips.wholesalepayment.dto.funding.IncreaseReqDTO;
import com.dcep.dips.wholesalepayment.dto.hvps.ClearReportReqDTO;
import com.dubbo.ldc.ZoneRouter;

@ZoneRouter
public interface FundingService {
    /**
     * 注资、预注资调增【准备金调用】，处理【大额系统】主动发起的注资、预注资请求，包括：hvps.112、hvps.115
     *
     * @param increaseReqDTO 请求对象
     * @return 应答对象
     */
    Response<BizStatusDTO> increase(IncreaseReqDTO increaseReqDTO) throws DcepException;

    /**
     * 清算通知【准备金调用】，处理【大额系统】回来的清算回执报文：saps.604
     * 原报文包括
     *    1. 预注资调增：hvps.115
     *    2. 注资调减、清零：hvps.112
     *    3. 预注资调减：hvps.118
     * @param reportReqDTO 请求对象
     * @return 应答对象
     */
    Response<BizStatusDTO> hvpsReport(ClearReportReqDTO reportReqDTO) throws DcepException;

    /**
     * 清零申请【结算钱包调用】，处理【结算钱包系统】发起的账户清零请求
     *
     * @param zeroOutReqDTO 请求对象
     * @return 应答对象
     */
    Response<ZeroOutRespDTO> zeroOutApply(ZeroOutReqDTO zeroOutReqDTO) throws DcepException;

    /**
     * 注资、预注资调减【内部网关调用】，处理【运营机构】发起的注资调减dcep.181报文、预注资调减dcep.183报文请求
     *
     * @param gwReqDTO 请求对象
     * @return 应答对象
     */
    Response<EnvelopeDTO<GwDTO>> decrease(EnvelopeDTO<GwDTO> gwReqDTO) throws DcepException;
}

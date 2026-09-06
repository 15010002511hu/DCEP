package com.dcep.dips.wholesalepayment.api;

import com.dcep.common.exception.DcepException;
import com.dcep.common.model.Response;
import com.dcep.dips.wholesalepayment.dto.summary.FundingInfoQryReqDTO;
import com.dcep.dips.wholesalepayment.dto.summary.FundingInfoQryRspDTO;
import com.dcep.dips.wholesalepayment.dto.summary.PtySummaryInfoReqDTO;
import com.dcep.dips.wholesalepayment.dto.summary.PtySummaryInfoRespDTO;
import com.dcep.dips.wholesalepayment.dto.summary.SysSummaryInfoQryReqDTO;
import com.dcep.dips.wholesalepayment.dto.summary.SysSummaryInfoQryRespDTO;
import com.dubbo.ldc.ZoneRouter;

@ZoneRouter
public interface SummaryVerifyService {
    /**
     * 供区块链服务平台调用，查询特定机构交易汇总信息
     * @param ptySummaryInfoReqDTO
     * @return
     * @throws DcepException
     */
    Response<PtySummaryInfoRespDTO> queryPtySummaryInfo(PtySummaryInfoReqDTO ptySummaryInfoReqDTO) throws DcepException;

    /**
     * 供区块链服务平台调用，查询特定系统标识交易汇总信息
     * @param sysSummaryInfoQryReqDTO
     * @return
     * @throws DcepException
     */
    Response<SysSummaryInfoQryRespDTO> querySysSummaryInfo(SysSummaryInfoQryReqDTO sysSummaryInfoQryReqDTO) throws DcepException;
    Response<FundingInfoQryRspDTO> queryFundingInfo(FundingInfoQryReqDTO fundingInfoQryReq);


}

package com.dcep.dips.wholesalepayment.service.impl;

import com.alibaba.fastjson.JSON;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.Response;
import com.dcep.dips.wholesalepayment.api.SummaryVerifyService;
import com.dcep.dips.wholesalepayment.dal.mapper.FundAdjustProdMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.HvpsTransMapper;
import com.dcep.dips.wholesalepayment.dal.model.FundAdjustProdDO;
import com.dcep.dips.wholesalepayment.dal.model.HvpsTransDO;
import com.dcep.dips.wholesalepayment.dto.summary.*;
import com.dcep.dips.wholesalepayment.enums.ClearingStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@DubboService
public class SummaryVerifyServiceImpl implements SummaryVerifyService{
    @Resource
    FundAdjustProdMapper fundAdjustProdMapper;
    @Resource
    HvpsTransMapper hvpsTransMapper;
    /**
     * 供区块链服务平台调用，查询特定机构交易汇总信息
     *
     * @param ptySummaryInfoReqDTO
     * @return
     * @throws DcepException
     */
    @Override
    public Response<PtySummaryInfoRespDTO> queryPtySummaryInfo(PtySummaryInfoReqDTO ptySummaryInfoReqDTO) throws DcepException {
        return null;
    }

    /**
     * 供区块链服务平台调用，查询特定系统标识交易汇总信息
     *
     * @param sysSummaryInfoQryReqDTO
     * @return
     * @throws DcepException
     */
    @Override
    public Response<SysSummaryInfoQryRespDTO> querySysSummaryInfo(SysSummaryInfoQryReqDTO sysSummaryInfoQryReqDTO) throws DcepException {
        return null;
    }

    @Override
    public Response<FundingInfoQryRspDTO> queryFundingInfo(FundingInfoQryReqDTO fundingInfoQryReq) {
        log.info("收到区块链资金汇总明细查询申请: {}", fundingInfoQryReq);
        List<FundAdjustProdDO> fundAdjustProdList = fundAdjustProdMapper.selectByAdjustPtyId(fundingInfoQryReq.getPtyId(),fundingInfoQryReq.getBookingDate(), ClearingStatusEnum.SETTLED.getCode());
        List<FundAdjustInfDTO> fundAdjustInfList = new ArrayList<>();
        for (FundAdjustProdDO fundAdjustProdDO : fundAdjustProdList) {
            FundAdjustInfDTO fundAdjustInfDTO = new FundAdjustInfDTO();
            fundAdjustInfDTO.setMsgId(fundAdjustProdDO.getMsgId());
            fundAdjustInfDTO.setMsgTp(fundAdjustProdDO.getMsgTp());
            fundAdjustInfDTO.setTransPtyId(fundAdjustProdDO.getAdjustPtyId());
            fundAdjustInfDTO.setCustodian(fundAdjustProdDO.getAdjustSysId());
            fundAdjustInfDTO.setWalletId(fundAdjustProdDO.getAdjustWltId());
            fundAdjustInfDTO.setAmount(fundAdjustProdDO.getAdjustAmt());
            fundAdjustInfDTO.setOperationType(fundAdjustProdDO.getAdjustTp());
            fundAdjustInfDTO.setPrcSts(fundAdjustProdDO.getBizSts());

            HvpsTransDO hvpsTransDO = hvpsTransMapper.selectByMsgId(fundAdjustProdDO.getMsgId());
            fundAdjustInfDTO.setOrgnlMsgId(hvpsTransDO.getHvpsMsgId());
            fundAdjustInfDTO.setOrgnlMsgTp(hvpsTransDO.getHvpsMsgTp());
            fundAdjustInfList.add(fundAdjustInfDTO);
        }
        FundAdjustDetail fundAdjustDetail = new FundAdjustDetail();
        fundAdjustDetail.setDatas(fundAdjustInfList);
        fundAdjustDetail.setCountNumber(Long.valueOf(fundAdjustProdList.size()));
        FundingInfoQryRspDTO fundingInfoQryRspDTO = new FundingInfoQryRspDTO();
        fundingInfoQryRspDTO.setPtyId(fundingInfoQryReq.getPtyId());
        fundingInfoQryRspDTO.setBookingDate(fundingInfoQryReq.getBookingDate());
        fundingInfoQryRspDTO.setFundAdjustDetail(JSON.toJSONString(fundAdjustDetail));
        log.info("收到区块链资金汇总明细查询返回: {}", fundingInfoQryRspDTO);
        return new Response<>(true,fundingInfoQryRspDTO);
    }
}

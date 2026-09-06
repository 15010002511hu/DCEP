/*
 * pbcdci.cn Inc.
 * Copyright © 2025 PBCDCI All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.manager.impl;

import com.alibaba.fastjson.JSON;
import com.dcep.common.model.Response;
import com.dcep.dips.acctrans.dto.accounting.transfer.TransferRespDTO;
import com.dcep.dips.common.constant.CommonConstant;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.common.utils.IdUtils;
import com.dcep.dips.wholesalepayment.dal.mapper.*;
import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import com.dcep.dips.wholesalepayment.dal.model.CommonRecordDO;
import com.dcep.dips.wholesalepayment.dal.model.CommonStsctrlDO;
import com.dcep.dips.wholesalepayment.dal.model.SettlementProdDO;
import com.dcep.dips.wholesalepayment.dto.chain.OnChainAdjustReqDTO;
import com.dcep.dips.wholesalepayment.dto.chain.OnChainAdjustRespDTO;
import com.dcep.dips.wholesalepayment.enums.ActgStsEnum;
import com.dcep.dips.wholesalepayment.enums.BizPrtyEnum;
import com.dcep.dips.wholesalepayment.enums.MsgTpEnum;
import com.dcep.dips.wholesalepayment.enums.WholesaleErrorEnum;
import com.dcep.dips.wholesalepayment.manager.AccountingManager;
import com.dcep.dips.wholesalepayment.manager.ChainManager;
import com.dcep.dips.wholesalepayment.manager.SettlementManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * @author hx.zhaolei
 */
@Component
@Slf4j
public class ChainManagerImpl implements ChainManager {

    @Resource
    private SettlementManager settlementManager;
    @Resource
    private SettlementProdMapper settlementProdMapper;
    @Autowired
    private SystemStatusDOMapper systemStatusDOMapper;
    @Autowired
    private CommonRecordMapper commonRecordMapper;
    @Autowired
    private AccountingInstrMapper accountingInstrMapper;
    @Autowired
    private CommonStsctrlMapper commonStsctrlMapper;
    @Autowired
    private AccountingManager accountingManager;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public AccountingInstrDO record(OnChainAdjustReqDTO onChainAdjustReqDTO) {
        String curSysDt = systemStatusDOMapper.selectCurSysDt(CommonConstant.SysCode.WHOLESALE);
        String settleDate = curSysDt;
        // 上下文调用信息
        RpcContext context = RpcContext.getContext();

        // 1.保存记录至结算产品表
        context.set(Constant.PREPARE_INSERT_STEP, Constant.INSERT_STEP_ONE);
        if (onChainAdjustReqDTO.getExpectedSettlementDate()!=null){
            settleDate = onChainAdjustReqDTO.getExpectedSettlementDate();
        }else{
            onChainAdjustReqDTO.setExpectedSettlementDate(curSysDt);
        }
        SettlementProdDO settle = new SettlementProdDO(settleDate,onChainAdjustReqDTO);
        settlementProdMapper.insert(settle);

        // 2.保存记录至记账流水表(生成交易流水号)
        context.set(Constant.PREPARE_INSERT_STEP, Constant.INSERT_STEP_TWO);

        String transId = IdUtils.randomTransIdWithBizDt(settleDate);
        AccountingInstrDO accountingInstrDO = new AccountingInstrDO(transId,onChainAdjustReqDTO);
        accountingInstrMapper.insert(accountingInstrDO);

        // 3.保存记录至交易档案表。
        String msgTp = null;
        if(onChainAdjustReqDTO.getAdjustTp().equals("QOT01")){
            msgTp =  MsgTpEnum.CHAIN_REQUEST_UP.getCode();
        } else if (onChainAdjustReqDTO.getAdjustTp().equals("QOT02")){
            msgTp = MsgTpEnum.CHAIN_REQUEST_DOWN.getCode();
        }
        commonRecordMapper.insert(new CommonRecordDO(onChainAdjustReqDTO.getMsgId(),msgTp, JSON.toJSONString(onChainAdjustReqDTO)));

        // 4.保存记录至交易控制表
        commonStsctrlMapper.insert(new CommonStsctrlDO(onChainAdjustReqDTO.getMsgId(), BizPrtyEnum.NORM.getCode()));


        return accountingInstrDO;
    }

    @Override
    public Response<OnChainAdjustRespDTO> transfer(AccountingInstrDO accountingInstrDO,String sttlmDt,String useCurrentSystemFlag){
        //调用结算钱包
        Response<TransferRespDTO> transfer = accountingManager.transfer(accountingInstrDO,sttlmDt,useCurrentSystemFlag,"20000000");

        if(transfer == null){
            return new Response<>(WholesaleErrorEnum.UNKNOWN_EXCEPTION.getCode(),WholesaleErrorEnum.UNKNOWN_EXCEPTION.getDescription());
        }
        if(!transfer.isSuccess()){
            //对于未知异常直接返回
            if(WholesaleErrorEnum.UNKNOWN_EXCEPTION.getCode().equals(transfer.getErrorCode())){
                return new Response<>(transfer.getErrorCode(),transfer.getErrorMsg());
            }
            //明确的异常直接置为失败
            accountingInstrDO.setActgSts(ActgStsEnum.FAILED.getCode());
            accountingInstrDO.setActgPrcCd(transfer.getErrorCode());
            accountingInstrDO.setActgPrcInf(transfer.getErrorMsg());
        }else if(transfer.getResult() == null || transfer.getResult().getAccountingStatus() == null){
            return new Response<>(WholesaleErrorEnum.UNKNOWN_EXCEPTION.getCode(),WholesaleErrorEnum.UNKNOWN_EXCEPTION.getDescription());
        }else{
            //完成结算流程（正常or失败）
            TransferRespDTO result = transfer.getResult();
            accountingInstrDO.setActgSts(result.getAccountingStatus());
            accountingInstrDO.setActgDt(result.getAccountingDate());
        }

        settlementManager.settleFinshed(accountingInstrDO);
        //组织返回报文
        OnChainAdjustRespDTO onChainAdjustRespDTO = new OnChainAdjustRespDTO();
        onChainAdjustRespDTO.setBizStatus(accountingInstrDO.getActgSts());
        onChainAdjustRespDTO.setSettlementDate(accountingInstrDO.getActgDt());
        onChainAdjustRespDTO.setBizProcessCode(accountingInstrDO.getActgPrcCd());
        onChainAdjustRespDTO.setBizProcessInfo(accountingInstrDO.getActgPrcInf());
        return new Response<>(onChainAdjustRespDTO);
    }
}

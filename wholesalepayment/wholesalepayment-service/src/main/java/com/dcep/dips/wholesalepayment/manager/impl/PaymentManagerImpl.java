/*
 * pbcdci.cn Inc.
 * Copyright © 2025 PBCDCI All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.manager.impl;

import com.dcep.common.exception.DcepException;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.dips.acctrans.dto.accounting.transfer.TransferRespDTO;
import com.dcep.dips.wholesalepayment.annotation.Clearing;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.common.utils.CommonUtil;
import com.dcep.dips.wholesalepayment.common.utils.DtoUtil;
import com.dcep.dips.wholesalepayment.dal.mapper.*;
import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import com.dcep.dips.wholesalepayment.dal.model.CommonRecordDO;
import com.dcep.dips.wholesalepayment.dal.model.CommonStsctrlDO;
import com.dcep.dips.wholesalepayment.dal.model.SettlementProdDO;
import com.dcep.dips.wholesalepayment.dto.ClearingDTO;
import com.dcep.dips.wholesalepayment.enums.*;
import com.dcep.dips.wholesalepayment.manager.PaymentManager;
import com.dcep.dips.wholesalepayment.manager.RecordManager;
import com.dcep.dips.wholesalepayment.manager.StorageForwardManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


/**
 * @author wangxiaoyu
 */
@Component
@Slf4j
public class PaymentManagerImpl implements PaymentManager {

    @Autowired
    SettlementProdMapper settlementProdMapper;

    @Autowired
    AccountingInstrMapper accountingInstrMapper;

    @Autowired
    CommonRecordMapper commonRecordMapper;

    @Autowired
    CommonStsctrlMapper commonStsctrlMapper;

    @Autowired
    HvpsTransMapper hvpsTransMapper;

    @Autowired
    StorageForwardManager storageForwardManager;

    @Autowired
    private RecordManager recordManager;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void record(EnvelopeDTO<GwDTO> gwReqDTO, AccountingInstrDO accountingInstrDO) {
        ClearingDTO clearingDTO = (ClearingDTO) gwReqDTO.body();
        Clearing clearing = clearingDTO.getClass().getAnnotation(Clearing.class);
        log.info("record msgId:{}, msgTp:{}", clearingDTO.getClrMsgId(), clearingDTO.clrMsgTp());

        // 上下文调用信息
        RpcContext context = RpcContext.getContext();

        // 1.保存记录至结算产品表
        context.set(Constant.PREPARE_INSERT_STEP, Constant.INSERT_STEP_ONE);
        settlementProdMapper.insert(new SettlementProdDO(clearingDTO, clearing));

        // 2.保存记录至记账流水表(生成交易流水号)
        context.set(Constant.PREPARE_INSERT_STEP, Constant.INSERT_STEP_TWO);
        accountingInstrMapper.insert(accountingInstrDO);

        // 3.保存记录至交易档案表
        commonRecordMapper.insert(new CommonRecordDO(clearingDTO, DtoUtil.obj2JsonStr(gwReqDTO)));

        // 4.保存记录至交易控制表
        commonStsctrlMapper.insert(new CommonStsctrlDO(clearingDTO, clearing));

        log.info("record end msgId:{}, msgTp:{}", clearingDTO.getClrMsgId(), clearingDTO.clrMsgTp());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void clearing(AccountingInstrDO accountingInstrDO, TransferRespDTO transferRespDTO) {
        log.info("clearing transId:{}", accountingInstrDO.getTransId());
        accountingInstrDO.setGmtModified(new Date());
        accountingInstrDO.setActgSts(transferRespDTO.getAccountingStatus());
        accountingInstrDO.setActgDt(transferRespDTO.getAccountingDate());
        accountingInstrDO.setActgPrcCd(transferRespDTO.getBizCode());
        accountingInstrDO.setActgPrcInf(transferRespDTO.getBizMsg());
        // 4.1.更新记账指令表，当更新不为一条记录时抛异常
        if (accountingInstrMapper.updateActgSts(accountingInstrDO, ActgStsEnum.PROCESS.getCode()) != 1) {
            throw new DcepException(ErrorEnum.NO_MATCH_ORIGNAL.getCode(),
                    ErrorEnum.NO_MATCH_ORIGNAL.getDescription());
        }
        //4.2.更新结算产品表
        if (settlementProdMapper.updateBizSts(new SettlementProdDO(accountingInstrDO.getMsgId(), transferRespDTO.getAccountingStatus(), transferRespDTO.getBizCode(), transferRespDTO.getBizMsg()), ClearingStatusEnum.WAIT_SETTLE.getCode()) != 1) {
            throw new DcepException(ErrorEnum.NO_MATCH_ORIGNAL.getCode(),
                    ErrorEnum.NO_MATCH_ORIGNAL.getDescription());
        }
        //4.3.删除交易控制表记录
        commonStsctrlMapper.deleteByPrimaryKey(new CommonStsctrlDO(accountingInstrDO.getMsgId()));
        //4.4.判断如果结算成功/失败/已排队，插入存储转发表
        if (ActgStsEnum.SUCCESS.getCode().equals(transferRespDTO.getAccountingStatus())
                || ActgStsEnum.FAILED.getCode().equals(transferRespDTO.getAccountingStatus())
                || ActgStsEnum.QUEUED.getCode().equals(transferRespDTO.getAccountingStatus())) {
            storageForwardManager.saveForInst(accountingInstrDO, true);
        }
        log.info("clearing end transId:{}", accountingInstrDO.getTransId());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean debitConfirm(EnvelopeDTO<GwDTO> gwRespDTO, AccountingInstrDO accountingInstrDO) {
        ClearingDTO clearingDTO = (ClearingDTO) gwRespDTO.body();
        // 1.保存应答报文至交易档案表，待后续转发使用
        commonRecordMapper.insert(new CommonRecordDO(clearingDTO, DtoUtil.obj2JsonStr(gwRespDTO)));
        // 2.获取应答报文业务处理状态
        String clrBizRspSts = clearingDTO.clrBizRspSts();
        if (ClearingStatusEnum.SUCCESS.getCode().equals(clrBizRspSts)) {
            // 3.借记确认成功
            // 3.1.结算产品表更新为待结算
            settlementProdMapper.updateBizSts(new SettlementProdDO(accountingInstrDO.getMsgId(), ClearingStatusEnum.WAIT_SETTLE), ClearingStatusEnum.PROCESS.getCode());
            // 3.2.更新超时控制表报文发送时间，重新计时
            commonStsctrlMapper.updateSendTime(new CommonStsctrlDO(accountingInstrDO.getMsgId(), new Date()));
            return true;
        } else {
            // 4.借记确认失败
            // 4.1.结算产品表更新为失败
            settlementProdMapper.updateBizSts(new SettlementProdDO(accountingInstrDO.getMsgId(), ClearingStatusEnum.FAILED, clearingDTO.clrBizRjctCd(), clearingDTO.clrRjctResn()), ClearingStatusEnum.PROCESS.getCode());
            accountingInstrDO.setActgSts(ActgStsEnum.FAILED.getCode());
            accountingInstrDO.setGmtModified(new Date());
            accountingInstrDO.setActgPrcCd(clearingDTO.clrBizRjctCd());
            accountingInstrDO.setActgPrcInf(clearingDTO.clrRjctResn());
            // 4.2.记账指令表更新为失败
            accountingInstrMapper.updateActgSts(accountingInstrDO, ActgStsEnum.PROCESS.getCode());
            // 4.3.插入存储转发表
            storageForwardManager.saveForInst(accountingInstrDO, false);
            // 4.4.删除交易控制表记录
            commonStsctrlMapper.deleteByPrimaryKey(new CommonStsctrlDO(accountingInstrDO.getMsgId()));
            return false;
        }
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Response<AccountingInstrDO> resultReportProcess(EnvelopeDTO<GwDTO> gwReqDTO) {
        ClearingDTO clearingDTO = (ClearingDTO) gwReqDTO.body();
        log.info("resultReportProcess msgId:{}, msgTp:{}", clearingDTO.getClrMsgId(), clearingDTO.clrMsgTp());
        // 1.更新结算产品表,原交易为处理中，则正常更新为PR12-待结算
        if (1 != settlementProdMapper.updateBizSts(new SettlementProdDO(clearingDTO.getClrMsgId(),
                ClearingStatusEnum.WAIT_SETTLE), ClearingStatusEnum.PROCESS.getCode())) {
            // 1.1更新失败则直接抛异常
            throw new DcepException(ErrorEnum.UNKNOWN_EXCEPTION.getCode(),
                    ErrorEnum.UNKNOWN_EXCEPTION.getDescription());
        }
        // 2.保存报文至交易档案表
        commonRecordMapper.insert(new CommonRecordDO(clearingDTO, DtoUtil.obj2JsonStr(gwReqDTO)));
        // 3.更新超时控制表报文发送时间，重新计时
        commonStsctrlMapper.updateSendTime(new CommonStsctrlDO(clearingDTO.getClrMsgId(), new Date()));
        // 4.查询记账指令表
        AccountingInstrDO accountingInstrDO = accountingInstrMapper.selectByMsgIdPrepare(clearingDTO.getClrMsgId());
        log.info("resultReportProcess end msgId:{}, msgTp:{}", clearingDTO.getClrMsgId(), clearingDTO.clrMsgTp());
        return new Response<>(accountingInstrDO);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void fail(EnvelopeDTO<GwDTO> gwReqDTO, ErrorEnum errorEnum) {
        ClearingDTO clearingDTO = (ClearingDTO) gwReqDTO.body();
        Clearing clearing = clearingDTO.getClass().getAnnotation(Clearing.class);
        log.info("checkBusinessInfo fail msgId:{}, msgTp:{}, prcCd:{}, rjctInf:{}, batId:{}", clearingDTO.getClrMsgId(),
                clearingDTO.clrMsgTp(), errorEnum.getCode(), errorEnum.getDescription(), clearingDTO.clrBatId());

        // 1.记录清算产品表
        settlementProdMapper
                .insert(new SettlementProdDO(clearingDTO, clearing, errorEnum));

        // 2.记录档案表，对原报文进行存档
        commonRecordMapper.insert(new CommonRecordDO(clearingDTO, DtoUtil.obj2JsonStr(gwReqDTO)));
    }


    private void updateStatus(SettlementProdDO settlementProdDO,String orgBizSts,
                                 AccountingInstrDO accountingInstrDO,String actgSts){


        // 更新记账指令表
        if(accountingInstrMapper.updateActgSts(accountingInstrDO, actgSts)!=1) {
            throw new DcepException(ErrorEnum.NO_MATCH_ORIGNAL.getCode(),ErrorEnum.NO_MATCH_ORIGNAL.getDescription());
        }
        //更新结算产品表
        if(settlementProdMapper.updateBizSts(settlementProdDO,orgBizSts) != 1){
            throw new DcepException(ErrorEnum.NO_MATCH_ORIGNAL.getCode(),ErrorEnum.NO_MATCH_ORIGNAL.getDescription());
        }

        if(ClearingStatusEnum.PRESUME_FAILED.getCode().equals(settlementProdDO.getBizSts())){
            //生成909报文，插入存储转发表
            ClearingDTO origClearingDTO = (ClearingDTO) recordManager.resume(settlementProdDO.getMsgTp(),settlementProdDO.getMsgId());
            EnvelopeDTO<GwDTO> receiver = DtoUtil.assembly909Msg(settlementProdDO.getRecvPtyId(), settlementProdDO,origClearingDTO);
            EnvelopeDTO<GwDTO> sender = DtoUtil.assembly909Msg(settlementProdDO.getSendPtyId(), settlementProdDO,origClearingDTO);
            GwDTO gwDTOReceiver = receiver.getSoapBody().getT();
            GwDTO gwDTOSender = sender.getSoapBody().getT();
            storageForwardManager.saveForInst(gwDTOReceiver.fetchMsgId(), MsgTpEnum.FINALNOTICE.getCode(), settlementProdDO.getRecvPtyId(),receiver);
            storageForwardManager.saveForInst(gwDTOSender.fetchMsgId(), MsgTpEnum.FINALNOTICE.getCode(), settlementProdDO.getSendPtyId(),sender);
        }else{
            //插入存储转发表
            storageForwardManager.saveForInst(accountingInstrDO, false);
        }

        if(settlementProdDO.finished()){
            //删除超时控制表
            commonStsctrlMapper.deleteByPrimaryKey(new CommonStsctrlDO(accountingInstrDO.getMsgId()));
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void presumeFail(SettlementProdDO settlementProdDO,AccountingInstrDO accountingInstrDO) {
        settlementProdDO.setBizSts(ClearingStatusEnum.PRESUME_FAILED.getCode());
        settlementProdDO.setBizPrcCd(WholesaleErrorEnum.BUSI_PSM_CODE.getCode());
        settlementProdDO.setBizPrcInf(WholesaleErrorEnum.BUSI_PSM_CODE.getDescription());
        accountingInstrDO.setActgSts(ActgStsEnum.FAILED.getCode());
        updateStatus(settlementProdDO,ClearingStatusEnum.PROCESS.getCode(),accountingInstrDO,ActgStsEnum.PROCESS.getCode());
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void payReject(SettlementProdDO settlementProdDO,AccountingInstrDO accountingInstrDO) {
        settlementProdDO.setBizSts(ClearingStatusEnum.FAILED.getCode());
        settlementProdDO.setBizPrcCd(WholesaleErrorEnum.BUSI_REJT.getCode());
        settlementProdDO.setBizPrcInf(WholesaleErrorEnum.BUSI_REJT.getDescription());
        accountingInstrDO.setActgSts(ActgStsEnum.FAILED.getCode());
        updateStatus(settlementProdDO,ClearingStatusEnum.PROCESS.getCode(),accountingInstrDO,ActgStsEnum.PROCESS.getCode());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void finished(AccountingInstrDO accountingInstrDO) {
        SettlementProdDO settlementProdDO = new SettlementProdDO();
        settlementProdDO.setMsgId(accountingInstrDO.getMsgId());
        settlementProdDO.setBizSts(CommonUtil.actgStsToBizSts(accountingInstrDO.getActgSts()));
        if (ActgStsEnum.SUCCESS.getCode().equals(accountingInstrDO.getActgSts())){
            settlementProdDO.setSttlmDt(accountingInstrDO.getActgDt());
        }
        updateStatus(settlementProdDO, ClearingStatusEnum.WAIT_SETTLE.getCode(), accountingInstrDO, ActgStsEnum.PROCESS.getCode());
    }
}

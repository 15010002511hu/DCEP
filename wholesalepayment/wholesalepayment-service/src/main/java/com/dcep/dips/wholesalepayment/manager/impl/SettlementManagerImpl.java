package com.dcep.dips.wholesalepayment.manager.impl;

import cn.hutool.core.util.StrUtil;
import com.dcep.common.exception.DcepException;
import com.dcep.dips.wholesalepayment.common.utils.DtoUtil;
import com.dcep.dips.wholesalepayment.dal.mapper.AccountingInstrMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.CommonStsctrlMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.SettlementProdMapper;
import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import com.dcep.dips.wholesalepayment.dal.model.CommonStsctrlDO;
import com.dcep.dips.wholesalepayment.dal.model.SettlementProdDO;
import com.dcep.dips.wholesalepayment.dal.model.StorageForwardDO;
import com.dcep.dips.wholesalepayment.enums.*;
import com.dcep.dips.wholesalepayment.manager.SettlementManager;
import com.dcep.dips.wholesalepayment.manager.StorageForwardManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Component
public class SettlementManagerImpl implements SettlementManager {

    @Autowired
    private SettlementProdMapper settlementProdMapper;

    @Autowired
    private AccountingInstrMapper accountingInstrMapper;

    @Autowired
    private CommonStsctrlMapper commonStsctrlMapper;
    @Autowired
    private StorageForwardManager storageForwardManager;


//
//    @Override
//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
//    public Response<StorageForwardDO> fail(SettlementProdDO settle,AccountingInstrDO accountingInstrDO, boolean out) {
//
//
//        accountingInstrDO.setGmtModified(new Date());
//        accountingInstrDO.setActgSts(ActgStsEnum.FAILED.getCode());
//
//        // 更新记账指令表为失败
//        if (accountingInstrMapper.updateActgSts(accountingInstrDO, ActgStsEnum.PROCESS.getCode()) != 1) {
//            throw new DcepException(ErrorEnum.NO_MATCH_ORIGNAL.getCode(),ErrorEnum.NO_MATCH_ORIGNAL.getDescription());
//        }
//        // 更新结算产品表为失败
//        SettlementProdDO updateSettle = new SettlementProdDO(accountingInstrDO.getMsgId(),ClearingStatusEnum.FAILED, accountingInstrDO.getActgDt());
//        if(ClearingStatusEnum.WAIT_SETTLE.getCode().equals(settle.getBizSts())){
//            if (settlementProdMapper.updateBizSts(updateSettle, ClearingStatusEnum.WAIT_SETTLE.getCode()) != 1) {
//                throw new DcepException(ErrorEnum.NO_MATCH_ORIGNAL.getCode(),ErrorEnum.NO_MATCH_ORIGNAL.getDescription());
//            }
//        }else if(ClearingStatusEnum.PROCESS.getCode().equals(settle.getBizSts())){
//            if (settlementProdMapper.updateBizSts(updateSettle, ClearingStatusEnum.PROCESS.getCode()) != 1) {
//                throw new DcepException(ErrorEnum.NO_MATCH_ORIGNAL.getCode(),ErrorEnum.NO_MATCH_ORIGNAL.getDescription());
//            }
//        }else{
//            throw new DcepException(WholesaleErrorEnum.BIZSTS_NOT_SUPPORTED.getCode(),WholesaleErrorEnum.BIZSTS_NOT_SUPPORTED.getDescription());
//        }
//
//        // 删除交易控制表记录
//        commonStsctrlMapper.deleteByPrimaryKey(new CommonStsctrlDO(accountingInstrDO.getMsgId()));
//
//        if(out){
//            // 失败，插入存储转发表
//            storageForwardManager.saveForInst(accountingInstrDO, true);
//        }else {
//
//        }
//        return null;
//    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public StorageForwardDO reversalSuccess(AccountingInstrDO accountingInstrDO, SettlementProdDO settle,boolean endReturn){

        //非结算排队状态和待付款方确认状态，不允许撤销。
        String actgSts = accountingInstrDO.getActgSts();
        if(!ActgStsEnum.QUEUED.getCode().equals(actgSts)
                && !ActgStsEnum.PROCESS.getCode().equals(actgSts) ){
            throw new DcepException(WholesaleErrorEnum.BIZSTS_NOT_SUPPORTED.getCode(),
                    WholesaleErrorEnum.BIZSTS_NOT_SUPPORTED.getDescription());
        }
        String bizSts = settle.getBizSts();
        if(!ClearingStatusEnum.PROCESS.getCode().equals(bizSts)
                && !ClearingStatusEnum.SETTLE_QUEUE.getCode().equals(bizSts)){
            throw new DcepException(WholesaleErrorEnum.BIZSTS_NOT_SUPPORTED.getCode(),
                    WholesaleErrorEnum.BIZSTS_NOT_SUPPORTED.getDescription());
        }

        //设置结算日期
        if(StrUtil.isBlank(settle.getSttlmDt())){
            settle.setSttlmDt(accountingInstrDO.getActgDt());
        }
        //设置结算状态。日终退回和主动撤销状态不一样
        if(endReturn){
            settle.setBizSts(ClearingStatusEnum.DAYEND_RETURN.getCode());
            accountingInstrDO.setActgSts(ActgStsEnum.QUEUE_RETURNED.getCode());
        }else{
            accountingInstrDO.setActgSts(ActgStsEnum.QUEUE_CANCELED.getCode());
            settle.setBizSts(ClearingStatusEnum.CANCELLED.getCode());
        }

        // 更新记账指令表为已取消
        if (accountingInstrMapper.updateActgSts(accountingInstrDO, actgSts) != 1) {
            throw new DcepException(ErrorEnum.NO_MATCH_ORIGNAL.getCode(),ErrorEnum.NO_MATCH_ORIGNAL.getDescription());
        }
        // 更新结算产品表为失败
        if (settlementProdMapper.updateBizSts(settle,bizSts) != 1) {
            throw new DcepException(ErrorEnum.NO_MATCH_ORIGNAL.getCode(),ErrorEnum.NO_MATCH_ORIGNAL.getDescription());
        }

        //删除超时控制表
        commonStsctrlMapper.deleteByPrimaryKey(new CommonStsctrlDO(accountingInstrDO.getMsgId()));

        return reversalForward(endReturn,accountingInstrDO,settle);

    }

    //存储转发报文
    private StorageForwardDO reversalForward(boolean endReturn,AccountingInstrDO accountingInstrDO, SettlementProdDO settle) {

        //TODO 中央对手方报文怎么发？

        if (MsgTpEnum.CCP_REQUEST.getCode().equals(accountingInstrDO.getMsgTp())){
            //TODO  dcep.131.001.01的报文通知不是200报文，需要特殊处理。
            return null;
        }else if(MsgTpEnum.FMI_SSS_REQUEST.getCode().equals(accountingInstrDO.getMsgTp())){
            //TODO  dcep.135.001.01的报文通知不是200报文，需要特殊处理。
            return null;
        }else if(MsgTpEnum.MER_SSS_REQUEST.getCode().equals(accountingInstrDO.getMsgTp())){
            //TODO  dcep.137.001.01的报文通知不是200报文，需要特殊处理。
            return null;
        }else if(MsgTpEnum.ORDR_CONF_RESPONSE.getCode().equals(accountingInstrDO.getMsgTp())){
            // 262报文是收款机构作为发送方,付款机构作为接受方,业务撤销是接收方发起的,跟其他报文有点相反
            //给发送结构发送200报文
            storageForwardManager.saveForInst(DtoUtil.assembly200Msg(accountingInstrDO,settle.getSendPtyId()),settle.getSendPtyId());
            if(endReturn){
                //给接受机构发送200报文
                storageForwardManager.saveForInst(DtoUtil.assembly200Msg(accountingInstrDO,settle.getRecvPtyId()),settle.getRecvPtyId());
            }
            return null;
        }

        //收款类交易
        if(ClearingProdCdtDbtIndEnum.DBIT.getCode().equals(settle.getCdtDbtInd())){
            //给接受机构发送200报文
            storageForwardManager.saveForInst(DtoUtil.assembly200Msg(accountingInstrDO,settle.getRecvPtyId()),settle.getRecvPtyId());

            if(endReturn){
                //如果是日终退回，给发送机构发送200报文
                storageForwardManager.saveForInst(DtoUtil.assembly200Msg(accountingInstrDO,settle.getSendPtyId()),settle.getSendPtyId());
            }
        }
        //付款类交易
        if (ClearingProdCdtDbtIndEnum.CRDT.getCode().equals(settle.getCdtDbtInd())){
            if(endReturn) {
                //如果是日终退回，给付款机构发送200报文
                storageForwardManager.saveForInst(DtoUtil.assembly200Msg(accountingInstrDO,settle.getSendPtyId()),settle.getSendPtyId());
            }
        }

        return  null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void settleFinshed(AccountingInstrDO accountingInstrDO) {
        accountingInstrDO.setGmtModified(new Date());
        if (ActgStsEnum.SUCCESS.getCode().equals(accountingInstrDO.getActgSts())){
            accountingInstrDO.setActgPrcCd(ErrorEnum.BUSI_SUCCESS.getCode());
            accountingInstrDO.setActgPrcInf(ErrorEnum.BUSI_SUCCESS.getDescription());
        }
        String prcInf = accountingInstrDO.getActgPrcInf();
        //错误信息，防止超长
        if(prcInf!=null){
            accountingInstrDO.setActgPrcInf((StrUtil.isNotBlank(prcInf) && StrUtil.length(prcInf)>105)?prcInf.substring(0,105):prcInf);
        }

        // 更新记账指令表
        if (accountingInstrMapper.updateActgSts(accountingInstrDO, ActgStsEnum.PROCESS.getCode()) != 1) {
            throw new DcepException(ErrorEnum.NO_MATCH_ORIGNAL.getCode(),ErrorEnum.NO_MATCH_ORIGNAL.getDescription());
        }

        //更新结算记录
        SettlementProdDO settle = new SettlementProdDO(accountingInstrDO.getMsgId(),accountingInstrDO.getActgSts());
        if (ActgStsEnum.PROCESS.getCode().equals(accountingInstrDO.getActgSts())){
            SettlementProdDO settlementProdDO = settlementProdMapper.selectByPrimaryKey(new SettlementProdDO(accountingInstrDO.getMsgId()));
            settle.setBizSts(settlementProdDO.getBizSts());
        }
        settle.setBizPrcCd(accountingInstrDO.getActgPrcCd());
        if(prcInf!=null){
            settle.setBizPrcInf((StrUtil.isNotBlank(prcInf) && StrUtil.length(prcInf)>315)?prcInf.substring(0,315):prcInf);
        }
        if (settlementProdMapper.updateBizSts(settle,ClearingStatusEnum.WAIT_SETTLE.getCode() ) != 1) {
            throw new DcepException(ErrorEnum.NO_MATCH_ORIGNAL.getCode(),ErrorEnum.NO_MATCH_ORIGNAL.getDescription());
        }

        //交易终态
        if(settle.finished()){
            //删除超时控制
            commonStsctrlMapper.deleteByPrimaryKey(new CommonStsctrlDO(settle.getMsgId()));
        }
    }
}

/*
 * pbcdci.cn Inc.
 * Copyright © 2025 PBCDCI All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.manager.impl;

import com.dcep.common.exception.DcepException;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.dips.wholesalepayment.common.utils.DtoUtil;
import com.dcep.dips.wholesalepayment.dal.mapper.*;
import com.dcep.dips.wholesalepayment.dal.model.*;
import com.dcep.dips.wholesalepayment.dto.ClearingDTO;
import com.dcep.dips.wholesalepayment.dto.acctrans.AccountingReportDTO;
import com.dcep.dips.wholesalepayment.dto.dc203.Dcep20301001DTO;
import com.dcep.dips.wholesalepayment.enums.ErrorEnum;
import com.dcep.dips.wholesalepayment.enums.LockEnum;
import com.dcep.dips.wholesalepayment.enums.MsgTpEnum;
import com.dcep.dips.wholesalepayment.manager.DcepToMbridgeConvertManager;
import com.dcep.dips.wholesalepayment.manager.MbridgeManager;
import com.dcep.dips.wholesalepayment.manager.StorageForwardManager;
import com.dcep.dips.wholesalepayment.manager.WholesaleReportManager;
import com.dcep.gateway.mcbdc.dto.soap.GenericEnvelopeDTO;
import com.dcep.gateway.mcbdc.dto.soap.GenericGwDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Component
@Slf4j
public class WholesaleReportManagerImpl implements WholesaleReportManager {
    @Autowired
    AccountingInstrMapper accountingInstrMapper;
    @Autowired
    SettlementProdMapper settlementProdMapper;
    @Autowired
    CommonStsctrlMapper commonStsctrlMapper;
    @Autowired
    StorageForwardManager storageForwardManager;
    @Autowired
    CommonRecordMapper commonRecordMapper;
    @Autowired
    DcepToMbridgeConvertManager dcep2mBridge;
    @Autowired
    DcepToMbridgeConvertManager dcepToMbridgeConvertManager;
    @Autowired
    ChainTransMapper chainTransMapper;
    @Autowired
    MbridgeManager mbridgeManager;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void process(AccountingReportDTO accountingReportDTO, AccountingInstrDO orgAccountingInstrDO) {
        log.info("report process transId:{}", accountingReportDTO.getTransId());
        // 1.查询原始结算状态
        String orgBizSts = settlementProdMapper.selectByPrimaryKey(new SettlementProdDO(orgAccountingInstrDO.getMsgId())).getBizSts();

        // 2.更新记账指令表
        String orgActgSts = orgAccountingInstrDO.getActgSts();
        orgAccountingInstrDO.setActgSts(accountingReportDTO.getAccountingStatus());
        orgAccountingInstrDO.setActgDt(accountingReportDTO.getAccountingDate());
        orgAccountingInstrDO.setGmtModified(new Date());
        if (accountingInstrMapper.updateActgSts(orgAccountingInstrDO, orgActgSts) != 1) {
            throw new DcepException(ErrorEnum.NO_MATCH_ORIGNAL.getCode(),
                    ErrorEnum.NO_MATCH_ORIGNAL.getDescription());
        }
        // 3.更新结算产品表
        if (settlementProdMapper.updateBizSts(new SettlementProdDO(orgAccountingInstrDO.getMsgId(), accountingReportDTO.getAccountingStatus()), orgBizSts) != 1) {
            throw new DcepException(ErrorEnum.NO_MATCH_ORIGNAL.getCode(),
                    ErrorEnum.NO_MATCH_ORIGNAL.getDescription());
        }
        // 4.删除交易控制表记录
        commonStsctrlMapper.deleteByPrimaryKey(new CommonStsctrlDO(orgAccountingInstrDO.getMsgId()));

        // 5.存储转发
        storageForwardManager.saveForInst(orgAccountingInstrDO, true);
        log.info("report process end transId:{}", accountingReportDTO.getTransId());
    }

    @Override
    public void processForMbridge(AccountingReportDTO accountingReportDTO, AccountingInstrDO orgAccountingInstrDO) {
        log.info("report process transId:{}", accountingReportDTO.getTransId());
        String orgMsgId = orgAccountingInstrDO.getMsgId();
        // 查询区块链对接产品表获取原mcbsMsgId
        String orgMcbsMsgId = chainTransMapper.selectByMsgId(orgMsgId).getOutMsgId();
        // 查询结算产品表获取原结算状态
        String orgBizSts = settlementProdMapper.selectByPrimaryKey(new SettlementProdDO(orgAccountingInstrDO.getMsgId())).getBizSts();

        String orgActgSts = orgAccountingInstrDO.getActgSts();
        orgAccountingInstrDO.setActgSts(accountingReportDTO.getAccountingStatus());
        orgAccountingInstrDO.setActgDt(accountingReportDTO.getAccountingDate());
        orgAccountingInstrDO.setGmtModified(new Date());
        // todo 处理码、处理信息
        // 查询交易档案表
        CommonRecordDO commonRecordDO = commonRecordMapper.selectByPrimaryKey(new CommonRecordDO(orgMsgId, orgAccountingInstrDO.getMsgTp()));
        TypeReference typeReference = new TypeReference<EnvelopeDTO<Dcep20301001DTO>>() {
        };
        Object object = DtoUtil.jsonStr2Obj(commonRecordDO.getDocument(), typeReference);
        EnvelopeDTO<GwDTO> envelopeDTO = (EnvelopeDTO<GwDTO>) object;
        ClearingDTO clearingDTO = (ClearingDTO) envelopeDTO.body();
        // 报文转换
        GenericEnvelopeDTO<GenericGwDTO> mBridgeReqEnvelopeDTO = dcep2mBridge.convertRequest(envelopeDTO);
        // 补充货币桥报文信息
        mBridgeReqEnvelopeDTO = dcepToMbridgeConvertManager.requestSupplement(mBridgeReqEnvelopeDTO, envelopeDTO, orgMcbsMsgId);
        // 插入存储转发表
        StorageForwardDO storageForwardDO = storageForwardManager.saveForMbridge(mBridgeReqEnvelopeDTO, mBridgeReqEnvelopeDTO.body().fetchMsgId(),
                MsgTpEnum.CDT_REQUEST_ASYN.getCode(), clearingDTO.clrRecvPtyId(), LockEnum.LOCK.getCode());
        // 更新状态并发报
        mbridgeManager.updateAndSend(mBridgeReqEnvelopeDTO, envelopeDTO, orgAccountingInstrDO, storageForwardDO, orgActgSts, orgBizSts);
        log.info("report process end transId:{}", accountingReportDTO.getTransId());
    }
}

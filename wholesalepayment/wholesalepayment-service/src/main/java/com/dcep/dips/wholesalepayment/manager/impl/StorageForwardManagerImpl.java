package com.dcep.dips.wholesalepayment.manager.impl;

import com.alibaba.fastjson.JSON;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.common.utils.DtoUtil;
import com.dcep.dips.wholesalepayment.dal.mapper.CommonRecordMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.SettlementProdMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.StorageForwardMapper;
import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import com.dcep.dips.wholesalepayment.dal.model.CommonRecordDO;
import com.dcep.dips.wholesalepayment.dal.model.SettlementProdDO;
import com.dcep.dips.wholesalepayment.dal.model.StorageForwardDO;
import com.dcep.dips.wholesalepayment.dto.ClearingDTO;
import com.dcep.dips.wholesalepayment.dto.FundingDTO;
import com.dcep.dips.wholesalepayment.dto.RecordDTO;
import com.dcep.dips.wholesalepayment.dto.acctrans.ZeroOutReqDTO;
import com.dcep.dips.wholesalepayment.dto.dc112.Dcep11200101DTO;
import com.dcep.dips.wholesalepayment.dto.dc114.Dcep11400101DTO;
import com.dcep.dips.wholesalepayment.dto.dc201.Dcep20101001DTO;
import com.dcep.dips.wholesalepayment.dto.dc212.Dcep21201001DTO;
import com.dcep.dips.wholesalepayment.dto.dc221.Dcep22101001DTO;
import com.dcep.dips.wholesalepayment.dto.dc226.Dcep22601001DTO;
import com.dcep.dips.wholesalepayment.dto.dc227.Dcep22701001DTO;
import com.dcep.dips.wholesalepayment.dto.dc263.Dcep26301001DTO;
import com.dcep.dips.wholesalepayment.dto.dc281.Dcep28101001DTO;
import com.dcep.dips.wholesalepayment.dto.dc801.Dcep80101001DTO;
import com.dcep.dips.wholesalepayment.enums.ActgStsEnum;
import com.dcep.dips.wholesalepayment.enums.LockEnum;
import com.dcep.dips.wholesalepayment.enums.MsgTpEnum;
import com.dcep.dips.wholesalepayment.enums.StorageTypeEnum;
import com.dcep.dips.wholesalepayment.manager.RecordManager;
import com.dcep.dips.wholesalepayment.manager.StorageForwardManager;
import com.dcep.gateway.mcbdc.dto.soap.GenericEnvelopeDTO;
import com.dcep.gateway.mcbdc.dto.soap.GenericGwDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

/**
 * 存储转发管理类
 */
@Component
@Slf4j
public class StorageForwardManagerImpl implements StorageForwardManager {
    @Autowired
    private StorageForwardMapper storageForwardMapper;

    @Autowired
    SettlementProdMapper settlementProdMapper;

    @Autowired
    CommonRecordMapper commonRecordMapper;
    @Autowired
    private RecordManager recordManager;

    private static final TypeReference<EnvelopeDTO<Dcep11200101DTO>> DCEP112_TYPE_REFERENCE = new TypeReference<EnvelopeDTO<Dcep11200101DTO>>() {
    };
    private static final TypeReference<EnvelopeDTO<Dcep11400101DTO>> DCEP114_TYPE_REFERENCE = new TypeReference<EnvelopeDTO<Dcep11400101DTO>>() {
    };
    private static final TypeReference<EnvelopeDTO<Dcep20101001DTO>> DCEP201_TYPE_REFERENCE = new TypeReference<EnvelopeDTO<Dcep20101001DTO>>() {
    };
    private static final TypeReference<EnvelopeDTO<Dcep21201001DTO>> DCEP212_TYPE_REFERENCE = new TypeReference<EnvelopeDTO<Dcep21201001DTO>>() {
    };
    private static final TypeReference<EnvelopeDTO<Dcep22101001DTO>> DCEP221_TYPE_REFERENCE = new TypeReference<EnvelopeDTO<Dcep22101001DTO>>() {
    };
    private static final TypeReference<EnvelopeDTO<Dcep22601001DTO>> DCEP226_TYPE_REFERENCE = new TypeReference<EnvelopeDTO<Dcep22601001DTO>>() {
    };
    private static final TypeReference<EnvelopeDTO<Dcep22701001DTO>> DCEP227_TYPE_REFERENCE = new TypeReference<EnvelopeDTO<Dcep22701001DTO>>() {
    };
    private static final TypeReference<EnvelopeDTO<Dcep26301001DTO>> DCEP263_TYPE_REFERENCE = new TypeReference<EnvelopeDTO<Dcep26301001DTO>>() {
    };
    private static final TypeReference<EnvelopeDTO<Dcep28101001DTO>> DCEP281_TYPE_REFERENCE = new TypeReference<EnvelopeDTO<Dcep28101001DTO>>() {
    };
    private static final TypeReference<EnvelopeDTO<Dcep80101001DTO>> DCEP801_TYPE_REFERENCE = new TypeReference<EnvelopeDTO<Dcep80101001DTO>>() {
    };

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveForHvpsZeroOut(String msgId, String msgTp, ZeroOutReqDTO zeroOutReqDTO) {
        // 获取发送时间及处理超时时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, -Constant.ASYNC_PUSH_INTERVAL);
        Date sendTime = calendar.getTime();
        calendar.add(Calendar.SECOND, Constant.TOTAL_PUSH_TIME);
        Date processTimeout = calendar.getTime();

        // 插入《存储转发表》，异步发送准备金系统
        StorageForwardDO storageForwardDO = new StorageForwardDO(
                msgId,
                msgTp,
                StorageTypeEnum.HVPS_ZERO_OUT.getCode(),
                zeroOutReqDTO.encode(),
                LockEnum.UNLOCK.getCode(),
                null,
                sendTime,
                Constant.ASYNC_PUSH_INTERVAL,
                processTimeout);

        storageForwardMapper.insert(storageForwardDO);
    }

    @Override
    public void saveForActgAdjustDecrease(FundingDTO fundingDTO) {
        // 获取发送时间及处理超时时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, -Constant.ASYNC_PUSH_INTERVAL);
        Date sendTime = calendar.getTime();
        calendar.add(Calendar.SECOND, Constant.TOTAL_PUSH_TIME);
        Date processTimeout = calendar.getTime();

        // 插入《存储转发表》，异步发送《结算钱包》
        StorageForwardDO storageForwardDO = new StorageForwardDO(
                fundingDTO.msgId(),
                fundingDTO.msgTp(),
                StorageTypeEnum.ACTG_ADJUST_DECREASE.getCode(),
                fundingDTO.encode(),
                LockEnum.UNLOCK.getCode(),
                null,
                sendTime,
                Constant.ASYNC_PUSH_INTERVAL,
                processTimeout);

        storageForwardMapper.insert(storageForwardDO);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveForHvpsAdjustDecrease(FundingDTO fundingDTO) {
        // 获取发送时间及处理超时时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, -Constant.ASYNC_PUSH_INTERVAL);
        Date sendTime = calendar.getTime();
        calendar.add(Calendar.SECOND, Constant.TOTAL_PUSH_TIME);
        Date processTimeout = calendar.getTime();

        // 插入《存储转发表》，异步发送《准备金》
        StorageForwardDO storageForwardDO = new StorageForwardDO(
                fundingDTO.msgId(),
                fundingDTO.msgTp(),
                StorageTypeEnum.HVPS_ADJUST_DECREASE.getCode(),
                fundingDTO.encode(),
                LockEnum.UNLOCK.getCode(),
                null,
                sendTime,
                Constant.ASYNC_PUSH_INTERVAL,
                processTimeout);

        storageForwardMapper.insert(storageForwardDO);
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveForInst(AccountingInstrDO accountingInstrDO, boolean isPassDebitConfirm) {
        String actgSts = accountingInstrDO.getActgSts();
        // select原交易
        SettlementProdDO settlementProdDO = settlementProdMapper.selectByPrimaryKey(new SettlementProdDO(accountingInstrDO.getMsgId()));
        // 根据不同记账状态进行处理
        if (ActgStsEnum.FAILED.getCode().equals(actgSts)) {
            // 原报文类型是262，需要通知收付双方
            if (MsgTpEnum.ORDR_CONF_RESPONSE.getCode().equals(accountingInstrDO.getMsgTp())) {
                insertSingle(accountingInstrDO, settlementProdDO.getSendPtyId());
                insertSingle(settlementProdDO);
                // 原报文类型是211、225且通过借记确认，需要通知收付双方
            } else if (isPassDebitConfirm && (MsgTpEnum.DBT_REQUEST.getCode().equals(accountingInstrDO.getMsgTp()) || MsgTpEnum.COV_REQUREST.getCode().equals(accountingInstrDO.getMsgTp()))) {
                insertSingle(accountingInstrDO, settlementProdDO.getRecvPtyId());
                insertSingle(settlementProdDO);
                // 原报文类型是211、225但未通过借记确认，将应答报文转发给发起方
            } else if (!isPassDebitConfirm && (MsgTpEnum.DBT_REQUEST.getCode().equals(accountingInstrDO.getMsgTp()) || MsgTpEnum.COV_REQUREST.getCode().equals(accountingInstrDO.getMsgTp()))) {
                insertSingle(settlementProdDO);
                // 其他场景，仅通知发起方
            } else {
                insertSingle(accountingInstrDO, settlementProdDO.getSendPtyId());
            }

        }
        if (ActgStsEnum.SUCCESS.getCode().equals(actgSts)) {
            // 原报文类型是211、225，需要通知收付双方
            if (MsgTpEnum.DBT_REQUEST.getCode().equals(accountingInstrDO.getMsgTp()) || MsgTpEnum.COV_REQUREST.getCode().equals(accountingInstrDO.getMsgTp())
                    || MsgTpEnum.ORDR_CONF_RESPONSE.getCode().equals(accountingInstrDO.getMsgTp())) {
                insertSingle(accountingInstrDO, settlementProdDO.getRecvPtyId());
                insertSingle(settlementProdDO);
            } else {
                insertSingle(accountingInstrDO, settlementProdDO.getSendPtyId());
                // 成功记账，且为贷记类业务，向接收方转发原报文
                insertSingle(settlementProdDO);
            }
        }
        if (ActgStsEnum.QUEUED.getCode().equals(actgSts)) {
            // 原报文类型是211、225、262，需要通知收付双方
            if (MsgTpEnum.DBT_REQUEST.getCode().equals(accountingInstrDO.getMsgTp()) || MsgTpEnum.COV_REQUREST.getCode().equals(accountingInstrDO.getMsgTp())
                    || MsgTpEnum.ORDR_CONF_RESPONSE.getCode().equals(accountingInstrDO.getMsgTp())) {
                insertBoth(accountingInstrDO, settlementProdDO);
            } else {
                insertSingle(accountingInstrDO, settlementProdDO.getSendPtyId());
            }
        }
    }

    private void insertBoth(AccountingInstrDO accountingInstrDO, SettlementProdDO settlementProdDO) {
        insertSingle(accountingInstrDO, settlementProdDO.getSendPtyId());
        insertSingle(accountingInstrDO, settlementProdDO.getRecvPtyId());
    }

    private void insertSingle(AccountingInstrDO accountingInstrDO, String receiver) {
        // 获取发送时间及处理超时时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, -Constant.ASYNC_PUSH_INTERVAL);
        Date sendTime = calendar.getTime();
        calendar.add(Calendar.SECOND, Constant.TOTAL_PUSH_TIME);
        Date processTimeout = calendar.getTime();
        // 构造200报文并插入存储转发表
        EnvelopeDTO<GwDTO> msg200ToSender = DtoUtil.assembly200Msg(accountingInstrDO, receiver);
        StorageForwardDO storageForwardDO = new StorageForwardDO(
                msg200ToSender.body().fetchMsgId(),
                MsgTpEnum.SETTLE_NOTICE.getCode(),
                StorageTypeEnum.INST_ASYNC_NOTICE.getCode(),
                DtoUtil.obj2JsonStr(msg200ToSender),
                LockEnum.UNLOCK.getCode(),
                receiver,
                sendTime,
                Constant.ASYNC_PUSH_INTERVAL,
                processTimeout);
        storageForwardMapper.insert(storageForwardDO);
    }

    private void insertSingle(SettlementProdDO settlementProdDO) {
        // 获取发送时间及处理超时时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, -Constant.ASYNC_PUSH_INTERVAL);
        Date sendTime = calendar.getTime();
        calendar.add(Calendar.SECOND, Constant.TOTAL_PUSH_TIME);
        Date processTimeout = calendar.getTime();

        String msgId = settlementProdDO.getMsgId();
        String msgTp = convertMsgTp(settlementProdDO.getMsgTp());
        // 根据msgId和msgTp查询出对应的报文
        CommonRecordDO commonRecordDO = commonRecordMapper.selectByPrimaryKey(new CommonRecordDO(msgId, msgTp));
        if(commonRecordDO==null){
            msgTp = MsgTpEnum.TXN_STATE_RESPONSE.getCode();
//            commonRecordDO = commonRecordMapper.selectByPrimaryKey(new CommonRecordDO(msgId, MsgTpEnum.TXN_STATE_RESPONSE.getCode()));
        }
        EnvelopeDTO<? extends GwDTO> envelopeDTO;
        // 根据不同报文类型组装报文
        if (MsgTpEnum.CDT_REQUEST.getCode().equals(msgTp)) {
            envelopeDTO = DtoUtil.jsonStr2Obj(commonRecordDO.getDocument(),
                    DCEP201_TYPE_REFERENCE);
        } else if (MsgTpEnum.DBT_RESPONSE.getCode().equals(msgTp)){
            envelopeDTO = DtoUtil.jsonStr2Obj(commonRecordDO.getDocument(),
                    DCEP212_TYPE_REFERENCE);
        } else if (MsgTpEnum.RECOV_REQUEST.getCode().equals(msgTp)){
            envelopeDTO = DtoUtil.jsonStr2Obj(commonRecordDO.getDocument(),
                    DCEP221_TYPE_REFERENCE);
        } else if (MsgTpEnum.COV_RESPONSE.getCode().equals(msgTp)){
            envelopeDTO = DtoUtil.jsonStr2Obj(commonRecordDO.getDocument(),
                    DCEP226_TYPE_REFERENCE);
        } else if (MsgTpEnum.CDT_COV_REQUREST.getCode().equals(msgTp)){
            envelopeDTO = DtoUtil.jsonStr2Obj(commonRecordDO.getDocument(),
                    DCEP227_TYPE_REFERENCE);
        } else if (MsgTpEnum.ORDR_CONF_RESULT_NOTICE.getCode().equals(msgTp)){
            envelopeDTO = DtoUtil.jsonStr2Obj(commonRecordDO.getDocument(),
                    DCEP263_TYPE_REFERENCE);
        } else if (MsgTpEnum.REFUND_REQUREST.getCode().equals(msgTp)){
            envelopeDTO = DtoUtil.jsonStr2Obj(commonRecordDO.getDocument(),
                    DCEP281_TYPE_REFERENCE);
        } else if (MsgTpEnum.CRDT_ADJ_REQUREST.getCode().equals(msgTp)){
            envelopeDTO = DtoUtil.jsonStr2Obj(commonRecordDO.getDocument(),
                    DCEP801_TYPE_REFERENCE);
        } else if (MsgTpEnum.FI_CDT.getCode().equals(msgTp)){
            envelopeDTO = DtoUtil.jsonStr2Obj(commonRecordDO.getDocument(),
                    DCEP112_TYPE_REFERENCE);
        } else if (MsgTpEnum.FI_RETUNE.getCode().equals(msgTp)){
            envelopeDTO = DtoUtil.jsonStr2Obj(commonRecordDO.getDocument(),
                    DCEP114_TYPE_REFERENCE);
        } else if (MsgTpEnum.TXN_STATE_RESPONSE.getCode().equals(msgTp)){
//            envelopeDTO = DtoUtil.jsonStr2Obj(commonRecordDO.getDocument(),
//                    DCEP114_TYPE_REFERENCE);
            ClearingDTO origClearingDTO = (ClearingDTO) recordManager.resume(settlementProdDO.getMsgTp(),settlementProdDO.getMsgId());
            EnvelopeDTO<GwDTO> gwDTOEnvelopeDTO = DtoUtil.assembly909Msg(settlementProdDO.getSendPtyId(), settlementProdDO, origClearingDTO);
            GwDTO gwDTO = gwDTOEnvelopeDTO.getSoapBody().getT();
            saveForInst(gwDTO.fetchMsgId(),MsgTpEnum.FINALNOTICE.getCode(), settlementProdDO.getSendPtyId(),gwDTOEnvelopeDTO);
            return;
        }else {
            throw new DcepException(com.dcep.common.enums.ErrorEnum.UNKNOWN_EXCEPTION.getCode(), "未知的报文类型");
        }

        ClearingDTO clearingDTO = (ClearingDTO) envelopeDTO.body();
        if (settlementProdDO.getSttlmDt() != null) {
            // 填充结算日期
            clearingDTO.fillSttlmDt(settlementProdDO.getSttlmDt().substring(0, 4) + "-" + settlementProdDO.getSttlmDt().substring(4, 6) +
                    "-" + settlementProdDO.getSttlmDt().substring(6));
        }
        // 填充业务状态字段
        clearingDTO.fillPlatPrcSts(settlementProdDO.getBizSts());
        // 将clearingDTO重新填充到envelopeDTO
        envelopeDTO = new EnvelopeDTO<>(envelopeDTO.getSoapHeader(), (GwDTO) clearingDTO);


        StorageForwardDO storageForwardDO = new StorageForwardDO(
                envelopeDTO.body().fetchMsgId(),
                msgTp,
                StorageTypeEnum.INST_ASYNC_NOTICE.getCode(),
                DtoUtil.obj2JsonStr(envelopeDTO),
                LockEnum.UNLOCK.getCode(),
                null,
                sendTime,
                Constant.ASYNC_PUSH_INTERVAL,
                processTimeout);
        storageForwardMapper.insert(storageForwardDO);

    }

    private String convertMsgTp(String msgTp) {
        if (msgTp.equals(MsgTpEnum.DBT_REQUEST.getCode())) {
            return MsgTpEnum.DBT_RESPONSE.getCode();
        }
        if (msgTp.equals(MsgTpEnum.COV_REQUREST.getCode())) {
            return MsgTpEnum.COV_RESPONSE.getCode();
        }
        if (msgTp.equals(MsgTpEnum.ORDR_CONF_RESPONSE.getCode())) {
            return MsgTpEnum.ORDR_CONF_RESULT_NOTICE.getCode();
        }
        return msgTp;
    }

    @Override
    public StorageForwardDO saveForMbridge(GenericEnvelopeDTO<GenericGwDTO> genericGwReqDTO, String msgId, String msgTp, String reciver, String lockFlag) {
        // 获取发送时间及处理超时时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, -Constant.ASYNC_PUSH_INTERVAL);
        Date sendTime = calendar.getTime();
        calendar.add(Calendar.SECOND, Constant.TOTAL_PUSH_TIME);
        Date processTimeout = calendar.getTime();

        // 插入《存储转发表》
        StorageForwardDO storageForwardDO = new StorageForwardDO(
                msgId,
                msgTp,
                StorageTypeEnum.MCBS_ASYNC_NOTICE.getCode(),
                DtoUtil.obj2JsonStr(genericGwReqDTO),
                lockFlag,
                reciver,
                sendTime,
                Constant.ASYNC_PUSH_INTERVAL,
                processTimeout);

        storageForwardMapper.insert(storageForwardDO);

        return storageForwardDO;
    }

    @Override
    public StorageForwardDO saveForInst(EnvelopeDTO<GwDTO> gwReqDTO, String reciver) {
        RecordDTO recordDTO = (RecordDTO) gwReqDTO.body();
        // 获取发送时间及处理超时时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, -Constant.ASYNC_PUSH_INTERVAL);
        Date sendTime = calendar.getTime();
        calendar.add(Calendar.SECOND, Constant.TOTAL_PUSH_TIME);
        Date processTimeout = calendar.getTime();

        // 插入《存储转发表》
        StorageForwardDO storageForwardDO = new StorageForwardDO(
                recordDTO.recMsgId(),
                recordDTO.recMsgTp(),
                StorageTypeEnum.INST_ASYNC_NOTICE.getCode(),
                DtoUtil.obj2JsonStr(gwReqDTO),
                LockEnum.UNLOCK.getCode(),
                reciver,
                sendTime,
                Constant.ASYNC_PUSH_INTERVAL,
                processTimeout);

        storageForwardMapper.insert(storageForwardDO);

        return storageForwardDO;
    }

    @Override
    public StorageForwardDO saveForInst(String msgId,String msgTp, String reciver,EnvelopeDTO<GwDTO> req) {
        // 获取发送时间及处理超时时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, -Constant.ASYNC_PUSH_INTERVAL);
        Date sendTime = calendar.getTime();
        calendar.add(Calendar.SECOND, Constant.TOTAL_PUSH_TIME);
        Date processTimeout = calendar.getTime();

        // 插入《存储转发表》
        StorageForwardDO storageForwardDO = new StorageForwardDO(
                msgId,
                msgTp,
                StorageTypeEnum.INST_ASYNC_NOTICE.getCode(),
                DtoUtil.obj2JsonStr(req),
                LockEnum.UNLOCK.getCode(),
                reciver,
                sendTime,
                Constant.ASYNC_PUSH_INTERVAL,
                processTimeout);

        storageForwardMapper.insert(storageForwardDO);

        return storageForwardDO;
    }

    @Override
    public void saveForInst(AccountingInstrDO accountingInstrDO, String receiver) {
        insertSingle(accountingInstrDO, receiver);
    }

    @Override
    public void unLock(boolean status, StorageForwardDO storageForwardDO) {
        log.info("UnLock Start msgId:{}, Status:{}", storageForwardDO.getMsgId(), status);
        storageForwardDO.setSendTime(new Date());
        // 通知成功删除 否则 更新状态
        boolean result = status ? storageForwardMapper.deleteByPrimaryKey(storageForwardDO) == 1
                : storageForwardMapper.updateForUnLock(storageForwardDO) == 1;
        log.info("UnLock End  msgId:{}, result:{}", storageForwardDO.getMsgId(), result);
    }


}

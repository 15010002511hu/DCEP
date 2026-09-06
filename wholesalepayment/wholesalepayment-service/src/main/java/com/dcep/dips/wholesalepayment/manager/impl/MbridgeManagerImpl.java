package com.dcep.dips.wholesalepayment.manager.impl;

import com.dcep.common.exception.DcepException;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.common.utils.ThreadPoolUtils;
import com.dcep.dips.acctrans.dto.accounting.transfer.TransferRespDTO;
import com.dcep.dips.common.constant.CommonConstant;
import com.dcep.dips.wholesalepayment.annotation.Clearing;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.common.utils.DtoUtil;
import com.dcep.dips.wholesalepayment.common.utils.IdUtils;
import com.dcep.dips.wholesalepayment.dal.bo.OnChainTransInfoBO;
import com.dcep.dips.wholesalepayment.dal.mapper.*;
import com.dcep.dips.wholesalepayment.dal.model.*;
import com.dcep.dips.wholesalepayment.dto.ClearingDTO;
import com.dcep.dips.wholesalepayment.enums.*;
import com.dcep.dips.wholesalepayment.manager.AccountingManager;
import com.dcep.dips.wholesalepayment.manager.DcepToMbridgeConvertManager;
import com.dcep.dips.wholesalepayment.manager.MbridgeManager;
import com.dcep.dips.wholesalepayment.manager.StorageForwardManager;
import com.dcep.gateway.mcbdc.api.GwoutService;
import com.dcep.gateway.mcbdc.dto.soap.GenericEnvelopeDTO;
import com.dcep.gateway.mcbdc.dto.soap.GenericGwDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Component
public class MbridgeManagerImpl implements MbridgeManager {

    @Autowired
    OnchainPaymentTransMapper onchainPaymentTransMapper;

    @Autowired
    AccountingInstrMapper accountingInstrMapper;

    @Autowired
    CommonRecordMapper commonRecordMapper;

    @Autowired
    SettlementProdMapper settlementProdMapper;

    @Autowired
    ChainTransMapper chainTransMapper;

    @Autowired
    CommonStsctrlMapper commonStsctrlMapper;

    @Autowired
    AccountingManager accountingManager;

    @Autowired
    StorageForwardManager storageForwardManager;

    @Autowired
    SystemStatusDOMapper systemStatusMapper;

    @Autowired
    MbridgeManager mbridgeManager;

    @Autowired
    DcepToMbridgeConvertManager dcepToMbridgeConvertManager;

    @DubboReference
    GwoutService mBridgeGwoutService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public AccountingInstrDO prepare( EnvelopeDTO<GwDTO> gwReqDTO, GenericEnvelopeDTO<GenericGwDTO> genericGwReqDTO) {
        ClearingDTO clearingDTO = (ClearingDTO) gwReqDTO.body();
        Clearing clearing = clearingDTO.getClass().getAnnotation(Clearing.class);

        // 上下文调用信息
        RpcContext context = RpcContext.getContext();

        // 1.登记结算产品表
        context.set(Constant.PREPARE_INSERT_STEP, Constant.INSERT_STEP_ONE);
        settlementProdMapper.insert(new SettlementProdDO(clearingDTO, clearing, ClearingStatusEnum.ACCEPTED.getCode()));

        context.set(Constant.PREPARE_INSERT_STEP, Constant.INSERT_STEP_TWO);
        // 2.登记区块链对接产品表
        chainTransMapper.insert(new ChainTransDO(clearingDTO, McbsStatusEnum.PDNG.getCode()));

        // 3.保存记录至交易档案表
        if (Constant.DIRECTION_FROM_HLHT_TO_MBRIDGE.equals(clearingDTO.clrMbridgeInf().getMsgDrn())){
            commonRecordMapper.insert(new CommonRecordDO(clearingDTO, DtoUtil.obj2JsonStr(gwReqDTO)));
        } else {
            commonRecordMapper.insert(new CommonRecordDO(clearingDTO, DtoUtil.obj2JsonStr(genericGwReqDTO), clearingDTO.clrMbridgeInf()));
        }

        // 4.保存记录至交易控制表
        commonStsctrlMapper.insert(new CommonStsctrlDO(clearingDTO, clearing));

        // 5.登记记账指令表
        context.set(Constant.PREPARE_INSERT_STEP, Constant.INSERT_STEP_THREE);
        String curSysDt = systemStatusMapper.selectCurSysDt(CommonConstant.SysCode.WHOLESALE);
        String transId = IdUtils.randomTransIdWithBizDt(curSysDt);
        AccountingInstrDO accountingInstrDO = new AccountingInstrDO(transId, gwReqDTO);
        accountingInstrMapper.insert(accountingInstrDO);

        return accountingInstrDO;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void pendingFinish(GenericEnvelopeDTO<GenericGwDTO> genericGwReqDTO, EnvelopeDTO<GwDTO> gwReqDTO, AccountingInstrDO accountingInstrDO, ClearingStatusEnum clearingStatusEnum,
                              boolean updateAcctFlag, boolean saveDcep200Flag, boolean saveDcepFlag, boolean saveMcbsFlag, String reciver, String mcbsReciver) {
        log.info("pendingFinish start, msgId={}", accountingInstrDO.getMsgId());
        // 1.结算产品表
        settlementProdMapper.updateByMsgId(new SettlementProdDO(accountingInstrDO.getMsgId(), clearingStatusEnum, accountingInstrDO.getActgPrcCd(), accountingInstrDO.getActgPrcInf()));

        // 2.记账指令表
        if (updateAcctFlag) {
            accountingInstrMapper.updateAccountingInstr(accountingInstrDO); // todo 受理成功->终态；待结算->终态
        }

        // 3.删除交易控制表
        commonStsctrlMapper.deleteByPrimaryKey(new CommonStsctrlDO(accountingInstrDO.getMsgId()));

        // 4.发送dcep200-插入存储转发表
        if (saveDcep200Flag){
            if (ClearingStatusEnum.FAILED.getCode().equals(clearingStatusEnum.getCode())) {
                accountingInstrDO.setActgSts(ActgStsEnum.FAILED.getCode());
            }
            storageForwardManager.saveForInst(accountingInstrDO, reciver);
        }

        // 5.发送机构-插入存储转发表
        if (saveDcepFlag){
            storageForwardManager.saveForInst(gwReqDTO, reciver);
        }

        // 6.发送货币桥-插入存储转发表
        if (saveMcbsFlag){
            storageForwardManager.saveForMbridge(genericGwReqDTO, genericGwReqDTO.body().fetchMsgId(), Constant.MCBS_MSGTYPE_203, mcbsReciver, LockEnum.UNLOCK.getCode());
        }
        log.info("pendingFinish end, msgId={}", accountingInstrDO.getMsgId());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Response<AccountingInstrDO> onChainRecord(OnChainTransInfoBO onChainTransInfoBO, boolean commonRecordFlag, Object object) {
        // 上下文调用信息
        RpcContext context = RpcContext.getContext();

        // 1.登记《链上支付产品表》
        context.set(Constant.PREPARE_INSERT_STEP, Constant.INSERT_STEP_ONE);
        OnChainPaymentTransDO onchainPaymentTransDO = new OnChainPaymentTransDO(onChainTransInfoBO);
        onchainPaymentTransMapper.insert(onchainPaymentTransDO);

        // 2.生成交易流水号，登记记账指令表
        context.set(Constant.PREPARE_INSERT_STEP, Constant.INSERT_STEP_TWO);
        String curSysDt = systemStatusMapper.selectCurSysDt(CommonConstant.SysCode.WHOLESALE);
        String transId = IdUtils.randomTransIdWithBizDt(curSysDt);
        AccountingInstrDO accountingInstrDO = new AccountingInstrDO(transId, onChainTransInfoBO, curSysDt);
        accountingInstrMapper.insert(accountingInstrDO);

        // 3.若渠道为货币桥，则登记档案表
        if (commonRecordFlag) {
            context.set(Constant.PREPARE_INSERT_STEP, Constant.INSERT_STEP_THREE);
            commonRecordMapper.insert(new CommonRecordDO(onChainTransInfoBO.getMsgId(), onChainTransInfoBO.getMsgTp(), DtoUtil.obj2JsonStr(object)));
        }

        return new Response<>(accountingInstrDO);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateAndSend(GenericEnvelopeDTO<GenericGwDTO> genericGwReqDTO, EnvelopeDTO<GwDTO> gwReqDTO, AccountingInstrDO accountingInstrDO,
                              StorageForwardDO storageForwardDO, String orgActgSts, String orgBizSts) {
        log.info("updateAndSend start, msgId={}", accountingInstrDO.getMsgId());
        String accountingStatus = accountingInstrDO.getActgSts();

        // 更新记账指令表，当更新不为一条记录时抛异常
        if (accountingInstrMapper.updateActgSts(accountingInstrDO, orgActgSts) != 1) {
            throw new DcepException(ErrorEnum.NO_MATCH_ORIGNAL.getCode(),
                    ErrorEnum.NO_MATCH_ORIGNAL.getDescription());
        }

        // 更新结算产品表
        settlementProdMapper.updateBizSts(new SettlementProdDO(accountingInstrDO), orgBizSts);

        // 若结算钱包返回成功，则调用货币桥网关发送mcbs200
        if (ActgStsEnum.SUCCESS.getCode().equals(accountingStatus)) {
            log.info("send mBridgeGwoutService start, msgId={}", accountingInstrDO.getMsgId());
            boolean status = mbridgeManager.hlht2MbridgeGateway(genericGwReqDTO);
            log.info("send mBridgeGwoutService end, msgId={}", accountingInstrDO.getMsgId());
            storageForwardManager.unLock(status, storageForwardDO);
            log.info("updateAndSend end, msgId={}", accountingInstrDO.getMsgId());
            return;
        }

        // 若结算钱包返回失败或排队，插入存储转发表，发送dcep200
        storageForwardManager.saveForInst(accountingInstrDO, gwReqDTO.getSoapHeader().getSender());

        // 若结算钱包返回失败，则删除交易控制表记录
        if (ActgStsEnum.FAILED.getCode().equals(accountingStatus)){
            commonStsctrlMapper.deleteByPrimaryKey(new CommonStsctrlDO(accountingInstrDO.getMsgId()));
        }
        log.info("updateAndSend end, msgId={}", accountingInstrDO.getMsgId());
    }

    @Override
    public boolean hlht2MbridgeGateway(GenericEnvelopeDTO<GenericGwDTO> genericGwReqDTO) {
        // 调用货币桥网关
        try {
            Response<GenericEnvelopeDTO<GenericGwDTO>> genericGwResp = mBridgeGwoutService.execute(genericGwReqDTO);
            if (!genericGwResp.isSuccess()){
                log.error("invocation mBridgeGwoutService failed, errCode={}, errMsg={}", genericGwResp.getErrorCode(), genericGwResp.getErrorMsg());
                return false;
            }
            log.info("mBridgeGwoutService response:{}", genericGwResp);
            return true;
        } catch (Exception e) {
            log.error(Constant.INTERNAL_ERROR_MSG, e);
            return false;
        }
    }

    @Override
    public void hlht2MbridgeAsync(GenericEnvelopeDTO<GenericGwDTO> genericGwReqDTO, EnvelopeDTO<GwDTO> gwReqDTO, AccountingInstrDO accountingInstrDO, String mcbsMsgId) {
        ClearingDTO clearingDTO = (ClearingDTO) gwReqDTO.body();
        // 补充货币桥报文信息
        genericGwReqDTO = dcepToMbridgeConvertManager.requestSupplement(genericGwReqDTO, gwReqDTO, mcbsMsgId);

        // 插入存储转发表
        StorageForwardDO storageForwardDO = storageForwardManager.saveForMbridge(genericGwReqDTO, genericGwReqDTO.body().fetchMsgId(),
                clearingDTO.clrMbridgeInf().getMcbsMsgTp(), clearingDTO.clrRecvPtyId(), LockEnum.LOCK.getCode());

        if (MsgTpEnum.CDT_REQUEST_ASYN.getCode().equals(clearingDTO.clrMsgTp())) {
            // dcep203
            // 调用结算钱包
            mbridgeManager.transfer(accountingInstrDO, true);
            // 更新状态并发报
            mbridgeManager.updateAndSend(genericGwReqDTO, gwReqDTO, accountingInstrDO, storageForwardDO,
                    ActgStsEnum.PROCESS.getCode(), ClearingStatusEnum.ACCEPTED.getCode());
        } else {
            // dcep213
            // 发送货币桥网关
            log.info("send mBridgeGwoutService start, msgId={}", clearingDTO.getClrMsgId());
            boolean status = mbridgeManager.hlht2MbridgeGateway(genericGwReqDTO);
            log.info("send mBridgeGwoutService end, msgId={}", clearingDTO.getClrMsgId());
            storageForwardManager.unLock(status, storageForwardDO);
        }
    }

    @Override
    public void mBridge2HlhtAsync(GenericEnvelopeDTO<GenericGwDTO> genericGwReqDTO, EnvelopeDTO<GwDTO> gwReqDTO, AccountingInstrDO accountingInstrDO) {
        ClearingDTO clearingDTO = (ClearingDTO) gwReqDTO.body();

        // 系统日期
//        String sysWorkDt = systemStatusMapper.selectCurSysDt(CommonConstant.SysCode.WHOLESALE);
        // 异步调用结算钱包
        mbridgeManager.transfer(accountingInstrDO, false);
        // 结算钱包返回成功-》通知双方
        if (ActgStsEnum.SUCCESS.getCode().equals(accountingInstrDO.getActgSts())) {
            // 组mcbs203报文
            GenericEnvelopeDTO<GenericGwDTO> mcbs203GenericGwReqDTO = DtoUtil.assemblyMcbs203(genericGwReqDTO, McbsStatusEnum.SUCD.getCode(),
                    null, clearingDTO.clrMbridgeInf());
            // 补充dcep203/213报文要素
//            mbridgeToDcepConvertManager.requestSupplement(genericGwReqDTO, gwReqDTO, accountingInstrDO.getActgDt(), sysWorkDt);
            // 终态维护
            mbridgeManager.pendingFinish(mcbs203GenericGwReqDTO, gwReqDTO, accountingInstrDO, ClearingStatusEnum.SETTLED, true, false,
                    true, true, clearingDTO.clrRecvPtyId(), clearingDTO.clrSendPtyId());
        // 结算钱包返回失败-》通知货币桥
        } else {
            // 组mcbs203报文
            GenericEnvelopeDTO<GenericGwDTO> mcbs203GenericGwReqDTO = DtoUtil.assemblyMcbs203(genericGwReqDTO, McbsStatusEnum.FAIL.getCode(),
                    Constant.MBRIDGE_DEFAULT_ERRCODE + ":" + Constant.MBRIDGE_DEFAULT_ERRMSG, clearingDTO.clrMbridgeInf());
            // 终态维护
            mbridgeManager.pendingFinish(mcbs203GenericGwReqDTO, null, accountingInstrDO, ClearingStatusEnum.FAILED, true, false,
                    false, true, null, clearingDTO.clrSendPtyId());
        }
    }

    @Override
    public void transfer(AccountingInstrDO accountingInstrDO, boolean queueFlag) {
        String settlementControlFlag = queueFlag ? "20100000" : "20000000"; // todo
        accountingInstrDO.setFromWlltId(null);
        accountingInstrDO.setToWlltId(null);
        Response<TransferRespDTO> result = accountingManager.transfer(accountingInstrDO, null, settlementControlFlag);
        if (!result.isSuccess()) {
            // 调用结算钱包失败
            throw new DcepException(result.getErrorCode(),
                    result.getErrorMsg());
        }
        TransferRespDTO transferRespDTO = result.getResult();
        String accountingStatus = transferRespDTO.getAccountingStatus();
        if(queueFlag){
            if (!ActgStsEnum.SUCCESS.getCode().equals(accountingStatus)
                    && !ActgStsEnum.FAILED.getCode().equals(accountingStatus)
                    && !ActgStsEnum.QUEUED.getCode().equals(accountingStatus)) {
                log.error("acctrans transfer API return error, accountingStatus={} ", accountingStatus);
                throw new DcepException(ErrorEnum.UNKNOWN_EXCEPTION.getCode(),
                        ErrorEnum.UNKNOWN_EXCEPTION.getDescription());
            }
        } else {
            if (!ActgStsEnum.SUCCESS.getCode().equals(accountingStatus)
                    && !ActgStsEnum.FAILED.getCode().equals(accountingStatus)) {
                log.error("acctrans transfer API return error, accountingStatus={} ", accountingStatus);
                throw new DcepException(ErrorEnum.UNKNOWN_EXCEPTION.getCode(),
                        ErrorEnum.UNKNOWN_EXCEPTION.getDescription());
            }
        }

        accountingInstrDO.setActgSts(accountingStatus);
        accountingInstrDO.setActgDt(transferRespDTO.getAccountingDate());
        accountingInstrDO.setGmtModified(new Date());
        accountingInstrDO.setActgPrcCd(transferRespDTO.getBizCode());
        accountingInstrDO.setActgPrcInf(transferRespDTO.getBizMsg());
    }
}

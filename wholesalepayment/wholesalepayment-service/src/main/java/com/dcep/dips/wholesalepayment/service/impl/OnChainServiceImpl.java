package com.dcep.dips.wholesalepayment.service.impl;

import com.dcep.common.exception.DcepException;
import com.dcep.common.model.Response;
import com.dcep.common.utils.ValidateUtils;
import com.dcep.dips.acctrans.dto.accounting.transfer.TransferRespDTO;
import com.dcep.dips.wholesalepayment.api.OnChainService;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.common.utils.CheckUtil;
import com.dcep.dips.wholesalepayment.common.utils.DtoUtil;
import com.dcep.dips.wholesalepayment.common.utils.ErrorCodeUtil;
import com.dcep.dips.wholesalepayment.dal.bo.OnChainTransInfoBO;
import com.dcep.dips.wholesalepayment.dal.mapper.AccountingInstrMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.OnchainPaymentTransMapper;
import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import com.dcep.dips.wholesalepayment.dal.model.OnChainPaymentTransDO;
import com.dcep.dips.wholesalepayment.dto.chain.OnChainTransReqDTO;
import com.dcep.gateway.mcbdc.dto.mcbs708.Mcbs70800101DTO;
import com.dcep.dips.wholesalepayment.enums.ActgStsEnum;
import com.dcep.dips.wholesalepayment.enums.McbsStatusEnum;
import com.dcep.dips.wholesalepayment.enums.WholesaleErrorEnum;
import com.dcep.dips.wholesalepayment.manager.AccountingManager;
import com.dcep.dips.wholesalepayment.manager.MbridgeManager;
import com.dcep.gateway.mcbdc.dto.soap.GenericEnvelopeDTO;
import com.dcep.gateway.mcbdc.dto.soap.GenericGwDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import java.util.Date;

@Slf4j
@DubboService
public class OnChainServiceImpl implements OnChainService {

    @Autowired
    MbridgeManager mbridgeManager;

    @Autowired
    OnchainPaymentTransMapper onchainPaymentTransMapper;

    @Autowired
    AccountingManager accountingManager;

    @Autowired
    AccountingInstrMapper accountingInstrMapper;
    @Override
    public Response<String> chainAccounting(OnChainTransReqDTO onChainTransReqDTO) throws DcepException {
        try {
            ValidateUtils.validate(onChainTransReqDTO);
        }catch (DcepException e){
            log.error("onChain syncAccounting service validate error: ", e);
            return new Response<>(false, null, e.getCode(), e.getMessage());
        }
        log.info("onChain syncAccounting service start: onChainTransReqDTO={}", onChainTransReqDTO);
        // 1.业务检查
        WholesaleErrorEnum errorEnum = CheckUtil.checkOnChainBusinessInfo(onChainTransReqDTO.getDbtrPtyId(), onChainTransReqDTO.getCdtrPtyId());
        if (errorEnum != null) {
            return new Response<>(false, null, errorEnum.getCode(), errorEnum.getDescription());
        }

        // 2.调用交易登记组件
        //创建交易信息对象
        OnChainTransInfoBO onChainTransInfoBO = new OnChainTransInfoBO(onChainTransReqDTO);
        AccountingInstrDO accountingInstrDO;
        try {
            accountingInstrDO = record(onChainTransInfoBO, false, null);
        } catch (DcepException e) {
            return new Response<>(false, null, e.getCode(),
                    e.getMessage());
        }

        // 3.同步调起结算钱包服务
        try {
            transfer(accountingInstrDO, onChainTransInfoBO.getUseCurrentSystemFlag());
        } catch (DcepException e) {
            return new Response<>(false, null, e.getCode(),
                    e.getMessage());
        }

        log.info("onChain syncAccounting service end: msgId={}", onChainTransReqDTO.getMsgId());
        return new Response<>(true, "成功");
    }

    @Override
    public Response<GenericEnvelopeDTO<GenericGwDTO>> mbridgeAccounting(GenericEnvelopeDTO<GenericGwDTO> genericGwReqDTO) throws DcepException {
        try {
            ValidateUtils.validate(genericGwReqDTO);
        }catch (DcepException e){
            log.error("onMbridge syncAccounting service validate error: ", e);
            return new Response<>(DtoUtil.assemblyMcbs900(genericGwReqDTO, McbsStatusEnum.FAIL.getCode(), ErrorCodeUtil.getMbridgeStsRsnInfAddtlInf(e.getCode() + "--" + e.getMessage())));
        }
        Mcbs70800101DTO mcbs7081DTO = (Mcbs70800101DTO) genericGwReqDTO.body();
        log.info("onMbridge syncAccounting service start: msgId={}", mcbs7081DTO.getGrpHdr().getMsgId());
        // 1.业务检查
        WholesaleErrorEnum errorEnum = CheckUtil.checkOnChainBusinessInfo(mcbs7081DTO.getTxInf().getDetailInf().getPayerAgt().getFinInstnId().getClrSysMmbId().getMmbId(),
                mcbs7081DTO.getTxInf().getDetailInf().getPayeeAgt().getFinInstnId().getClrSysMmbId().getMmbId());
        if (errorEnum != null) {
            return new Response<>(DtoUtil.assemblyMcbs900(genericGwReqDTO, McbsStatusEnum.FAIL.getCode(), ErrorCodeUtil.getMbridgeStsRsnInfAddtlInf(errorEnum.getCode())));
        }

        // 2.调用交易登记组件
        //创建交易信息对象
        OnChainTransInfoBO onChainTransInfoBO = new OnChainTransInfoBO(mcbs7081DTO);
        AccountingInstrDO accountingInstrDO;
        try {
            accountingInstrDO = record(onChainTransInfoBO, true, mcbs7081DTO);
        } catch (DcepException e) {
            return new Response<>(DtoUtil.assemblyMcbs900(genericGwReqDTO, McbsStatusEnum.FAIL.getCode(), ErrorCodeUtil.getMbridgeStsRsnInfAddtlInf(e.getCode())));
        }

        // 3.同步调起结算钱包服务
        try {
            transfer(accountingInstrDO, null);
        } catch (DcepException e) {
            return new Response<>(DtoUtil.assemblyMcbs900(genericGwReqDTO, McbsStatusEnum.FAIL.getCode(), ErrorCodeUtil.getMbridgeStsRsnInfAddtlInf(e.getCode())));
        }

        log.info("onMbridge syncAccounting service end: msgId={}", mcbs7081DTO.getGrpHdr().getMsgId());
        return new Response<>(DtoUtil.assemblyMcbs900(genericGwReqDTO, McbsStatusEnum.SUCD.getCode(), Constant.MBRIDGE_REQHDLG_DESC_SUCC));
    }

    private AccountingInstrDO record(OnChainTransInfoBO onChainTransInfoBO, boolean commonRecordFlag, Object object) throws DcepException {
        AccountingInstrDO accountingInstrDO;
        try {
            log.info("syncAccounting service record start: msgId={}", onChainTransInfoBO.getMsgId());
            Response<AccountingInstrDO> response = mbridgeManager.onChainRecord(onChainTransInfoBO, commonRecordFlag, object);
            log.info("syncAccounting service record end: msgId={}", onChainTransInfoBO.getMsgId());
            accountingInstrDO = response.getResult();
        } catch (DuplicateKeyException ex) { // 若主键冲突，幂等要素不一致抛异常
            log.info("duplicationException: msgId={}, msgTp={}", onChainTransInfoBO.getMsgId(), onChainTransInfoBO.getMsgTp());

            // 中心生成的transId重复（出现几率非常小），直接抛异常
            if (Constant.INSERT_STEP_TWO.equals(RpcContext.getContext().get(Constant.PREPARE_INSERT_STEP))) {
                log.error("transId insert repeat: msgId={}, insertStep={}", onChainTransInfoBO.getMsgId(), Constant.INSERT_STEP_TWO);
                throw new DcepException(WholesaleErrorEnum.BUSI_DB_KEYWORD_REPEAT.getCode(),
                        WholesaleErrorEnum.BUSI_DB_KEYWORD_REPEAT.getDescription());
            }

            // 请求报文msgId重复，则为请求机构发起重试，进行幂等要素检查
            // 1.查询原交易
            OnChainPaymentTransDO onchainPaymentTransDO = onchainPaymentTransMapper.selectByPrimaryKey(onChainTransInfoBO.getMsgId());
            // 2.幂等要素检查
            if (!CheckUtil.checkOnChainIdempotent(onChainTransInfoBO, onchainPaymentTransDO)) {
                throw new DcepException(WholesaleErrorEnum.NO_MATCH_ORIGNAL.getCode(),
                        WholesaleErrorEnum.NO_MATCH_ORIGNAL.getDescription());
            }
            // 查询记账指令表获取需要发送给结算钱包系统的transId
            accountingInstrDO = accountingInstrMapper.selectByMsgIdPrepare(onchainPaymentTransDO.getMsgId());
        }

        return accountingInstrDO;
    }

    private void transfer(AccountingInstrDO actgInstrDO, String useCurrentSystemFlag) throws DcepException {
        actgInstrDO.setFromWlltId(null);
        actgInstrDO.setToWlltId(null);
        Response<TransferRespDTO> transferResp = accountingManager.transfer(actgInstrDO, useCurrentSystemFlag, "20000000");
        if (!transferResp.isSuccess()) {
            // 结算钱包：通讯异常(状态不明)
            throw new DcepException(transferResp.getErrorCode(),
                    transferResp.getErrorMsg());
        }
        TransferRespDTO transferRespDTO = transferResp.getResult();
        // 更新记账指令表
        actgInstrDO.setGmtModified(new Date());
        actgInstrDO.setActgSts(transferRespDTO.getAccountingStatus());
        actgInstrDO.setActgDt(transferRespDTO.getAccountingDate());
        // 更新不为一条记录时抛异常
        if (accountingInstrMapper.updateAccountingInstr(actgInstrDO) != 1) {
            throw new DcepException(WholesaleErrorEnum.NO_MATCH_ORIGNAL.getCode(),
                    WholesaleErrorEnum.NO_MATCH_ORIGNAL.getDescription());
        }
        if (!ActgStsEnum.SUCCESS.getCode().equals(transferRespDTO.getAccountingStatus())) {
            // 结算钱包返回记账状态不为成功时，抛异常
            throw new DcepException(WholesaleErrorEnum.ACCT_REJECT.getCode(),
                    WholesaleErrorEnum.ACCT_REJECT.getDescription());
        }
        log.info("update accountingInstr succ, transId={}, msgId={}", actgInstrDO.getTransId(), actgInstrDO.getMsgId());
    }

}

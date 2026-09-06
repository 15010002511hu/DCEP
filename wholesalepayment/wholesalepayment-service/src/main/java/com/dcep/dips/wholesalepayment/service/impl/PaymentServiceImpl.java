/*
 * pbcdci.cn Inc.
 * Copyright © 2025 PBCDCI All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.service.impl;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.common.model.msg.Dcep90000101DTO;
import com.dcep.common.utils.DcepDateUtils;
import com.dcep.common.utils.MsgIdUtil;
import com.dcep.common.utils.ThreadPoolUtils;
import com.dcep.dips.acctrans.dto.accounting.transfer.TransferRespDTO;
import com.dcep.dips.common.constant.CommonConstant;
import com.dcep.dips.wholesalepayment.annotation.Clearing;
import com.dcep.dips.wholesalepayment.api.PaymentService;
import com.dcep.dips.wholesalepayment.aspect.GwReq;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.common.utils.CheckUtil;
import com.dcep.dips.wholesalepayment.common.utils.IdUtils;
import com.dcep.dips.wholesalepayment.common.utils.SoapHeaderUtil;
import com.dcep.dips.wholesalepayment.dal.mapper.SettlementProdMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.SystemStatusDOMapper;
import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import com.dcep.dips.wholesalepayment.dal.model.SettlementProdDO;
import com.dcep.dips.wholesalepayment.dto.ClearingDTO;
import com.dcep.dips.wholesalepayment.dto.ClearingStatus;
import com.dcep.dips.wholesalepayment.enums.ClearingCheckModeEnum;
import com.dcep.dips.wholesalepayment.enums.ClearingStatusEnum;
import com.dcep.dips.wholesalepayment.enums.ErrorEnum;
import com.dcep.dips.wholesalepayment.enums.MsgTpEnum;
import com.dcep.dips.wholesalepayment.manager.AccountingManager;
import com.dcep.dips.wholesalepayment.manager.CommonManager;
import com.dcep.dips.wholesalepayment.manager.PaymentManager;
import com.dcep.dips.wholesalepayment.utils.AmtUtils;
import com.dcep.dips.wholesalepayment.utils.InfoCacheUtil;
import com.dcep.supergw.api.GwoutService;
import com.dcepex.trace.support.async.TraceExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.dao.DuplicateKeyException;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

@Slf4j
@DubboService
public class PaymentServiceImpl implements PaymentService {

    @NacosValue(value = "${credttm_interval}", autoRefreshed = true)
    String CREDTTM_INTERVAL;

    @NacosValue(value = "${msgid_date_interval}", autoRefreshed = true)
    String MSGID_DATE_INTERVAL;

    ExecutorService asyncPool = new TraceExecutorService(ThreadPoolUtils.getThreadPoolFixSize(100, "clearing-async"));
    @Resource
    PaymentManager paymentManager;
    @Resource
    SettlementProdMapper settlementProdMapper;
    @Resource
    CommonManager commonManager;
    @Resource
    SystemStatusDOMapper systemStatusMapper;
    @Resource
    AccountingManager accountingManager;
    @DubboReference
    GwoutService gwoutService;

    @Override
    @GwReq
    public Response<EnvelopeDTO<GwDTO>> dbtrSettle(EnvelopeDTO<GwDTO> in) {
        ClearingDTO clearingDTO = (ClearingDTO) in.body();
        Clearing clearing = clearingDTO.getClass().getAnnotation(Clearing.class);
        log.info("dbtrSettle msgId:{}, msgTp:{}", clearingDTO.getClrMsgId(), clearingDTO.clrMsgTp());

        // 1.业务检查：检查收付款机构状态，检查系统工作日期
        Response<EnvelopeDTO<GwDTO>> checkResult = checkBusinessInfo(in, clearingDTO, clearing);
        if (checkResult != null){
            // 返回900报文
            return checkResult;
        }

        String curSysDt = systemStatusMapper.selectCurSysDt(CommonConstant.SysCode.WHOLESALE);
        AccountingInstrDO accountingInstrDO = new AccountingInstrDO(IdUtils.randomTransIdWithBizDt(curSysDt), clearingDTO, clearing);
        try {
            // 2.调用交易登记组件
            paymentManager.record(in, accountingInstrDO);
            log.info("paymentManager.record end: msgId={}", clearingDTO.getClrMsgId());
        } catch (DuplicateKeyException e) {
            // 3.幂等处理
            handleDuplicateKeyException(clearingDTO, clearing);
        }
        // 4.异步处理
        handleAsyncProcessing(accountingInstrDO, clearingDTO);
        // 5.同步应答受理成功的900报文
        return assembly900Msg(in, clearingDTO, ClearingStatusEnum.ACCEPTED.getCode(),
                ErrorEnum.BUSI_SUCCESS);
    }

    @Override
    @GwReq
    public Response<EnvelopeDTO<GwDTO>> cdtrSettle(EnvelopeDTO<GwDTO> in) {
        ClearingDTO clearingDTO = (ClearingDTO) in.body();
        Clearing clearing = clearingDTO.getClass().getAnnotation(Clearing.class);
        log.info("cdtrSettle msgId:{}, msgTp:{}", clearingDTO.getClrMsgId(), clearingDTO.clrMsgTp());

        // 1.业务检查：检查收付款机构状态，检查系统工作日期
        Response<EnvelopeDTO<GwDTO>> checkResult = checkBusinessInfo(in, clearingDTO, clearing);
        if (checkResult != null){
            // 返回900报文
            return checkResult;
        }

        String curSysDt = systemStatusMapper.selectCurSysDt(CommonConstant.SysCode.WHOLESALE);
        AccountingInstrDO accountingInstrDO = new AccountingInstrDO(IdUtils.randomTransIdWithBizDt(curSysDt), clearingDTO, clearing);
        try {
            // 2.调用交易登记组件
            paymentManager.record(in, accountingInstrDO);
            log.info("paymentManager.record end: msgId={}", clearingDTO.getClrMsgId());
        } catch (DuplicateKeyException e) {
            // 3.幂等处理
            handleDuplicateKeyException(clearingDTO, clearing);
        }

        try {
            asyncPool.execute(() -> {// 4.异步处理
                Response<EnvelopeDTO<GwDTO>> gwResult = invokeGwoutService(in);
                if (!gwResult.isSuccess()) {
                    // 调用网关失败，直接返回，待计时控制组件处理
                    log.info("invokeGwoutService fail msgId={}", clearingDTO.getClrMsgId());
                    return;
                }
                // 4.调用借记确认，借记确认成功后调用结算记账组件
                log.info("paymentManager.debitConfirm in async thread: msgId={}", clearingDTO.getClrMsgId());
                if (paymentManager.debitConfirm(gwResult.getResult(), accountingInstrDO)) {
                    // 5.借记确认成功，调用结算钱包系统transfer API
                    Response<TransferRespDTO> result = accountingManager.transfer(accountingInstrDO);
                    if (!result.isSuccess()) {
                        // 6.调用结算钱包失败，直接返回，待计时控制组件处理
                        log.info("clearing fail transId:{}", accountingInstrDO.getTransId());
                        return;
                    }
                    log.info("paymentManager.debitConfirm success msgId={}", clearingDTO.getClrMsgId());
                    paymentManager.clearing(accountingInstrDO, result.getResult());
                } else {
                    // 借记确认失败
                    log.info("paymentManager.debitConfirm fail msgId={}", clearingDTO.getClrMsgId());
                }
            });
        } catch (RejectedExecutionException e) {
            // 异步线程池已满，打印日志，待定时任务处理
            log.error("asyncPool is full,paymentManager.debitConfirm rejected MsgId:{}, Exception:{}", clearingDTO.getClrMsgId(), e.getMessage());
        }
        // 7.同步应答受理成功的900报文
        return assembly900Msg(in, clearingDTO, ClearingStatusEnum.ACCEPTED.getCode(),
                ErrorEnum.BUSI_SUCCESS);
    }

    @Override
    @GwReq
    public Response<GwDTO> orderConfirm(EnvelopeDTO<GwDTO> in) {
        ClearingDTO clearingDTO = (ClearingDTO) in.body();
        Clearing clearing = clearingDTO.getClass().getAnnotation(Clearing.class);
        log.info("orderConfirm msgId:{}, msgTp:{}", clearingDTO.getClrMsgId(), clearingDTO.clrMsgTp());
        // 如果262报文的业务回执状态是失败,则直接返回
        if (MsgTpEnum.ORDR_CONF_RESPONSE.getCode().equals(clearingDTO.clrMsgTp())
                && ClearingStatusEnum.FAILED.getCode().equals(clearingDTO.clrBizRspSts())) {
            return new Response<>(new ClearingStatus(ClearingStatusEnum.FAILED.getCode(), null));
        }
        // 1.业务检查：检查收付款机构状态，检查系统工作日期
        Response<EnvelopeDTO<GwDTO>> checkResult = checkBusinessInfo(in, clearingDTO, clearing);
        if (checkResult != null){
            // 返回900报文
            return new Response<>(checkResult.getResult().body());
        }
        try {
            String curSysDt = systemStatusMapper.selectCurSysDt(CommonConstant.SysCode.WHOLESALE);
            AccountingInstrDO accountingInstrDO = new AccountingInstrDO(IdUtils.randomTransIdWithBizDt(curSysDt), clearingDTO, clearing);
            // 2.调用交易登记组件
            paymentManager.record(in, accountingInstrDO);
            log.info("paymentManager.record end: msgId={}", clearingDTO.getClrMsgId());
            return new Response<>(new ClearingStatus(ClearingStatusEnum.PROCESS.getCode(), clearingDTO.clrBatId()));
        } catch (DuplicateKeyException e) {
            // 3.幂等处理
            SettlementProdDO origSettlementProdDO = handleDuplicateKeyException(clearingDTO, clearing);
            // 3.1.属于幂等交易,正常返回清算状态 ,使用数据库中原交易的批次号
            return new Response<>(new ClearingStatus(origSettlementProdDO.getBizSts(), origSettlementProdDO.getBatId()));
        }
    }

    @Override
    @GwReq
    public Response<EnvelopeDTO<GwDTO>> resultReport(EnvelopeDTO<GwDTO> in) {
        ClearingDTO clearingDTO = (ClearingDTO) in.body();
        log.info("resultReport msgSn:{}, msgTp:{}", in.getSoapHeader().getMsgSN(), clearingDTO.clrMsgTp());

        // 1.查询原交易SettlementProdDO
        SettlementProdDO origSettlementProdDO = settlementProdMapper
                .selectByPrimaryKey(new SettlementProdDO(clearingDTO.getClrMsgId()));

        // 2.检查原交易是否存在
        if (origSettlementProdDO == null) {
            log.error("checkOrig-No original: MsgId={}, MsgTp={}", clearingDTO.getClrMsgId(), clearingDTO.clrMsgTp());
            throw new DcepException(ErrorEnum.RESP_NO_MATCH_ORIGINAL_BUSI.getCode(),
                    ErrorEnum.RESP_NO_MATCH_ORIGINAL_BUSI.getDescription());
        }

        // 3.逻辑校验是否匹配原交易
        checkOrig(origSettlementProdDO, clearingDTO);

        // 4.如果原交易已为终态，则返回有对应处理码的900报文
        if (!ClearingStatusEnum.PROCESS.getCode().equals(origSettlementProdDO.getBizSts())) {
            return assembly900Msg(in, clearingDTO,
                    getNoticePrcSts(origSettlementProdDO.getBizSts()),
                    getNoticePrcEnum(origSettlementProdDO.getBizSts()));
        }

        // 5.调用消费通知处理组件
        Response<AccountingInstrDO> result = paymentManager.resultReportProcess(in);
        log.info("paymentManager.resultReportProcess end: msgId={}, isSuccess={}", clearingDTO.getClrMsgId(), result.isSuccess());
        AccountingInstrDO accountingInstrDO = result.getResult();
        // 6.异步处理
        handleAsyncProcessing(accountingInstrDO, clearingDTO);

        // 7.返回900报文
        return assembly900Msg(in, clearingDTO, ClearingStatusEnum.ACCEPTED.getCode(),
                ErrorEnum.BUSI_SUCCESS);
    }

    private SettlementProdDO handleDuplicateKeyException(ClearingDTO clearingDTO, Clearing clearing) {
        log.info("handleDuplicateKeyException msgId:{}, msgTp:{}", clearingDTO.getClrMsgId(), clearingDTO.clrMsgTp());
        // 1.幂等后判断是第几步 若第二步则为transid生成重复情况，直接抛异常
        if (Constant.INSERT_STEP_TWO.equals(RpcContext.getContext().get(Constant.PREPARE_INSERT_STEP))) {
            log.error("paymentManager.record duplicate: msgId={}, insertStep={}", clearingDTO.getClrMsgId(), Constant.INSERT_STEP_TWO);
            throw new DcepException(ErrorEnum.BUSI_DB_KEYWORD_REPEAT.getCode(),
                    ErrorEnum.BUSI_DB_KEYWORD_REPEAT.getDescription());
        }
        // 2.若第一步，select原交易, 并对原交易核心要素幂等要素进行检查
        SettlementProdDO origSettlementProdDO = settlementProdMapper.selectByPrimaryKey(new SettlementProdDO(clearingDTO.getClrMsgId()));
        // 2.1.检查该笔交易在数据库中是否不为终态
        if (!ClearingStatusEnum.PROCESS.getCode().equals(origSettlementProdDO.getBizSts()) && !ClearingStatusEnum.WAIT_SETTLE.getCode().equals(origSettlementProdDO.getBizSts())
                && !ClearingStatusEnum.SETTLE_QUEUE.getCode().equals(origSettlementProdDO.getBizSts())) {
            throw new DcepException(ErrorEnum.BUSI_DUPLICATION.getCode(),
                    ErrorEnum.BUSI_DUPLICATION.getDescription());
        }
        // 2.2.幂等要素(核心业务要素)，如不匹配,报异常
        if (!CheckUtil.idempotentMatch(clearingDTO, origSettlementProdDO, clearing)) {
            throw new DcepException(ErrorEnum.NO_MATCH_ORIGNAL.getCode(),
                    ErrorEnum.NO_MATCH_ORIGNAL.getDescription());
        }
        return origSettlementProdDO;
    }

    private Response<EnvelopeDTO<GwDTO>> assembly900Msg(EnvelopeDTO<GwDTO> req, ClearingDTO clearingDTO, String prcSts,
                                                        ErrorEnum prcEnum) {
        // 组装900DTO
        Dcep90000101DTO dto = new Dcep90000101DTO(
                MsgIdUtil.genMsgIdByOriMsgId(clearingDTO.getClrMsgId(),
                        MsgTpEnum.COMMON_PROCESS_CONFIRM_ABBR.getCode()),
                DcepDateUtils.getDcepDateStrNow(), InfoCacheUtil.getPbocInf(), clearingDTO.clrSendPtyId(), null,
                req.getSoapBody().getT().fetchMsgId(), clearingDTO.clrSendPtyId(), clearingDTO.clrMsgTp(), prcSts,
                prcEnum.getCode(), prcEnum.getDescription(), clearingDTO.clrBatId());
        // 返回900报文
        return new Response<>(new EnvelopeDTO<>(
                SoapHeaderUtil.createSoapHeader(MsgTpEnum.COMMON_PROCESS_CONFIRM.getCode(),
                        req.getSoapHeader().getMsgSN(), InfoCacheUtil.getPbocInf(), req.getSoapHeader().getSender()),
                dto));
    }

    /**
     * 检查金额是否一致
     */
    private void checkOrig(SettlementProdDO origSettlementProdDO, ClearingDTO clearingDTO) {
        // 检查原交易金额要素信息是否匹配，不匹配报错
        if (StringUtils.isNotBlank(clearingDTO.clrAmt())
                && (AmtUtils.toCents(clearingDTO.clrAmt()).compareTo(origSettlementProdDO.getSttlmAmt()) != 0)) {
            log.info("checkOrig-Amt not match: MsgId={}, clrAmt={}, origAmt={}", clearingDTO.getClrMsgId(),
                    clearingDTO.clrAmt(), origSettlementProdDO.getSttlmAmt().toString());
            throw new DcepException(ErrorEnum.BUSI_DATA_NOTMATCH.getCode(),
                    ErrorEnum.BUSI_DATA_NOTMATCH.getDescription());
        }
    }

    /**
     * 根据业务状态获取处理状态
     */
    private String getNoticePrcSts(String bizSts) {
        // 已结算、待结算、结算排队，则返回：成功
        if (ClearingStatusEnum.WAIT_SETTLE.getCode().equals(bizSts) || ClearingStatusEnum.SETTLE_QUEUE.getCode().equals(bizSts)
                || ClearingStatusEnum.SETTLED.getCode().equals(bizSts)) {
            return ClearingStatusEnum.ACCEPTED.getCode();
        }else {
            // 其他状态，则返回：失败
            return ClearingStatusEnum.FAILED.getCode();
        }
    }

    /**
     * 根据业务状态获取错误码
     */
    private ErrorEnum getNoticePrcEnum(String bizSts) {
        // 待结算、已结算、结算排队，则返回：成功
        if (ClearingStatusEnum.WAIT_SETTLE.getCode().equals(bizSts) || ClearingStatusEnum.SETTLE_QUEUE.getCode().equals(bizSts)
                || ClearingStatusEnum.SETTLED.getCode().equals(bizSts)) {
            return ErrorEnum.BUSI_SUCCESS;
            // 已撤销，则返回：交易已被撤销，不能支付
        } else if (ClearingStatusEnum.CANCELLED.getCode().equals(bizSts)) {
            return ErrorEnum.BUSI_CANCELLED_CANNOT_PAY;
            // 已推定失败，则返回：交易已推定失败，不能支付
        } else if (ClearingStatusEnum.PRESUME_FAILED.getCode().equals(bizSts)) {
            return ErrorEnum.BUSI_PRESUMED_CANNOT_PAY;
            // 失败，则返回：交易已失败，不能支付
        } else if (ClearingStatusEnum.FAILED.getCode().equals(bizSts)) {
            return ErrorEnum.BUSI_FAILED_CANNOT_PAY;
            // 日终退回，则返回：交易已日终退回，不能支付
        } else if (ClearingStatusEnum.DAYEND_RETURN.getCode().equals(bizSts)) {
            return ErrorEnum.BUSI_DAYEND_RETURN_CANNOT_PAY;
            // 其他终态为异常
        }else {
            throw new DcepException(ErrorEnum.UNKNOWN_EXCEPTION.getCode(),
                    ErrorEnum.UNKNOWN_EXCEPTION.getDescription());
        }
    }

    /**
     * 业务检查
     */
    private Response<EnvelopeDTO<GwDTO>> checkBusinessInfo(EnvelopeDTO<GwDTO> in, ClearingDTO clearingDTO, Clearing clearing) {
        if (clearing.checkMode() == ClearingCheckModeEnum.NON_CHECK) {
            return null;
        }
        // 1. 进行业务检查，返回枚举
        ErrorEnum errorEnum = CheckUtil.checkBusinessInfo(clearingDTO,
                CREDTTM_INTERVAL, MSGID_DATE_INTERVAL);
        if (errorEnum == null) {
            // 2.检查系统时间
            if (!commonManager.checkSystemDate(clearingDTO.clrSysWorkDt().replaceAll("-",""))){
                errorEnum = ErrorEnum.CREDTTM_ILLEGAL;
            } else {
                //检查通过
                return null;
            }
        }

        try {
            // 3.调用fail接口，记录结算产品表，档案表
            paymentManager.fail(in, errorEnum);
        } catch (DuplicateKeyException ex) { // 幂等情况直接抛异常返回911报文
            log.warn("checkBusinessInfo--" + Constant.DUPLICATION_EXCEPTION_LOG, clearingDTO.getClrMsgId(),
                    clearingDTO.clrMsgTp());
            throw new DcepException(ErrorEnum.BUSI_DUPLICATION.getCode(),
                    ErrorEnum.BUSI_DUPLICATION.getDescription());
        }
        // 4.返回900报文DTO
        return assembly900Msg(in, clearingDTO, ClearingStatusEnum.FAILED.getCode(), errorEnum);
    }

    /**
     * 网关调用
     */
    private Response<EnvelopeDTO<GwDTO>> invokeGwoutService(EnvelopeDTO<GwDTO> req) {
        // 1.通过网关请求接收机构，由接收机构进行借记确认
        log.info("gwoutService.execute request:{}", req);
        Response<EnvelopeDTO<GwDTO>> result = gwoutService.execute(req);
        log.info("gwoutService.execute response:{}", result);
        return result;
    }

    private void handleAsyncProcessing(AccountingInstrDO accountingInstrDO, ClearingDTO clearingDTO) {
        try {
            asyncPool.execute(() -> {
                // 1.调用结算钱包系统transfer API
                Response<TransferRespDTO> result = accountingManager.transfer(accountingInstrDO);
                if (!result.isSuccess()) {
                    // 2.调用结算钱包失败，直接返回，待计时控制组件处理
                    log.info("transfer fail transId:{}", accountingInstrDO.getTransId());
                    return;
                }
                log.info("paymentManager.clearing in async thread: msgId={}", clearingDTO.getClrMsgId());
                // 3.调用结算钱包成功，调用结算记账组件
                paymentManager.clearing(accountingInstrDO, result.getResult());
            });
        } catch (RejectedExecutionException e) {
            // 异步线程池已满，打印日志，待定时任务处理
            log.error("asyncPool is full,paymentManager.clearing rejected MsgId:{}, Exception:{}", clearingDTO.getClrMsgId(), e.getMessage());
        }
    }
}

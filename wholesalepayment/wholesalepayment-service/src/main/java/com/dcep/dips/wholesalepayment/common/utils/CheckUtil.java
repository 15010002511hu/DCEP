/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.common.utils;


import com.dcep.common.exception.DcepException;
import com.dcep.common.model.Response;
import com.dcep.common.utils.CheckUtils;
import com.dcep.dips.common.constant.ChnlSysEnum;
import com.dcep.dips.common.util.DateUtils;
import com.dcep.dips.wholesalepayment.annotation.Clearing;
import com.dcep.dips.wholesalepayment.dal.model.*;
import com.dcep.dips.wholesalepayment.dal.bo.OnChainTransInfoBO;
import com.dcep.dips.wholesalepayment.dto.BizStatusDTO;
import com.dcep.dips.wholesalepayment.dto.ClearingDTO;
import com.dcep.dips.wholesalepayment.dto.FundingDTO;
import com.dcep.dips.wholesalepayment.dto.acctrans.ZeroOutReqDTO;
import com.dcep.dips.wholesalepayment.dto.acctrans.ZeroOutRespDTO;
import com.dcep.dips.wholesalepayment.dto.chain.OnChainAdjustReqDTO;
import com.dcep.dips.wholesalepayment.dto.dc201.Dcep20101001DTO;
import com.dcep.dips.wholesalepayment.dto.dc211.Dcep21101001DTO;
import com.dcep.dips.wholesalepayment.dto.dc227.Dcep22701001DTO;
import com.dcep.dips.wholesalepayment.dto.dc281.Dcep28101001DTO;
import com.dcep.dips.wholesalepayment.dto.funding.IncreaseReqDTO;
import com.dcep.dips.wholesalepayment.dto.hvps.ClearReportReqDTO;
import com.dcep.dips.wholesalepayment.enums.*;
import com.dcep.dips.wholesalepayment.utils.AmtUtils;
import com.dcep.dips.wholesalepayment.utils.InfoCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
public class CheckUtil {

    public static Response<BizStatusDTO> checkHvpsReportInfo(ClearReportReqDTO clearReportReqDTO, HvpsTransDO orgnlHvpsTransDO) {
        // 检查原报文编号，原报文编号可能有：hvps.115、hvps.112、hpvs.118
        if (!clearReportReqDTO.getOrgnlMsgTp().equals(orgnlHvpsTransDO.getHvpsMsgTp())) {
            log.error("大额通知的原报文编号和数据库中原报文编号不一致，通知原报文：{}，数据库原报文：{}",
                    clearReportReqDTO.getOrgnlMsgTp(), orgnlHvpsTransDO.getHvpsMsgTp());
            throw new DcepException(WholesaleErrorEnum.BUSI_NOT_SUPPORTED.getCode(),
                    WholesaleErrorEnum.BUSI_NOT_SUPPORTED.getDescription());
        }

        if (orgnlHvpsTransDO.getBizSts().equals(clearReportReqDTO.getPrcStatus())) {
            log.error("大额通知的业务状态和数据库中原业务状态一致，报文重复，返回成功");
            return new Response<>(new BizStatusDTO(ClearingStatusEnum.SETTLED.getCode()));
        }

        // 将人行的日期格式YYYY-MM-DD转换为YYYYMMDD
        if (!StringUtils.isEmpty(clearReportReqDTO.getSettlementDate())) {
            clearReportReqDTO.setSettlementDate(DateUtils.convertToDatabase(clearReportReqDTO.getSettlementDate()));
        }

        return null;
    }

    /**
     * 检查业务信息（机构状态）
     *
     * @return 枚举
     */
    public static WholesaleErrorEnum checkOnChainBusinessInfo(String dbtrPtyId, String cdtrPtyId) {
        log.info("syncAccounting service checkBusinessInfo start: dbtrPtyId={} cdtrPtyId={}", dbtrPtyId, cdtrPtyId);
        if (!InfoCacheUtil.checkInstState(dbtrPtyId)) {
            log.info("dbtr state illegal,dbtrPtyId：{}", dbtrPtyId);
            return WholesaleErrorEnum.DBTR_STATE_ILLEGAL;
        }
        if (!InfoCacheUtil.checkInstState(cdtrPtyId)) {
            log.info("cdtr state illegal,cdtrPtyId：{}", cdtrPtyId);
            return WholesaleErrorEnum.CDTR_STATE_ILLEGAL;
        }
        log.info("syncAccounting service checkBusinessInfo end");
        return null;
    }

    /**
     * 检查报文中的日期、时间
     *
     * @param wholesaleDTO 请求对象
     * @return 错误对象
     */
    public static void checkCommonDateTime(FundingDTO wholesaleDTO, String CREDTTM_INTERVAL,
                                           String MSGID_DATE_INTERVAL) {
        // 报文发送时间校验
        if (!checkCreDtTm(wholesaleDTO.creDtTm(), Long.parseLong(CREDTTM_INTERVAL))) {
            log.info("creDtTm is illegal: msgId={}, creDtTm={}, CREDTTM_INTERVAL={}",
                    wholesaleDTO.msgId(), wholesaleDTO.creDtTm(), CREDTTM_INTERVAL);
            throw new DcepException(WholesaleErrorEnum.CREDTTM_ILLEGAL.getCode(),
                    WholesaleErrorEnum.CREDTTM_ILLEGAL.getDescription());
        }

        // 报文标识号中的日期校验
        if (!checkMsgIdAndCreDtTm(wholesaleDTO.creDtTm(), wholesaleDTO.msgId(),
                Integer.parseInt(MSGID_DATE_INTERVAL))) {
            log.info("msgId date is illegal: msgId={}, creDtTm={}, MSGID_DATE_INTERVAL={}",
                    wholesaleDTO.msgId(), wholesaleDTO.creDtTm(), MSGID_DATE_INTERVAL);
            throw new DcepException(WholesaleErrorEnum.MSGID_DATE_ILLEGAL.getCode(),
                    WholesaleErrorEnum.MSGID_DATE_ILLEGAL.getDescription());
        }
    }

//    /**
//     * 检查业务信息（包含机构状态等）
//     *
//     * @param wholesaleDTO 请求对象
//     * @return 错误对象
//     */
//    public static WholesaleErrorEnum checkBusinessInfo(FundingDTO wholesaleDTO, String CREDTTM_INTERVAL,
//                                                       String MSGID_DATE_INTERVAL) {
//        // 报文发送时间校验
//        checkCommonDateTime(wholesaleDTO, CREDTTM_INTERVAL, MSGID_DATE_INTERVAL);
//
//        // TODO
//        //   1) 时间的检查，特定时间段能做此业务（结算时间相关表）
//
//        // 报文发送机构、接收机构及钱柜机构校验
//        WholesaleCheckModeEnum checkModeEnum = wholesaleDTO.getClass().getAnnotation(WholesalePayment.class).checkMode();
//        if (WholesaleCheckModeEnum.PAYER_CHECK == checkModeEnum) {
//            log.info("checkInstState: sender={}, receiver={}, msgId={}",
//                    wholesaleDTO.dbtrPtyId(), wholesaleDTO.cbtrPtyId(), wholesaleDTO.msgId());
//            if (!InfoCacheUtil.checkInstState(wholesaleDTO.dbtrPtyId())){
//                return WholesaleErrorEnum.SENDER_STATE_ILLEGAL;
//            }
//            if (!InfoCacheUtil.checkInstState(wholesaleDTO.cbtrPtyId())){
//                return WholesaleErrorEnum.RECEIVER_STATE_ILLEGAL;
//            }
//        } else if (WholesaleCheckModeEnum.PAYEE_CHECK == checkModeEnum) {
//            // TODO
//        }
//
//        return checkRecvPtyId(wholesaleDTO);
//    }

    /**
     * 检查接收机构是否正确
     *
     * @param wholesaleDTO 请求报文类
     * @return 结算错误枚举类
     */
    public static WholesaleErrorEnum checkRecvPtyId(FundingDTO wholesaleDTO) {
        // TODO
        return null;
    }

    // 检查报文头日期是否在业务受理范围内，判断msgId的前8位日期是否在GrpHdr里的creDtTm日期正负1天之内
    public static boolean checkMsgIdAndCreDtTm(String creDtTm, String msgId, int MSGID_DATE_INTERVAL) {
        String msgIdDate = msgId.substring(0, 8); // 报文标识号前8位日期
        String bizDt = getBizDt(creDtTm); // creDtTm的8位日期
        return CheckUtils.msgIdDateIsNearCreDtTm(bizDt, msgIdDate, MSGID_DATE_INTERVAL);
    }

    // 检查报文头日期是否在业务受理范围内，判断msgId的前8位日期是否在GrpHdr里的creDtTm日期正负1天之内
    public static String getBizDt(String creDtTm) {
        return creDtTm.substring(0, 10).replaceAll("-", "");
    }

    // 检查发送时间是否在业务受理范围内
    public static boolean checkCreDtTm(String creDtTm, long CREDTTM_INTERVAL) {
        LocalDateTime sendDate = LocalDateTime.parse(creDtTm); // 默认格式: "yyyy-MM-dd'T'HH:mm:ss"
        LocalDateTime nowDate = LocalDateTime.now();
        // 发送时间校验，在正负10分钟之内(毫秒对比)
        if (Math.abs(Duration.between(sendDate, nowDate).toMillis()) > CREDTTM_INTERVAL) {
            return false;
        }
        return true;
    }

    public static void checkDecreaseIdempotent(FundingDTO fundingDTO, FundAdjustProdDO origFundAdjustProdDO) {
        String msgTp = fundingDTO.msgTp();
        String origMsgTp = origFundAdjustProdDO.getMsgTp();

        String adjustAmt = AmtUtils.toCents(fundingDTO.adjustAmt()).toString();
        String origAdjustAmt = origFundAdjustProdDO.getAdjustAmt().toString();

        String adjustPtyId = fundingDTO.adjustPtyId();
        String origAdjustPtyId = origFundAdjustProdDO.getAdjustPtyId();

        // 1. 报文编号不一致 ，抛异常返回机构911报文
        if (!origMsgTp.equals(msgTp)) {
            log.info("msgTp not match: msgId={}, origMsgTp={}, msgTp={}", fundingDTO.msgId(), origMsgTp, msgTp);
            throw new DcepException(WholesaleErrorEnum.NO_MATCH_ORIGNAL.getCode(),
                    WholesaleErrorEnum.NO_MATCH_ORIGNAL.getDescription());
        }

        // 2. 交易金额不一致，抛异常返回机构911报文
        if (!origAdjustAmt.equals(adjustAmt)) {
            log.info("adjustAmt not match: msgId={}, origAdjustAmt={}, adjustAmt={}",
                    fundingDTO.msgId(), origAdjustAmt, adjustAmt);
            throw new DcepException(WholesaleErrorEnum.NO_MATCH_ORIGNAL.getCode(),
                    WholesaleErrorEnum.NO_MATCH_ORIGNAL.getDescription());
        }

        // 3. 被调整方不一致，抛异常返回机构911报文
        if (!(origAdjustPtyId.equals(adjustPtyId))) {
            log.info("adjustPtyId not match: msgId={}, origAdjustPtyId={}, adjustPtyId={}",
                    fundingDTO.msgId(), origAdjustPtyId, adjustPtyId);
            throw new DcepException(WholesaleErrorEnum.NO_MATCH_ORIGNAL.getCode(),
                    WholesaleErrorEnum.NO_MATCH_ORIGNAL.getDescription());
        }

        // 4. 如果数据库中原有业务状态已为终态，抛异常返回机构911报文
        // 5. TODO，将数据库中的终态信息，转换为900报文，同步返回机构？这样是否可以依赖机构的重发，批发到结算钱包就不存储转发了？
        if (!ClearingStatusEnum.PROCESS.getCode().equals(origFundAdjustProdDO.getBizSts())) {
            log.info("《资金调整产品表》原业务状态已不是处理中，业务重复，抛出异常，msgId={},bizSts={}",
                    origFundAdjustProdDO.getMsgId(), origFundAdjustProdDO.getBizSts());
            throw new DcepException(ErrorEnum.BUSI_DUPLICATION.getCode(), ErrorEnum.BUSI_DUPLICATION.getDescription());
        }
    }

    public static void checkIdempotent(FundingDTO fundingDTO, SettlementProdDO origSettlementProdDO) {
        String msgTp = fundingDTO.msgTp();
        String origMsgTp = origSettlementProdDO.getMsgTp();
        String sttlmAmt = null;
        String origSttlmAmt = origSettlementProdDO.getSttlmAmt().toString();

        String dbtrPtyId = fundingDTO.dbtrPtyId();
        String origDbtrPtyId = origSettlementProdDO.getDbtrPtyId();
        String cdtrPtyId = null;
        String origCdtrPtyId = origSettlementProdDO.getCbtrPtyId();

        // 1. 如果数据库中原有业务状态已为终态，抛异常返回机构911报文
        if (!ClearingStatusEnum.PROCESS.getCode().equals(origSettlementProdDO.getBizSts())) {
            throw new DcepException(ErrorEnum.BUSI_DUPLICATION.getCode(), ErrorEnum.BUSI_DUPLICATION.getDescription());
        }

        // 2. 报文编号不一致 或 交易金额不一致，抛异常返回机构911报文
        if (!origMsgTp.equals(msgTp) || !origSttlmAmt.equals(sttlmAmt)) {
            log.info("msgTp or sttlmAmt not match: msgId={}, origMsgTp={}, msgTp={}, origSttlmAmt={}, sttlmAmt={}",
                    fundingDTO.msgId(), origMsgTp, msgTp, origSttlmAmt, sttlmAmt);
            throw new DcepException(WholesaleErrorEnum.NO_MATCH_ORIGNAL.getCode(),
                    WholesaleErrorEnum.NO_MATCH_ORIGNAL.getDescription());
        }

        // 3. 付款方不一致 或 收款方不一致，抛异常返回机构911报文
        if (!(origDbtrPtyId.equals(dbtrPtyId) && origCdtrPtyId.equals(cdtrPtyId))) {
            log.info("dbtrPtyId or cdtrPtyId not match: msgId={}, " +
                            "origDbtrPtyId={}, dbtrPtyId={}, origCdtrPtyId={}, cdtrPtyId={}",
                    fundingDTO.msgId(), origDbtrPtyId, dbtrPtyId, origCdtrPtyId, cdtrPtyId);
            throw new DcepException(WholesaleErrorEnum.NO_MATCH_ORIGNAL.getCode(),
                    WholesaleErrorEnum.NO_MATCH_ORIGNAL.getDescription());
        }
    }

    public static Response<ZeroOutRespDTO> checkAccTransZerooutIdempotent(ZeroOutReqDTO zeroOutReqDTO, AccountingInstrDO orgnlAccountingInstrDO) {
        String amount = zeroOutReqDTO.getAmount().toString();
        String orgnlAmount = orgnlAccountingInstrDO.getAmount().toString();

        String memberId = zeroOutReqDTO.getMemberId();
        String orgnlMemberId = orgnlAccountingInstrDO.getFromClrMmbId();

        if (!orgnlAmount.equals(amount)) {
            log.info("交易金额和原业务不一致: transId={}, orgnlAmount={}, amount={}", zeroOutReqDTO.getTransId(), orgnlAmount, amount);
            throw new DcepException(WholesaleErrorEnum.NO_MATCH_ORIGNAL.getCode(),
                    WholesaleErrorEnum.NO_MATCH_ORIGNAL.getDescription());
        }

        if (!(orgnlMemberId.equals(memberId))) {
            log.info("被清零机构和原业务不一致: transId={}, orgnlAmount={}, amount={}", zeroOutReqDTO.getTransId(), orgnlAmount, amount);
            throw new DcepException(WholesaleErrorEnum.NO_MATCH_ORIGNAL.getCode(),
                    WholesaleErrorEnum.NO_MATCH_ORIGNAL.getDescription());
        }

        ZeroOutRespDTO zeroOutRespDTO =  new ZeroOutRespDTO();
        zeroOutRespDTO.setTransId(zeroOutReqDTO.getTransId());
        zeroOutRespDTO.setMsgId(orgnlAccountingInstrDO.getMsgId());
        zeroOutRespDTO.setEndToEndId(orgnlAccountingInstrDO.getMsgId());
        zeroOutRespDTO.setPrcStatus(ClearingStatusEnum.SUCCESS.getCode());
        return new Response<>(true, new ZeroOutRespDTO());
    }

    public static void checkHvpsIncreaseIdempotent(IncreaseReqDTO increaseReqDTO, HvpsTransDO orgnlHvpsTransDO) {
        String msgTp = increaseReqDTO.getMsgTp();
        String orgnlMsgTp = orgnlHvpsTransDO.getHvpsMsgTp();

        String sttlmAmt = AmtUtils.toCents(increaseReqDTO.getAmount()).toString();
        String orgnlSttlmAmt = orgnlHvpsTransDO.getSttlmAmt().toString();

        String dbtrClrPtyId = increaseReqDTO.getClearingMemberId();
        String orgnlDbtrClrPtyId = orgnlHvpsTransDO.getDbtrClrPtyId();

        if (!orgnlMsgTp.equals(msgTp) || !orgnlSttlmAmt.equals(sttlmAmt)) {
            log.info("大额报文编号或交易金额不一致: hvpsMsgId={}, orgnlMsgTp={}, msgTp={}, orgnlSttlmAmt={}, sttlmAmt={}",
                    increaseReqDTO.getMsgId(), orgnlMsgTp, msgTp, orgnlSttlmAmt, sttlmAmt);
            throw new DcepException(WholesaleErrorEnum.NO_MATCH_ORIGNAL.getCode(),
                    WholesaleErrorEnum.NO_MATCH_ORIGNAL.getDescription());
        }

        if (!(orgnlDbtrClrPtyId.equals(dbtrClrPtyId))) {
            log.info("大额报文付款清算行号不一致: hvpsMsgId={}, orgnlDbtrClrPtyId={}, dbtrClrPtyId={}",
                    increaseReqDTO.getMsgId(), orgnlDbtrClrPtyId, dbtrClrPtyId);
            throw new DcepException(WholesaleErrorEnum.NO_MATCH_ORIGNAL.getCode(),
                    WholesaleErrorEnum.NO_MATCH_ORIGNAL.getDescription());
        }
    }

    public static boolean checkOnChainIdempotent(OnChainTransInfoBO onChainTransInfo, OnChainPaymentTransDO onchainPaymentTransDO) {

        String sttlmAmt = onChainTransInfo.getTransAmt().toString();
        String origSttlmAmt = onchainPaymentTransDO.getTransAmt().toString();

        String dbtrPtyId = onChainTransInfo.getDbtrPtyId();
        String origDbtrPtyId = onchainPaymentTransDO.getDbtrPtyId();

        String cdtrPtyId = onChainTransInfo.getCdtrPtyId();
        String origCdtrPtyId = onchainPaymentTransDO.getCdtrPtyId();

        // 1. 交易金额不一致，则抛异常
        if (!origSttlmAmt.equals(sttlmAmt)) {
            log.info("sttlmAmt not match: msgId={}, origSttlmAmt={}, sttlmAmt={}",
                    onChainTransInfo.getMsgId(), origSttlmAmt, sttlmAmt);
            return false;
        }

        // 2. 付款方不一致 或 收款方不一致，则抛异常
        if (!(origDbtrPtyId.equals(dbtrPtyId)) || !(origCdtrPtyId.equals(cdtrPtyId))) {
            log.info("dbtrPtyId or cdtrPtyId not match: msgId={}, origDbtrPtyId={}, dbtrPtyId={}, origDbtrPtyId={}, cdtrPtyId={}",
                    onChainTransInfo.getMsgId(), origDbtrPtyId, dbtrPtyId, origCdtrPtyId, cdtrPtyId);
            return false;
        }
        return true;
    }

    // 幂等要素(核心业务要素)
    public static boolean idempotentMatch(ClearingDTO clearingDTO, SettlementProdDO origSettlementProdDO,
                                          Clearing clearing) {
        String clrMsgTp = clearingDTO.clrMsgTp();
        String origMsgTP = origSettlementProdDO.getMsgTp();
        String clrAmt = clearingDTO.clrAmt();
        String origAmt = AmtUtils.toYuan(origSettlementProdDO.getSttlmAmt()).toString();
        String clrDbtrPtyId = clearingDTO.clrDbtrPtyId();
        String origDbtrPtyId = origSettlementProdDO.getDbtrPtyId();
        String clrCdtrPtyId = clearingDTO.clrCdtrPtyId();
        String origCdtrPtyId = origSettlementProdDO.getCbtrPtyId();

        // 1.报文编号不一致+交易金额不一致 匹配失败
        if (!origMsgTP.equals(clrMsgTp) || !origAmt.equals(clrAmt)) {
            log.info("MsgTp or Amt not match: MsgId={}, origMsgTP={}, clrMsgTp={}, origAmt={}, clrAmt={}",
                    clearingDTO.getClrMsgId(), origMsgTP, clrMsgTp, origAmt, clrAmt);
            return false;
        }

        // 2.清算模式是待清算,比较付款方和收款方是否一致，不一致则匹配失败
        if (ClearingActionEnum.PREPARE.equals(clearing.action())) {
            if (origDbtrPtyId.equals(clrDbtrPtyId) && origCdtrPtyId.equals(clrCdtrPtyId)) {
                return true;
            }
            log.info(
                    "DbtrPtyId and CdtrPtyId not match: MsgId={}, origDbtrPtyId={}, clrDbtrPtyId={}, origCdtrPtyId={}, clrCdtrPtyId={}",
                    clearingDTO.getClrMsgId(), origDbtrPtyId, clrDbtrPtyId, origCdtrPtyId, clrCdtrPtyId);
            return false;
        }
        return true;
    }

    public static ErrorEnum checkBusinessInfo(ClearingDTO clearingDTO, String CREDTTM_INTERVAL,
                                                          String MSGID_DATE_INTERVAL) {

        // 报文发送时间校验
        if (!checkClrCreDtTm(clearingDTO.clrCreDtTm(), Long.valueOf(CREDTTM_INTERVAL))) {
            log.info("creDtTm is illegal: msgId={}, creDtTm={}, creDtTm_interval={}", clearingDTO.getClrMsgId(),
                    clearingDTO.clrCreDtTm(), CREDTTM_INTERVAL);
            throw new DcepException(ErrorEnum.CREDTTM_ILLEGAL.getCode(),
                    ErrorEnum.CREDTTM_ILLEGAL.getDescription());
        }

        // 报文标识号中的日期校验
        if (!checkClrMsgIdAndCreDtTm(clearingDTO.clrCreDtTm(), clearingDTO.getClrMsgId(),
                Integer.valueOf(MSGID_DATE_INTERVAL))) {
            log.info("msgId date is illegal: msgId={}, creDtTm={}, msgId_date_interval={}", clearingDTO.getClrMsgId(),
                    clearingDTO.clrCreDtTm(), MSGID_DATE_INTERVAL);
            throw new DcepException(ErrorEnum.MSGID_DATE_ILLEGAL.getCode(),
                    ErrorEnum.MSGID_DATE_ILLEGAL.getDescription());
        }
        // 报文发送机构、接收机校验
        ClearingCheckModeEnum checkModeEnum = clearingDTO.getClass().getAnnotation(Clearing.class).checkMode();
        if (ClearingCheckModeEnum.PAYER_CHECK == checkModeEnum) {
            log.info("checkBusinessInfo: sender={}, receiver={}, msgId={}", clearingDTO.clrDbtrPtyId(),
                    clearingDTO.clrCdtrPtyId(), clearingDTO.getClrMsgId());
            if (!InfoCacheUtil.checkInstState(clearingDTO.clrDbtrPtyId())){
                return ErrorEnum.SENDER_STATE_ILLEGAL;
            }
            if (!InfoCacheUtil.checkInstState(clearingDTO.clrCdtrPtyId())){
                return ErrorEnum.RECEIVER_STATE_ILLEGAL;
            }
        } else if (ClearingCheckModeEnum.PAYEE_CHECK == checkModeEnum) {
            log.info("checkBusinessInfo: sender={}, receiver={}, msgId={}", clearingDTO.clrCdtrPtyId(),
                    clearingDTO.clrDbtrPtyId(), clearingDTO.getClrMsgId());
            if (!InfoCacheUtil.checkInstState(clearingDTO.clrDbtrPtyId())){
                return ErrorEnum.RECEIVER_STATE_ILLEGAL;
            }
            if (!InfoCacheUtil.checkInstState(clearingDTO.clrCdtrPtyId())){
                return ErrorEnum.SENDER_STATE_ILLEGAL;
            }
        }
        return checkRecvPtyId(clearingDTO);
    }

    // 检查发送时间是否在业务受理范围内
    public static boolean checkClrCreDtTm(String clrCreDtTm, long CREDTTM_INTERVAL) {
        //默认格式:"yyyy-MM-dd'T'HH:mm:ss"
        LocalDateTime sendDate = LocalDateTime.parse(clrCreDtTm);
        LocalDateTime nowDate = LocalDateTime.now();
        // 发送时间校验，在正负10分钟之内(毫秒对比)
        return Math.abs(Duration.between(sendDate, nowDate).toMillis()) <= CREDTTM_INTERVAL;
    }

    // 检查报文头日期是否在业务受理范围内，判断msgId的前8位日期是否在GrpHdr里的creDtTm日期正负1天之内
    public static boolean checkClrMsgIdAndCreDtTm(String clrCreDtTm, String clrMsgId, int MSGID_DATE_INTERVAL) {
        // 报文标识号前8位日期
        String msgIdDateStr = clrMsgId.substring(0, 8);
        // creDtTm的8位日期
        String creDtTmStr = clrCreDtTm.substring(0, 10).replaceAll("-", "");
        return CheckUtils.msgIdDateIsNearCreDtTm(creDtTmStr, msgIdDateStr, MSGID_DATE_INTERVAL);
    }

    /**
     * 检查接收机构是否正确
     * @param clearingDTO
     * @return
     */
    public static ErrorEnum checkRecvPtyId(ClearingDTO clearingDTO) {

        // 201、227、281报文判断收款钱包ID为收款方运营机构
        if (clearingDTO instanceof Dcep20101001DTO
                || clearingDTO instanceof Dcep22701001DTO
                || clearingDTO instanceof Dcep28101001DTO) {
            //校验收款方钱包ID属于收款方运营机构，钱包ID前3位与机构内码相同
            if (null != clearingDTO.clrCdtrWltId()) {
                String code = clearingDTO.clrCdtrWltId().substring(0, 3);
                String orgInnerCode = InfoCacheUtil.getOrgInnerCode(clearingDTO.clrCdtrPtyId());
                if (!code.equals(orgInnerCode)) {
                    log.error("recvPtyId is illegal: msgId={},cdtrWltId-code={},orgInnerCode={}",
                            clearingDTO.getClrMsgId(), code, orgInnerCode);
                    return ErrorEnum.USER_NOT_MATCH_RECEIVER;
                }
            }
        } else if (clearingDTO instanceof Dcep21101001DTO) {
            //校验付款方tocken属于付款方运营机构，付款方tocken(AuthInfo）第4-6位与机构内码相同
            Dcep21101001DTO dcep211 = (Dcep21101001DTO) clearingDTO;
            if (null != dcep211.getDrctDbtTxInf().getRmtInf()
                    && null != dcep211.getDrctDbtTxInf().getRmtInf().getAuthInfo()) {
                String code = dcep211.getDrctDbtTxInf().getRmtInf().getAuthInfo().substring(3, 6);
                String orgInnerCode = InfoCacheUtil.getOrgInnerCode(dcep211.clrDbtrPtyId());
                if (!code.equals(orgInnerCode)) {
                    log.error("recvPtyId is illegal: msgId={},tocken-code={},orgInnerCode={}",
                            clearingDTO.getClrMsgId(), code, orgInnerCode);
                    return ErrorEnum.USER_NOT_MATCH_RECEIVER;
                }
            }
        }
        return null;
    }
    // 幂等要素(核心业务要素)
    public static boolean idempotentMatch(OnChainAdjustReqDTO onChainAdjustReqDTO, SettlementProdDO origSettlementProdDO) {
        String clrAmt = AmtUtils.toCents(onChainAdjustReqDTO.getAmount()).toString();
        String clrDbtrPtyId = onChainAdjustReqDTO.getClearingMemberId();
        String clrCdtrPtyId = onChainAdjustReqDTO.getClearingMemberId();

        String origAmt = origSettlementProdDO.getSttlmAmt().toString();
        String origDbtrPtyId = origSettlementProdDO.getDbtrPtyId();
        String origCdtrPtyId = origSettlementProdDO.getCbtrPtyId();

        if(onChainAdjustReqDTO.getAdjustTp().equals("QOT01")){
            if(!ChnlSysEnum.DCEP.getCode().equals(origSettlementProdDO.getSendSysId())
                    ||!ChnlSysEnum.BCSP.getCode().equals(origSettlementProdDO.getRecvSysId())
                        || !origAmt.equals(clrAmt) ||!origDbtrPtyId.equals(clrDbtrPtyId) || !origCdtrPtyId.equals(clrCdtrPtyId)){
                return false;
            }
        }else if (onChainAdjustReqDTO.getAdjustTp().equals("QOT02")){
            if(!ChnlSysEnum.BCSP.getCode().equals(origSettlementProdDO.getSendSysId())
                    ||!ChnlSysEnum.DCEP.getCode().equals(origSettlementProdDO.getRecvSysId())
                    || !origAmt.equals(clrAmt) ||!origDbtrPtyId.equals(clrDbtrPtyId) || !origCdtrPtyId.equals(clrCdtrPtyId)){
                return false;
            }
        }
        return true;
    }
}

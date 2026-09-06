/*
 * pbcdci.cn Inc.
 * Copyright © 2025 PBCDCI All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.common.utils;

import com.dcep.common.enums.MessageTypeEnum;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.common.model.msg.CmonConfInf;
import com.dcep.common.model.msg.Dcep90000101DTO;
import com.dcep.common.model.msg.Dcep91100101DTO;
import com.dcep.common.model.soap.FinInstnId;
import com.dcep.common.model.soap.OrgnlGrpHdr;
import com.dcep.common.model.soap.SoapBody;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.soap.SoapUtils;
import com.dcep.common.utils.DcepDateUtils;
import com.dcep.common.utils.MsgIdUtil;
import com.dcep.dips.common.constant.ChnlSysEnum;
import com.dcep.dips.common.dto.dc411.Dcep41100101DTO;
import com.dcep.dips.common.dto.dc909.Dcep90900101DTO;
import com.dcep.dips.common.dto.dc909.OrgnlMsgCntt;
import com.dcep.dips.common.enums.ClearingProdErrorEnum;
import com.dcep.dips.common.util.DateUtils;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import com.dcep.dips.wholesalepayment.dal.model.FundAdjustProdDO;
import com.dcep.dips.wholesalepayment.dal.model.HvpsTransDO;
import com.dcep.dips.wholesalepayment.dal.model.SettlementProdDO;
import com.dcep.dips.wholesalepayment.dto.ClearingDTO;
import com.dcep.dips.wholesalepayment.dto.FundingDTO;
import com.dcep.dips.wholesalepayment.dto.dc185.*;
import com.dcep.dips.wholesalepayment.dto.dc185.Sts;
import com.dcep.dips.wholesalepayment.dto.dc200.Envlp;
import com.dcep.dips.wholesalepayment.dto.dc200.GrpHdr;
import com.dcep.dips.wholesalepayment.dto.dc200.OrgnlGrpInf;
import com.dcep.dips.wholesalepayment.dto.dc200.Rsn;
import com.dcep.dips.wholesalepayment.dto.dc200.SplmtryData;
import com.dcep.dips.wholesalepayment.dto.dc200.*;
import com.dcep.dips.wholesalepayment.dto.dc200.TxInfAndSts;
import com.dcep.dips.wholesalepayment.dto.dc427.Dcep42700101DTO;
import com.dcep.dips.wholesalepayment.dto.dc428.*;
import com.dcep.dips.wholesalepayment.dto.mcbs.MbridgeReqDTO;
import com.dcep.dips.wholesalepayment.dto.mcbs100.Mcbs10000101DTO;
import com.dcep.dips.wholesalepayment.dto.mcbs100.NoInf;
import com.dcep.dips.wholesalepayment.dto.mcbs101.MsgHdr;
import com.dcep.dips.wholesalepayment.dto.mcbs102.Mcbs10200101DTO;
import com.dcep.dips.wholesalepayment.enums.*;
import com.dcep.dips.wholesalepayment.utils.AmtUtils;
import com.dcep.dips.wholesalepayment.utils.InfoCacheUtil;
import com.dcep.gateway.mcbdc.dto.common.GroupHeader;
import com.dcep.gateway.mcbdc.dto.common.OriginGroupInfo;
import com.dcep.gateway.mcbdc.dto.common.OriginGroupInfoAndStatus;
import com.dcep.gateway.mcbdc.dto.common.OriginMsgId;
import com.dcep.gateway.mcbdc.dto.mcbs203.Mcbs20300101DTO;
import com.dcep.gateway.mcbdc.dto.mcbs203.StatusReasonInfo;
import com.dcep.gateway.mcbdc.dto.mcbs203.TransactionInfoAndStatus;
import com.dcep.gateway.mcbdc.dto.mcbs203.TransactionInfoAndStatusSupplementaryData;
import com.dcep.gateway.mcbdc.dto.mcbs204.Mcbs20400101DTO;
import com.dcep.gateway.mcbdc.dto.mcbs900.Mcbs90000101DTO;
import com.dcep.gateway.mcbdc.dto.mcbs900.ReceiptDetails;
import com.dcep.gateway.mcbdc.dto.mcbs900.RequestHandling;
import com.dcep.gateway.mcbdc.dto.soap.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

@Slf4j
public class DtoUtil {

    public static final ThreadLocal<ObjectMapper> OBJ_MAPPER_HOLDER = ThreadLocal.withInitial(() -> {
        ObjectMapper objMapper = new ObjectMapper();
        // 反序列化数组对象时接受单值
        objMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        // 反序列化忽略不识别的属性
        objMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 只序列化非null属性
        objMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objMapper;
    });

    public static String obj2JsonStr(Object obj) {
        try {
            return OBJ_MAPPER_HOLDER.get().writeValueAsString(obj);
        } catch (Exception e) {
            log.error("obj2JsonStr failed Object={}", obj, e);
            throw new DcepException(ErrorEnum.UNKNOWN_EXCEPTION.getCode(), ErrorEnum.UNKNOWN_EXCEPTION.getDescription());
        }
    }

    public static <T> T jsonStr2Obj(String jsonStr, TypeReference<T> valueTypeRef) {
        try {
            return OBJ_MAPPER_HOLDER.get().readValue(jsonStr, valueTypeRef);
        } catch (Exception e) {
            log.error("JsonStr2Obj failed jsonStr={}", jsonStr, e);
            throw new DcepException(ErrorEnum.UNKNOWN_EXCEPTION.getCode(), ErrorEnum.UNKNOWN_EXCEPTION.getDescription());
        }
    }

//    public static McbsSoapHeader toMcbsSoapHeader(EnvelopeDTO<GwDTO> dcepReqEnvelopeDTO) {
//        McbsSoapHeader mcbsSoapHeader = new McbsSoapHeader();
//        mcbsSoapHeader.setVer(Constant.MCBS_SOAPHEADER_VER);
//        mcbsSoapHeader.setSndDtTm(TimeUtil.getMcbsCurrentTime(DcepDateUtils.ISO_DATETIME_PATTERN));
//        String dcepMsgTp = dcepReqEnvelopeDTO.getSoapHeader().getMsgTp();
//        switch (dcepMsgTp) {
//            case Constant.DCEP_MSGTYPE_203:
//                mcbsSoapHeader.setMsgTp(Constant.MCBS_MSGTYPE_200);
//                break;
//            case Constant.DCEP_MSGTYPE_213:
//                mcbsSoapHeader.setMsgTp(Constant.MCBS_MSGTYPE_201);
//                break;
//            default:
//                throw new DcepException(com.dcep.common.enums.ErrorEnum.REQUEST_PARAM_ILLEGAL.getCode(),
//                        "dcep报文编号(" + dcepMsgTp + ")没有对应的mcbs货币桥报文");
//        }
//        mcbsSoapHeader.setSenderLEI(Constant.LEI_PBOC);
//        mcbsSoapHeader.setSenderCBMALEI(Constant.LEI_PBOC);
//        mcbsSoapHeader.setReceiverLEI(Constant.LEI_MCBS);
//        mcbsSoapHeader.setReceiverCBMALEI(Constant.LEI_MCBS);
//        mcbsSoapHeader.setMessageDirection(Constant.DIRECTION_FROM_HLHT_TO_MBRIDGE);
//        return mcbsSoapHeader;
//    }

    public static SoapHeader toDcepSoapHeader(McbsEnvelopeDTO<McbsGwDTO> mcbsReqEnvelopeDTO) {
        SoapHeader dcepSoapHeader = new SoapHeader();
        dcepSoapHeader.setVer(Constant.DCEP_SOAPHEADER_VER);
        dcepSoapHeader.setSndDtTm(TimeUtil.getHlhtCurrentTime(DcepDateUtils.ISO_DATETIME_PATTERN));
        String mcbsMsgTp = mcbsReqEnvelopeDTO.getSoapHeader().getMsgTp();
        switch (mcbsMsgTp) {
            case Constant.MCBS_MSGTYPE_200:
            case Constant.MCBS_MSGTYPE_202:
                dcepSoapHeader.setMsgTp(Constant.DCEP_MSGTYPE_203);
                break;
            case Constant.MCBS_MSGTYPE_201:
                dcepSoapHeader.setMsgTp(Constant.DCEP_MSGTYPE_213);
                break;
            default:
                throw new DcepException(com.dcep.common.enums.ErrorEnum.REQUEST_PARAM_ILLEGAL.getCode(),
                        "mcbs报文编号(" + mcbsMsgTp + ")没有对应的互联互通报文");
        }
        // 货币桥报文头里的发送接收机构LEI码分别是0000和央行LEI码，dcep报文头填写的是报文体里的收、付款机构编码，因此需在转换报文体时再进行设置。
        return dcepSoapHeader;
    }

    public static EnvelopeDTO<GwDTO> assembly200Msg(AccountingInstrDO accountingInstrDO, String receiver) {
        GrpHdr grpHdr = new GrpHdr(MsgIdUtil.randomMsgId(MsgTpEnum.SETTLE_NOTICE_ABBR.getCode(), InfoCacheUtil.getPbocInnerCode(), CommonUtil.getEnvVal()), DcepDateUtils.getDcepDateStrNow());
        OrgnlGrpInf orgnlGrpInf = new OrgnlGrpInf(accountingInstrDO.getMsgId(), accountingInstrDO.getMsgTp());
        InstgAgt instgAgt = new InstgAgt(createFinInstnId(ChnlSysEnum.DCEP.getCode(), InfoCacheUtil.getPbocInf()));
        InstdAgt instdAgt = new InstdAgt(createFinInstnId(ChnlSysEnum.DCEP.getCode(), receiver));
        OrgnlTxRef orgnlTxRef = new OrgnlTxRef(new ActiveCurrencyAndAmount(accountingInstrDO.getCurrency(),
                AmtUtils.toYuan(accountingInstrDO.getAmount()).toString()));
        TxInfAndSts txInfAndSts = new TxInfAndSts(orgnlGrpInf, null, null, null, null,
                CommonUtil.actgStsToBizSts(accountingInstrDO.getActgSts()), null, instgAgt, instdAgt, orgnlTxRef, null);
        // 如果有结算失败原因，则设置结算失败原因
        if(StringUtils.isNotBlank(accountingInstrDO.getActgPrcCd()) && StringUtils.isNotBlank(accountingInstrDO.getActgPrcInf())) {
            StsRsnInf stsRsnInf = new StsRsnInf(new Rsn(accountingInstrDO.getActgPrcCd()), accountingInstrDO.getActgPrcInf());
            txInfAndSts.setStsRsnInf(stsRsnInf);
        } else {
            StsRsnInf stsRsnInf = new StsRsnInf(new Rsn(ErrorEnum.BUSI_SUCCESS.getCode()), ErrorEnum.BUSI_SUCCESS.getDescription());
            txInfAndSts.setStsRsnInf(stsRsnInf);
        }
        // 仅当业务状态为已结算时 返回结算日期、结算时间
        if (ActgStsEnum.SUCCESS.getCode().equals(accountingInstrDO.getActgSts())) {
            SplmtryData splmtryData = new SplmtryData("/Document/FIToFIPmtStsRpt/TxInfAndSts",
                    new Envlp(accountingInstrDO.getActgDt().substring(0, 4) + "-" + accountingInstrDO.getActgDt().substring(4, 6) +
                            "-" + accountingInstrDO.getActgDt().substring(6)));
            txInfAndSts.setSplmtryData(splmtryData);
        }
        Dcep20000101DTO dcep20000101DTO = new Dcep20000101DTO(grpHdr, txInfAndSts);
        SoapHeader soapHeader = SoapHeaderUtil.createSoapHeader(MsgTpEnum.SETTLE_NOTICE.getCode(),
                grpHdr.getMsgId() + "0001", InfoCacheUtil.getPbocInf(), receiver);
        return new EnvelopeDTO<>(soapHeader, dcep20000101DTO);
    }
    public static EnvelopeDTO<GwDTO> assembly200Msg(SettlementProdDO settle,AccountingInstrDO account,String errorCode,String errorMsg) {
        //TODO 需要检查赋值是否正确
//        SettlementProdDO settlementProdDO = response.getResult();
//        ClearingDTO clearingDTO = (ClearingDTO) req.body();
        Dcep20000101DTO dcep20000101DTO = new Dcep20000101DTO();
        GrpHdr grpHdr = new GrpHdr(MsgIdUtil.randomMsgId(MsgTpEnum.SETTLE_NOTICE_ABBR.getCode(), InfoCacheUtil.getPbocInnerCode(), CommonUtil.getEnvVal()), DcepDateUtils.getDcepDateStrNow());
        dcep20000101DTO.setGrpHdr(grpHdr);
        OrgnlGrpInf orgnlGrpInf = new OrgnlGrpInf(settle.getMsgId(), settle.getMsgTp());
        StsRsnInf stsRsnInf = new StsRsnInf(new Rsn(errorCode),errorMsg);
        InstgAgt instgAgt = new InstgAgt(createFinInstnId(ChnlSysEnum.DCEP.getCode(), InfoCacheUtil.getPbocInf()));
        InstdAgt instdAgt = new InstdAgt(createFinInstnId(ChnlSysEnum.DCEP.getCode(), settle.getSendPtyId()));
        //TODO 需要检查金额赋值是否正确
        OrgnlTxRef orgnlTxRef = new OrgnlTxRef(new ActiveCurrencyAndAmount(settle.getCurrency(),AmtUtils.toYuan(settle.getSttlmAmt()).toString()));

        TxInfAndSts txInfAndSts = new TxInfAndSts(orgnlGrpInf,settle.getBatId() ,account.getEndToEndId(),null,null,
                settle.getBizSts(),stsRsnInf,instgAgt,instdAgt,orgnlTxRef,null);

        // 仅当业务状态为已结算时 返回结算日期、结算时间
        if (ClearingStatusEnum.SETTLED.getCode().equals(settle.getBizSts())) {
            //TODO 日期
            SplmtryData splmtryData = new SplmtryData("/Document/FIToFIPmtStsRpt/TxInfAndSts", new Envlp(settle.getSttlmDt()));
            txInfAndSts.setSplmtryData(splmtryData);
        }
        dcep20000101DTO.setTxInfAndSts(txInfAndSts);
        SoapHeader header = SoapHeaderUtil.createSoapHeader(MsgTpEnum.SETTLE_NOTICE.getCode(),grpHdr.getMsgId()+"0001",settle.getSendPtyId(),settle.getRecvPtyId());
        return new EnvelopeDTO<>(header, dcep20000101DTO);
    }

    /**
     * 组装200报文
     * @return 应答报文
     */
    public static EnvelopeDTO<GwDTO> assembly200Msg(FundAdjustProdDO fundAdjustProdDO, String receiver, String prcInf) {
        // 组装200DTO
        String msgId = MsgIdUtil.genMsgIdByOriMsgId(fundAdjustProdDO.getMsgId(), MsgTpEnum.SETTLE_NOTICE_ABBR.getCode());
        String creDtTm = DcepDateUtils.getDcepDateStrNow();

        Dcep20000101DTO dcep200DTO = new Dcep20000101DTO();
        dcep200DTO.setGrpHdr(new com.dcep.dips.wholesalepayment.dto.dc200.GrpHdr(msgId, creDtTm));

        OrgnlGrpInf orgnlGrpInf = new OrgnlGrpInf(fundAdjustProdDO.getMsgId(), fundAdjustProdDO.getMsgTp());

        StsRsnInf stsRsnInf;
        if (ClearingStatusEnum.SETTLED.getCode().equals(fundAdjustProdDO.getBizSts())) {
            stsRsnInf = new StsRsnInf(new Rsn(ClearingProdErrorEnum.BUSI_SUCCESS.getCode()), "已结算");
        } else {
            stsRsnInf = new StsRsnInf(new Rsn(WholesaleErrorEnum.HVPS_REJECT.getCode()), prcInf);
        }

        InstgAgt instgAgt = new InstgAgt(createFinInstnId(ChnlSysEnum.DCEP.getCode(), InfoCacheUtil.getPbocInf()));
        InstdAgt instdAgt = new InstdAgt(createFinInstnId(ChnlSysEnum.DCEP.getCode(), receiver));

        String currency = fundAdjustProdDO.getCurrency();
        String amount = AmtUtils.toYuan(fundAdjustProdDO.getAdjustAmt()).toString();
        OrgnlTxRef orgnlTxRef = new OrgnlTxRef(new ActiveCurrencyAndAmount(currency, amount));

        TxInfAndSts txInfAndSts = new TxInfAndSts(orgnlGrpInf, null, null, null,null,
                fundAdjustProdDO.getBizSts(), stsRsnInf, instgAgt, instdAgt, orgnlTxRef,null);

        // 仅当业务状态为已结算时 返回结算日期、结算时间
        if (ClearingStatusEnum.SETTLED.getCode().equals(fundAdjustProdDO.getBizSts())) {
            Envlp envlp = new Envlp(DateUtils.formatInstDate(fundAdjustProdDO.getSttlmDt()));
            txInfAndSts.setSplmtryData(new SplmtryData("/Document/FIToFIPmtStsRpt/TxInfAndSts", envlp));
        }
        dcep200DTO.setTxInfAndSts(txInfAndSts);

        SoapHeader soapHeader = SoapHeaderUtil.createSoapHeader(
                MsgTpEnum.SETTLE_NOTICE.getCode(),
                msgId + "0001",
                InfoCacheUtil.getPbocInf(),
                receiver);

        return new EnvelopeDTO<>(soapHeader, dcep200DTO);
    }

    /**
     * 组装185报文
     * @return 应答报文
     */
    public static EnvelopeDTO<GwDTO> assembly185Msg(FundAdjustProdDO fundAdjustProdDO,
                                                    HvpsTransDO hvpsTransDO, String receiver) {
        com.dcep.dips.wholesalepayment.dto.dc185.GrpHdr grpHdr = new com.dcep.dips.wholesalepayment.dto.dc185.GrpHdr();
        grpHdr.setMsgId(fundAdjustProdDO.getReportMsgId());
        grpHdr.setCreDtTm(DcepDateUtils.getDcepDateStrNow());

        // 结算钱包编码
        Othr othr1 = new Othr();
        othr1.setId(fundAdjustProdDO.getAdjustWltId());
        Id id = new Id();
        id.setOthr(othr1);

        // 接收参与机构编码
        Othr othr = new Othr();
        othr.setId(fundAdjustProdDO.getAdjustPtyId());
        OrgId orgId = new OrgId();
        orgId.setOthr(othr);
        IdOfOrgId idOfOrgId = new IdOfOrgId();
        idOfOrgId.setOrgId(orgId);
        Ownr ownr = new Ownr();
        ownr.setId(idOfOrgId);

        Acct acct = new Acct();
        acct.setId(id);
        acct.setOwnr(ownr);

        // 调整金额
        Ntry ntry = new Ntry();
        String currency = fundAdjustProdDO.getCurrency();
        String amount = AmtUtils.toYuan(fundAdjustProdDO.getAdjustAmt()).toString();
        ActiveCurrencyAndAmount activeCurrencyAndAmount = new ActiveCurrencyAndAmount(currency, amount);
        ntry.setAmt(activeCurrencyAndAmount);

        // 借贷记标识
        if (ClearingProdCdtDbtIndEnum.CRDT.getCode().equals(fundAdjustProdDO.getCdtDbtInd())) {
            ntry.setCdtDbtInd(ClearingProdCdtDbtIndEnum.CRDT.name());
        } else {
            ntry.setCdtDbtInd(ClearingProdCdtDbtIndEnum.DBIT.name());
        }

        // 业务状态
        Sts sts = new Sts();
        sts.setPrtry(fundAdjustProdDO.getBizSts());
        ntry.setSts(sts);

        BkTxCd bkTxCd = new BkTxCd();
        Prtry prtry = new Prtry();
        prtry.setCd("0");
        bkTxCd.setPrtry(prtry);
        ntry.setBkTxCd(bkTxCd);

        // 记账日期
        BookgDt bookgDt = new BookgDt();
        bookgDt.setDt(DateUtils.formatInstDate(fundAdjustProdDO.getSttlmDt()));
        ntry.setBookgDt(bookgDt);

        // 原HVPS报文标识号
        Refs refs = new Refs();
        refs.setMsgId(hvpsTransDO.getHvpsMsgId());

        // 原HVPS业务发起直接参与者行号
        refs.setAcctSvcrRef(hvpsTransDO.getHvpsSendPty());

        PrtryOfTp prtryOfTp = new PrtryOfTp();
        prtryOfTp.setTp(hvpsTransDO.getHvpsMsgTp());  // 原HVPS报文编号
        refs.setPrtry(prtryOfTp);

        TxDtls txDtls = new TxDtls();
        txDtls.setRefs(refs);

        NtryDtls ntryDtls = new NtryDtls();
        ntryDtls.setTxDtls(txDtls);
        ntry.setNtryDtls(ntryDtls);

        Ntfctn ntfctn = new Ntfctn();
        ntfctn.setId(fundAdjustProdDO.getReportMsgId());    // 唯一标识（填写报文标识号）
        ntfctn.setAcct(acct);
        ntfctn.setNtry(ntry);

        com.dcep.dips.wholesalepayment.dto.dc185.SplmtryData splmtryData =
                new com.dcep.dips.wholesalepayment.dto.dc185.SplmtryData();
        splmtryData.setPlcAndNm("/Document/BkToCstmrDbtCdtNtfctn");

        com.dcep.dips.wholesalepayment.dto.dc185.Envlp envlp = new com.dcep.dips.wholesalepayment.dto.dc185.Envlp();
        envlp.setTrfdPty(fundAdjustProdDO.getAdjustPtyId());
        String custodianInst = InfoCacheUtil.getCustodianInstNo(fundAdjustProdDO.getAdjustPtyId());
        if (!fundAdjustProdDO.getAdjustPtyId().equals(custodianInst)) {
            envlp.setCstdn(custodianInst);
        }

        envlp.setOptTp(fundAdjustProdDO.getAdjustTp());
        envlp.setCILmt(AmtUtils.toYuan(fundAdjustProdDO.getCiLimit()).toString());
        envlp.setNtQt(AmtUtils.toYuan(fundAdjustProdDO.getNetQuota()).toString());

        if (HvpsAdjTypEnum.DECREASE.getCode().equals(fundAdjustProdDO.getAdjustTp())
                || HvpsAdjTypEnum.PRE_DECREASE.getCode().equals(fundAdjustProdDO.getAdjustTp())) {
            envlp.setOrgnlDbtReqInstgPty(fundAdjustProdDO.getSendPtyId());
            envlp.setOrgnlDbtReqMsgId(fundAdjustProdDO.getMsgId());
        }

        splmtryData.setEnvlp(envlp);

        Dcep18500101DTO dcep185DTO = new Dcep18500101DTO();
        dcep185DTO.setGrpHdr(grpHdr);
        dcep185DTO.setNtfctn(ntfctn);
        dcep185DTO.setSplmtryData(splmtryData);

        SoapHeader soapHeader = SoapHeaderUtil.createSoapHeader(
                fundAdjustProdDO.getReportMsgTp(),
                fundAdjustProdDO.getReportMsgId() + "0001",
                InfoCacheUtil.getPbocInf(),
                receiver);

        // 返回185报文
        return new EnvelopeDTO<>(soapHeader, dcep185DTO);
    }

    public static Response<EnvelopeDTO<GwDTO>> assembly900Msg(EnvelopeDTO<GwDTO> gwReqDTO, FundAdjustProdDO fundAdjustProdDO) {
        FundingDTO fundingDTO = (FundingDTO) gwReqDTO.body();

        Dcep90000101DTO dcep900DTO = new Dcep90000101DTO();

        String msgId = MsgIdUtil.genMsgIdByOriMsgId(fundingDTO.msgId(), MsgTpEnum.COMMON_PROCESS_CONFIRM_ABBR.getCode());
        String creDtTm = DcepDateUtils.getDcepDateStrNow();
        String sender = InfoCacheUtil.getPbocInf();
        String receiver = fundingDTO.sendPtyId();

        dcep900DTO.setGrpHdr(new com.dcep.common.model.soap.GrpHdr(msgId, creDtTm, sender, receiver, null));

        String orgnlMsgId = fundingDTO.msgId();
        String orgnlInstgPty = fundingDTO.sendPtyId();
        String orgnlMT = fundingDTO.msgTp();

        dcep900DTO.setOrgnlGrpHdr(new OrgnlGrpHdr(orgnlMsgId, orgnlInstgPty, orgnlMT));

        CmonConfInf cmonConfInf = new CmonConfInf(fundAdjustProdDO.getBizSts(),
                fundAdjustProdDO.getBizPrcCd(), fundAdjustProdDO.getBizPrcInf(), null);

        dcep900DTO.setCmonConfInf(cmonConfInf);

        SoapHeader soapHeader = SoapHeaderUtil.createSoapHeader(MsgTpEnum.COMMON_PROCESS_CONFIRM.getCode(),
                gwReqDTO.getSoapHeader().getMsgSN(),
                InfoCacheUtil.getPbocInf(),
                gwReqDTO.getSoapHeader().getSender());

        log.info("返回机构dcep.900报文，bizSts={}, {}, {}", fundAdjustProdDO.getBizSts(), soapHeader, dcep900DTO);
        return new Response<>(new EnvelopeDTO<>(soapHeader, dcep900DTO));
    }

    public static GenericEnvelopeDTO<GenericGwDTO> assemblyMcbs900(GenericEnvelopeDTO<GenericGwDTO> mBridgeReqEnvelopeDTO,
                                                                   String mBridgeReqHdlgCode,
                                                                   String mBridgeReqHdlgDesc){

        McbsEnvelopeDTO<McbsGwDTO> mcbsEnvelopeDTO = (McbsEnvelopeDTO<McbsGwDTO>) mBridgeReqEnvelopeDTO;
        Mcbs90000101DTO mcbs900DTO = new Mcbs90000101DTO();
        com.dcep.gateway.mcbdc.dto.common.MessageHeader mcbs900MsgHeader =
                new com.dcep.gateway.mcbdc.dto.common.MessageHeader();
        mcbs900MsgHeader.setMsgId(mBridgeReqEnvelopeDTO.body().fetchMsgId());
        String mcbsSndDtTm = TimeUtil.getMcbsCurrentTime(DcepDateUtils.ISO_DATETIME_PATTERN);
        mcbs900MsgHeader.setCreateDateTime(mcbsSndDtTm);
        mcbs900DTO.setGrpHdr(mcbs900MsgHeader);

        ReceiptDetails mcbs900ReceiptDetails = new ReceiptDetails();
        mcbs900ReceiptDetails.setOriginMsgId(new OriginMsgId(mBridgeReqEnvelopeDTO.body().fetchMsgId(),
                mcbsEnvelopeDTO.getSoapHeader().getMsgTp()));
        mcbs900ReceiptDetails.setRequestHandling(new RequestHandling(mBridgeReqHdlgCode, mBridgeReqHdlgDesc));
        mcbs900DTO.setReceiptDetails(mcbs900ReceiptDetails);

        McbsSoapHeader soapHeader = new McbsSoapHeader(Constant.MCBS_SOAPHEADER_VER, mcbsSndDtTm,
                Constant.MCBS_MSGTYPE_900, mcbsEnvelopeDTO.getSoapHeader().getSenderLEI(),
                mcbsEnvelopeDTO.getSoapHeader().getSenderCBMALEI(), mcbsEnvelopeDTO.getSoapHeader().getReceiverLEI(),
                mcbsEnvelopeDTO.getSoapHeader().getReceiverCBMALEI(), Constant.DIRECTION_FROM_HLHT_TO_MBRIDGE, null, null, null, "wholesalepayment");
        McbsSoapBody<McbsGwDTO> soapBody = new McbsSoapBody<>();
        soapBody.setT(mcbs900DTO);
        McbsEnvelopeDTO<McbsGwDTO> respMcbsEnvelopeDTO = new McbsEnvelopeDTO<>();
        respMcbsEnvelopeDTO.setSoapHeader(soapHeader);
        respMcbsEnvelopeDTO.setSoapBody(soapBody);
        return respMcbsEnvelopeDTO;
    }

    public static GenericEnvelopeDTO<GenericGwDTO> assemblyMcbs203(GenericEnvelopeDTO<GenericGwDTO> mBridgeReqEnvelopeDTO,
                                                                   String mBridgeReqHdlgCode,
                                                                   String mBridgeReqHdlgDesc,
                                                                   MbridgeReqDTO mbridgeDTO){
        McbsEnvelopeDTO<McbsGwDTO> mcbsEnvelopeDTO = (McbsEnvelopeDTO<McbsGwDTO>) mBridgeReqEnvelopeDTO;
        Mcbs20300101DTO mcbs203DTO = new Mcbs20300101DTO();
        GroupHeader groupHeader = new GroupHeader();
        groupHeader.setMsgId(com.dcep.dips.wholesalepayment.common.utils.MsgIdUtil.genMcbsMsgId(mbridgeDTO));
        String mcbsSndDtTm = TimeUtil.getMcbsCurrentTime(DcepDateUtils.ISO_DATETIME_PATTERN);
        groupHeader.setCreateDateTime(mcbsSndDtTm);
        mcbs203DTO.setGrpHdr(groupHeader);

        mcbs203DTO.setOriginGroupInfoAndStatus(new OriginGroupInfoAndStatus(mBridgeReqEnvelopeDTO.body().fetchMsgId(), mcbsEnvelopeDTO.getSoapHeader().getMsgTp()));

        TransactionInfoAndStatus transactionInfoAndStatus = getTransactionInfoAndStatus(mBridgeReqHdlgCode, mBridgeReqHdlgDesc);
        mcbs203DTO.setTransactionInfoAndStatus(transactionInfoAndStatus);

        McbsSoapHeader soapHeader = toMcbsSoapHeader(Constant.MCBS_MSGTYPE_203);
        McbsSoapBody<McbsGwDTO> soapBody = new McbsSoapBody<>();
        soapBody.setT(mcbs203DTO);
        McbsEnvelopeDTO<McbsGwDTO> respMcbsEnvelopeDTO = new McbsEnvelopeDTO<>();
        respMcbsEnvelopeDTO.setSoapHeader(soapHeader);
        respMcbsEnvelopeDTO.setSoapBody(soapBody);
        return respMcbsEnvelopeDTO;
    }

    public static GenericEnvelopeDTO<GenericGwDTO> assemblyMcbs204(GenericEnvelopeDTO<GenericGwDTO> mBridgeReqEnvelopeDTO, MbridgeReqDTO mbridgeDTO){
        McbsEnvelopeDTO<McbsGwDTO> mcbsEnvelopeDTO = (McbsEnvelopeDTO<McbsGwDTO>) mBridgeReqEnvelopeDTO;
        Mcbs20400101DTO mcbs204DTO = new Mcbs20400101DTO();
        GroupHeader groupHeader = new GroupHeader();
        groupHeader.setMsgId(com.dcep.dips.wholesalepayment.common.utils.MsgIdUtil.genMcbsMsgId(mbridgeDTO));
        String mcbsSndDtTm = TimeUtil.getMcbsCurrentTime(DcepDateUtils.ISO_DATETIME_PATTERN);
        groupHeader.setCreateDateTime(mcbsSndDtTm);
        mcbs204DTO.setGrpHdr(groupHeader);

        mcbs204DTO.setOriginGroupInfo(new OriginGroupInfo(mBridgeReqEnvelopeDTO.body().fetchMsgId(), mcbsEnvelopeDTO.getSoapHeader().getMsgTp()));

        McbsSoapHeader soapHeader = toMcbsSoapHeader(MsgTpEnum.QUERY_STATUS_MCBS.getCode());
        McbsSoapBody<McbsGwDTO> soapBody = new McbsSoapBody<>();
        soapBody.setT(mcbs204DTO);
        McbsEnvelopeDTO<McbsGwDTO> respMcbsEnvelopeDTO = new McbsEnvelopeDTO<>();
        respMcbsEnvelopeDTO.setSoapHeader(soapHeader);
        respMcbsEnvelopeDTO.setSoapBody(soapBody);
        return respMcbsEnvelopeDTO;
    }

    public static TransactionInfoAndStatus getTransactionInfoAndStatus(String mBridgeReqHdlgCode, String mBridgeReqHdlgDesc) {
        TransactionInfoAndStatus transactionInfoAndStatus = new TransactionInfoAndStatus();
        transactionInfoAndStatus.setStatusId(mBridgeReqHdlgCode);
        TransactionInfoAndStatusSupplementaryData transactionInfoAndStatusSupplementaryData = new TransactionInfoAndStatusSupplementaryData();
        transactionInfoAndStatusSupplementaryData.setPlaceAndName(Constant.MBRIDGE_203_PLACE_AND_NAME);
        transactionInfoAndStatus.setTransactionInfoAndStatusSupplementaryData(transactionInfoAndStatusSupplementaryData);
        if (mBridgeReqHdlgDesc != null){
            transactionInfoAndStatus.setStatusReasonInfo(new StatusReasonInfo(mBridgeReqHdlgDesc));
        }
        return transactionInfoAndStatus;
    }

    public static McbsSoapHeader toMcbsSoapHeader(String msgTp) {
        McbsSoapHeader mcbsSoapHeader = new McbsSoapHeader();
        mcbsSoapHeader.setVer(Constant.MCBS_SOAPHEADER_VER);
        mcbsSoapHeader.setSndDtTm(TimeUtil.getMcbsCurrentTime(DcepDateUtils.ISO_DATETIME_PATTERN));
        mcbsSoapHeader.setMsgTp(msgTp);
        mcbsSoapHeader.setSenderLEI(Constant.LEI_PBOC);
        mcbsSoapHeader.setSenderCBMALEI(Constant.LEI_PBOC);
        mcbsSoapHeader.setReceiverLEI(Constant.LEI_MCBS);
        mcbsSoapHeader.setReceiverCBMALEI(Constant.LEI_MCBS);
        mcbsSoapHeader.setMessageDirection(Constant.DIRECTION_FROM_HLHT_TO_MBRIDGE);
        return mcbsSoapHeader;
    }

    public static EnvelopeDTO<GwDTO> dcep911(String msgSn, String sender, String receiver, String code, String msg,
                                             String actor, String detail) {
        Dcep91100101DTO fault = new Dcep91100101DTO();
        fault.setFaultcode(code);
        fault.setFaultstring(msg);
        fault.setFaultactor(actor);
        if (null != detail) {
            if (detail.length() <= Constant.DCEP_911_DETAIL_MAX) {
                fault.setDetail(detail);
            } else {
                fault.setDetail(detail.substring(0, Constant.DCEP_911_DETAIL_MAX));
            }
        }
        SoapBody<GwDTO> soapBody = new SoapBody<>();
        soapBody.setT(fault);
        EnvelopeDTO<GwDTO> dto = new EnvelopeDTO<>();
        SoapHeader soapHeader = new SoapHeader(Constant.DCEP_SOAPHEADER_VER,
                TimeUtil.getHlhtCurrentTime(DcepDateUtils.ISO_DATETIME_PATTERN),
                MessageTypeEnum.DCEP_911_001_01.getCode(), msgSn, sender, receiver);
        dto.setSoapHeader(soapHeader);
        dto.setSoapBody(soapBody);
        return dto;
    }

    /**
     * 组装mcbs100报文
     * @param msgId
     * @return
     */
    public static GenericEnvelopeDTO<GenericGwDTO> assembly100Msg(String msgId) {
        Mcbs10000101DTO mcbs10000101DTO = new Mcbs10000101DTO();
        com.dcep.dips.wholesalepayment.dto.mcbs100.GrpHdr grpHdr = new com.dcep.dips.wholesalepayment.dto.mcbs100.GrpHdr();
        grpHdr.setMsgId(msgId);
        grpHdr.setCreDtTm(TimeUtil.getMcbsCurrentTime(DcepDateUtils.ISO_DATETIME_PATTERN));
        mcbs10000101DTO.setGrpHdr(grpHdr);
        NoInf noInf = new NoInf();
        noInf.setClrZeDt(TimeUtil.getMcbsCurrentTime(DcepDateUtils.ISO_DATE_PATTERN));
        mcbs10000101DTO.setNoInf(noInf);
        McbsSoapBody<McbsGwDTO> soapBody = new McbsSoapBody<>();
        McbsSoapHeader soapHeader = toMcbsSoapHeader(Constant.MCBS_MSGTYPE_100);
        soapBody.setT(mcbs10000101DTO);
        McbsEnvelopeDTO<McbsGwDTO> respMcbsEnvelopeDTO = new McbsEnvelopeDTO<>();
        respMcbsEnvelopeDTO.setSoapHeader(soapHeader);
        respMcbsEnvelopeDTO.setSoapBody(soapBody);
        return respMcbsEnvelopeDTO;
    }

    private static FinInstnId createFinInstnId(String code, String mmbId) {
        FinInstnId finInstnId = new FinInstnId();
        FinInstnId.ClrSysMmbId clrSysMmbId = new FinInstnId.ClrSysMmbId();
        FinInstnId.ClrSysId clrSysId = new FinInstnId.ClrSysId();
        clrSysId.setCd(code);
        clrSysMmbId.setClrSysId(clrSysId);
        clrSysMmbId.setMmbId(mmbId);
        finInstnId.setClrSysMmbId(clrSysMmbId);
        return finInstnId;
    }

    /**
     * 组装业务撤销响应报文-428报文
     * @param
     * @return
     */
    public static Response<EnvelopeDTO> assembly428Msg(EnvelopeDTO<GwDTO> gwReqDTO,SettlementProdDO orgiSettle,String reverseStatus) {
        //TODO 需要检查赋值是否正确
        ClearingDTO clearingDTO = (ClearingDTO) gwReqDTO.body();
        Dcep42700101DTO dcep42700101DTO = (Dcep42700101DTO) gwReqDTO.body();
        Dcep42800101DTO dcep42800101DTO = new Dcep42800101DTO();
        Assgnmt assgnmt = new Assgnmt();
        assgnmt.setId(clearingDTO.getClrMsgId());

        assgnmt.setAssgnr(new Assgnr(new Agt(createFinInstnId(clearingDTO.clrSendSysId(),clearingDTO.clrSendSysId()))));
        assgnmt.setAssgne(new Assgne(new Agt(createFinInstnId(clearingDTO.clrRecvSysId(),clearingDTO.clrRecvPtyId()))));
        assgnmt.setCreDtTm(DcepDateUtils.getDcepDateStrNow());
        dcep42800101DTO.setAssgnmt(assgnmt);

        //撤销处理状态
        dcep42800101DTO.setSts(new com.dcep.dips.wholesalepayment.dto.dc428.Sts(reverseStatus));

        String bizSts = orgiSettle!=null?orgiSettle.getBizSts():"";
        String bizPrcCd = (orgiSettle!=null && orgiSettle.getBizPrcCd()!=null)?orgiSettle.getBizPrcCd():"000";
        String bizPrcInf = (orgiSettle!=null && orgiSettle.getBizPrcInf()!=null) ?orgiSettle.getBizPrcInf():"000";

        CxlDtls cxlDtls = new CxlDtls();
        com.dcep.dips.wholesalepayment.dto.dc428.TxInfAndSts txInfAndSts = new com.dcep.dips.wholesalepayment.dto.dc428.TxInfAndSts();
        txInfAndSts.setCxlStsId(bizSts);
        txInfAndSts.setRslvdCase(new RslvdCase(clearingDTO.getClrMsgId(), new Cretr(new Agt(createFinInstnId(clearingDTO.clrSendSysId(),clearingDTO.clrSendPtyId())))));
        txInfAndSts.setOrgnlGrpInf(new com.dcep.dips.wholesalepayment.dto.dc428.OrgnlGrpInf(clearingDTO.recOrgnlMsgId(), clearingDTO.recOrgnlMsgTp()));
        txInfAndSts.setOrgnlUETR(dcep42700101DTO.getUndrlyg().getTxInf().getOrgnlUETR());
        txInfAndSts.setCxlStsRsnInf(new CxlStsRsnInf(new com.dcep.dips.wholesalepayment.dto.dc428.Rsn(bizPrcCd), bizPrcInf));
        cxlDtls.setTxInfAndSts(txInfAndSts);

        dcep42800101DTO.setCxlDtls(cxlDtls);
        SoapHeader header = SoapHeaderUtil.createSoapHeader(MsgTpEnum.REVERSE_RESPONSE.getCode(), gwReqDTO.getSoapHeader().getMsgSN(),clearingDTO.clrSendPtyId(),clearingDTO.clrRecvPtyId());
        return new Response<>(new EnvelopeDTO<>(header, dcep42800101DTO));
    }


//    public static EnvelopeDTO<GwDTO> assembly200Msg(String receiver,SettlementProdDO settle,AccountingInstrDO account,String errorCode,String errorMsg) {
//        //TODO 需要检查赋值是否正确
////        SettlementProdDO settlementProdDO = response.getResult();
////        ClearingDTO clearingDTO = (ClearingDTO) req.body();
//        Dcep20000101DTO dcep20000101DTO = new Dcep20000101DTO();
//        GrpHdr grpHdr = new GrpHdr(MsgIdUtil.randomMsgId(MsgTpEnum.SETTLE_NOTICE_ABBR.getCode(), InfoCacheUtil.getPbocInnerCode(), CommonUtil.getEnvVal()), DcepDateUtils.getDcepDateStrNow());
//        dcep20000101DTO.setGrpHdr(grpHdr);
//
//        OrgnlGrpInf orgnlGrpInf = new OrgnlGrpInf(settle.getMsgId(), settle.getMsgTp());
//        StsRsnInf stsRsnInf = new StsRsnInf(new Rsn(errorCode),errorMsg);
//        InstgAgt instgAgt = new InstgAgt(createFinInstnId(ChnlSysEnum.DCEP.getCode(), InfoCacheUtil.getPbocInf()));
//        InstdAgt instdAgt = new InstdAgt(createFinInstnId(ChnlSysEnum.DCEP.getCode(),receiver));//报文接受机构
//        OrgnlTxRef orgnlTxRef = new OrgnlTxRef(new ActiveCurrencyAndAmount(settle.getCurrency(),AmtUtils.toYuan(settle.getSttlmAmt()).toString()));
//
//        TxInfAndSts txInfAndSts = new TxInfAndSts(orgnlGrpInf,settle.getBatId() ,account.getEndToEndId(),null,null,
//                settle.getBizSts(),stsRsnInf,instgAgt,instdAgt,orgnlTxRef,null);
//
//        // 仅当业务状态为已结算时 返回结算日期、结算时间
//        if (ClearingStatusEnum.SETTLED.getCode().equals(settle.getBizSts())) {
//            SplmtryData splmtryData = new SplmtryData("/Document/FIToFIPmtStsRpt/TxInfAndSts", new Envlp(settle.getSttlmDt()));
//            txInfAndSts.setSplmtryData(splmtryData);
//        }
//        dcep20000101DTO.setTxInfAndSts(txInfAndSts);
//        SoapHeader header = SoapHeaderUtil.createSoapHeader(MsgTpEnum.SETTLE_NOTICE.getCode(),grpHdr.getMsgId()+"0001",InfoCacheUtil.getPbocInf(),receiver);
//        return new EnvelopeDTO<>(header, dcep20000101DTO);
//    }


    /**
     * 组装mcbs102报文
     * @param msgId
     * @return
     */
    public static GenericEnvelopeDTO<GenericGwDTO> assembly102Msg(com.dcep.dips.wholesalepayment.dto.mcbs102.OrgnlGrpInf orgnlGrpInf,String msgId) {
        Mcbs10200101DTO mcbs10200101DTO = new Mcbs10200101DTO();
        MsgHdr msgHdr = new MsgHdr();
        msgHdr.setMsgId(msgId);
        msgHdr.setCreDtTm(TimeUtil.getMcbsCurrentTime(DcepDateUtils.ISO_DATETIME_PATTERN));
        mcbs10200101DTO.setMsgHdr(msgHdr);
        mcbs10200101DTO.setOrgnlGrpInf(orgnlGrpInf);
        McbsSoapBody<McbsGwDTO> soapBody = new McbsSoapBody<>();
        McbsSoapHeader soapHeader = toMcbsSoapHeader(Constant.MCBS_MSGTYPE_102);
        soapBody.setT(mcbs10200101DTO);
        McbsEnvelopeDTO<McbsGwDTO> respMcbsEnvelopeDTO = new McbsEnvelopeDTO<>();
        respMcbsEnvelopeDTO.setSoapHeader(soapHeader);
        respMcbsEnvelopeDTO.setSoapBody(soapBody);
        return respMcbsEnvelopeDTO;
    }


    /**
     * 组装411报文
     * @param receiver
     * @param settlementProdDO
     * @return
     */
    public static EnvelopeDTO<GwDTO> assembly411Msg(String receiver,SettlementProdDO settlementProdDO)  {

        // 411报文环境标识位取原交易的环境标识位
        String msgId = MsgIdUtil.randomMsgId(MsgTpEnum.TXN_STATE_REQUEST_ABBR.getCode(), Constant.PBOC, CommonUtil.getEnvVal());
        com.dcep.common.model.soap.GrpHdr grpHdr = new com.dcep.common.model.soap.GrpHdr(msgId, DcepDateUtils.formateDcepDate(new Date()), InfoCacheUtil.getPbocInf(), receiver,"交易状态查询");
        com.dcep.dips.common.dto.dc411.OrgnlGrpHdr orgnlGrpHdr = new com.dcep.dips.common.dto.dc411.OrgnlGrpHdr(settlementProdDO.getMsgId(), settlementProdDO.getSendPtyId(), settlementProdDO.getMsgTp(), settlementProdDO.getBizTp(),
                settlementProdDO.getBizKind(), null, null);
        
        SoapHeader header = SoapHeaderUtil.createSoapHeader(MsgTpEnum.TXN_STATE_REQUEST.getCode(), msgId+"0001",InfoCacheUtil.getPbocInf(),receiver);
        return new EnvelopeDTO<>(header, new Dcep41100101DTO(grpHdr,orgnlGrpHdr));
    }

    /**
     * 组装909文
     * @param receiver
     * @param settlementProdDO
     * @return
     */
    public static EnvelopeDTO<GwDTO> assembly909Msg(String receiver,SettlementProdDO settlementProdDO,ClearingDTO origClearingDTO)  {

        String msgId909 = MsgIdUtil.randomMsgId(MsgTpEnum.FINALNOTICE_ABBR.getCode(), Constant.PBOC,CommonUtil.getEnvVal());
        // 业务头组件
        com.dcep.common.model.soap.GrpHdr grpHdr = new com.dcep.common.model.soap.GrpHdr();
        grpHdr.setMsgId(msgId909);
        grpHdr.setCreDtTm(DcepDateUtils.getDcepDateStrNow());
        // 发起机构是央行
        com.dcep.common.model.soap.InstgPty instgPty = new com.dcep.common.model.soap.InstgPty();
        instgPty.setInstgDrctPty(InfoCacheUtil.getPbocInf());
        grpHdr.setInstgPty(instgPty);
        // 接收机构
        com.dcep.common.model.soap.InstdPty instdPty = new com.dcep.common.model.soap.InstdPty();
        instdPty.setInstdDrctPty(receiver);
        grpHdr.setInstdPty(instdPty);

        // 原报文主键组件
        OrgnlGrpHdr orgnlGrpHdr = origClearingDTO.presumeConfirm();


        SoapHeader header = SoapHeaderUtil.createSoapHeader(MsgTpEnum.FINALNOTICE.getCode(), msgId909+"0001",InfoCacheUtil.getPbocInf(),receiver);
        return new EnvelopeDTO<>(header, new Dcep90900101DTO(grpHdr, orgnlGrpHdr, getOrgnlMsgCntt( settlementProdDO, origClearingDTO)));
    }



    private static  OrgnlMsgCntt getOrgnlMsgCntt(SettlementProdDO clearingProdDO, ClearingDTO origClearingDTO){

        // 调用组件将DTO转为xml报文字符串
        String content = null;
        try {
            content = SoapUtils.toXml(origClearingDTO);
        } catch (Exception e) {
            throw  new DcepException("解析报文错误");
        }
        OrgnlMsgCntt orgnlMsgCntt = new OrgnlMsgCntt(content);
        orgnlMsgCntt.setPrcSts(clearingProdDO.getBizSts());
        orgnlMsgCntt.setPrcCd(clearingProdDO.getBizPrcCd());
        if (null != clearingProdDO.getBizPrcCd()) {
            orgnlMsgCntt.setRjctCd(clearingProdDO.getBizPrcCd());
        }
        if (null != clearingProdDO.getBizPrcInf()) {
            orgnlMsgCntt.setRjctInf(clearingProdDO.getBizPrcInf());
        }
//        // 251/262交易推定时，赋值909报文推送付款人钱包名称、付款人钱包ID信息
//        if ((MsgTpEnum.CSM_REQUEST.getCode().equals(clearingProdDO.getMsgTp())
//                || MsgTpEnum.ORDR_CONF_RESPONSE.getCode().equals(clearingProdDO.getMsgTp()))
//                && StringUtils.isNotBlank(clearingProdDO.getTrxInf())) {
//            orgnlMsgCntt.setDbtrWltId(clearingProdDO.getTrxInf().split("-")[0]);
//            orgnlMsgCntt.setDbtrWltNm(clearingProdDO.getTrxInf().split("-")[1]);
//        }

        // 原交易成功且412查询返回营销信息时，赋值909报文营销信息
        if (ClearingStatusEnum.SETTLED.getCode().equals(clearingProdDO.getBizSts())) {
//            RecordDTO presumeRecord = clearingProdDO.getPresumeRecord();
//            // 原交易营销信息为空情况下查询档案表412报文获取营销信息
//            if (null == presumeRecord) {
//                presumeRecord = recordManager.resume(MsgTpEnum.TXN_STATE_RESPONSE.getCode(), clearingProdDO.getMsgId());
//            }
//
//            if (null != presumeRecord) {
//                // 412报文营销信息不为空，赋值营销信息
//                if (null != presumeRecord.clrPrmtInf()) {
//                    orgnlMsgCntt.setPrmtInf(presumeRecord.clrPrmtInf());
//                }
//
//                // 412报文收款人名称不为空，赋值收款人名称
//                if (null != presumeRecord.clrCdtrNm()) {
//                    orgnlMsgCntt.setOrgnlCdtrNm(presumeRecord.clrCdtrNm());
//                }
//
//                // 居民类型不为空，赋值居民类型
//                if (null != presumeRecord.clrResdtTp()) {
//                    orgnlMsgCntt.setResdtTp(presumeRecord.clrResdtTp());
//                }
//                // 常驻国家/地区代码不为空，赋值常驻国家/地区代码
//                if (null != presumeRecord.clrResdtCtryCd()) {
//                    orgnlMsgCntt.setResdtCtryCd(presumeRecord.clrResdtCtryCd());
//                }
//                // 钱包注册手机号所在国家/地区代码不为空，赋值钱包注册手机号所在国家/地区代码
//                if (null != presumeRecord.clrRegrCtryCd()) {
//                    orgnlMsgCntt.setRegrCtryCd(presumeRecord.clrRegrCtryCd());
//                }
//
//                // 412报文原付款钱包ID不为空，赋值原付款钱包ID
//                if (null != presumeRecord.recDbtrWltId()
//                        && (!MsgTpEnum.COV_REQUREST.getCode().equals(clearingProdDO.getMsgTp()))) {
//                    orgnlMsgCntt.setDbtrWltId(presumeRecord.recDbtrWltId());
//                }
//
//                //242报文终端支持类型
//                if (null != presumeRecord.clrBizPayMtd()) {
//                    orgnlMsgCntt.setBizPayMtd(presumeRecord.clrBizPayMtd());
//                }
//
//                //242报文跳转信息
//                if (null != presumeRecord.clrRdrctUrl()) {
//                    orgnlMsgCntt.setRdrctInf(new RdrctInf(presumeRecord.clrRdrctUrl()));
//                }
//            }
        }

        return orgnlMsgCntt;
    }



}

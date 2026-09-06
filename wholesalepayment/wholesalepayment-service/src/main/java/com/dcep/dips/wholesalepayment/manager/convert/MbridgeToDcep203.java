/*
 * pbcdci.cn Inc.
 * Copyright © 2022 PBCDCI All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.manager.convert;

import com.dcep.dips.common.constant.CommonConstant;
import com.dcep.dips.wholesalepayment.dto.dc203.Dcep20301001DTO;
import com.dcep.dips.wholesalepayment.dto.mcbs.MbridgeReqDTO;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.SoapBody;
import com.dcep.common.utils.DcepDateUtils;
import com.dcep.gateway.mcbdc.dto.mcbs200.Mcbs20000101DTO;
import com.dcep.gateway.mcbdc.dto.mcbs202.Mcbs20200101DTO;
import com.dcep.gateway.mcbdc.dto.soap.GenericEnvelopeDTO;
import com.dcep.gateway.mcbdc.dto.soap.GenericGwDTO;
import com.dcep.gateway.mcbdc.dto.soap.McbsEnvelopeDTO;
import com.dcep.gateway.mcbdc.dto.soap.McbsGwDTO;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.common.utils.BatIdUtil;
import com.dcep.dips.wholesalepayment.common.utils.DtoUtil;
import com.dcep.dips.wholesalepayment.common.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 货币桥200、202转DCEP203
 *
 * @author laowei
 * @version $Id: MbridgeToDcep203.java, v 0.1 2022年5月24日 上午7:46:11 laowei Exp $
 */
public final class MbridgeToDcep203 {
    public static EnvelopeDTO<GwDTO> convert(GenericEnvelopeDTO<GenericGwDTO> mBridgeEnvelope) {
        if (mBridgeEnvelope.body() instanceof Mcbs20200101DTO) {
            return convert202(mBridgeEnvelope);
        }

        McbsEnvelopeDTO<McbsGwDTO> mcbs200Envelope = (McbsEnvelopeDTO<McbsGwDTO>) mBridgeEnvelope;
        EnvelopeDTO<GwDTO> dcep203Envelope = new EnvelopeDTO<>();
        com.dcep.common.model.soap.SoapHeader dcepSoapHeader = DtoUtil.toDcepSoapHeader(mcbs200Envelope);
        dcep203Envelope.setSoapHeader(dcepSoapHeader);
        SoapBody<GwDTO> dcepSoapBody = new SoapBody<>();
        dcep203Envelope.setSoapBody(dcepSoapBody);

        Mcbs20000101DTO mcbs200DTO = (Mcbs20000101DTO) mcbs200Envelope.body();
        String mcbsTime = mcbs200DTO.getGrpHdr().getCreateDateTime();
        String hlhtTime = TimeUtil.fromMcbs(mcbsTime, DcepDateUtils.ISO_DATETIME_PATTERN);
        com.dcep.dips.wholesalepayment.dto.dc203.GrpHdr dcep203GroupHeader = new com.dcep.dips.wholesalepayment.dto.dc203.GrpHdr();
        dcep203GroupHeader.setCreDtTm(hlhtTime);
        dcep203GroupHeader.setNbOfTxs(Constant.NUM_OF_TXS_1);
        dcep203GroupHeader.setSttlmInf(new com.dcep.dips.wholesalepayment.dto.dc203.SttlmInf(Constant.SETTLE_METHOD_CLRG));
        Dcep20301001DTO dcep203DTO = new Dcep20301001DTO();
        dcep203DTO.setGrpHdr(dcep203GroupHeader);

        com.dcep.dips.wholesalepayment.dto.dc203.CdtTrfTxInf dcep203CdtTrfTxInf = new com.dcep.dips.wholesalepayment.dto.dc203.CdtTrfTxInf();
        String mcbsBatchNo = mcbs200DTO.getCreditTransferTxInfo()
                .getCreditTransferTxInfoSupplementaryData()
                .getCreditTxInfoEnvelope().getContents().getBatchNo();
        String dcepBatchNo = BatIdUtil.trsfMcbsBatIdToHlhtBatId(mcbsBatchNo);
        com.dcep.dips.wholesalepayment.dto.dc203.PmtId dcep203PmtId = new com.dcep.dips.wholesalepayment.dto.dc203.PmtId();
        dcep203PmtId.setInstrId(dcepBatchNo);
        dcep203CdtTrfTxInf.setPmtId(dcep203PmtId);

        com.dcep.dips.wholesalepayment.dto.dc203.PmtTpInf dcep203PmtTpInf = new com.dcep.dips.wholesalepayment.dto.dc203.PmtTpInf();
        com.dcep.dips.wholesalepayment.dto.dc203.SvcLvl dcep203SvcLvl = new com.dcep.dips.wholesalepayment.dto.dc203.SvcLvl();
        dcep203SvcLvl.setPrtry(Constant.SVCSVL_PRTRY_TT00);
        dcep203PmtTpInf.setSvcLvl(dcep203SvcLvl);
        com.dcep.dips.wholesalepayment.dto.dc203.CtgyPurp dcep203CtgyPurp = new com.dcep.dips.wholesalepayment.dto.dc203.CtgyPurp();
        dcep203CtgyPurp.setPrtry(Constant.DCEP_CTGYPURP_223);
        dcep203PmtTpInf.setCtgyPurp(dcep203CtgyPurp);
        dcep203CdtTrfTxInf.setPmtTpInf(dcep203PmtTpInf);

        dcep203CdtTrfTxInf.setIntrBkSttlmAmt(mcbs200DTO.getCreditTransferTxInfo().getInterBankSettleAmount());
        dcep203CdtTrfTxInf.setChrgBr(Constant.CHRGBR_DEBT);

        // mcbs200发起机构为dcep203付款运营机构
        com.dcep.dips.wholesalepayment.dto.dc203.FinInstnId dcep203InstgFinInstnId = new com.dcep.dips.wholesalepayment.dto.dc203.FinInstnId();
        String instgOrgCode = mcbs200DTO.getCreditTransferTxInfo()
                .getInstructingAgent().getFinancialInstitutionID()
                .getClearingSystemMemberID().getMemberId();
        com.dcep.dips.wholesalepayment.dto.dc203.ClrSysMmbId dcep203InstgMmbId = new com.dcep.dips.wholesalepayment.dto.dc203.ClrSysMmbId();
        dcep203InstgMmbId.setMmbId(instgOrgCode);
        dcep203InstgFinInstnId.setClrSysMmbId(dcep203InstgMmbId);
        String instgOrgLei = mcbs200DTO.getCreditTransferTxInfo()
                .getInstructingAgent().getFinancialInstitutionID().getLei();
        dcep203InstgFinInstnId.setLei(instgOrgLei);
        com.dcep.dips.wholesalepayment.dto.dc203.InstgAgt dcep203InstgAgt = new com.dcep.dips.wholesalepayment.dto.dc203.InstgAgt();
        dcep203InstgAgt.setFinInstnId(dcep203InstgFinInstnId);

        // 付款机构
        com.dcep.dips.wholesalepayment.dto.dc203.BrnchId dcep203InstgBrnchId = new com.dcep.dips.wholesalepayment.dto.dc203.BrnchId();
        String debtorOrgCode = mcbs200DTO.getCreditTransferTxInfo()
                .getDebtor().getFinancialInstitutionID()
                .getClearingSystemMemberID().getMemberId();
        dcep203InstgBrnchId.setId(debtorOrgCode);
        String debtorOrgLei = mcbs200DTO.getCreditTransferTxInfo()
                .getDebtor().getFinancialInstitutionID().getLei();
        dcep203InstgBrnchId.setLei(debtorOrgLei);
        dcep203InstgAgt.setBrnchId(dcep203InstgBrnchId);
        dcep203CdtTrfTxInf.setInstgAgt(dcep203InstgAgt);

        dcep203Envelope.getSoapHeader().setSender(instgOrgCode);

        // mcbs200接收机构为dcep203收款运营机构
        com.dcep.dips.wholesalepayment.dto.dc203.FinInstnId dcep203InstdFinInstnId = new com.dcep.dips.wholesalepayment.dto.dc203.FinInstnId();
        String instdOrgCode = mcbs200DTO.getCreditTransferTxInfo()
                .getInstructedAgent().getFinancialInstitutionID()
                .getClearingSystemMemberID().getMemberId();
        com.dcep.dips.wholesalepayment.dto.dc203.ClrSysMmbId dcep203InstdMmbId = new com.dcep.dips.wholesalepayment.dto.dc203.ClrSysMmbId();
        dcep203InstdMmbId.setMmbId(instdOrgCode);
        dcep203InstdFinInstnId.setClrSysMmbId(dcep203InstdMmbId);
        String instdOrgLei = mcbs200DTO.getCreditTransferTxInfo()
                .getInstructedAgent().getFinancialInstitutionID().getLei();
        dcep203InstdFinInstnId.setLei(instdOrgLei);
        com.dcep.dips.wholesalepayment.dto.dc203.InstdAgt dcep203InstdAgt = new com.dcep.dips.wholesalepayment.dto.dc203.InstdAgt();
        dcep203InstdAgt.setFinInstnId(dcep203InstdFinInstnId);

        // 收款机构
        com.dcep.dips.wholesalepayment.dto.dc203.BrnchId dcep203InstdBrnchId = new com.dcep.dips.wholesalepayment.dto.dc203.BrnchId();
        String creditorOrgCode = mcbs200DTO.getCreditTransferTxInfo()
                .getCreditor().getFinancialInstitutionID()
                .getClearingSystemMemberID().getMemberId();
        dcep203InstdBrnchId.setId(creditorOrgCode);
        String creditorOrgLei = mcbs200DTO.getCreditTransferTxInfo()
                .getCreditor().getFinancialInstitutionID().getLei();
        dcep203InstdBrnchId.setLei(creditorOrgLei);
        dcep203InstdAgt.setBrnchId(dcep203InstdBrnchId);
        dcep203CdtTrfTxInf.setInstdAgt(dcep203InstdAgt);

        dcep203Envelope.getSoapHeader().setReceiver(instdOrgCode);

        com.dcep.dips.wholesalepayment.dto.dc203.DbtrAcct dcep203DbtrAcct = new com.dcep.dips.wholesalepayment.dto.dc203.DbtrAcct();
        com.dcep.dips.wholesalepayment.dto.dc203.Othr dcepDbtrOther = new com.dcep.dips.wholesalepayment.dto.dc203.Othr();
        dcepDbtrOther.setId(mcbs200DTO.getCreditTransferTxInfo().getDebtorAccount().getId().getOther().getId());
        com.dcep.dips.wholesalepayment.dto.dc203.Id dcep203DbtrId = new com.dcep.dips.wholesalepayment.dto.dc203.Id();
        dcep203DbtrId.setOthr(dcepDbtrOther);
        dcep203DbtrAcct.setId(dcep203DbtrId);
        dcep203DbtrAcct.setNm(mcbs200DTO.getCreditTransferTxInfo().getDebtorAccount().getName());
        dcep203CdtTrfTxInf.setDbtrAcct(dcep203DbtrAcct);

        com.dcep.dips.wholesalepayment.dto.dc203.CdtrAcct dcep203CdtrAcct = new com.dcep.dips.wholesalepayment.dto.dc203.CdtrAcct();
        com.dcep.dips.wholesalepayment.dto.dc203.Othr dcepCdtrOther = new com.dcep.dips.wholesalepayment.dto.dc203.Othr();
        dcepCdtrOther.setId(mcbs200DTO.getCreditTransferTxInfo().getCreditorAccount().getId().getOther().getId());
        com.dcep.dips.wholesalepayment.dto.dc203.Id dcep203CdtrId = new com.dcep.dips.wholesalepayment.dto.dc203.Id();
        dcep203CdtrId.setOthr(dcepCdtrOther);
        dcep203CdtrAcct.setId(dcep203CdtrId);
        dcep203CdtrAcct.setNm(mcbs200DTO.getCreditTransferTxInfo().getCreditorAccount().getName());
        dcep203CdtTrfTxInf.setCdtrAcct(dcep203CdtrAcct);

        com.dcep.dips.wholesalepayment.dto.dc203.Purp dcep203Purp = new com.dcep.dips.wholesalepayment.dto.dc203.Purp();
        dcep203Purp.setPrtry(Constant.DCEP_PURP_22300002);

        dcep203CdtTrfTxInf.setPurp(dcep203Purp);

        com.dcep.dips.wholesalepayment.dto.dc203.RmtInf dcep203RmtInf = new com.dcep.dips.wholesalepayment.dto.dc203.RmtInf();
        if (mcbs200DTO.getCreditTransferTxInfo().getRmtInf() != null
                && mcbs200DTO.getCreditTransferTxInfo().getRmtInf().getUnstructed() != null) {
            dcep203RmtInf.setPostscript(mcbs200DTO.getCreditTransferTxInfo().getRmtInf().getUnstructed());
        }
        dcep203RmtInf.setReason(mcbs200DTO.getCreditTransferTxInfo()
                .getCreditTransferTxInfoSupplementaryData()
                .getCreditTxInfoEnvelope().getContents().getReason());
        dcep203RmtInf.setParameterId(mcbs200DTO.getCreditTransferTxInfo()
                .getCreditTransferTxInfoSupplementaryData()
                .getCreditTxInfoEnvelope().getContents().getParameterId());
        dcep203RmtInf.setSndChnlSys(Constant.MCBS);
        dcep203RmtInf.setRcvChnlSys(Constant.DCEP);
        dcep203CdtTrfTxInf.setRmtInf(dcep203RmtInf);

        List<String> ustrds = new ArrayList<>();
        if (dcep203RmtInf.getPostscript() != null) {
            ustrds.add(Constant.PREFIX_POSTSCRIPT + dcep203RmtInf.getPostscript());
        }
        ustrds.add(Constant.PREFIX_REASON + dcep203RmtInf.getReason());
        ustrds.add(Constant.PREFIX_PARAMETERID + dcep203RmtInf.getParameterId());
        ustrds.add(Constant.PREFIX_SNDCHNLSYS + Constant.MCBS);
        ustrds.add(Constant.PREFIX_RCVCHNLSYS + Constant.DCEP);
        dcep203CdtTrfTxInf.setUstrds(ustrds);
        dcep203DTO.setCdtTrfTxInf(dcep203CdtTrfTxInf);

        // 设置dcep203DTO货币桥交易请求信息
        MbridgeReqDTO mbridgeReqDTO = new MbridgeReqDTO();
        mbridgeReqDTO.setMcbsMsgId(mcbs200DTO.fetchMsgId());
        mbridgeReqDTO.setSndDtTm(mcbsTime);
        mbridgeReqDTO.setMcbsBatId(mcbsBatchNo);
        mbridgeReqDTO.setMcbsMsgTp(mcbs200Envelope.getSoapHeader().getMsgTp());
        mbridgeReqDTO.setMsgDrn(mcbs200Envelope.getSoapHeader().getMessageDirection());
        String mcbsCode = mcbs200DTO.getCreditTransferTxInfo().getPaymentTypeInfo().getCategoryPurpose().getCode();
        mbridgeReqDTO.setClrTp(mcbsCode);
        mbridgeReqDTO.setSenderPtyId(instgOrgCode);
        mbridgeReqDTO.setSenderLEI(instgOrgLei);
        mbridgeReqDTO.setReceiverPtyId(instdOrgCode);
        mbridgeReqDTO.setReceiverLEI(instdOrgLei);
        mbridgeReqDTO.setDbtrPtyId(debtorOrgCode);
        mbridgeReqDTO.setDbtrPtyLEI(debtorOrgLei);
        mbridgeReqDTO.setCdtrPtyId(creditorOrgCode);
        mbridgeReqDTO.setCdtrPtyLEI(creditorOrgLei);
        mbridgeReqDTO.setDbtrWltId(dcepDbtrOther.getId());
        mbridgeReqDTO.setCdtrWltId(dcepCdtrOther.getId());
        mbridgeReqDTO.setParameterId(dcep203RmtInf.getParameterId());
        mbridgeReqDTO.setShardingMsgId(mcbs200DTO.fetchMsgId());
        dcep203DTO.setMbridgeReqDTO(mbridgeReqDTO);

        dcepSoapBody.setT(dcep203DTO);
        return dcep203Envelope;
    }

    private static EnvelopeDTO<GwDTO> convert202(GenericEnvelopeDTO<GenericGwDTO> mBridgeEnvelope) {
        McbsEnvelopeDTO<McbsGwDTO> mcbs202Envelope = (McbsEnvelopeDTO<McbsGwDTO>) mBridgeEnvelope;
        EnvelopeDTO<GwDTO> dcep203Envelope = new EnvelopeDTO<>();
        com.dcep.common.model.soap.SoapHeader dcepSoapHeader = DtoUtil.toDcepSoapHeader(mcbs202Envelope);
        dcep203Envelope.setSoapHeader(dcepSoapHeader);
        SoapBody<GwDTO> dcepSoapBody = new SoapBody<>();
        dcep203Envelope.setSoapBody(dcepSoapBody);
        Mcbs20200101DTO mcbs202DTO = (Mcbs20200101DTO) mcbs202Envelope.body();
        String mcbsTime = mcbs202DTO.getGrpHdr().getCreDtTm();
        String hlhtTime = TimeUtil.fromMcbs(mcbsTime, DcepDateUtils.ISO_DATETIME_PATTERN);
        com.dcep.dips.wholesalepayment.dto.dc203.GrpHdr dcep203GroupHeader = new com.dcep.dips.wholesalepayment.dto.dc203.GrpHdr();
        dcep203GroupHeader.setCreDtTm(hlhtTime);
        dcep203GroupHeader.setNbOfTxs(Constant.NUM_OF_TXS_1);
        dcep203GroupHeader.setSttlmInf(new com.dcep.dips.wholesalepayment.dto.dc203.SttlmInf(Constant.SETTLE_METHOD_CLRG));
        Dcep20301001DTO dcep203DTO = new Dcep20301001DTO();
        dcep203DTO.setGrpHdr(dcep203GroupHeader);

        com.dcep.dips.wholesalepayment.dto.dc203.CdtTrfTxInf dcep203CdtTrfTxInf = new com.dcep.dips.wholesalepayment.dto.dc203.CdtTrfTxInf();
        String mcbsBatchNo = mcbs202DTO.getTxInf().getSplmtryData().getEnvlp().getCnts().getBatchNO();
        String dcepBatchNo = BatIdUtil.trsfMcbsBatIdToHlhtBatId(mcbsBatchNo);
        com.dcep.dips.wholesalepayment.dto.dc203.PmtId dcep203PmtId = new com.dcep.dips.wholesalepayment.dto.dc203.PmtId();
        dcep203PmtId.setInstrId(dcepBatchNo);
        dcep203CdtTrfTxInf.setPmtId(dcep203PmtId);

        com.dcep.dips.wholesalepayment.dto.dc203.PmtTpInf dcep203PmtTpInf = new com.dcep.dips.wholesalepayment.dto.dc203.PmtTpInf();
        com.dcep.dips.wholesalepayment.dto.dc203.SvcLvl dcep203SvcLvl = new com.dcep.dips.wholesalepayment.dto.dc203.SvcLvl();
        dcep203SvcLvl.setPrtry(Constant.SVCSVL_PRTRY_TT00);
        dcep203PmtTpInf.setSvcLvl(dcep203SvcLvl);
        com.dcep.dips.wholesalepayment.dto.dc203.CtgyPurp dcep203CtgyPurp = new com.dcep.dips.wholesalepayment.dto.dc203.CtgyPurp();
        dcep203CtgyPurp.setPrtry(Constant.DCEP_CTGYPURP_223);
        dcep203PmtTpInf.setCtgyPurp(dcep203CtgyPurp);
        dcep203CdtTrfTxInf.setPmtTpInf(dcep203PmtTpInf);

        dcep203CdtTrfTxInf.setIntrBkSttlmAmt(mcbs202DTO.getTxInf().getRtrdIntrBkSttlmAmt());
        dcep203CdtTrfTxInf.setChrgBr(Constant.CHRGBR_DEBT);

        // mcbs202发起机构为dcep203付款运营机构
        com.dcep.dips.wholesalepayment.dto.dc203.FinInstnId dcep203InstgFinInstnId = new com.dcep.dips.wholesalepayment.dto.dc203.FinInstnId();
        String instgOrgCode = mcbs202DTO.getGrpHdr().getInstgAgt().getFinInstnId().getClrSysMmbId().getMmbId();
        com.dcep.dips.wholesalepayment.dto.dc203.ClrSysMmbId dcep203InstgMmbId = new com.dcep.dips.wholesalepayment.dto.dc203.ClrSysMmbId();
        dcep203InstgMmbId.setMmbId(instgOrgCode);
        dcep203InstgFinInstnId.setClrSysMmbId(dcep203InstgMmbId);
        String instgOrgLei = mcbs202DTO.getGrpHdr().getInstgAgt().getFinInstnId().getLei();
        dcep203InstgFinInstnId.setLei(instgOrgLei);
        com.dcep.dips.wholesalepayment.dto.dc203.InstgAgt dcep203InstgAgt = new com.dcep.dips.wholesalepayment.dto.dc203.InstgAgt();
        dcep203InstgAgt.setFinInstnId(dcep203InstgFinInstnId);

        // 付款机构-原收款机构为dcep203付款机构
        com.dcep.dips.wholesalepayment.dto.dc203.BrnchId dcep203InstgBrnchId = new com.dcep.dips.wholesalepayment.dto.dc203.BrnchId();
        String debtorOrgCode = mcbs202DTO.getTxInf().getOrgnlTxRef().getCdtr().getAgt().getFinInstnId().getClrSysMmbId().getMmbId();
        dcep203InstgBrnchId.setId(debtorOrgCode);
        String debtorOrgLei = mcbs202DTO.getTxInf().getOrgnlTxRef().getCdtr().getAgt().getFinInstnId().getLei();
        dcep203InstgBrnchId.setLei(debtorOrgLei);
        dcep203InstgAgt.setBrnchId(dcep203InstgBrnchId);
        dcep203CdtTrfTxInf.setInstgAgt(dcep203InstgAgt);

        // dcep203报文头里的发送机构编码为dcep203付款运营机构
        dcep203Envelope.getSoapHeader().setSender(instgOrgCode);

        // mcbs200接收机构为dcep203收款运营机构
        com.dcep.dips.wholesalepayment.dto.dc203.FinInstnId dcep203InstdFinInstnId = new com.dcep.dips.wholesalepayment.dto.dc203.FinInstnId();
        String instdOrgCode = mcbs202DTO.getGrpHdr().getInstdAgt().getFinInstnId().getClrSysMmbId().getMmbId();
        com.dcep.dips.wholesalepayment.dto.dc203.ClrSysMmbId dcep203InstdMmbId = new com.dcep.dips.wholesalepayment.dto.dc203.ClrSysMmbId();
        dcep203InstdMmbId.setMmbId(instdOrgCode);
        dcep203InstdFinInstnId.setClrSysMmbId(dcep203InstdMmbId);
        String instdOrgLei = mcbs202DTO.getGrpHdr().getInstdAgt().getFinInstnId().getLei();
        com.dcep.dips.wholesalepayment.dto.dc203.InstdAgt dcep203InstdAgt = new com.dcep.dips.wholesalepayment.dto.dc203.InstdAgt();
        dcep203InstdAgt.setFinInstnId(dcep203InstdFinInstnId);

        // 收款机构-原付款机构为dcep203收款机构
        com.dcep.dips.wholesalepayment.dto.dc203.BrnchId dcep203InstdBrnchId = new com.dcep.dips.wholesalepayment.dto.dc203.BrnchId();
        String creditorOrgCode = mcbs202DTO.getTxInf().getOrgnlTxRef().getDbtr().getAgt().getFinInstnId().getClrSysMmbId().getMmbId();
        dcep203InstdBrnchId.setId(creditorOrgCode);
        String creditorOrgLei = mcbs202DTO.getTxInf().getOrgnlTxRef().getDbtr().getAgt().getFinInstnId().getLei();
        dcep203InstdAgt.setBrnchId(dcep203InstdBrnchId);
        dcep203CdtTrfTxInf.setInstdAgt(dcep203InstdAgt);

        // dcep203报文头里的接收机构编码为dcep203收款运营机构
        dcep203Envelope.getSoapHeader().setReceiver(instdOrgCode);

        // 原收款方钱包ID为dcep203付款方钱包ID
        com.dcep.dips.wholesalepayment.dto.dc203.DbtrAcct dcep203DbtrAcct = new com.dcep.dips.wholesalepayment.dto.dc203.DbtrAcct();
        com.dcep.dips.wholesalepayment.dto.dc203.Othr dcepDbtrOther = new com.dcep.dips.wholesalepayment.dto.dc203.Othr();
        dcepDbtrOther.setId(mcbs202DTO.getTxInf().getOrgnlTxRef().getCdtrAcct().getId().getOther().getId());
        com.dcep.dips.wholesalepayment.dto.dc203.Id dcep203DbtrId = new com.dcep.dips.wholesalepayment.dto.dc203.Id();
        dcep203DbtrId.setOthr(dcepDbtrOther);
        dcep203DbtrAcct.setId(dcep203DbtrId);
//        dcep203DbtrAcct.setNm();
        dcep203CdtTrfTxInf.setDbtrAcct(dcep203DbtrAcct);

        // 原付款方钱包ID为dcep203收款方钱包ID
        com.dcep.dips.wholesalepayment.dto.dc203.CdtrAcct dcep203CdtrAcct = new com.dcep.dips.wholesalepayment.dto.dc203.CdtrAcct();
        com.dcep.dips.wholesalepayment.dto.dc203.Othr dcepCdtrOther = new com.dcep.dips.wholesalepayment.dto.dc203.Othr();
        dcepCdtrOther.setId(mcbs202DTO.getTxInf().getOrgnlTxRef().getDbtrAcct().getId().getOther().getId());
        com.dcep.dips.wholesalepayment.dto.dc203.Id dcep203CdtrId = new com.dcep.dips.wholesalepayment.dto.dc203.Id();
        dcep203CdtrId.setOthr(dcepCdtrOther);
        dcep203CdtrAcct.setId(dcep203CdtrId);
//        dcep203CdtrAcct.setNm();
        dcep203CdtTrfTxInf.setCdtrAcct(dcep203CdtrAcct);

        com.dcep.dips.wholesalepayment.dto.dc203.Purp dcep203Purp = new com.dcep.dips.wholesalepayment.dto.dc203.Purp();
        dcep203Purp.setPrtry(Constant.DCEP_PURP_22300002);
        dcep203CdtTrfTxInf.setPurp(dcep203Purp);

        dcep203DTO.setCdtTrfTxInf(dcep203CdtTrfTxInf);

        com.dcep.dips.wholesalepayment.dto.dc203.RmtInf dcep203RmtInf = new com.dcep.dips.wholesalepayment.dto.dc203.RmtInf();

        dcep203RmtInf.setPostscript("postscript");  // todo mcbs202报文无此字段
        dcep203RmtInf.setReason("reason");  // todo mcbs202报文无此字段
        dcep203RmtInf.setParameterId(mcbs202DTO.getTxInf().getSplmtryData().getEnvlp().getCnts().getOrgnlTxParamId());
        dcep203RmtInf.setSndChnlSys(Constant.MCBS);
        dcep203RmtInf.setRcvChnlSys(Constant.DCEP);
        dcep203CdtTrfTxInf.setRmtInf(dcep203RmtInf);

        List<String> ustrds = new ArrayList<>();
        if (dcep203RmtInf.getPostscript() != null) {
            ustrds.add(Constant.PREFIX_POSTSCRIPT + dcep203RmtInf.getPostscript());
        }
        ustrds.add(Constant.PREFIX_REASON + dcep203RmtInf.getReason());
        ustrds.add(Constant.PREFIX_PARAMETERID + dcep203RmtInf.getParameterId());
        ustrds.add(Constant.PREFIX_SNDCHNLSYS + Constant.MCBS);
        ustrds.add(Constant.PREFIX_RCVCHNLSYS + Constant.DCEP);
        dcep203CdtTrfTxInf.setUstrds(ustrds);
        dcep203DTO.setCdtTrfTxInf(dcep203CdtTrfTxInf);

        // 设置dcep203DTO货币桥交易请求信息
        MbridgeReqDTO mbridgeReqDTO = new MbridgeReqDTO();
        mbridgeReqDTO.setMcbsMsgId(mcbs202DTO.fetchMsgId());
        mbridgeReqDTO.setSndDtTm(mcbsTime);
        mbridgeReqDTO.setMcbsBatId(mcbsBatchNo);
        mbridgeReqDTO.setMcbsMsgTp(mcbs202Envelope.getSoapHeader().getMsgTp());
        mbridgeReqDTO.setMsgDrn(mcbs202Envelope.getSoapHeader().getMessageDirection());
        String mcbsCode = mcbs202DTO.getTxInf().getOrgnlTxRef().getPmtTpInf().getCtgyPurp().getCd();
        mbridgeReqDTO.setClrTp(mcbsCode);
        mbridgeReqDTO.setSenderPtyId(instgOrgCode);
        mbridgeReqDTO.setSenderLEI(instgOrgLei);
        mbridgeReqDTO.setReceiverPtyId(instdOrgCode);
        mbridgeReqDTO.setReceiverLEI(instdOrgLei);
        mbridgeReqDTO.setDbtrPtyId(debtorOrgCode);
        mbridgeReqDTO.setDbtrPtyLEI(debtorOrgLei);
        mbridgeReqDTO.setCdtrPtyId(creditorOrgCode);
        mbridgeReqDTO.setCdtrPtyLEI(creditorOrgLei);
        mbridgeReqDTO.setDbtrWltId(dcepDbtrOther.getId());
        mbridgeReqDTO.setCdtrWltId(dcepCdtrOther.getId());
        mbridgeReqDTO.setParameterId(mcbs202DTO.getTxInf().getSplmtryData().getEnvlp().getCnts().getOrgnlTxParamId());
        mbridgeReqDTO.setShardingMsgId(mcbs202DTO.fetchMsgId());
        dcep203DTO.setMbridgeReqDTO(mbridgeReqDTO);

        dcepSoapBody.setT(dcep203DTO);
        return dcep203Envelope;
    }
    private MbridgeToDcep203() {
        // comply with Sonar rules.
    }

}

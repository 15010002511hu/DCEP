/*
 * pbcdci.cn Inc.
 * Copyright © 2022 PBCDCI All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.manager.convert;

import com.dcep.dips.wholesalepayment.dto.dc203.CdtTrfTxInf;
import com.dcep.dips.wholesalepayment.dto.dc203.Dcep20301001DTO;
import com.dcep.dips.wholesalepayment.dto.mcbs.MbridgeReqDTO;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.utils.DcepDateUtils;
import com.dcep.gateway.mcbdc.dto.common.*;
import com.dcep.gateway.mcbdc.dto.mcbs200.*;
import com.dcep.gateway.mcbdc.dto.soap.*;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.common.utils.BatIdUtil;
import com.dcep.dips.wholesalepayment.common.utils.DtoUtil;
import com.dcep.dips.wholesalepayment.common.utils.TimeUtil;

/**
 * Dcep203ToMbridge200
 * @author laowei
 * @version $Id: Dcep203ToMbridge200.java, v 0.1 2022年5月21日 上午7:13:11 laowei Exp $
 */
public final class Dcep203ToMbridge200 {

    public static GenericEnvelopeDTO<GenericGwDTO> convert(EnvelopeDTO<GwDTO> dcep203Envelope) {
        Dcep20301001DTO dcep203DTO = (Dcep20301001DTO) dcep203Envelope.body();

        McbsEnvelopeDTO<McbsGwDTO> mcbs200Envelope = new McbsEnvelopeDTO<>();
        McbsSoapHeader mcbsSoapHeader = DtoUtil.toMcbsSoapHeader(dcep203Envelope.getSoapHeader().getMsgTp());
        mcbs200Envelope.setSoapHeader(mcbsSoapHeader);
        McbsSoapBody<McbsGwDTO> mcbsSoapBody = new McbsSoapBody<>();
        mcbs200Envelope.setSoapBody(mcbsSoapBody);

        String mcbsTime = TimeUtil.toMcbs(dcep203DTO.getGrpHdr().getCreDtTm(), DcepDateUtils.ISO_DATETIME_PATTERN);
        Mcbs20000101DTO mcbs200DTO = new Mcbs20000101DTO();
        setMcbsGrpHdr(mcbs200DTO, mcbsTime);

        String mcbsCode = Constant.MBRIDGE_CTGYPURP_ISUE;
        CreditTransferTxInfo creditTransferTxInfo = new CreditTransferTxInfo();
        CdtTrfTxInf cdtTrfTxInf = dcep203DTO.getCdtTrfTxInf();
        creditTransferTxInfo.setPaymentTypeInfo(new PaymentTypeInfo(new CategoryPurpose(mcbsCode)));
        creditTransferTxInfo.setInterBankSettleAmount(cdtTrfTxInf.getIntrBkSttlmAmt());
        creditTransferTxInfo.setSettlePriority(Constant.MBRIDGE_SETTLE_PRIORITY);

        // dcep203付款运营机构为mcbs200发起机构
        String instgOrgCode = cdtTrfTxInf.getInstgAgt().getFinInstnId().getClrSysMmbId().getMmbId();
        String instgOrgLei = cdtTrfTxInf.getInstgAgt().getFinInstnId().getLei();
        setMcbsInstructingAgent(creditTransferTxInfo, instgOrgCode, instgOrgLei);

        // dcep203收款运营机构为mcbs200接收机构
        String instdOrgCode = cdtTrfTxInf.getInstdAgt().getFinInstnId().getClrSysMmbId().getMmbId();
        String instdOrgLei = cdtTrfTxInf.getInstdAgt().getFinInstnId().getLei();
        setMcbsInstructedAgent(creditTransferTxInfo, instdOrgCode, instdOrgLei);

        // 付款机构
        String debtorOrgCode = cdtTrfTxInf.getInstgAgt().getBrnchId().getId();
        String debtorOrgLei = cdtTrfTxInf.getInstgAgt().getBrnchId().getLei();
        String debtorOtherId = cdtTrfTxInf.getDbtrAcct().getId().getOthr().getId();
        String debtorNm = cdtTrfTxInf.getDbtrAcct().getNm();
        setMcbsDebtorInfo(creditTransferTxInfo, debtorOrgCode, debtorOrgLei, debtorOtherId, debtorNm);

        // 收款机构
        String creditorOrgCode = cdtTrfTxInf.getInstdAgt().getBrnchId().getId();
        String creditorOrgLei = cdtTrfTxInf.getInstdAgt().getBrnchId().getLei();
        String creditorOtherId = cdtTrfTxInf.getCdtrAcct().getId().getOthr().getId();
        String creditorNm = cdtTrfTxInf.getCdtrAcct().getNm();
        setMcbsCreditorInfo(creditTransferTxInfo, creditorOrgCode, creditorOrgLei, creditorOtherId, creditorNm);

        String parameterId = null;
        String reason = null;
        com.dcep.dips.wholesalepayment.dto.dc203.RmtInf rmtInf = cdtTrfTxInf.getRmtInf();
        if (rmtInf != null) {
            String postscript = rmtInf.getPostscript();
            if (postscript != null) {
                creditTransferTxInfo.setRmtInf(new com.dcep.gateway.mcbdc.dto.mcbs200.RmtInf(postscript));
            }
            parameterId = rmtInf.getParameterId();
            reason = rmtInf.getReason();
        }

        String dcepBatchNo = cdtTrfTxInf.getPmtId().getInstrId();
        String mcbsBatchNo = BatIdUtil.trsfHlhtBatIdToMcbsBatId(dcepBatchNo);
        String mcbsMsgDrn = mcbsSoapHeader.getMessageDirection();
        setMcbsContents(creditTransferTxInfo, mcbsMsgDrn, mcbsBatchNo,
            parameterId, reason, dcep203DTO.fetchMsgId(), dcepBatchNo);

        mcbs200DTO.setCreditTransferTxInfo(creditTransferTxInfo);
        mcbsSoapBody.setT(mcbs200DTO);

        // 设置dcep203DTO货币桥交易请求信息
        MbridgeReqDTO mbridgeReqDTO = new MbridgeReqDTO();
        mbridgeReqDTO.setSndDtTm(mcbsTime);
        mbridgeReqDTO.setMcbsBatId(mcbsBatchNo);
        mbridgeReqDTO.setMcbsMsgTp(mcbsSoapHeader.getMsgTp());
        mbridgeReqDTO.setMsgDrn(mcbsMsgDrn);
        mbridgeReqDTO.setClrTp(mcbsCode);
        mbridgeReqDTO.setSenderPtyId(instgOrgCode);
        mbridgeReqDTO.setSenderLEI(instgOrgLei);
        mbridgeReqDTO.setReceiverPtyId(instdOrgCode);
        mbridgeReqDTO.setReceiverLEI(instdOrgLei);
        mbridgeReqDTO.setDbtrPtyId(debtorOrgCode);
        mbridgeReqDTO.setDbtrPtyLEI(debtorOrgLei);
        mbridgeReqDTO.setCdtrPtyId(creditorOrgCode);
        mbridgeReqDTO.setCdtrPtyLEI(creditorOrgLei);
        mbridgeReqDTO.setDbtrWltId(debtorOtherId);
        mbridgeReqDTO.setCdtrWltId(creditorOtherId);
        mbridgeReqDTO.setParameterId(parameterId);
        mbridgeReqDTO.setShardingMsgId(dcep203DTO.fetchMsgId());
        dcep203DTO.setMbridgeReqDTO(mbridgeReqDTO);

        return mcbs200Envelope;
    }



    public static void supplement(GenericEnvelopeDTO<GenericGwDTO> mBridge200Envelope, String mcbsMsgId) {
        Mcbs20000101DTO mcbs200DTO = (Mcbs20000101DTO) mBridge200Envelope.body();
        mcbs200DTO.getGrpHdr().setMsgId(mcbsMsgId);
        mcbs200DTO.getCreditTransferTxInfo().setPaymentId(new com.dcep.gateway.mcbdc.dto.common.PaymentId(mcbsMsgId, mcbsMsgId));
    }

    private static void setMcbsGrpHdr(Mcbs20000101DTO mcbs200DTO, String mcbsTime) {
        com.dcep.gateway.mcbdc.dto.mcbs200.GroupHeader mcbs200GroupHeader =
                new com.dcep.gateway.mcbdc.dto.mcbs200.GroupHeader();
        mcbs200GroupHeader.setCreateDateTime(mcbsTime);
        mcbs200GroupHeader.setNumberOfTxs(Constant.NUM_OF_TXS_1);
        mcbs200GroupHeader.setSettlementInfo(new SettlementInfo(Constant.SETTLE_METHOD_CLRG));
        mcbs200DTO.setGrpHdr(mcbs200GroupHeader);
    }

    private static void setMcbsInstructingAgent(CreditTransferTxInfo creditTransferTxInfo,
            String instgOrgCode, String instgOrgLei) {
        InstrFinancialInstitutionID instructingInstitutionID = new InstrFinancialInstitutionID();
        instructingInstitutionID.setClearingSystemMemberID(new ClearingSystemMemberID(
            instgOrgCode, new ClearingSystemID(Constant.MBRIDGE_CHINA_CLEARINGCODE)));
        instructingInstitutionID.setLei(instgOrgLei);
        creditTransferTxInfo.setInstructingAgent(new InstructingAgent(instructingInstitutionID));
    }

    private static void setMcbsInstructedAgent(CreditTransferTxInfo creditTransferTxInfo,
        String instdOrgCode, String instdOrgLei) {
        InstrFinancialInstitutionID instructedInstitutionID = new InstrFinancialInstitutionID();
        instructedInstitutionID.setClearingSystemMemberID(new ClearingSystemMemberID(
            instdOrgCode, new ClearingSystemID(Constant.MBRIDGE_CHINA_CLEARINGCODE)));
        instructedInstitutionID.setLei(instdOrgLei);
        creditTransferTxInfo.setInstructedAgent(new InstructedAgent(instructedInstitutionID));
    }

    private static void setMcbsDebtorInfo(CreditTransferTxInfo creditTransferTxInfo,
            String debtorOrgCode, String debtorOrgLei, String debtorOtherId, String debtorNm) {
        FinancialInstitutionID debtorInstitutionID = new FinancialInstitutionID();
        debtorInstitutionID.setClearingSystemMemberID(new ClearingSystemMemberID(
            debtorOrgCode, new ClearingSystemID(Constant.MBRIDGE_CHINA_CLEARINGCODE)));
        debtorInstitutionID.setLei(debtorOrgLei);
        creditTransferTxInfo.setDebtor(new Debtor(debtorInstitutionID));

        com.dcep.gateway.mcbdc.dto.common.Other debtorOther =
                new com.dcep.gateway.mcbdc.dto.common.Other(debtorOtherId);
        com.dcep.gateway.mcbdc.dto.common.Id debtorId = new com.dcep.gateway.mcbdc.dto.common.Id(debtorOther);
        creditTransferTxInfo.setDebtorAccount(new DebtorAccount(debtorId, debtorNm));
    }

    private static void setMcbsCreditorInfo(CreditTransferTxInfo creditTransferTxInfo,
            String creditorOrgCode, String creditorOrgLei, String creditorOtherId, String creditorNm) {
        FinancialInstitutionID creditorInstitutionID = new FinancialInstitutionID();
        creditorInstitutionID.setClearingSystemMemberID(new ClearingSystemMemberID(
            creditorOrgCode, new ClearingSystemID(Constant.MBRIDGE_CHINA_CLEARINGCODE)));
        creditorInstitutionID.setLei(creditorOrgLei);
        creditTransferTxInfo.setCreditor(new Creditor(creditorInstitutionID));

        com.dcep.gateway.mcbdc.dto.common.Other creditorOther =
                new com.dcep.gateway.mcbdc.dto.common.Other(creditorOtherId);
        com.dcep.gateway.mcbdc.dto.common.Id id = new com.dcep.gateway.mcbdc.dto.common.Id(creditorOther);
        creditTransferTxInfo.setCreditorAccount(new CreditorAccount(id, creditorNm));
    }

    private static void setMcbsContents(CreditTransferTxInfo creditTransferTxInfo,
            String mcbsMsgDrn, String mcbsBatchNo,
            String parameterId, String reason, String dcepMsgId, String dcepBatchNo) {
        CreditTransferTxInfoContents contents = new CreditTransferTxInfoContents();
        contents.setDirection(mcbsMsgDrn);
        contents.setBatchNo(mcbsBatchNo);

        if (parameterId != null) {
            contents.setParameterId(parameterId);
        }

        if (reason != null) {
            contents.setReason(reason);
        }

        String extra = "{\"" + Constant.MBRIDGE_EXTRA_MSGID + "\":\"" + dcepMsgId
                       + "\"," + "\"" + Constant.MBRIDGE_EXTRA_BATCHID + "\":\"" + dcepBatchNo + "\"}";
        contents.setExtra(extra);

        creditTransferTxInfo.setCreditTransferTxInfoSupplementaryData(
            new CreditTransferTxInfoSupplementaryData(
                Constant.MBRIDGE_200_PLACE_AND_NAME, new CreditTransferTxInfoEnvelope(contents)));
    }

    private Dcep203ToMbridge200() {
        // Do nothing to comply with Sonar rules.
    }

}

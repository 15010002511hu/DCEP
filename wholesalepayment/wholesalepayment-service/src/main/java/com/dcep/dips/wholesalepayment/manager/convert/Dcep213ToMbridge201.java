/*
 * pbcdci.cn Inc.
 * Copyright © 2022 PBCDCI All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.manager.convert;

import com.dcep.dips.wholesalepayment.dto.dc213.Dcep21301001DTO;
import com.dcep.dips.wholesalepayment.dto.mcbs.MbridgeReqDTO;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.utils.DcepDateUtils;
import com.dcep.gateway.mcbdc.dto.common.ClearingSystemMemberID;
import com.dcep.gateway.mcbdc.dto.common.Id;
import com.dcep.gateway.mcbdc.dto.common.Other;
import com.dcep.gateway.mcbdc.dto.common.*;
import com.dcep.gateway.mcbdc.dto.mcbs201.GroupHeader;
import com.dcep.gateway.mcbdc.dto.mcbs201.*;
import com.dcep.gateway.mcbdc.dto.soap.*;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.common.utils.BatIdUtil;
import com.dcep.dips.wholesalepayment.common.utils.DtoUtil;
import com.dcep.dips.wholesalepayment.common.utils.TimeUtil;

/**
 * Dcep213ToMbridge201
 * @author mxy
 * @version $Id: Dcep213ToMbridge201.java, v 0.1 2022年6月6日 下午3:41:03 laowei Exp $
 */
public final class Dcep213ToMbridge201 {

    /**
     * DCEP213转货币桥201
     * @param dcep213Envelope DCEP213报文
     * @return 货币桥201报文
     */
    public static GenericEnvelopeDTO<GenericGwDTO> convert(EnvelopeDTO<GwDTO> dcep213Envelope) {
        Dcep21301001DTO dcep21300101DTO = (Dcep21301001DTO) dcep213Envelope.body();
        McbsEnvelopeDTO<McbsGwDTO> mcbs201Envelope = new McbsEnvelopeDTO<>();
        McbsSoapHeader mcbsSoapHeader = DtoUtil.toMcbsSoapHeader(dcep213Envelope.getSoapHeader().getMsgTp());
        mcbs201Envelope.setSoapHeader(mcbsSoapHeader);
        McbsSoapBody<McbsGwDTO> mcbsSoapBody = new McbsSoapBody<>();
        mcbs201Envelope.setSoapBody(mcbsSoapBody);

        // 时区转换
        String mcbsTime = TimeUtil.toMcbs(
            dcep21300101DTO.getGrpHdr().getCreDtTm(), DcepDateUtils.ISO_DATETIME_PATTERN);
        GroupHeader groupHeader = new GroupHeader();
        groupHeader.setCreateDateTime(mcbsTime);
        groupHeader.setNumberOfTxs(Constant.NUM_OF_TXS_1);

        // paymentTypeInfo
        PaymentTypeInfo paymentTypeInfo = new PaymentTypeInfo();
        CategoryPurpose categoryPurpose = new CategoryPurpose();
        categoryPurpose.setCode(Constant.MBRIDGE_CTGYPURP_REDT);
        paymentTypeInfo.setCategoryPurpose(categoryPurpose);

        // instructingAgent
        InstrFinancialInstitutionID instructingAgentInstitutionID = new InstrFinancialInstitutionID();
        ClearingSystemMemberID instructingAgentMemberID = new ClearingSystemMemberID(
            dcep21300101DTO.getDrctDbtTxInf().getCdtrAgt().getFinInstnId().getClrSysMmbId().getMmbId(),
            new ClearingSystemID(Constant.MBRIDGE_CHINA_CLEARINGCODE));
        instructingAgentInstitutionID.setLei(dcep21300101DTO.getDrctDbtTxInf().getCdtrAgt().getFinInstnId().getLei());
        instructingAgentInstitutionID.setClearingSystemMemberID(instructingAgentMemberID);
        InstructingAgent instructingAgent = new InstructingAgent();
        instructingAgent.setFinancialInstitutionID(instructingAgentInstitutionID);

        // instructedAgent
        InstrFinancialInstitutionID instructedAgentInstitutionID = new InstrFinancialInstitutionID();
        ClearingSystemMemberID instructedAgentMemberID = new ClearingSystemMemberID(
            dcep21300101DTO.getDrctDbtTxInf().getDbtrAgt().getFinInstnId().getClrSysMmbId().getMmbId(),
            new ClearingSystemID(Constant.MBRIDGE_CHINA_CLEARINGCODE));
        instructedAgentInstitutionID.setClearingSystemMemberID(instructedAgentMemberID);
        instructedAgentInstitutionID.setLei(dcep21300101DTO.getDrctDbtTxInf().getDbtrAgt().getFinInstnId().getLei());
        InstructedAgent instructedAgent = new InstructedAgent();
        instructedAgent.setFinancialInstitutionID(instructedAgentInstitutionID);

        // creditor
        FinancialInstitutionID creditorInstitutionID = new FinancialInstitutionID();
        ClearingSystemMemberID creditorMemberID = new ClearingSystemMemberID(
            dcep21300101DTO.getDrctDbtTxInf().getCdtrAgt().getFinInstnId().getClrSysMmbId().getMmbId(),
            new ClearingSystemID(Constant.MBRIDGE_CHINA_CLEARINGCODE));
        creditorInstitutionID.setClearingSystemMemberID(creditorMemberID);
        creditorInstitutionID.setLei(dcep21300101DTO.getDrctDbtTxInf().getCdtrAgt().getFinInstnId().getLei());
        Creditor creditor = new Creditor();
        creditor.setFinancialInstitutionID(creditorInstitutionID);

        // creditorAccount
        CreditorAccount creditorAccount = new CreditorAccount();
        Id creditorId = new Id();
        Other creditorOther = new Other();
        creditorOther.setId(dcep21300101DTO.getDrctDbtTxInf().getCdtrAcct().getId().getOthr().getId());
        creditorId.setOther(creditorOther);
        creditorAccount.setId(creditorId);
        creditorAccount.setName(dcep21300101DTO.getDrctDbtTxInf().getCdtrAcct().getNm());

        // directDebitTransactionInfo
        DirectDebitTransactionInfo directDebitTransactionInfo = new DirectDebitTransactionInfo();
        PaymentId paymentId = new PaymentId();
        directDebitTransactionInfo.setPaymentId(paymentId);
        directDebitTransactionInfo.setInterBankSettleAmount(dcep21300101DTO.getDrctDbtTxInf().getIntrBkSttlmAmt());

        // debtor
        FinancialInstitutionID debtorInstitutionID = new FinancialInstitutionID();
        ClearingSystemMemberID debtorMemberID = new ClearingSystemMemberID(
                dcep21300101DTO.getDrctDbtTxInf().getDbtrAgt().getBrnchId().getId(),
                new ClearingSystemID(Constant.MBRIDGE_CHINA_CLEARINGCODE)); // todo 修改原路由服务赋值-》待确认
        debtorInstitutionID.setClearingSystemMemberID(debtorMemberID);
        debtorInstitutionID.setLei(dcep21300101DTO.getDrctDbtTxInf().getCdtrAgt().getBrnchId().getLei());
        com.dcep.gateway.mcbdc.dto.mcbs201.Debtor debtor = new com.dcep.gateway.mcbdc.dto.mcbs201.Debtor();
        debtor.setFinancialInstitutionID(debtorInstitutionID);
        directDebitTransactionInfo.setDebtor(debtor);

        // debtorAccount
        com.dcep.gateway.mcbdc.dto.mcbs201.DebtorAccount debtorAccount =
                new com.dcep.gateway.mcbdc.dto.mcbs201.DebtorAccount();
        com.dcep.gateway.mcbdc.dto.mcbs201.Id debtorId = new com.dcep.gateway.mcbdc.dto.mcbs201.Id();
        com.dcep.gateway.mcbdc.dto.mcbs201.Other debtorOther = new com.dcep.gateway.mcbdc.dto.mcbs201.Other();
        debtorOther.setId(dcep21300101DTO.getDrctDbtTxInf().getDbtrAcct().getId().getOthr().getId());
        debtorId.setOther(debtorOther);
        debtorAccount.setId(debtorId);
        debtorAccount.setName(dcep21300101DTO.getDrctDbtTxInf().getDbtrAcct().getNm());
        directDebitTransactionInfo.setDebtorAccount(debtorAccount);

        // RmtInf
        com.dcep.gateway.mcbdc.dto.mcbs201.RmtInf rmtInf = new com.dcep.gateway.mcbdc.dto.mcbs201.RmtInf();
        rmtInf.setUnstructed(dcep21300101DTO.getDrctDbtTxInf().getUstrds().get(0));
        directDebitTransactionInfo.setRmtInf(rmtInf);

        // creditInstructionSupplementaryData
        CreditInstructionSupplementaryData creditInstructionSupplementaryData
            = new CreditInstructionSupplementaryData();
        creditInstructionSupplementaryData.setPlaceAndName(Constant.MBRIDGE_201_PLACE_AND_NAME);

        // envelope
        CreditInstructionContents contents = new CreditInstructionContents();
        contents.setDirection(mcbsSoapHeader.getMessageDirection());
        String instrId = dcep21300101DTO.getDrctDbtTxInf().getPmtId().getInstrId();
        String mcbsBactchNo = BatIdUtil.trsfHlhtBatIdToMcbsBatId(instrId);
        contents.setBatchNo(mcbsBactchNo);
        contents.setParameterId(dcep21300101DTO.getDrctDbtTxInf().getRmtInf().getParameterId());
        contents.setReason(dcep21300101DTO.getDrctDbtTxInf().getRmtInf().getReason());
        String extra = "{\"" + Constant.MBRIDGE_EXTRA_MSGID + "\":\"" + dcep21300101DTO.fetchMsgId()
                + "\"," + "\"" + Constant.MBRIDGE_EXTRA_BATCHID + "\":\""
                + instrId + "\"}";
        contents.setExtra(extra);
        CreditInstructionEnvelope envelope = new CreditInstructionEnvelope();
        envelope.setContents(contents);
        creditInstructionSupplementaryData.setCreditInstructionEnvelope(envelope);

        // CreditInstruction
        CreditInstruction creditInstruction = new CreditInstruction();
        creditInstruction.setPaymentTypeInfo(paymentTypeInfo);
        creditInstruction.setInstructingAgent(instructingAgent);
        creditInstruction.setInstructedAgent(instructedAgent);
        creditInstruction.setCreditor(creditor);
        creditInstruction.setCreditorAccount(creditorAccount);
        creditInstruction.setDirectDebitTransactionInfo(directDebitTransactionInfo);
        creditInstruction.setCreditInstructionSupplementaryData(creditInstructionSupplementaryData);

        Mcbs20100101DTO mcbs20100101DTO = new Mcbs20100101DTO();
        mcbs20100101DTO.setGrpHdr(groupHeader);
        mcbs20100101DTO.setCreditInstruction(creditInstruction);


        MbridgeReqDTO mbridgeReqDTO = new MbridgeReqDTO();
        mbridgeReqDTO.setSndDtTm(mcbsTime);
        mbridgeReqDTO.setMcbsBatId(mcbsBactchNo);
        mbridgeReqDTO.setMcbsMsgTp(mcbsSoapHeader.getMsgTp());
        mbridgeReqDTO.setMsgDrn(mcbsSoapHeader.getMessageDirection());
        mbridgeReqDTO.setClrTp(mcbs20100101DTO.getCreditInstruction().getPaymentTypeInfo()
            .getCategoryPurpose().getCode());
        mbridgeReqDTO.setSenderPtyId(mcbs20100101DTO.getCreditInstruction().getInstructingAgent()
            .getFinancialInstitutionID().getClearingSystemMemberID().getMemberId());
        mbridgeReqDTO.setSenderLEI(mcbs20100101DTO.getCreditInstruction().getInstructingAgent()
            .getFinancialInstitutionID().getLei());
        mbridgeReqDTO.setReceiverPtyId(mcbs20100101DTO.getCreditInstruction().getInstructedAgent()
            .getFinancialInstitutionID().getClearingSystemMemberID().getMemberId());
        mbridgeReqDTO.setReceiverLEI(mcbs20100101DTO.getCreditInstruction().getInstructedAgent()
            .getFinancialInstitutionID().getLei());
        mbridgeReqDTO.setDbtrPtyId(mcbs20100101DTO.getCreditInstruction().getDirectDebitTransactionInfo()
            .getDebtor().getFinancialInstitutionID().getClearingSystemMemberID().getMemberId());
        mbridgeReqDTO.setDbtrPtyLEI(mcbs20100101DTO.getCreditInstruction().getDirectDebitTransactionInfo()
            .getDebtor().getFinancialInstitutionID().getLei());
        mbridgeReqDTO.setCdtrPtyId(mcbs20100101DTO.getCreditInstruction().getCreditor()
            .getFinancialInstitutionID().getClearingSystemMemberID().getMemberId());
        mbridgeReqDTO.setCdtrPtyLEI(mcbs20100101DTO.getCreditInstruction().getCreditor()
            .getFinancialInstitutionID().getLei());
        mbridgeReqDTO.setDbtrWltId(mcbs20100101DTO.getCreditInstruction().getDirectDebitTransactionInfo()
            .getDebtorAccount().getId().getOther().getId());
        mbridgeReqDTO.setCdtrWltId(mcbs20100101DTO.getCreditInstruction().getCreditorAccount().getId()
            .getOther().getId());
        mbridgeReqDTO.setParameterId(contents.getParameterId());
        mbridgeReqDTO.setShardingMsgId(dcep21300101DTO.getGrpHdr().getMsgId());
        dcep21300101DTO.setMbridgeReqDTO(mbridgeReqDTO);

        mcbsSoapBody.setT(mcbs20100101DTO);
        return mcbs201Envelope;
    }

    public static void supplement(GenericEnvelopeDTO<GenericGwDTO> mBridge201Envelope, String mcbsMsgId) {
            Mcbs20100101DTO mcbs20100101DTO = (Mcbs20100101DTO) mBridge201Envelope.body();
            mcbs20100101DTO.getGrpHdr().setMsgId(mcbsMsgId);
            mcbs20100101DTO.getCreditInstruction().setCreditId(mcbsMsgId);
            mcbs20100101DTO.getCreditInstruction().getDirectDebitTransactionInfo().getPaymentId()
                .setEndToEndId(mcbsMsgId);
            mcbs20100101DTO.getCreditInstruction().getDirectDebitTransactionInfo().getPaymentId()
                .setTxId(mcbsMsgId);
    }

    private Dcep213ToMbridge201() {
        // comply with Sonar rules.
    }

}

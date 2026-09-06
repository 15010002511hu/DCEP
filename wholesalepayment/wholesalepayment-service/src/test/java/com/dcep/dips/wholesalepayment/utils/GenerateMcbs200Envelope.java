package com.dcep.dips.wholesalepayment.utils;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.dcep.common.utils.DcepDateUtils;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.common.utils.BatIdUtil;
import com.dcep.dips.wholesalepayment.common.utils.TimeUtil;
import com.dcep.gateway.mcbdc.dto.common.*;
import com.dcep.gateway.mcbdc.dto.mcbs200.*;
import com.dcep.gateway.mcbdc.dto.soap.*;

public class GenerateMcbs200Envelope {

    public static GenericEnvelopeDTO<GenericGwDTO> generateMcbs200EnvelopeDTO() {

        McbsEnvelopeDTO<McbsGwDTO> mcbs200Envelope = new McbsEnvelopeDTO<>();
        McbsSoapHeader mcbsSoapHeader = TestUtils.generateMcbsSoapHeader("mcbs.200.001.01");
        mcbs200Envelope.setSoapHeader(mcbsSoapHeader);
        McbsSoapBody<McbsGwDTO> mcbsSoapBody = new McbsSoapBody<>();
        mcbs200Envelope.setSoapBody(mcbsSoapBody);

        String mcbsTime = TimeUtil.getMcbsCurrentTime(DcepDateUtils.ISO_DATETIME_PATTERN);
        Mcbs20000101DTO mcbs200DTO = new Mcbs20000101DTO();
        setMcbsGrpHdr(mcbs200DTO, mcbsTime);

        String mcbsCode = Constant.MBRIDGE_CTGYPURP_REDT;
        CreditTransferTxInfo creditTransferTxInfo = new CreditTransferTxInfo();
        creditTransferTxInfo.setPaymentTypeInfo(new PaymentTypeInfo(new CategoryPurpose(mcbsCode)));
        creditTransferTxInfo.setInterBankSettleAmount(new ActiveCurrencyAndAmount("CNY", "2222.22"));
        creditTransferTxInfo.setSettlePriority(Constant.MBRIDGE_SETTLE_PRIORITY);

        // dcep203付款运营机构为mcbs200发起机构
        String instgOrgCode = "C8888888888887";
        String instgOrgLei = "7657326926816417863";
        setMcbsInstructingAgent(creditTransferTxInfo, instgOrgCode, instgOrgLei);

        // dcep203收款运营机构为mcbs200接收机构
        String instdOrgCode = "C8888888888888";
        String instdOrgLei = "7657326926816417864";
        setMcbsInstructedAgent(creditTransferTxInfo, instdOrgCode, instdOrgLei);

        // 付款机构
        String debtorOrgCode = "C8888888888887";
        String debtorOrgLei = "7657326926816417863";
        String debtorOtherId = "0021000000122282";
        String debtorNm = "付款机构";
        setMcbsDebtorInfo(creditTransferTxInfo, debtorOrgCode, debtorOrgLei, debtorOtherId, debtorNm);

        // 收款机构
        String creditorOrgCode = "C8888888888888";
        String creditorOrgLei = "7657326926816417864";
        String creditorOtherId = "0021000000122281";
        String creditorNm = "收款机构";
        setMcbsCreditorInfo(creditTransferTxInfo, creditorOrgCode, creditorOrgLei, creditorOtherId, creditorNm);

        String parameterId = "22222222";
        String reason = "reason";

        String dcepBatchNo = "B202510151100";
        String mcbsBatchNo = BatIdUtil.trsfHlhtBatIdToMcbsBatId(dcepBatchNo);
        String mcbsMsgDrn = mcbsSoapHeader.getMessageDirection();
        setMcbsContents(creditTransferTxInfo, mcbsMsgDrn, mcbsBatchNo,
                parameterId, reason, mcbs200DTO.getGrpHdr().getMsgId(), dcepBatchNo);

        mcbs200DTO.setCreditTransferTxInfo(creditTransferTxInfo);
        mcbsSoapBody.setT(mcbs200DTO);

        return mcbs200Envelope;
    }

    private static void setMcbsGrpHdr(Mcbs20000101DTO mcbs200DTO, String mcbsTime) {
        com.dcep.gateway.mcbdc.dto.mcbs200.GroupHeader mcbs200GroupHeader =
                new com.dcep.gateway.mcbdc.dto.mcbs200.GroupHeader();
        mcbs200GroupHeader.setCreateDateTime(mcbsTime);
        mcbs200GroupHeader.setNumberOfTxs(Constant.NUM_OF_TXS_1);
        mcbs200GroupHeader.setSettlementInfo(new SettlementInfo(Constant.SETTLE_METHOD_CLRG));
        mcbs200GroupHeader.setMsgId(TestUtils.getMsgId("000", "200"));
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

}

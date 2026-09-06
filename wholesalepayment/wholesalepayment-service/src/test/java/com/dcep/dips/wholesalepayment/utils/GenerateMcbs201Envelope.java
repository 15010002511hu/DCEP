package com.dcep.dips.wholesalepayment.utils;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.dcep.common.utils.DcepDateUtils;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.common.utils.BatIdUtil;
import com.dcep.dips.wholesalepayment.common.utils.TimeUtil;
import com.dcep.gateway.mcbdc.dto.common.*;
import com.dcep.gateway.mcbdc.dto.common.ClearingSystemMemberID;
import com.dcep.gateway.mcbdc.dto.common.Id;
import com.dcep.gateway.mcbdc.dto.common.Other;
import com.dcep.gateway.mcbdc.dto.mcbs201.*;
import com.dcep.gateway.mcbdc.dto.mcbs201.GroupHeader;
import com.dcep.gateway.mcbdc.dto.soap.*;

public class GenerateMcbs201Envelope {

    public static GenericEnvelopeDTO<GenericGwDTO> generateMcbs201EnvelopeDTO() {

        McbsEnvelopeDTO<McbsGwDTO> mcbs201Envelope = new McbsEnvelopeDTO<>();
        McbsSoapHeader mcbsSoapHeader = TestUtils.generateMcbsSoapHeader("mcbs.201.001.01");
        mcbs201Envelope.setSoapHeader(mcbsSoapHeader);
        McbsSoapBody<McbsGwDTO> mcbsSoapBody = new McbsSoapBody<>();
        mcbs201Envelope.setSoapBody(mcbsSoapBody);

        // 时区转换
        String mcbsTime = TimeUtil.getMcbsCurrentTime(DcepDateUtils.ISO_DATETIME_PATTERN);
        GroupHeader groupHeader = new GroupHeader();
        groupHeader.setCreateDateTime(mcbsTime);
        groupHeader.setNumberOfTxs(Constant.NUM_OF_TXS_1);
        groupHeader.setMsgId(TestUtils.getMsgId("000", "201"));
        Mcbs20100101DTO mcbs20100101DTO = new Mcbs20100101DTO();
        mcbs20100101DTO.setGrpHdr(groupHeader);

        // paymentTypeInfo
        PaymentTypeInfo paymentTypeInfo = new PaymentTypeInfo();
        CategoryPurpose categoryPurpose = new CategoryPurpose();
        categoryPurpose.setCode(Constant.MBRIDGE_CTGYPURP_ISUE);
        paymentTypeInfo.setCategoryPurpose(categoryPurpose);

        // instructingAgent
        InstrFinancialInstitutionID instructingAgentInstitutionID = new InstrFinancialInstitutionID();
        ClearingSystemMemberID instructingAgentMemberID = new ClearingSystemMemberID("C1010511003703", new ClearingSystemID(Constant.MBRIDGE_CHINA_CLEARINGCODE));
        instructingAgentInstitutionID.setLei("LEI-C1010511003703");
        instructingAgentInstitutionID.setClearingSystemMemberID(instructingAgentMemberID);
        InstructingAgent instructingAgent = new InstructingAgent();
        instructingAgent.setFinancialInstitutionID(instructingAgentInstitutionID);

        // instructedAgent
        InstrFinancialInstitutionID instructedAgentInstitutionID = new InstrFinancialInstitutionID();
        ClearingSystemMemberID instructedAgentMemberID = new ClearingSystemMemberID("C1010211000012", new ClearingSystemID(Constant.MBRIDGE_CHINA_CLEARINGCODE));
        instructedAgentInstitutionID.setClearingSystemMemberID(instructedAgentMemberID);
        instructedAgentInstitutionID.setLei("LEI-C1010211000012");
        InstructedAgent instructedAgent = new InstructedAgent();
        instructedAgent.setFinancialInstitutionID(instructedAgentInstitutionID);

        // creditor
        FinancialInstitutionID creditorInstitutionID = new FinancialInstitutionID();
        ClearingSystemMemberID creditorMemberID = new ClearingSystemMemberID("C1010511003703", new ClearingSystemID(Constant.MBRIDGE_CHINA_CLEARINGCODE));
        creditorInstitutionID.setClearingSystemMemberID(creditorMemberID);
        creditorInstitutionID.setLei("LEI-C1010511003703");
        Creditor creditor = new Creditor();
        creditor.setFinancialInstitutionID(creditorInstitutionID);

        // creditorAccount
        CreditorAccount creditorAccount = new CreditorAccount();
        Id creditorId = new Id();
        Other creditorOther = new Other();
        creditorOther.setId("0021000000122282");
        creditorId.setOther(creditorOther);
        creditorAccount.setId(creditorId);
        creditorAccount.setName("收款机构");

        // directDebitTransactionInfo
        DirectDebitTransactionInfo directDebitTransactionInfo = new DirectDebitTransactionInfo();
        PaymentId paymentId = new PaymentId();
        directDebitTransactionInfo.setPaymentId(paymentId);
        directDebitTransactionInfo.setInterBankSettleAmount(new ActiveCurrencyAndAmount("CNY", "3333.33"));

        // debtor
        FinancialInstitutionID debtorInstitutionID = new FinancialInstitutionID();
        ClearingSystemMemberID debtorMemberID = new ClearingSystemMemberID("C1010211000012", new ClearingSystemID(Constant.MBRIDGE_CHINA_CLEARINGCODE));
        debtorInstitutionID.setClearingSystemMemberID(debtorMemberID);
        debtorInstitutionID.setLei("LEI-C1010211000012");
        com.dcep.gateway.mcbdc.dto.mcbs201.Debtor debtor = new com.dcep.gateway.mcbdc.dto.mcbs201.Debtor();
        debtor.setFinancialInstitutionID(debtorInstitutionID);
        directDebitTransactionInfo.setDebtor(debtor);

        // debtorAccount
        com.dcep.gateway.mcbdc.dto.mcbs201.DebtorAccount debtorAccount =
                new com.dcep.gateway.mcbdc.dto.mcbs201.DebtorAccount();
        com.dcep.gateway.mcbdc.dto.mcbs201.Id debtorId = new com.dcep.gateway.mcbdc.dto.mcbs201.Id();
        com.dcep.gateway.mcbdc.dto.mcbs201.Other debtorOther = new com.dcep.gateway.mcbdc.dto.mcbs201.Other();
        debtorOther.setId("0021000000122281");
        debtorId.setOther(debtorOther);
        debtorAccount.setId(debtorId);
        debtorAccount.setName("付款机构");
        directDebitTransactionInfo.setDebtorAccount(debtorAccount);

        // creditInstructionSupplementaryData
        CreditInstructionSupplementaryData creditInstructionSupplementaryData
                = new CreditInstructionSupplementaryData();
        creditInstructionSupplementaryData.setPlaceAndName(Constant.MBRIDGE_201_PLACE_AND_NAME);

        // envelope
        CreditInstructionContents contents = new CreditInstructionContents();
        contents.setDirection(mcbsSoapHeader.getMessageDirection());
        String instrId = "B202510151100";
        String mcbsBactchNo = BatIdUtil.trsfHlhtBatIdToMcbsBatId(instrId);
        contents.setBatchNo(mcbsBactchNo);
        contents.setParameterId("33333333");
        contents.setReason("reason");
        String extra = "{\"" + Constant.MBRIDGE_EXTRA_MSGID + "\":\"" + mcbs20100101DTO.getGrpHdr().getMsgId()
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

        mcbs20100101DTO.setCreditInstruction(creditInstruction);

        mcbsSoapBody.setT(mcbs20100101DTO);
        return mcbs201Envelope;
    }

}

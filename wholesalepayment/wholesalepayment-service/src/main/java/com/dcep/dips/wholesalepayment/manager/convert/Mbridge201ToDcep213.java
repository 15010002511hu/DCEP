/*
 * pbcdci.cn Inc.
 * Copyright © 2022 PBCDCI All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.manager.convert;

import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.SoapBody;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.DcepDateUtils;
import com.dcep.dips.common.constant.CommonConstant;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.common.utils.BatIdUtil;
import com.dcep.dips.wholesalepayment.common.utils.DtoUtil;
import com.dcep.dips.wholesalepayment.common.utils.TimeUtil;
import com.dcep.dips.wholesalepayment.dto.dc213.*;
import com.dcep.dips.wholesalepayment.dto.mcbs.MbridgeReqDTO;
import com.dcep.gateway.mcbdc.dto.mcbs201.Mcbs20100101DTO;
import com.dcep.gateway.mcbdc.dto.soap.GenericEnvelopeDTO;
import com.dcep.gateway.mcbdc.dto.soap.GenericGwDTO;
import com.dcep.gateway.mcbdc.dto.soap.McbsEnvelopeDTO;
import com.dcep.gateway.mcbdc.dto.soap.McbsGwDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Mbridge201ToDcep213
 * @author : maxinyu
 * @version : Mbridge201ToDcep213.java v 0.1 2022-05-28
 * @description :
 */
public final class Mbridge201ToDcep213 {
    /**
     * @param mBridge201Envelope 货币桥201报文
     * @param dcepReqEnvelopeDTO DCEP213报文（待加工）
     * @return DCEP213报文（完成加工）
     */
    public static EnvelopeDTO<GwDTO> convert(GenericEnvelopeDTO<GenericGwDTO> mBridge201Envelope,
            EnvelopeDTO<GwDTO> dcepReqEnvelopeDTO) {
        McbsEnvelopeDTO<McbsGwDTO> mcbs201Envelope = (McbsEnvelopeDTO<McbsGwDTO>) mBridge201Envelope;
        Mcbs20100101DTO mcbs20100101DTO = (Mcbs20100101DTO) mcbs201Envelope.body();

        String hlhtTime = TimeUtil.fromMcbs(
            mcbs20100101DTO.getGrpHdr().getCreateDateTime(), DcepDateUtils.ISO_DATETIME_PATTERN);
        GrpHdr grpHdr = new GrpHdr();
        grpHdr.setCreDtTm(hlhtTime);
        grpHdr.setNbOfTxs(Constant.NUM_OF_TXS_1);
        SttlmInf sttlmInf = new SttlmInf();
        sttlmInf.setSttlmMtd(Constant.SETTLE_METHOD_CLRG);
        grpHdr.setSttlmInf(sttlmInf);
        Dcep21301001DTO dcep21300101DTO = new Dcep21301001DTO();
        dcep21300101DTO.setGrpHdr(grpHdr);

        DrctDbtTxInf drctDbtTxInf = new DrctDbtTxInf();

        PmtId pmtId = new PmtId();
        String mcbsBatchNo = mcbs20100101DTO.getCreditInstruction().getCreditInstructionSupplementaryData()
                .getCreditInstructionEnvelope().getContents().getBatchNo();
        String instrId = BatIdUtil.trsfMcbsBatIdToHlhtBatId(mcbsBatchNo);
        pmtId.setInstrId(instrId);
        drctDbtTxInf.setPmtId(pmtId);

        PmtTpInf pmtTpInf = new PmtTpInf();
        LclInstrm lclInstrm = new LclInstrm();
        lclInstrm.setPrtry(Constant.DCEP_CTGYPURP_223);
        pmtTpInf.setLclInstrm(lclInstrm);
        drctDbtTxInf.setPmtTpInf(pmtTpInf);

        drctDbtTxInf.setIntrBkSttlmAmt(mcbs20100101DTO.getCreditInstruction().getDirectDebitTransactionInfo()
            .getInterBankSettleAmount());
        drctDbtTxInf.setChrgBr(Constant.CHRGBR_CRED);

        CdtrAcct cdtrAcct = new CdtrAcct();
        Id cdtrId = new Id();
        Othr cdtrOther = new Othr();
        cdtrOther.setId(mcbs20100101DTO.getCreditInstruction().getCreditorAccount().getId().getOther().getId());
        cdtrId.setOthr(cdtrOther);
        cdtrAcct.setId(cdtrId);
        cdtrAcct.setNm(mcbs20100101DTO.getCreditInstruction().getCreditorAccount().getName());
        drctDbtTxInf.setCdtrAcct(cdtrAcct);

        FinInstnId cdtrInstnId = new FinInstnId();
        com.dcep.dips.wholesalepayment.dto.dc213.ClrSysMmbId cdtrMmbId = new com.dcep.dips.wholesalepayment.dto.dc213.ClrSysMmbId();
        cdtrMmbId.setMmbId(mcbs20100101DTO.getCreditInstruction().getInstructingAgent()
            .getFinancialInstitutionID().getClearingSystemMemberID().getMemberId());
        cdtrInstnId.setClrSysMmbId(cdtrMmbId);
        cdtrInstnId.setLei(mcbs20100101DTO.getCreditInstruction().getInstructingAgent()
            .getFinancialInstitutionID().getLei());
        CdtrAgt cdtrAgt = new CdtrAgt();
        cdtrAgt.setFinInstnId(cdtrInstnId);

        BrnchId cdtrBrnchId = new BrnchId();
        cdtrBrnchId.setId(mcbs20100101DTO.getCreditInstruction().getCreditor()
            .getFinancialInstitutionID().getClearingSystemMemberID().getMemberId());
        cdtrBrnchId.setLei(mcbs20100101DTO.getCreditInstruction().getCreditor().getFinancialInstitutionID().getLei());
        cdtrAgt.setBrnchId(cdtrBrnchId);
        drctDbtTxInf.setCdtrAgt(cdtrAgt);

        DbtrAcct dbtrAcct = new DbtrAcct();
        Id dbtrId = new Id();
        Othr dbtrOther = new Othr();
        dbtrOther.setId(mcbs20100101DTO.getCreditInstruction().getDirectDebitTransactionInfo()
            .getDebtorAccount().getId().getOther().getId());
        dbtrId.setOthr(dbtrOther);
        dbtrAcct.setId(dbtrId);
        dbtrAcct.setNm(mcbs20100101DTO.getCreditInstruction().getDirectDebitTransactionInfo()
            .getDebtorAccount().getName());
        drctDbtTxInf.setDbtrAcct(dbtrAcct);

        Purp purp = new Purp();
        purp.setPrtry(Constant.DCEP_PURP_22300001);
        drctDbtTxInf.setPurp(purp);

        FinInstnId dbtrInstnId = new FinInstnId();
        com.dcep.dips.wholesalepayment.dto.dc213.ClrSysMmbId dbtrMmbId = new com.dcep.dips.wholesalepayment.dto.dc213.ClrSysMmbId();
        // todo 修改原路由服务赋值-》待确认
        dbtrMmbId.setMmbId(mcbs20100101DTO.getCreditInstruction().getInstructedAgent().getFinancialInstitutionID().getClearingSystemMemberID().getMemberId());
        dbtrInstnId.setClrSysMmbId(dbtrMmbId);
        dbtrInstnId.setLei(mcbs20100101DTO.getCreditInstruction().getInstructedAgent().getFinancialInstitutionID().getLei());
        DbtrAgt dbtrAgt = new DbtrAgt();
        dbtrAgt.setFinInstnId(dbtrInstnId);

        BrnchId dbtrBrnchId = new BrnchId();
        dbtrBrnchId.setId(mcbs20100101DTO.getCreditInstruction().getDirectDebitTransactionInfo().getDebtor()
            .getFinancialInstitutionID().getClearingSystemMemberID().getMemberId());
        dbtrBrnchId.setLei(mcbs20100101DTO.getCreditInstruction().getDirectDebitTransactionInfo().getDebtor()
            .getFinancialInstitutionID().getLei());
        dbtrAgt.setBrnchId(dbtrBrnchId);
        drctDbtTxInf.setDbtrAgt(dbtrAgt);

        com.dcep.dips.wholesalepayment.dto.dc213.RmtInf dcep213RmtInf = new com.dcep.dips.wholesalepayment.dto.dc213.RmtInf();
        com.dcep.gateway.mcbdc.dto.mcbs201.RmtInf mcbs201RmtInf =
                mcbs20100101DTO.getCreditInstruction().getDirectDebitTransactionInfo().getRmtInf();
        if (mcbs201RmtInf != null && mcbs201RmtInf.getUnstructed() != null) {
            dcep213RmtInf.setPostscript(mcbs201RmtInf.getUnstructed());
        }
        dcep213RmtInf.setReason(mcbs20100101DTO.getCreditInstruction().getCreditInstructionSupplementaryData()
            .getCreditInstructionEnvelope().getContents().getReason());
        dcep213RmtInf.setParameterId(mcbs20100101DTO.getCreditInstruction().getCreditInstructionSupplementaryData()
            .getCreditInstructionEnvelope().getContents().getParameterId());
        dcep213RmtInf.setSndChnlSys(Constant.MCBS);
        dcep213RmtInf.setRcvChnlSys(Constant.DCEP);
        drctDbtTxInf.setRmtInf(dcep213RmtInf);

        List<String> ustrds = new ArrayList<>();
        if (dcep213RmtInf.getPostscript() != null) {
            ustrds.add(Constant.PREFIX_POSTSCRIPT + dcep213RmtInf.getPostscript());
        }
        ustrds.add(Constant.PREFIX_REASON + dcep213RmtInf.getReason());
        ustrds.add(Constant.PREFIX_PARAMETERID + dcep213RmtInf.getParameterId());
        ustrds.add(Constant.PREFIX_SNDCHNLSYS + Constant.MCBS);
        ustrds.add(Constant.PREFIX_RCVCHNLSYS + Constant.DCEP);
        drctDbtTxInf.setUstrds(ustrds);
        dcep21300101DTO.setDrctDbtTxInf(drctDbtTxInf);


        MbridgeReqDTO mbridgeReqDTO = new MbridgeReqDTO();
        mbridgeReqDTO.setMcbsMsgId(mcbs20100101DTO.getGrpHdr().getMsgId());
        mbridgeReqDTO.setSndDtTm(mcbs20100101DTO.getGrpHdr().getCreateDateTime());
        mbridgeReqDTO.setMcbsBatId(mcbsBatchNo);
        mbridgeReqDTO.setMcbsMsgTp(mcbs201Envelope.getSoapHeader().getMsgTp());
        mbridgeReqDTO.setMsgDrn(mcbs201Envelope.getSoapHeader().getMessageDirection());
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
        mbridgeReqDTO.setParameterId(mcbs20100101DTO.getCreditInstruction().getCreditInstructionSupplementaryData()
            .getCreditInstructionEnvelope().getContents().getParameterId());
        mbridgeReqDTO.setShardingMsgId(mcbs20100101DTO.getGrpHdr().getMsgId());
        dcep21300101DTO.setMbridgeReqDTO(mbridgeReqDTO);

        SoapHeader soapHeader = DtoUtil.toDcepSoapHeader(mcbs201Envelope);
        dcepReqEnvelopeDTO.setSoapHeader(soapHeader);
        dcepReqEnvelopeDTO.getSoapHeader().setSender(mcbs20100101DTO.getCreditInstruction().getInstructingAgent()
            .getFinancialInstitutionID().getClearingSystemMemberID().getMemberId());
        dcepReqEnvelopeDTO.getSoapHeader().setReceiver(mcbs20100101DTO.getCreditInstruction().getInstructedAgent()
            .getFinancialInstitutionID().getClearingSystemMemberID().getMemberId());
        dcepReqEnvelopeDTO.setSoapBody(new SoapBody<>(dcep21300101DTO));
        return dcepReqEnvelopeDTO;
    }

    private Mbridge201ToDcep213() {
        // comply with Sonar rules.
    }

}

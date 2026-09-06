package com.dcep.dips.wholesalepayment.utils;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.dcep.common.utils.DcepDateUtils;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.common.utils.BatIdUtil;
import com.dcep.dips.wholesalepayment.common.utils.TimeUtil;
import com.dcep.gateway.mcbdc.dto.common.Id;
import com.dcep.gateway.mcbdc.dto.common.Other;
import com.dcep.gateway.mcbdc.dto.mcbs202.*;
import com.dcep.gateway.mcbdc.dto.mcbs202.ClearingSystemID;
import com.dcep.gateway.mcbdc.dto.soap.*;

public class GenerateMcbs202Envelope {

    public static GenericEnvelopeDTO<GenericGwDTO> generateMcbs202EnvelopeDTO() {

        McbsEnvelopeDTO<McbsGwDTO> mcbs202EnvelopeDTO = new McbsEnvelopeDTO<>();
        McbsSoapHeader mcbsSoapHeader = TestUtils.generateMcbsSoapHeader("mcbs.202.001.01");
        mcbs202EnvelopeDTO.setSoapHeader(mcbsSoapHeader);
        McbsSoapBody<McbsGwDTO> mcbsSoapBody = new McbsSoapBody<>();
        Mcbs20200101DTO mcbs20200101DTO = new Mcbs20200101DTO();
        mcbsSoapBody.setT(mcbs20200101DTO);
        mcbs202EnvelopeDTO.setSoapBody(mcbsSoapBody);

        GrpHdr groupHeader = new GrpHdr();
        groupHeader.setCreDtTm(TimeUtil.getMcbsCurrentTime(DcepDateUtils.ISO_DATETIME_PATTERN));
        SttlmInf sttlmInf = new SttlmInf();
        sttlmInf.setSttlmMtd(Constant.SETTLE_METHOD_CLRG);
        groupHeader.setSttlmInf(sttlmInf);
        InstrFinInstnId instgInstrFinInstnId = new InstrFinInstnId();
        String instgCode = "C3333333333333";
        instgInstrFinInstnId.setClrSysMmbId(new InstrClrSysMmbId(
                instgCode, new com.dcep.gateway.mcbdc.dto.mcbs202.ClearingSystemID(Constant.MBRIDGE_CHINA_CLEARINGCODE)));
        groupHeader.setInstgAgt(new InstgAgt(instgInstrFinInstnId));
        InstrFinInstnId instdInstrFinInstnId = new InstrFinInstnId();
        String instdCode = "C3333333333334";
        instdInstrFinInstnId.setClrSysMmbId(new InstrClrSysMmbId(
                instdCode, new com.dcep.gateway.mcbdc.dto.mcbs202.ClearingSystemID(Constant.MBRIDGE_CHINA_CLEARINGCODE)));
        groupHeader.setInstdAgt(new InstdAgt(instdInstrFinInstnId));
        mcbs20200101DTO.setGrpHdr(groupHeader);

        TxInf txInf = new TxInf();
        txInf.setRtrdIntrBkSttlmAmt(new ActiveCurrencyAndAmount("CNY", "4444.44"));
        Cnts cnts = new Cnts();

        cnts.setBatchNO(BatIdUtil.trsfHlhtBatIdToMcbsBatId("B202510151100"));
        Envlp envlp = new Envlp();
        envlp.setCnts(cnts);
        SplmtryData splmtryData = new SplmtryData();
        splmtryData.setPlcAndNm("/Envelope/PmtRtr/TxInf");
        splmtryData.setEnvlp(envlp);
        OrgnlTxRef orgnlTxRef = new OrgnlTxRef();
        txInf.setOrgnlTxRef(orgnlTxRef);
        txInf.setSplmtryData(splmtryData);
        mcbs20200101DTO.setTxInf(txInf);

        mcbs20200101DTO.getGrpHdr().setMsgId(TestUtils.getMsgId("000", "202"));
        OrgnlGrpInf orgnlGrpHdr = new OrgnlGrpInf();
        orgnlGrpHdr.setOrgnlMsgId(TestUtils.getMsgId("000", "202"));
        orgnlGrpHdr.setOrgnlMsgNmId("orgMsgTp");
        mcbs20200101DTO.setOrgnlGrpInf(orgnlGrpHdr);
        mcbs20200101DTO.getTxInf().setRtrId(mcbs20200101DTO.getGrpHdr().getMsgId());

        PmtTpInf pmtTpInf = new PmtTpInf();
        CtgyPurp ctgyPurp = new CtgyPurp();
        ctgyPurp.setCd("code");
        pmtTpInf.setCtgyPurp(ctgyPurp);
        mcbs20200101DTO.getTxInf().getOrgnlTxRef().setPmtTpInf(pmtTpInf);

        FinInstnId dbtrFinInstnId = new FinInstnId();
        dbtrFinInstnId.setLei("7657326926816417864");
        ClrSysMmbId dbtrMmbId = new ClrSysMmbId(
                "C3333333333333",
                new com.dcep.gateway.mcbdc.dto.mcbs202.ClearingSystemID(Constant.MBRIDGE_CHINA_CLEARINGCODE));
        dbtrFinInstnId.setClrSysMmbId(dbtrMmbId);
        Agt dbtrAgt = new Agt();
        dbtrAgt.setFinInstnId(dbtrFinInstnId);
        Dbtr dbtr = new Dbtr();
        dbtr.setAgt(dbtrAgt);
        mcbs20200101DTO.getTxInf().getOrgnlTxRef().setDbtr(dbtr);

        DbtrAcct dbtrAcct = new DbtrAcct();
        Id id = new Id();
        Other other = new Other();
        other.setId("0021000000122282");
        id.setOther(other);
        dbtrAcct.setId(id);
        mcbs20200101DTO.getTxInf().getOrgnlTxRef().setDbtrAcct(dbtrAcct);

        FinInstnId cdtrFinInstnId = new FinInstnId();
        cdtrFinInstnId.setLei("7657326926816417864");
        ClrSysMmbId cdtrMmbId = new ClrSysMmbId(
                "C3333333333334",
                new ClearingSystemID(Constant.MBRIDGE_CHINA_CLEARINGCODE));
        cdtrFinInstnId.setClrSysMmbId(cdtrMmbId);
        Agt cdtrAgt = new Agt();
        cdtrAgt.setFinInstnId(cdtrFinInstnId);
        Cdtr cdtr = new Cdtr();
        cdtr.setAgt(cdtrAgt);
        mcbs20200101DTO.getTxInf().getOrgnlTxRef().setCdtr(cdtr);

        CdtrAcct cdtrAcct = new CdtrAcct();
        Id cdtrAcctId = new Id();
        Other cdtrAcctOther = new Other();
        cdtrAcctOther.setId("0021000000122281");
        cdtrAcctId.setOther(cdtrAcctOther);
        cdtrAcct.setId(cdtrAcctId);
        mcbs20200101DTO.getTxInf().getOrgnlTxRef().setCdtrAcct(cdtrAcct);

        mcbs20200101DTO.getTxInf().getSplmtryData().getEnvlp().getCnts().setOrgnlTxDrn(
                "R");
        mcbs20200101DTO.getTxInf().getSplmtryData().getEnvlp().getCnts().setOrgnlTxParamId(
                "22222222");
        mcbs20200101DTO.getTxInf().getSplmtryData().getEnvlp().getCnts().setOrgnlTxBatchNO(
                "B202510151100");

        return mcbs202EnvelopeDTO;
    }

}

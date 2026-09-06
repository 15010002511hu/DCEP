package com.dcep.dips.wholesalepayment.manager.convert;

import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.SoapBody;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.DcepDateUtils;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.common.utils.BatIdUtil;
import com.dcep.dips.wholesalepayment.common.utils.TimeUtil;
import com.dcep.dips.wholesalepayment.dto.dc203.*;
import com.dcep.dips.wholesalepayment.dto.mcbs101.ClrDtlInf;
import com.dcep.dips.wholesalepayment.dto.mcbs101.Mcbs10100101DTO;
import com.dcep.gateway.mcbdc.dto.soap.GenericEnvelopeDTO;
import com.dcep.gateway.mcbdc.dto.soap.GenericGwDTO;

/**
 * 货币桥101报文转Dcep203报文
 * @author: luzhikuan
 * @create: 2025-10-27 16:34
 */
public class Mbridge101ToDcep203 {

    /**
     *
     * @param clrDtlInf
     * @return
     */
    public static EnvelopeDTO<GwDTO> convertToDcep203(ClrDtlInf clrDtlInf, Mcbs10100101DTO mcbs10100101DTO) {
        EnvelopeDTO<GwDTO> dcep203Envelope = new EnvelopeDTO<>();
        //机构编码
        String instgId = clrDtlInf.getFinInsTnId().getClrSysMmbId().getMmbId();
        //报文头
        SoapHeader dcepSoapHeader = new SoapHeader();
        dcepSoapHeader.setVer(Constant.DCEP_SOAPHEADER_VER);
        dcepSoapHeader.setSndDtTm(TimeUtil.getHlhtCurrentTime(DcepDateUtils.ISO_DATETIME_PATTERN));
        dcepSoapHeader.setMsgTp(Constant.DCEP_MSGTYPE_203);
        dcep203Envelope.setSoapHeader(dcepSoapHeader);

        com.dcep.dips.wholesalepayment.dto.dc203.GrpHdr dcep203GroupHeader = new com.dcep.dips.wholesalepayment.dto.dc203.GrpHdr();
        String creDtTm = mcbs10100101DTO.getMsgHdr().getCreDtTm();
        String hlhtTime = TimeUtil.fromMcbs(creDtTm, DcepDateUtils.ISO_DATETIME_PATTERN);
        dcep203GroupHeader.setCreDtTm(hlhtTime);
        dcep203GroupHeader.setNbOfTxs(Constant.NUM_OF_TXS_1);
        dcep203GroupHeader.setSttlmInf(new com.dcep.dips.wholesalepayment.dto.dc203.SttlmInf(Constant.SETTLE_METHOD_CLRG));
        Dcep20301001DTO dcep203DTO = new Dcep20301001DTO();
        dcep203DTO.setGrpHdr(dcep203GroupHeader);

        //报文体参数组装
        CdtTrfTxInf cdtTrfTxInf = new CdtTrfTxInf();
        String mcbsBatchNo = clrDtlInf.getEnvlp().getCnts().getBatchNO();
        String dcepBatchNo = BatIdUtil.trsfMcbsBatIdToHlhtBatId(mcbsBatchNo);
        PmtId dcep203PmtId = new PmtId();
        dcep203PmtId.setInstrId(dcepBatchNo);
        cdtTrfTxInf.setPmtId(dcep203PmtId);

        PmtTpInf dcep203PmtTpInf = new PmtTpInf();
        SvcLvl dcep203SvcLvl = new SvcLvl();
        dcep203SvcLvl.setPrtry(Constant.SVCSVL_PRTRY_TT00);
        dcep203PmtTpInf.setSvcLvl(dcep203SvcLvl);
        CtgyPurp dcep203CtgyPurp = new CtgyPurp();
        dcep203CtgyPurp.setPrtry(Constant.DCEP_CTGYPURP_223);
        dcep203PmtTpInf.setCtgyPurp(dcep203CtgyPurp);
        cdtTrfTxInf.setPmtTpInf(dcep203PmtTpInf);
        //todo 交易批次号
        //cdtTrfTxInf.setIntrBkSttlmAmt();
        cdtTrfTxInf.setChrgBr(Constant.CHRGBR_DEBT);


        FinInstnId dcep203InstgFinInstnId = new FinInstnId();
        //todo 付款直接参与者编码--同机构
        String instgOrgCode = instgId;
        ClrSysMmbId dcep203InstgMmbId = new ClrSysMmbId();
        dcep203InstgMmbId.setMmbId(instgOrgCode);
        dcep203InstgFinInstnId.setClrSysMmbId(dcep203InstgMmbId);
        //todo LEI--同机构
        String instgOrgLei = instgId;
        dcep203InstgFinInstnId.setLei(instgOrgLei);
        InstgAgt dcep203InstgAgt = new InstgAgt();
        dcep203InstgAgt.setFinInstnId(dcep203InstgFinInstnId);

        BrnchId dcep203InstgBrnchId = new BrnchId();
        //todo 付款机构--同机构
        String debtorOrgCode = instgId;
        dcep203InstgBrnchId.setId(debtorOrgCode);
        //todo LEI--同机构
        String debtorOrgLei = instgId;
        dcep203InstgBrnchId.setLei(debtorOrgLei);
        dcep203InstgAgt.setBrnchId(dcep203InstgBrnchId);
        cdtTrfTxInf.setInstgAgt(dcep203InstgAgt);
        //todo sender
        dcep203Envelope.getSoapHeader().setSender(instgOrgCode);

        //todo 收款直接参与机构信息
        FinInstnId dcep203InstdFinInstnId = new FinInstnId();
        String instdOrgCode = "";//todo 收款直接参与机构编码
        ClrSysMmbId dcep203InstdMmbId = new ClrSysMmbId();
        dcep203InstdMmbId.setMmbId(instdOrgCode);
        dcep203InstdFinInstnId.setClrSysMmbId(dcep203InstdMmbId);
        String instdOrgLei = "";//todo 收款直接参与机构LEI
        dcep203InstdFinInstnId.setLei(instdOrgLei);
        InstdAgt dcep203InstdAgt = new InstdAgt();
        dcep203InstdAgt.setFinInstnId(dcep203InstdFinInstnId);

        // 收款机构
        BrnchId dcep203InstdBrnchId = new BrnchId();
        String creditorOrgCode = instgId;//todo 收款机构编码
        dcep203InstdBrnchId.setId(creditorOrgCode);
        String creditorOrgLei = instgId;//todo 收款机构LEI码
        dcep203InstdBrnchId.setLei(creditorOrgLei);
        dcep203InstdAgt.setBrnchId(dcep203InstdBrnchId);
        cdtTrfTxInf.setInstdAgt(dcep203InstdAgt);
        //todo receiver
        dcep203Envelope.getSoapHeader().setReceiver(instdOrgCode);

        //付款人信息
        com.dcep.dips.wholesalepayment.dto.dc203.DbtrAcct dcep203DbtrAcct = new com.dcep.dips.wholesalepayment.dto.dc203.DbtrAcct();
        com.dcep.dips.wholesalepayment.dto.dc203.Othr dcepDbtrOther = new com.dcep.dips.wholesalepayment.dto.dc203.Othr();
        dcepDbtrOther.setId("");//todo  付款人钱包ID
        com.dcep.dips.wholesalepayment.dto.dc203.Id dcep203DbtrId = new com.dcep.dips.wholesalepayment.dto.dc203.Id();
        dcep203DbtrId.setOthr(dcepDbtrOther);

        dcep203DbtrAcct.setId(dcep203DbtrId);
        dcep203DbtrAcct.setNm("");//todo 付款人钱包名称
        cdtTrfTxInf.setDbtrAcct(dcep203DbtrAcct);

        //收款人信息 清零任务无
//        com.dcep.dips.wholesalepayment.dto.dc203.CdtrAcct dcep203CdtrAcct = new com.dcep.dips.wholesalepayment.dto.dc203.CdtrAcct();
//        com.dcep.dips.wholesalepayment.dto.dc203.Othr dcepCdtrOther = new com.dcep.dips.wholesalepayment.dto.dc203.Othr();
//        dcepCdtrOther.setId("");//todo 收款人钱包ID
//        com.dcep.dips.wholesalepayment.dto.dc203.Id dcep203CdtrId = new com.dcep.dips.wholesalepayment.dto.dc203.Id();
//        dcep203CdtrId.setOthr(dcepCdtrOther);
//        dcep203CdtrAcct.setId(dcep203CdtrId);
//        dcep203CdtrAcct.setNm("");//todo 收款人钱包名称
//        cdtTrfTxInf.setCdtrAcct(dcep203CdtrAcct);

        com.dcep.dips.wholesalepayment.dto.dc203.Purp dcep203Purp = new com.dcep.dips.wholesalepayment.dto.dc203.Purp();
        //todo 业务种类编码
        dcep203Purp.setPrtry(Constant.DCEP_PRTRY_REDT);
        cdtTrfTxInf.setPurp(dcep203Purp);

        com.dcep.dips.wholesalepayment.dto.dc203.RmtInf dcep203RmtInf = new com.dcep.dips.wholesalepayment.dto.dc203.RmtInf();
        // todo 发行和注销方式编码 赋值层级存在问题
        dcep203RmtInf.setParameterId(Constant.DCEP_PARAMETER_ID);
        cdtTrfTxInf.setRmtInf(dcep203RmtInf);

        dcep203DTO.setCdtTrfTxInf(cdtTrfTxInf);

        SoapBody<GwDTO> dcepSoapBody = new SoapBody<>();
        dcepSoapBody.setT(dcep203DTO);
        dcep203Envelope.setSoapBody(dcepSoapBody);

        return dcep203Envelope;
    }
}


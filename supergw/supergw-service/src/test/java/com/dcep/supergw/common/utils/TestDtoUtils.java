package com.dcep.supergw.common.utils;

import com.dcep.common.enums.MessageTypeEnum;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.msg.Dcep91100101DTO;
import com.dcep.common.model.soap.SoapBody;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.DcepDateUtils;
import com.dcep.supergw.dto.dc415.Dcep41500101DTO;

/**
 * @author nieyanping
 * @version TestDtoUtils.java, v 0.1, 2020/1/13 14:32
 * @description TODO
 */
public class TestDtoUtils {

    public static EnvelopeDTO envelopeDTO911() {
        EnvelopeDTO<Dcep91100101DTO> dto = new EnvelopeDTO<>();

        String msgId = TestMsgUtils.getMsgId("911");
        SoapHeader soapHeader = new SoapHeader("01", DcepDateUtils.getDcepDateStrNow(),
            MessageTypeEnum.DCEP_911_001_01.getCode(), msgId + "0000", "00000000000000", "C1010211000012");
        soapHeader.setNcrptnSN("4");
        soapHeader.setSignSN("1");
        soapHeader.setDgtlEnvlp("aldfhhhhhhhhhhhhhhhhhhajhfdsak=");

        SoapBody<Dcep91100101DTO> soapBody = new SoapBody<>();
        Dcep91100101DTO fault = new Dcep91100101DTO();
        fault.setFaultcode("DCEPS9999");
        fault.setFaultstring("未知错误");
        fault.setFaultactor("C1010211000012");

        soapBody.setT(fault);

        dto.setSoapHeader(soapHeader);
        dto.setSoapBody(soapBody);
        return dto;
    }

    public static EnvelopeDTO envelopeDTO415() {
        EnvelopeDTO<Dcep41500101DTO> dto = new EnvelopeDTO<>();

        String msgId = TestMsgUtils.getMsgId("415");
        SoapHeader soapHeader = new SoapHeader("01", DcepDateUtils.getDcepDateStrNow(),
            MessageTypeEnum.DCEP_415_001_01.getCode(), msgId + "0000", "C1010411000013", "C1010211000012");
        soapHeader.setNcrptnSN("4");
        soapHeader.setDgtlEnvlp("aldfhhhhhhhhhhhhhhhhhhajhfdsak=");

        SoapBody<Dcep41500101DTO> soapBody = new SoapBody<>();

        Dcep41500101DTO dcep41500101DTO = new Dcep41500101DTO();

        soapBody.setT(dcep41500101DTO);

        dto.setSoapHeader(soapHeader);
        dto.setSoapBody(soapBody);
        return dto;
    }

}